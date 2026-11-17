<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList, java.util.HashMap" %>
<%!
    String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    String hotel_id   = (String) request.getAttribute("hotel_id");
    String hotel_name = (String) request.getAttribute("hotel_name");
    String status     = (String) request.getAttribute("status");

    @SuppressWarnings("unchecked")
    HashMap<String, String[]> bookings =
        (HashMap<String, String[]>) request.getAttribute("bookings");

    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String, String>> roomList =
        (ArrayList<HashMap<String, String>>) request.getAttribute("roomList");

    if (hotel_id == null) {
        response.sendRedirect(request.getContextPath() + "/H_signin.html");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= esc(hotel_name) %> — Dashboard | WayToGo Hotels</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Fraunces:opsz,wght@9..144,400;9..144,500;9..144,600&family=Work+Sans:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        :root {
            --ink:        #10141F;
            --panel:      #171C29;
            --panel-line: #2A3143;
            --brass:      #C6A15B;
            --brass-soft: rgba(198,161,91,0.25);
            --parchment:  #EDE7DA;
            --slate:      #8890A3;
            --danger:     #E08A8A;
            --success:    #7FBF8C;
        }

        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            min-height: 100vh;
            background: var(--ink);
            background-image:
                radial-gradient(circle at 12% 8%,  rgba(198,161,91,0.06), transparent 40%),
                radial-gradient(circle at 88% 92%, rgba(198,161,91,0.05), transparent 40%);
            font-family: 'Work Sans', sans-serif;
            color: var(--parchment);
        }

        /* ── Top bar ───────────────────────────────────────────────────────── */
        .topbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 20px 44px;
            border-bottom: 1px solid var(--panel-line);
            background: var(--panel);
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .wordmark {
            font-family: 'Fraunces', serif;
            font-weight: 500;
            font-size: 19px;
            color: var(--parchment);
        }

        .wordmark span { color: var(--brass); }

        .topbar-right {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .hotel-badge {
            background: var(--brass-soft);
            border: 1px solid var(--brass);
            color: var(--brass);
            border-radius: 20px;
            padding: 4px 12px;
            font-size: 11px;
            font-weight: 600;
            letter-spacing: 0.08em;
            text-transform: uppercase;
        }

        .topbar-right a {
            color: var(--slate);
            text-decoration: none;
            font-size: 13px;
            transition: color 0.15s;
        }

        .topbar-right a:hover { color: var(--brass); }

        /* ── Layout ────────────────────────────────────────────────────────── */
        .layout {
            display: flex;
            min-height: calc(100vh - 61px);
        }

        /* ── Sidebar ───────────────────────────────────────────────────────── */
        .sidebar {
            width: 200px;
            flex-shrink: 0;
            background: var(--panel);
            border-right: 1px solid var(--panel-line);
            padding: 28px 0;
        }

        .sidebar-label {
            font-size: 10px;
            letter-spacing: 0.18em;
            text-transform: uppercase;
            color: var(--slate);
            padding: 0 22px 10px;
        }

        .nav-link {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 11px 22px;
            font-size: 13.5px;
            color: var(--slate);
            text-decoration: none;
            border-left: 3px solid transparent;
            transition: color 0.15s, background 0.15s;
            cursor: pointer;
        }

        .nav-link:hover {
            color: var(--parchment);
            background: rgba(255,255,255,0.03);
        }

        .nav-link.active {
            color: var(--brass);
            border-left-color: var(--brass);
            background: var(--brass-soft);
        }

        /* ── Main content ──────────────────────────────────────────────────── */
        .main {
            flex: 1;
            padding: 36px 44px;
            overflow-x: auto;
        }

        .page-heading {
            font-family: 'Fraunces', serif;
            font-weight: 500;
            font-size: 24px;
            margin-bottom: 6px;
        }

        .page-sub {
            font-size: 13px;
            color: var(--slate);
            margin-bottom: 28px;
        }

        .hotel-id-badge {
            display: inline-block;
            font-size: 12px;
            color: var(--brass);
            background: var(--brass-soft);
            border: 1px solid rgba(198,161,91,0.35);
            border-radius: 20px;
            padding: 4px 14px;
            margin-bottom: 32px;
        }

        /* ── Status banner ─────────────────────────────────────────────────── */
        .banner {
            border-radius: 8px;
            padding: 11px 16px;
            font-size: 13px;
            margin-bottom: 24px;
            display: none;
        }

        .banner.ok {
            background: rgba(127,191,140,0.1);
            border: 1px solid rgba(127,191,140,0.35);
            color: var(--success);
        }

        .banner.fail {
            background: rgba(224,138,138,0.1);
            border: 1px solid rgba(224,138,138,0.35);
            color: var(--danger);
        }

        /* ── Section heading ───────────────────────────────────────────────── */
        .section-heading {
            font-family: 'Fraunces', serif;
            font-weight: 500;
            font-size: 17px;
            margin-bottom: 14px;
            padding-bottom: 10px;
            border-bottom: 1px solid var(--panel-line);
        }

        /* ── Table ─────────────────────────────────────────────────────────── */
        .table-wrap {
            overflow-x: auto;
            border: 1px solid var(--panel-line);
            border-radius: 10px;
            margin-bottom: 40px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 13.5px;
        }

        thead tr { background: #0D1119; }

        th {
            text-align: left;
            padding: 12px 18px;
            font-size: 11px;
            letter-spacing: 0.1em;
            text-transform: uppercase;
            color: var(--slate);
            white-space: nowrap;
        }

        td {
            padding: 13px 18px;
            border-top: 1px solid var(--panel-line);
            color: var(--parchment);
            vertical-align: middle;
        }

        tr:hover td { background: rgba(255,255,255,0.02); }

        .empty-row td {
            text-align: center;
            color: var(--slate);
            font-style: italic;
            padding: 32px;
        }

        /* ── Delete button ─────────────────────────────────────────────────── */
        .btn-delete {
            background: transparent;
            border: 1px solid rgba(224,138,138,0.4);
            color: var(--danger);
            border-radius: 6px;
            padding: 5px 12px;
            font-size: 12px;
            font-family: 'Work Sans', sans-serif;
            cursor: pointer;
            text-decoration: none;
            transition: background 0.15s;
            white-space: nowrap;
        }

        .btn-delete:hover { background: rgba(224,138,138,0.1); }

        /* ── Add room form ─────────────────────────────────────────────────── */
        .add-form {
            background: var(--panel);
            border: 1px solid var(--panel-line);
            border-radius: 12px;
            padding: 24px 26px;
            margin-bottom: 40px;
            max-width: 560px;
        }

        .add-form h3 {
            font-family: 'Fraunces', serif;
            font-weight: 500;
            font-size: 16px;
            margin-bottom: 18px;
            color: var(--parchment);
        }

        .form-row {
            display: flex;
            gap: 14px;
            flex-wrap: wrap;
            margin-bottom: 14px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            flex: 1;
            min-width: 140px;
        }

        .form-group label {
            font-size: 11px;
            letter-spacing: 0.08em;
            text-transform: uppercase;
            color: var(--slate);
            margin-bottom: 6px;
        }

        .form-group input,
        .form-group select {
            background: #0D1119;
            border: 1px solid var(--panel-line);
            border-radius: 7px;
            padding: 10px 12px;
            font-family: 'Work Sans', sans-serif;
            font-size: 13.5px;
            color: var(--parchment);
            outline: none;
            transition: border-color 0.15s;
        }

        .form-group input:focus,
        .form-group select:focus { border-color: var(--brass-soft); }

        .form-group select option { background: var(--panel); }

        .btn-add {
            background: var(--brass);
            color: #10141F;
            border: none;
            border-radius: 7px;
            padding: 10px 22px;
            font-family: 'Work Sans', sans-serif;
            font-weight: 600;
            font-size: 13.5px;
            cursor: pointer;
            transition: opacity 0.15s;
            margin-top: 4px;
        }

        .btn-add:hover { opacity: 0.88; }

        /* ── Tab sections ──────────────────────────────────────────────────── */
        .tab-section { display: none; }
        .tab-section.active { display: block; }

        @media (max-width: 640px) {
            .topbar, .main { padding-left: 20px; padding-right: 20px; }
            .sidebar { display: none; }
        }
    </style>
</head>
<body>

<!-- Top bar -->
<div class="topbar">
    <div class="wordmark">Way<span>To</span>Go Hotels</div>
    <div class="topbar-right">
        <span class="hotel-badge"><%= esc(hotel_name) %></span>
        <a href="hotel_fetch">Home</a>
    </div>
</div>

<div class="layout">

    <!-- Sidebar -->
    <nav class="sidebar">
        <p class="sidebar-label">Dashboard</p>
        <a class="nav-link" id="nav-bookings" onclick="showTab('bookings')">
            📋 Bookings
        </a>
        <a class="nav-link" id="nav-rooms" onclick="showTab('rooms')">
            🛏 Rooms
        </a>
    </nav>

    <!-- Main -->
    <main class="main">

        <h1 class="page-heading"><%= esc(hotel_name) %></h1>
        <p class="page-sub">Manage your property — bookings and rooms.</p>
        <span class="hotel-id-badge">Hotel ID: <%= esc(hotel_id) %></span>

        <!-- Status banner -->
        <div class="banner" id="statusBanner"></div>

        <!-- ================================================================
             BOOKINGS TAB
             ================================================================ -->
        <div class="tab-section" id="tab-bookings">
            <p class="section-heading">Your Bookings</p>
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Hotel ID</th>
                            <th>Room ID</th>
                            <th>Check In</th>
                            <th>Check Out</th>
                            <th>Nights</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        if (bookings == null || bookings.isEmpty()) {
                    %>
                        <tr class="empty-row">
                            <td colspan="6">No bookings yet for your property.</td>
                        </tr>
                    <%
                        } else {
                            for (HashMap.Entry<String, String[]> entry : bookings.entrySet()) {
                                String bookingId = entry.getKey();
                                String[] d = entry.getValue();
                    %>
                        <tr>
                            <td>#<%= esc(bookingId) %></td>
                            <td><%= esc(d[0]) %></td>
                            <td><%= esc(d[1]) %></td>
                            <td><%= esc(d[2]) %></td>
                            <td><%= esc(d[3]) %></td>
                            <td><%= esc(d[4]) %></td>
                        </tr>
                    <%      }
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- ================================================================
             ROOMS TAB
             ================================================================ -->
        <div class="tab-section" id="tab-rooms">
            <p class="section-heading">Your Rooms</p>

            <!-- Add room form -->
            <div class="add-form">
                <h3>Add a New Room</h3>
                <form action="rooms" method="post">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="room_number">Room Number</label>
                            <input type="text" id="room_number" name="room_number"
                                   placeholder="e.g. 101" required>
                        </div>
                        <div class="form-group">
                            <label for="room_type">Room Type</label>
                            <select id="room_type" name="room_type" required>
                                <option value="" disabled selected>Select type</option>
                                <option value="Single Room">Single Room</option>
                                <option value="Double Room">Double Room</option>
                                <option value="Deluxe">Deluxe</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="price_per_night">Price / Night (₹)</label>
                            <input type="number" id="price_per_night" name="price_per_night"
                                   placeholder="e.g. 2500" min="1" step="0.01" required>
                        </div>
                    </div>
                    <button type="submit" class="btn-add">Add Room</button>
                </form>
            </div>

            <!-- Rooms table -->
            <div class="table-wrap">
                <table>
                    <thead>
                        <tr>
                            <th>Room ID</th>
                            <th>Room Number</th>
                            <th>Type</th>
                            <th>Price / Night (₹)</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        if (roomList == null || roomList.isEmpty()) {
                    %>
                        <tr class="empty-row">
                            <td colspan="5">No rooms added yet. Use the form above to add one.</td>
                        </tr>
                    <%
                        } else {
                            for (HashMap<String, String> room : roomList) {
                    %>
                        <tr>
                            <td><%= esc(room.get("room_id")) %></td>
                            <td><%= esc(room.get("room_number")) %></td>
                            <td><%= esc(room.get("room_type")) %></td>
                            <td>&#8377;<%= esc(room.get("price_per_night")) %></td>
                            <td>
                                <a class="btn-delete"
                                   href="rooms?action=delete&room_id=<%= esc(room.get("room_id")) %>"
                                   onclick="return confirm('Delete room <%= esc(room.get("room_number")) %>? This cannot be undone.')">
                                   Delete
                                </a>
                            </td>
                        </tr>
                    <%      }
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>

    </main>
</div>

<script>
    // ── Tab switching ─────────────────────────────────────────────────────────
    function showTab(name) {
        document.querySelectorAll('.tab-section').forEach(function(s) {
            s.classList.remove('active');
        });
        document.querySelectorAll('.nav-link').forEach(function(l) {
            l.classList.remove('active');
        });
        document.getElementById('tab-' + name).classList.add('active');
        document.getElementById('nav-' + name).classList.add('active');
        localStorage.setItem('hotelTab', name);
    }

    // ── Restore last tab or default to bookings ───────────────────────────────
    (function () {
        // Check if we arrived from rooms servlet (roomList present means rooms tab)
        var hasRoomList = <%= (roomList != null) ? "true" : "false" %>;
        var saved = localStorage.getItem('hotelTab');

        // If arrived via rooms servlet, show rooms tab
        var status = '<%= status != null ? status : "" %>';
        if (status.indexOf('add') !== -1 || status.indexOf('delete') !== -1 || hasRoomList) {
            showTab('rooms');
        } else if (saved) {
            showTab(saved);
        } else {
            showTab('bookings');
        }
    })();

    // ── Status banner ─────────────────────────────────────────────────────────
    (function () {
        var status  = '<%= status != null ? status : "" %>';
        var banner  = document.getElementById('statusBanner');
        var messages = {
            add_ok:     'Room added successfully.',
            add_fail:   'Failed to add room. Please check your inputs and try again.',
            delete_ok:  'Room deleted successfully.',
            delete_fail:'Failed to delete room. It may have active bookings.'
        };
        if (status && messages[status]) {
            banner.textContent = messages[status];
            banner.classList.add(status.endsWith('_ok') ? 'ok' : 'fail');
            banner.style.display = 'block';
            setTimeout(function() { banner.style.display = 'none'; }, 4000);
        }
    })();
</script>

</body>
</html>
