<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login – SnapVault</title>
    <meta name="description" content="Access SnapVault admin panel.">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: 'Inter', sans-serif;
            min-height: 100vh;
            background: #0a0a0f;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            color: rgba(255,255,255,0.85);
        }

        /* ── Background Elements ── */
        .bg {
            position: fixed; inset: 0; z-index: 0;
            background: radial-gradient(ellipse at 20% 50%, rgba(120,40,200,0.25) 0%, transparent 60%),
                        radial-gradient(ellipse at 80% 20%, rgba(40,100,255,0.2) 0%, transparent 50%),
                        radial-gradient(ellipse at 60% 80%, rgba(200,60,120,0.15) 0%, transparent 50%),
                        #0a0a0f;
        }
        .orb {
            position: fixed; border-radius: 50%; filter: blur(80px); opacity: 0.5; z-index: 0;
            animation: float 8s ease-in-out infinite;
        }
        .orb1 { width: 400px; height: 400px; background: rgba(120,40,200,0.4); top: -100px; left: -100px; animation-delay: 0s; }
        .orb2 { width: 350px; height: 350px; background: rgba(40,100,255,0.35); bottom: -80px; right: -80px; animation-delay: 3s; }
        
        @keyframes float {
            0%, 100% { transform: translateY(0px) scale(1); }
            50% { transform: translateY(-30px) scale(1.05); }
        }

        /* ── Login Card ── */
        .card {
            position: relative; z-index: 1;
            width: 100%; max-width: 440px;
            margin: 20px;
            padding: 40px 32px;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 24px;
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
            box-shadow: 0 40px 80px rgba(0,0,0,0.5), inset 0 1px 0 rgba(255,255,255,0.1);
            animation: slideUp 0.6s cubic-bezier(0.16,1,0.3,1) both;
        }

        @keyframes slideUp {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }

        h1 {
            font-size: 1.8rem; font-weight: 700;
            background: linear-gradient(135deg, #fff 40%, rgba(255,255,255,0.5));
            -webkit-background-clip: text; -webkit-text-fill-color: transparent;
            background-clip: text;
            text-align: center;
            margin-bottom: 8px;
        }

        .hint {
            text-align: center;
            color: rgba(255,255,255,0.4);
            font-size: 0.82rem;
            margin-bottom: 24px;
        }
        .hint strong { color: rgba(255,255,255,0.7); }

        label {
            display: block;
            margin-bottom: 8px;
            font-size: 0.85rem;
            font-weight: 500;
            color: rgba(255,255,255,0.7);
        }

        input {
            width: 100%;
            padding: 12px 16px;
            margin-bottom: 20px;
            background: rgba(255,255,255,0.05);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 10px;
            color: white;
            font-family: inherit;
            font-size: 0.95rem;
            transition: all 0.2s ease;
        }
        input:focus {
            outline: none;
            border-color: #a855f7;
            box-shadow: 0 0 10px rgba(168,85,247,0.3);
            background: rgba(255,255,255,0.08);
        }

        button {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #7828c8, #006FEE);
            color: white;
            font-weight: 600;
            font-size: 0.95rem;
            border: none;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.2s ease;
            box-shadow: 0 4px 24px rgba(120,40,200,0.35);
            margin-top: 8px;
        }
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 32px rgba(120,40,200,0.5);
        }

        /* ── Messages ── */
        .message {
            margin-bottom: 20px;
            padding: 12px 16px;
            border-radius: 10px;
            font-size: 0.88rem;
            text-align: center;
        }
        .error {
            color: #f87171;
            background: rgba(239,68,68,0.15);
            border: 1px solid rgba(239,68,68,0.3);
        }
        .success {
            color: #4ade80;
            background: rgba(34,197,94,0.15);
            border: 1px solid rgba(34,197,94,0.3);
        }

        /* ── Back Links ── */
        .links {
            display: flex;
            justify-content: center;
            gap: 16px;
            margin-top: 24px;
            font-size: 0.85rem;
        }
        .links a {
            color: rgba(255,255,255,0.4);
            text-decoration: none;
            transition: color 0.2s;
        }
        .links a:hover {
            color: white;
        }
    </style>
</head>
<body>
<div class="bg"></div>
<div class="orb orb1"></div>
<div class="orb orb2"></div>

<div class="card">
    <h1>Admin Login</h1>
    <p class="hint">Default login: <strong>admin</strong> / <strong>admin123</strong></p>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <c:if test="${param.loggedOut == '1'}">
        <div class="message success">You have been logged out.</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/admin/login" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" value="admin" required autocomplete="username">

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required autocomplete="current-password">

        <button type="submit">Login</button>
    </form>

    <div class="links">
        <a href="${pageContext.request.contextPath}/">← Home</a>
        <span style="color: rgba(255,255,255,0.1)">|</span>
        <a href="${pageContext.request.contextPath}/gallery">Gallery</a>
    </div>
</div>

</body>
</html>
