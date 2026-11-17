<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
    String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    String hotel_id    = (String) request.getAttribute("hotel_id");
    String hotel_name  = (String) request.getAttribute("hotel_name");
%>
<!DOCTYPE html>
<html>
<head>
    <title><%= (hotel_name != null) ? esc(hotel_name) : "Hotel" %> - WayToGo Hotels</title>
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
            max-width: 520px;
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
            font-size: 28px;
            margin: 0 0 14px;
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
            margin: 0 0 24px;
        }

        .section-label {
            font-size: 11px;
            letter-spacing: 0.06em;
            text-transform: uppercase;
            color: var(--slate);
            margin: 0 0 8px;
        }

        .description {
            background: #0D1119;
            border: 1px solid var(--panel-line);
            border-radius: 10px;
            padding: 16px 18px;
            font-size: 14px;
            line-height: 1.65;
            color: var(--parchment);
            margin: 0 0 28px;
        }

        .description.empty { color: var(--slate); }

        a.button {
            display: block;
            text-align: center;
            padding: 13px;
            border-radius: 7px;
            background: var(--brass);
            color: #171208;
            font-family: 'Work Sans', sans-serif;
            font-weight: 600;
            font-size: 14px;
            text-decoration: none;
            transition: background 0.15s ease, transform 0.05s ease;
        }

        a.button:hover { background: #D4B06E; }
        a.button:active { transform: translateY(1px); }
    </style>
</head>
<body>

    <div class="card">
        <p class="eyebrow">WayToGo Hotels</p>
        <h1><%= (hotel_name != null) ? esc(hotel_name) : "Hotel name unavailable" %></h1>
        <span class="hotel-id-badge">Hotel ID: <%= (hotel_id != null) ? esc(hotel_id) : "Not specified" %></span>

        <p class="section-label">About this hotel</p>

        <a class="button" href="Hotels?hotel_id=<%= (hotel_id != null) ? esc(hotel_id) : "" %>">View Hotel</a>
    </div>

</body>
</html>
