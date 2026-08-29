<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList, java.util.HashMap" %>
<%
    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String, String>> hotelList =
        (ArrayList<HashMap<String, String>>) request.getAttribute("hotelList");
    if (hotelList == null) {
        response.sendRedirect(request.getContextPath() + "/hotel_fetch");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>WayToGo Hotels — Find a Stay</title>
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
        --brass-dim: rgba(198, 161, 91, 0.14);
        --parchment: #EDE7DA;
        --slate: #8890A3;
    }

    * { box-sizing: border-box; }

    html, body {
        margin: 0;
        padding: 0;
        min-height: 100%;
        background: var(--ink);
        background-image:
            radial-gradient(circle at 12% 8%, rgba(198,161,91,0.06), transparent 40%),
            radial-gradient(circle at 88% 92%, rgba(198,161,91,0.05), transparent 40%);
        font-family: 'Work Sans', sans-serif;
        color: var(--parchment);
    }

    /* ---------- Top bar ---------- */
    .topbar {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 22px 44px;
        border-bottom: 1px solid var(--panel-line);
    }

    .wordmark {
        font-family: 'Fraunces', serif;
        font-weight: 500;
        font-size: 20px;
        letter-spacing: 0.01em;
        color: var(--parchment);
    }

    .wordmark span { color: var(--brass); }

    .topbar-links {
        display: flex;
        gap: 28px;
        font-size: 13px;
        letter-spacing: 0.04em;
        text-transform: uppercase;
        color: var(--slate);
    }

    .topbar-links a {
        color: var(--slate);
        text-decoration: none;
        transition: color 0.15s ease;
    }
    .topbar-links a:hover { color: var(--brass); }

    /* ---------- Hero ---------- */
    .hero {
        display: flex;
        align-items: flex-start;
        justify-content: space-between;
        gap: 40px;
        padding: 64px 44px 40px;
        flex-wrap: wrap;
    }

    .hero-copy { max-width: 480px; }

    .eyebrow {
        font-size: 11px;
        letter-spacing: 0.22em;
        text-transform: uppercase;
        color: var(--brass);
        margin: 0 0 14px;
    }

    .hero h1 {
        font-family: 'Fraunces', serif;
        font-weight: 500;
        font-size: 42px;
        line-height: 1.12;
        margin: 0 0 14px;
        color: var(--parchment);
    }

    .hero p {
        font-size: 14.5px;
        line-height: 1.6;
        color: var(--slate);
        margin: 0;
    }

    /* ---------- Locator panel ---------- */
    .locator {
        background: var(--panel);
        border: 1px solid var(--panel-line);
        border-radius: 14px;
        padding: 26px 26px 24px;
        width: 100%;
        max-width: 320px;
        box-shadow: 0 30px 60px -20px rgba(0,0,0,0.6);
        flex-shrink: 0;
        position: relative;
    }

    .locator::before {
        content: "";
        position: absolute;
        top: 14px;
        right: 14px;
        width: 7px;
        height: 7px;
        border-radius: 50%;
        background: var(--brass);
        box-shadow: 0 0 0 3px var(--brass-dim);
    }

    .locator-label {
        font-size: 11px;
        letter-spacing: 0.18em;
        text-transform: uppercase;
        color: var(--brass);
        margin: 0 0 16px;
    }

    .field { margin-bottom: 14px; }

    label {
        display: block;
        font-size: 11px;
        letter-spacing: 0.06em;
        text-transform: uppercase;
        color: var(--slate);
        margin-bottom: 7px;
    }

    .required-mark { color: var(--brass); }

    input[type="text"] {
        width: 100%;
        background: #0D1119;
        border: 1px solid var(--panel-line);
        border-radius: 7px;
        padding: 11px 12px;
        font-family: 'Work Sans', sans-serif;
        font-size: 14px;
        color: var(--parchment);
        outline: none;
        transition: border-color 0.15s ease, box-shadow 0.15s ease;
    }

    input::placeholder { color: #545C6E; }

    input:focus {
        border-color: var(--brass);
        box-shadow: 0 0 0 3px var(--brass-soft);
    }

    .locator button {
        width: 100%;
        margin-top: 6px;
        padding: 12px;
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

    .locator button:hover { background: #D4B06E; }
    .locator button:active { transform: translateY(1px); }

    .locator-hint {
        font-size: 11.5px;
        color: var(--slate);
        margin: 10px 0 0;
        line-height: 1.4;
    }

    /* ---------- Rail ---------- */
    .rail-section { padding: 20px 0 60px; }

    .rail-heading {
        display: flex;
        align-items: baseline;
        justify-content: space-between;
        padding: 0 44px;
        margin-bottom: 22px;
    }

    .rail-heading h2 {
        font-family: 'Fraunces', serif;
        font-weight: 500;
        font-size: 22px;
        margin: 0;
        color: var(--parchment);
    }

    .rail-heading .count {
        font-size: 12.5px;
        color: var(--slate);
    }

    .rail-outer {
        -webkit-mask-image: linear-gradient(to right, transparent 0, black 44px, black calc(100% - 44px), transparent 100%);
        mask-image: linear-gradient(to right, transparent 0, black 44px, black calc(100% - 44px), transparent 100%);
    }

    .rail {
        display: flex;
        gap: 20px;
        overflow-x: auto;
        padding: 6px 44px 26px;
        scroll-snap-type: x proximity;
        scrollbar-width: thin;
        scrollbar-color: var(--brass-soft) transparent;
    }

    .rail::-webkit-scrollbar { height: 6px; }
    .rail::-webkit-scrollbar-track { background: transparent; }
    .rail::-webkit-scrollbar-thumb { background: var(--brass-soft); border-radius: 4px; }

    .hotel-card {
        scroll-snap-align: start;
        flex: 0 0 268px;
        background: var(--panel);
        border: 1px solid var(--panel-line);
        border-radius: 14px;
        overflow: hidden;
        position: relative;
        transition: transform 0.18s ease, border-color 0.18s ease, box-shadow 0.18s ease;
        cursor: pointer;
    }

    .hotel-card:hover {
        transform: translateY(-4px);
        border-color: var(--brass-soft);
        box-shadow: 0 24px 40px -18px rgba(0,0,0,0.7);
    }

    .pin {
        position: absolute;
        top: 12px;
        left: 12px;
        width: 22px;
        height: 22px;
        border-radius: 50%;
        background: var(--brass);
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 4px 10px rgba(0,0,0,0.5);
        z-index: 2;
    }

    .hotel-photo {
        width: 100%;
        height: 150px;
        object-fit: cover;
        display: block;
        background: #0D1119;
    }

    .hotel-body { padding: 16px 16px 18px; }

    .hotel-loc {
        font-size: 10.5px;
        letter-spacing: 0.12em;
        text-transform: uppercase;
        color: var(--brass);
        margin: 0 0 6px;
    }

    .hotel-name {
        font-family: 'Fraunces', serif;
        font-weight: 500;
        font-size: 17px;
        margin: 0 0 8px;
        color: var(--parchment);
        line-height: 1.25;
    }

    .hotel-desc {
        font-size: 12.5px;
        color: var(--slate);
        line-height: 1.5;
        margin: 0;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

    .rail-empty {
        padding: 40px 44px;
        color: var(--slate);
        font-size: 13.5px;
    }

    @media (max-width: 640px) {
        .topbar, .hero, .rail-heading { padding-left: 20px; padding-right: 20px; }
        .rail { padding-left: 20px; padding-right: 20px; }
        .hero h1 { font-size: 32px; }
        .locator { max-width: 100%; }
    }
</style>
</head>
<body>

    <div class="topbar">
        <div class="wordmark">WayToGo <span>Hotels</span></div>
        <div class="topbar-links">
            <a href="H_signup.html">List your hotel</a>
            <a href="H_signin.html">Sign in</a>
        </div>
    </div>

    <div class="hero">
        <div class="hero-copy">
            <p class="eyebrow">Property Network</p>
            <h1>Find your next stay, anywhere on the map.</h1>
            <p>Browse hotels across the network, or narrow it down to a state and city.</p>
        </div>

        <div class="locator">
            <p class="locator-label">Search by location</p>
            <form action="hotel_fetch" method="post">
                <div class="field">
                    <label for="state">State <span class="required-mark">*</span></label>
                    <input type="text" id="state" name="state" placeholder="e.g. West Bengal" required>
                </div>
                <div class="field">
                    <label for="city">City <span class="required-mark">*</span></label>
                    <input type="text" id="city" name="city" placeholder="e.g. Asansol" required>
                </div>
                <button type="submit">Search hotels</button>
            </form>
            <p class="locator-hint">Results are matched against the state and city you enter.</p>
        </div>
    </div>

    <div class="rail-section">
        <div class="rail-heading">
            <h2>
                <% if (request.getMethod().equalsIgnoreCase("POST")) { %>
                    Search results
                <% } else { %>
                    Featured properties
                <% } %>
            </h2>
            <span class="count">
                <% if (hotelList != null && !hotelList.isEmpty()) { %>
                    <%= hotelList.size() %> <%= hotelList.size() == 1 ? "property" : "properties" %>
                <% } %>
            </span>
        </div>
        <div class="rail-outer">
            <div class="rail">
                <% if (hotelList == null || hotelList.isEmpty()) { %>
                    <div class="rail-empty">No properties found for that location.</div>
                <% } else {
                    for (HashMap<String, String> hotel : hotelList) {
                        String name     = hotel.get("hotel_name");
                        String city     = hotel.get("city");
                        String state    = hotel.get("state");
                        String desc     = hotel.get("description");
                        String imgLink  = hotel.get("h_image_link");
                        
                %>
                    <div class="hotel-card">
                        <div class="pin">
                            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7z" fill="#171208"/>
                                <circle cx="12" cy="9" r="2.4" fill="#C6A15B"/>
                            </svg>
                        </div>
                        <img class="hotel-photo" src="<%= imgLink %>"  loading="lazy">
                        <div class="hotel-body">
                            <p class="hotel-loc"><%= city %>, <%= state %></p>
                            <h3 class="hotel-name"><%= name %></h3>
                            <p class="hotel-desc"><%= desc != null ? desc : "" %></p>
                        </div>
                    </div>
                <% } } %>
            </div>
        </div>
    </div>

</body>
</html>
