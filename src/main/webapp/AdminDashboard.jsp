<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList, java.util.HashMap" %>
<%
    // ── Session guard ─────────────────────────────────────────────────────────
    String adminEmail = (String) session.getAttribute("admin_email");
    String adminName  = (String) session.getAttribute("admin_name");
    if (adminEmail == null) {
        response.sendRedirect(request.getContextPath() + "/A_signin.html");
        return;
    }

    // ── Data from servlet ─────────────────────────────────────────────────────
    String activeTab = (String) request.getAttribute("activeTab");
    if (activeTab == null) activeTab = "overview";

    String statusParam = (String) request.getAttribute("status");

    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String,String>> hotels =
        (ArrayList<HashMap<String,String>>) request.getAttribute("hotels");

    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String,String>> users =
        (ArrayList<HashMap<String,String>>) request.getAttribute("users");

    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String,String>> bookings =
        (ArrayList<HashMap<String,String>>) request.getAttribute("bookings");

    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String,String>> commissions =
        (ArrayList<HashMap<String,String>>) request.getAttribute("commissions");

    // ── Commission total ──────────────────────────────────────────────────────
    double grandTotal = 0.0;
    if (commissions != null) {
        for (HashMap<String,String> c : commissions) {
            try { grandTotal += Double.parseDouble(c.get("total_commission")); }
            catch (Exception ignored) {}
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard — WayToGo Hotels</title>
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
            font-family: 'Work Sans', sans-serif;
            color: var(--parchment);
        }

        /* ── Top bar ─────────────────────────────────────────────────────── */
        .topbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 18px 40px;
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
            font-size: 13px;
        }

        .admin-badge {
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

        .admin-name { color: var(--slate); font-size: 13px; }

        .logout-btn {
            color: var(--danger);
            text-decoration: none;
            font-size: 13px;
            font-weight: 500;
            transition: opacity 0.15s;
        }

        .logout-btn:hover { opacity: 0.75; }

        /* ── Layout ──────────────────────────────────────────────────────── */
        .layout {
            display: flex;
            min-height: calc(100vh - 61px);
        }

        /* ── Sidebar ─────────────────────────────────────────────────────── */
        .sidebar {
            width: 210px;
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
            transition: color 0.15s, border-color 0.15s, background 0.15s;
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

        .nav-icon { font-size: 15px; width: 18px; text-align: center; }

        /* ── Main content ────────────────────────────────────────────────── */
        .main {
            flex: 1;
            padding: 36px 40px;
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
            margin-bottom: 30px;
        }

        /* ── Status banner ───────────────────────────────────────────────── */
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

        /* ── Overview stat cards ─────────────────────────────────────────── */
        .stat-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(190px, 1fr));
            gap: 16px;
            margin-bottom: 36px;
        }

        .stat-card {
            background: var(--panel);
            border: 1px solid var(--panel-line);
            border-radius: 12px;
            padding: 20px 22px;
        }

        .stat-label {
            font-size: 11px;
            letter-spacing: 0.14em;
            text-transform: uppercase;
            color: var(--slate);
            margin-bottom: 8px;
        }

        .stat-value {
            font-family: 'Fraunces', serif;
            font-size: 30px;
            font-weight: 500;
            color: var(--brass);
        }

        /* ── Section heading ─────────────────────────────────────────────── */
        .section-heading {
            font-family: 'Fraunces', serif;
            font-weight: 500;
            font-size: 17px;
            margin-bottom: 14px;
            padding-bottom: 10px;
            border-bottom: 1px solid var(--panel-line);
        }

        /* ── Table ───────────────────────────────────────────────────────── */
        .table-wrap {
            overflow-x: auto;
            border: 1px solid var(--panel-line);
            border-radius: 10px;
            margin-bottom: 40px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 13px;
        }

        thead tr {
            background: #0D1119;
        }

        th {
            text-align: left;
            padding: 11px 16px;
            font-size: 11px;
            letter-spacing: 0.1em;
            text-transform: uppercase;
            color: var(--slate);
            white-space: nowrap;
        }

        td {
            padding: 12px 16px;
            border-top: 1px solid var(--panel-line);
            color: var(--parchment);
            vertical-align: middle;
        }

        tr:hover td { background: rgba(255,255,255,0.02); }

        .empty-row td {
            text-align: center;
            color: var(--slate);
            font-style: italic;
            padding: 24px;
        }

        /* ── Status pill ─────────────────────────────────────────────────── */
        .pill {
            display: inline-block;
            border-radius: 20px;
            padding: 3px 10px;
            font-size: 11px;
            font-weight: 600;
            letter-spacing: 0.06em;
            text-transform: uppercase;
        }

        .pill-pending  { background: rgba(198,161,91,0.15); color: var(--brass); }
        .pill-confirmed{ background: rgba(127,191,140,0.15); color: var(--success); }
        .pill-cancelled{ background: rgba(224,138,138,0.15); color: var(--danger); }

        /* ── Delete button ───────────────────────────────────────────────── */
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

        .btn-delete:hover {
            background: rgba(224,138,138,0.1);
        }

        /* ── Commission total row ────────────────────────────────────────── */
        .total-row td {
            font-weight: 600;
            color: var(--brass);
            border-top: 2px solid var(--panel-line);
        }
    </style>
