<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Guard — redirect if not logged in
    String userId = (String) session.getAttribute("user_id");
    if (userId == null || userId.isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/U_signIn.html");
        return;
    }

    // Read booking details from session
    String hotel_id    = (String) session.getAttribute("hotel_id");
    String room_id     = (String) session.getAttribute("room_id");
    String room_number = (String) session.getAttribute("room_number");
    String room_type   = (String) session.getAttribute("room_type");
    String checkIn     = (String) session.getAttribute("check_in");
    String checkOut    = (String) session.getAttribute("check_out");

    double pricePerNight = 0.0;
    long   nights        = 1;
    double totalPrice    = 0.0;

    Object priceObj  = session.getAttribute("room_price");
    Object nightsObj = session.getAttribute("number_of_days");
    Object totalObj  = session.getAttribute("total_price");

    if (priceObj  != null) pricePerNight = Double.parseDouble(priceObj.toString());
    if (nightsObj != null) nights        = Long.parseLong(nightsObj.toString());
    if (totalObj  != null) totalPrice    = Double.parseDouble(totalObj.toString());
    else totalPrice = pricePerNight * nights;

    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Confirm Payment - WayToGo Hotels</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Fraunces:opsz,wght@9..144,400;9..144,500;9..144,600&family=Work+Sans:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        :root {
            --ink: #10141F;
            --panel: #171C29;
            --panel-line: #2A3143;
            --brass: #C6A15B;
            --brass-soft: rgba(198, 161, 91, 0.35);
            --parchment: #EDE7DA;
            --slate: #8890A3;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            background: var(--ink);
            background-image:
                radial-gradient(circle at 12% 8%, rgba(198,161,91,0.06), transparent 40%),
                radial-gradient(circle at 88% 92%, rgba(198,161,91,0.05), transparent 40%);
            font-family: 'Work Sans', sans-serif;
            color: var(--parchment);
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
        }

        .card {
            width: 100%;
            max-width: 460px;
            background: var(--panel);
            border: 1px solid var(--panel-line);
            border-radius: 14px;
            padding: 36px 34px 32px;
            box-shadow: 0 30px 60px -30px rgba(0,0,0,0.7);
        }

        .eyebrow {
            font-size: 11px;
            letter-spacing: 0.22em;
            text-transform: uppercase;
            color: var(--brass);
            margin: 0 0 10px;
        }

        h1 {
            font-family: 'Fraunces', serif;
            font-weight: 500;
            font-size: 26px;
            margin: 0 0 6px;
        }

        .sub {
            font-size: 13px;
            color: var(--slate);
            margin: 0 0 28px;
        }

        .badge {
            display: inline-block;
            font-size: 12px;
            letter-spacing: 0.04em;
            color: var(--brass);
            background: var(--brass-soft);
            border: 1px solid var(--brass-soft);
            border-radius: 20px;
            padding: 5px 14px;
            margin: -14px 0 24px;
        }

        .server-error {
            font-size: 13px;
            color: #E08A8A;
            background: rgba(224, 138, 138, 0.1);
            border: 1px solid rgba(224, 138, 138, 0.3);
            border-radius: 8px;
            padding: 12px 14px;
            margin: 0 0 20px;
        }

        .summary {
            background: #0D1119;
            border: 1px solid var(--panel-line);
            border-radius: 10px;
            padding: 18px 20px;
            margin-bottom: 24px;
        }

        .summary-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 9px 0;
            border-bottom: 1px solid var(--panel-line);
        }

        .summary-row:last-child { border-bottom: none; }

        .summary-row .label {
            font-size: 11px;
            letter-spacing: 0.06em;
            text-transform: uppercase;
            color: var(--slate);
        }

        .summary-row .value {
            font-family: 'Fraunces', serif;
            font-size: 15px;
            color: var(--parchment);
        }

        .summary-row.total .label {
            font-size: 12px;
            color: var(--parchment);
        }

        .summary-row.total .value {
            font-size: 22px;
            color: var(--brass);
        }

        button {
            width: 100%;
            margin-top: 6px;
            padding: 13px;
            border: none;
            border-radius: 7px;
            background: var(--brass);
            color: #171208;
            font-family: 'Work Sans', sans-serif;
            font-weight: 600;
            font-size: 14px;
            cursor: pointer;
            transition: background 0.15s ease, transform 0.05s ease;
        }

        button:hover { background: #D4B06E; }
        button:active { transform: translateY(1px); }

        .secure-note {
            text-align: center;
            font-size: 11px;
            color: var(--slate);
            margin-top: 14px;
        }
    </style>
</head>
<body>

    <div class="card">
        <p class="eyebrow">WayToGo Hotels</p>
        <h1>Confirm your payment</h1>
        <p class="sub">Review your stay details before completing payment.</p>
        <span class="badge">Room: <%= (room_number != null) ? room_number : room_id %></span>

        <% if ("payment_failed".equals(error)) { %>
            <p class="server-error">Payment could not be processed. Please try again.</p>
        <% } %>

        <div class="summary">
            <div class="summary-row">
                <span class="label">Room ID</span>
                <span class="value"><%= (room_id != null) ? room_id : "N/A" %></span>
            </div>
            <div class="summary-row">
                <span class="label">Room Type</span>
                <span class="value"><%= (room_type != null) ? room_type : "N/A" %></span>
            </div>
            <div class="summary-row">
                <span class="label">Check-In</span>
                <span class="value"><%= (checkIn != null) ? checkIn : "N/A" %></span>
            </div>
            <div class="summary-row">
                <span class="label">Check-Out</span>
                <span class="value"><%= (checkOut != null) ? checkOut : "N/A" %></span>
            </div>
            <div class="summary-row">
                <span class="label">Price per Night</span>
                <span class="value">&#8377; <%= String.format("%.2f", pricePerNight) %></span>
            </div>
            <div class="summary-row">
                <span class="label">Nights</span>
                <span class="value"><%= nights %></span>
            </div>
            <div class="summary-row total">
                <span class="label">Total Price</span>
                <span class="value">&#8377; <%= String.format("%.2f", totalPrice) %></span>
            </div>
        </div>

        <form action="payment" method="post">
            <button type="submit">Pay &#8377; <%= String.format("%.2f", totalPrice) %></button>
        </form>

        <p class="secure-note">Your payment details are encrypted and processed securely.</p>
    </div>

</body>
</html>
