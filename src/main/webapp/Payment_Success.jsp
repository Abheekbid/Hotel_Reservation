<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Integer bookingId   = (Integer) session.getAttribute("bookingId");
    if (bookingId == null) {
        response.sendRedirect(request.getContextPath() + "/U_signIn.html");
        return;
    }

    String roomId    = (String) session.getAttribute("room_id");
    String roomType  = (String) session.getAttribute("room_type");
    String hotelName = (String) session.getAttribute("hotel_name");
    String name      = (String) session.getAttribute("name");
    String checkIn   = (String) session.getAttribute("check_in");
    String checkOut  = (String) session.getAttribute("check_out");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Payment Successful - WayToGo Hotels</title>
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
            --success: #7FBF8C;
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
            text-align: center;
        }
        .check-icon {
            width: 56px;
            height: 56px;
            border-radius: 50%;
            background: rgba(127, 191, 140, 0.15);
            border: 1px solid rgba(127, 191, 140, 0.4);
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            font-size: 28px;
            color: var(--success);
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
        .summary {
            background: #0D1119;
            border: 1px solid var(--panel-line);
            border-radius: 10px;
            padding: 18px 20px;
            margin-bottom: 24px;
            text-align: left;
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
        .summary-row.booking-id .value {
            color: var(--brass);
            font-size: 17px;
        }
        a.btn {
            display: block;
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
            text-align: center;
            text-decoration: none;
            box-sizing: border-box;
        }
        a.btn:hover { background: #D4B06E; }
    </style>
</head>
<body>
    <div class="card">
        <div class="check-icon">&#10003;</div>
        <p class="eyebrow">WayToGo Hotels</p>
        <h1>Payment Successful</h1>
        <p class="sub">Your booking has been confirmed, <%= name %>.</p>

        <div class="summary">
            <div class="summary-row booking-id">
                <span class="label">Booking ID</span>
                <span class="value">#<%= bookingId %></span>
            </div>
            <div class="summary-row">
                <span class="label">Hotel</span>
                <span class="value"><%= (hotelName != null) ? hotelName : "N/A" %></span>
            </div>
            <div class="summary-row">
                <span class="label">Room ID</span>
                <span class="value"><%= roomId %></span>
            </div>
            <div class="summary-row">
                <span class="label">Room Type</span>
                <span class="value"><%= roomType %></span>
            </div>
            <div class="summary-row">
                <span class="label">Check-In</span>
                <span class="value"><%= checkIn %></span>
            </div>
            <div class="summary-row">
                <span class="label">Check-Out</span>
                <span class="value"><%= checkOut %></span>
            </div>
        </div>

        <a class="btn" href="<%= request.getContextPath() %>/hotel_fetch">Back to Hotels</a>
    </div>
</body>
</html>