</head>
<body>

<!-- ── Top bar ──────────────────────────────────────────────────────────── -->
<div class="topbar">
    <div class="wordmark">Way<span>To</span>Go <span style="color:var(--slate);font-size:13px;font-family:'Work Sans',sans-serif;font-weight:400;">Admin</span></div>
    <div class="topbar-right">
        <span class="admin-badge">Admin</span>
        <span class="admin-name"><%= adminName %></span>
        <a class="logout-btn" href="AdminDashboard?action=logout">Sign out</a>
    </div>
</div>

<!-- ── Layout ───────────────────────────────────────────────────────────── -->
<div class="layout">

    <!-- Sidebar -->
    <nav class="sidebar">
        <p class="sidebar-label">Navigation</p>
        <a class="nav-link <%= "overview".equals(activeTab) ? "active" : "" %>"
           href="AdminDashboard">
            <span class="nav-icon">⊞</span> Overview
        </a>
        <a class="nav-link <%= "hotels".equals(activeTab) ? "active" : "" %>"
           href="AdminDashboard?action=hotels">
            <span class="nav-icon">🏨</span> Hotels
        </a>
        <a class="nav-link <%= "users".equals(activeTab) ? "active" : "" %>"
           href="AdminDashboard?action=users">
            <span class="nav-icon">👤</span> Users
        </a>
        <a class="nav-link <%= "bookings".equals(activeTab) ? "active" : "" %>"
           href="AdminDashboard?action=bookings">
            <span class="nav-icon">📋</span> Bookings
        </a>
        <a class="nav-link <%= "commissions".equals(activeTab) ? "active" : "" %>"
           href="AdminDashboard?action=commissions">
            <span class="nav-icon">💰</span> Commissions
        </a>
    </nav>

    <!-- Main -->
    <main class="main">

        <!-- Status banner -->
        <div class="banner" id="statusBanner"></div>

        <!-- ================================================================
             OVERVIEW TAB
             ================================================================ -->
        <% if ("overview".equals(activeTab)) { %>

        <h1 class="page-heading">Dashboard Overview</h1>
        <p class="page-sub">Welcome back, <%= adminName %>. Here's a snapshot of the platform.</p>

        <!-- Stat cards -->
        <div class="stat-grid">
            <div class="stat-card">
                <p class="stat-label">Hotels</p>
                <p class="stat-value"><%= hotels != null ? hotels.size() : 0 %></p>
            </div>
            <div class="stat-card">
                <p class="stat-label">Users</p>
                <p class="stat-value"><%= users != null ? users.size() : 0 %></p>
            </div>
            <div class="stat-card">
                <p class="stat-label">Bookings</p>
                <p class="stat-value"><%= bookings != null ? bookings.size() : 0 %></p>
            </div>
            <div class="stat-card">
                <p class="stat-label">Total Commission</p>
                <p class="stat-value">&#8377;<%= String.format("%.0f", grandTotal) %></p>
            </div>
        </div>

        <!-- Recent bookings preview (top 5) -->
        <p class="section-heading">Recent Bookings</p>
        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>Booking ID</th>
                        <th>Guest</th>
                        <th>Hotel</th>
                        <th>Check In</th>
                        <th>Check Out</th>
                        <th>Total</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                <% if (bookings == null || bookings.isEmpty()) { %>
                    <tr class="empty-row"><td colspan="7">No bookings found.</td></tr>
                <% } else {
                       int limit = Math.min(5, bookings.size());
                       for (int i = 0; i < limit; i++) {
                           HashMap<String,String> b = bookings.get(i);
                           String status = b.get("booking_status");
                           String pillClass = "CONFIRMED".equalsIgnoreCase(status) ? "pill-confirmed"
                                            : "CANCELLED".equalsIgnoreCase(status) ? "pill-cancelled"
                                            : "pill-pending";
                %>
                    <tr>
                        <td>#<%= b.get("booking_id") %></td>
                        <td><%= b.get("name") %></td>
                        <td><%= b.get("hotel_name") %></td>
                        <td><%= b.get("check_in") %></td>
                        <td><%= b.get("check_out") %></td>
                        <td>&#8377;<%= b.get("total_price") %></td>
                        <td><span class="pill <%= pillClass %>"><%= status %></span></td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>

        <!-- ================================================================
             HOTELS TAB
             ================================================================ -->
        <% } else if ("hotels".equals(activeTab)) { %>

        <h1 class="page-heading">Hotels</h1>
        <p class="page-sub">All registered hotels on the platform.</p>

        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Hotel Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>State</th>
                        <th>City</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <% if (hotels == null || hotels.isEmpty()) { %>
                    <tr class="empty-row"><td colspan="7">No hotels found.</td></tr>
                <% } else { for (HashMap<String,String> h : hotels) { %>
                    <tr>
                        <td><%= h.get("hotel_id") %></td>
                        <td><%= h.get("hotel_name") %></td>
                        <td><%= h.get("email") %></td>
                        <td><%= h.get("phone") %></td>
                        <td><%= h.get("state") %></td>
                        <td><%= h.get("city") %></td>
                        <td>
                            <a class="btn-delete"
                               href="AdminDashboard?action=deleteHotel&hotel_id=<%= h.get("hotel_id") %>"
                               onclick="return confirm('Delete hotel <%= h.get("hotel_name").replace("'","") %> and all its rooms? This cannot be undone.')">
                               Delete
                            </a>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>

        <!-- ================================================================
             USERS TAB
             ================================================================ -->
        <% } else if ("users".equals(activeTab)) { %>

        <h1 class="page-heading">Users</h1>
        <p class="page-sub">All registered guests on the platform.</p>

        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>Type</th>
                        <th>Joined</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <% if (users == null || users.isEmpty()) { %>
                    <tr class="empty-row"><td colspan="8">No users found.</td></tr>
                <% } else { for (HashMap<String,String> u : users) { %>
                    <tr>
                        <td><%= u.get("user_id") %></td>
                        <td><%= u.get("name") %></td>
                        <td><%= u.get("email") %></td>
                        <td><%= u.get("phone") %></td>
                        <td><%= u.get("address") %></td>
                        <td><%= u.get("user_type") %></td>
                        <td><%= u.get("created_at") %></td>
                        <td>
                            <a class="btn-delete"
                               href="AdminDashboard?action=deleteUser&user_id=<%= u.get("user_id") %>"
                               onclick="return confirm('Delete user <%= u.get("name").replace("'","") %>? This cannot be undone.')">
                               Delete
                            </a>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>

        <!-- ================================================================
             BOOKINGS TAB
             ================================================================ -->
        <% } else if ("bookings".equals(activeTab)) { %>

        <h1 class="page-heading">Bookings</h1>
        <p class="page-sub">All reservations across the platform.</p>

        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Guest</th>
                        <th>Hotel</th>
                        <th>Room</th>
                        <th>Check In</th>
                        <th>Check Out</th>
                        <th>Nights</th>
                        <th>Total</th>
                        <th>Commission</th>
                        <th>Status</th>
                        <th>Booked At</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                <% if (bookings == null || bookings.isEmpty()) { %>
                    <tr class="empty-row"><td colspan="12">No bookings found.</td></tr>
                <% } else { for (HashMap<String,String> b : bookings) {
                       String status = b.get("booking_status");
                       String pillClass = "CONFIRMED".equalsIgnoreCase(status) ? "pill-confirmed"
                                        : "CANCELLED".equalsIgnoreCase(status) ? "pill-cancelled"
                                        : "pill-pending";
                %>
                    <tr>
                        <td>#<%= b.get("booking_id") %></td>
                        <td><%= b.get("name") %></td>
                        <td><%= b.get("hotel_name") %></td>
                        <td><%= b.get("room_id") %></td>
                        <td><%= b.get("check_in") %></td>
                        <td><%= b.get("check_out") %></td>
                        <td><%= b.get("number_of_days") %></td>
                        <td>&#8377;<%= b.get("total_price") %></td>
                        <td>&#8377;<%= b.get("commission_amount") %> (<%= b.get("commission_rate") %>%)</td>
                        <td><span class="pill <%= pillClass %>"><%= status %></span></td>
                        <td><%= b.get("booked_at") %></td>
                        <td>
                            <a class="btn-delete"
                               href="AdminDashboard?action=deleteBooking&booking_id=<%= b.get("booking_id") %>"
                               onclick="return confirm('Delete booking #<%= b.get("booking_id") %>? This cannot be undone.')">
                               Delete
                            </a>
                        </td>
                    </tr>
                <% } } %>
                </tbody>
            </table>
        </div>

        <!-- ================================================================
             COMMISSIONS TAB
             ================================================================ -->
        <% } else if ("commissions".equals(activeTab)) { %>

        <h1 class="page-heading">Commissions</h1>
        <p class="page-sub">Total commission earned per hotel.</p>

        <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>Hotel ID</th>
                        <th>Hotel Name</th>
                        <th>Bookings</th>
                        <th>Total Commission (&#8377;)</th>
                    </tr>
                </thead>
                <tbody>
                <% if (commissions == null || commissions.isEmpty()) { %>
                    <tr class="empty-row"><td colspan="4">No commission data available.</td></tr>
                <% } else { for (HashMap<String,String> c : commissions) { %>
                    <tr>
                        <td><%= c.get("hotel_id") %></td>
                        <td><%= c.get("hotel_name") %></td>
                        <td><%= c.get("booking_count") %></td>
                        <td>&#8377;<%= c.get("total_commission") %></td>
                    </tr>
                <% } %>
                    <tr class="total-row">
                        <td colspan="3">Grand Total</td>
                        <td>&#8377;<%= String.format("%.2f", grandTotal) %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>

        <% } %>

    </main>
</div>

<script>
    // ── Status banner from redirect param ────────────────────────────────────
    (function () {
        const status  = '<%= statusParam != null ? statusParam : "" %>';
        const banner  = document.getElementById('statusBanner');
        const messages = {
            deleted_hotel_ok:    'Hotel deleted successfully.',
            deleted_hotel_fail:  'Failed to delete hotel. Please try again.',
            deleted_user_ok:     'User deleted successfully.',
            deleted_user_fail:   'Failed to delete user. Please try again.',
            deleted_booking_ok:  'Booking deleted successfully.',
            deleted_booking_fail:'Failed to delete booking. Please try again.'
        };
        if (status && messages[status]) {
            banner.textContent  = messages[status];
            banner.classList.add(status.endsWith('_ok') ? 'ok' : 'fail');
            banner.style.display = 'block';
            // Auto-dismiss after 4 seconds
            setTimeout(() => { banner.style.display = 'none'; }, 4000);
        }
    })();
</script>

</body>
</html>
