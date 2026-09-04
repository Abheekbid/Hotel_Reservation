<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Map"%>
    <%
    String hotel_id = (String) request.getAttribute("hotel_id");
    String error = request.getParameter("error");
    Map<String, String> roomIds = (Map<String, String>) request.getAttribute("roomIds");
    %>
<!DOCTYPE html>
<html>
<head>
    <title>Book Your Stay - WayToGo Hotels</title>
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

        .hotel-id-badge {
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

        .field { margin-bottom: 18px; }

        .field label {
            display: block;
            font-size: 11px;
            letter-spacing: 0.06em;
            text-transform: uppercase;
            color: var(--slate);
            margin-bottom: 7px;
        }

        .field input {
            width: 100%;
            background: #0D1119;
            border: 1px solid var(--panel-line);
            border-radius: 7px;
            padding: 12px 13px;
            font-family: 'Work Sans', sans-serif;
            font-size: 14px;
            color: var(--parchment);
            outline: none;
            transition: border-color 0.15s ease, box-shadow 0.15s ease;
        }

        .field input::placeholder { color: #545C6E; }

        .field input:focus {
            border-color: var(--brass);
            box-shadow: 0 0 0 3px var(--brass-soft);
        }

        /* native date picker icon needs to be visible on the dark input */
        .field input[type="date"]::-webkit-calendar-picker-indicator {
            filter: invert(70%) sepia(20%) saturate(500%) hue-rotate(0deg);
            cursor: pointer;
        }

        .row {
            display: flex;
            gap: 14px;
        }

        .row .field { flex: 1; }

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

        .field select {
            width: 100%;
            background: #0D1119;
            border: 1px solid var(--panel-line);
            border-radius: 7px;
            padding: 12px 13px;
            font-family: 'Work Sans', sans-serif;
            font-size: 14px;
            color: var(--parchment);
            outline: none;
            appearance: none;
            -webkit-appearance: none;
            -moz-appearance: none;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='8' viewBox='0 0 12 8'%3E%3Cpath fill='%23C6A15B' d='M0 0l6 8 6-8z'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 14px center;
            cursor: pointer;
            transition: border-color 0.15s ease, box-shadow 0.15s ease;
        }

        .field select:focus {
            border-color: var(--brass);
            box-shadow: 0 0 0 3px var(--brass-soft);
        }

        .field select option {
            background: #0D1119;
            color: var(--parchment);
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

        .rail-heading {
            margin: 0 0 14px;
            display: flex;
            align-items: baseline;
            justify-content: space-between;
        }

        .rail-heading h2 {
            font-family: 'Fraunces', serif;
            font-weight: 500;
            font-size: 17px;
            margin: 0;
        }

        .rail-heading span {
            font-size: 11px;
            color: var(--slate);
        }

        .room-rail {
            display: flex;
            gap: 14px;
            overflow-x: auto;
            padding: 4px 2px 14px;
            margin-bottom: 22px;
            scroll-snap-type: x mandatory;
            scrollbar-width: thin;
            scrollbar-color: var(--brass) var(--panel);
        }

        .room-rail::-webkit-scrollbar { height: 6px; }
        .room-rail::-webkit-scrollbar-track { background: var(--panel); border-radius: 8px; }
        .room-rail::-webkit-scrollbar-thumb { background: var(--brass); border-radius: 8px; }

        .room-card {
            flex: 0 0 auto;
            width: 110px;
            height: 70px;
            background: #0D1119;
            border: 1px solid var(--panel-line);
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            scroll-snap-align: start;
            cursor: pointer;
            text-decoration: none;
            transition: border-color 0.15s ease, transform 0.15s ease;
        }

        .room-card:hover {
            border-color: var(--brass-soft);
            transform: translateY(-2px);
        }

        .room-card .room-id {
            font-family: 'Fraunces', serif;
            font-size: 15px;
            color: var(--parchment);
        }

        .no-rooms {
            font-size: 13px;
            color: var(--slate);
            margin: 0 0 22px;
        }

        .error {
            font-size: 12px;
            color: #E08A8A;
            margin: -10px 0 16px;
            display: none;
        }
    </style>
</head>
<body>

    <div class="card">
        <p class="eyebrow">WayToGo Hotels</p>
        <h1>Start your booking</h1>
        <p class="sub">Enter your details and travel dates below.</p>
        <span class="hotel-id-badge">Hotel ID: <%= (hotel_id != null) ? hotel_id : "Not specified" %></span>

        <% if ("invalid_dates".equals(error)) { %>
            <p class="server-error">Check-out date must be after check-in date. Please choose valid travel dates.</p>
        <% } %>

        <form id="bookingForm" action="bookings" method="post">
            <input type="hidden" name="hotel_id" value="<%= (hotel_id != null) ? hotel_id : "" %>">

            <% if (roomIds != null && !roomIds.isEmpty()) { %>
                <div class="rail-heading">
                    <h2>Available Rooms</h2>
                    <span>Scroll &rarr;</span>
                </div>
                <div class="room-rail">
                    <% for (Map.Entry<String, String> entry : roomIds.entrySet()) { %>
                        <a href="bookings?room_id=<%= entry.getKey() %>" class="room-card">
                            <span class="room-number"><%= entry.getValue() %></span>
                        </a>
                    <% } %>
                </div>
            <% } else if (roomIds != null) { %>
                <p class="no-rooms">No rooms available for the selected dates and room type.</p>
            <% } %>

            <div class="field">
                <label for="name">Full Name</label>
                <input type="text" id="name" name="name" placeholder="e.g. Abheek Sharma" required>
            </div>

            <div class="field">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="you@example.com" required>
            </div>

            <div class="row">
                <div class="field">
                    <label for="check_in">Check-In</label>
                    <input type="date" id="check_in" name="check_in" required>
                </div>
                <div class="field">
                    <label for="check_out">Check-Out</label>
                    <input type="date" id="check_out" name="check_out" required>
                </div>
            </div>

            <div class="field">
                <label for="room_type">Room Type</label>
                <select id="room_type" name="room_type" required>
                    <option value="" disabled selected>Select a room type</option>
                    <option value="Single Room">Single Room</option>
                    <option value="Double Room">Double Room</option>
                    <option value="Deluxe">Deluxe</option>
                </select>
            </div>

            <p class="error" id="dateError">Check-out date must be after check-in date.</p>

            <button type="submit">Confirm Booking</button>
        </form>
    </div>

    <script>
        document.getElementById('bookingForm').addEventListener('submit', function(e) {
            const checkIn = document.getElementById('check_in').value;
            const checkOut = document.getElementById('check_out').value;
            const errorMsg = document.getElementById('dateError');

            if (checkIn && checkOut && checkOut <= checkIn) {
                e.preventDefault();
                errorMsg.style.display = 'block';
            } else {
                errorMsg.style.display = 'none';
            }
        });
    </script>

</body>
</html>
