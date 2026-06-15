<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SnapVault – Photo Gallery</title>
    <meta name="description" content="A premium photo gallery powered by Cloudinary and MySQL.">
    <!-- version 1.1 - auto-db -->
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
        }

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
        .orb3 { width: 250px; height: 250px; background: rgba(200,60,120,0.3); top: 50%; left: 60%; animation-delay: 6s; }

        @keyframes float {
            0%, 100% { transform: translateY(0px) scale(1); }
            50% { transform: translateY(-30px) scale(1.05); }
        }

        .container {
            position: relative; z-index: 1;
            width: 100%; max-width: 520px;
            margin: 20px;
            padding: 48px 40px;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 24px;
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
            box-shadow: 0 40px 80px rgba(0,0,0,0.5), inset 0 1px 0 rgba(255,255,255,0.1);
            text-align: center;
            animation: slideUp 0.6s cubic-bezier(0.16,1,0.3,1) both;
        }

        @keyframes slideUp {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .logo {
            width: 64px; height: 64px;
            background: linear-gradient(135deg, #7828c8, #006FEE);
            border-radius: 18px;
            display: flex; align-items: center; justify-content: center;
            font-size: 28px;
            margin: 0 auto 24px;
            box-shadow: 0 8px 32px rgba(120,40,200,0.4);
        }

        h1 {
            font-size: 2rem; font-weight: 700;
            background: linear-gradient(135deg, #fff 40%, rgba(255,255,255,0.5));
            -webkit-background-clip: text; -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 8px;
        }

        .tagline {
            color: rgba(255,255,255,0.4);
            font-size: 0.9rem; font-weight: 400;
            margin-bottom: 32px;
        }

        .status {
            display: inline-flex; align-items: center; gap: 8px;
            padding: 8px 16px;
            border-radius: 50px;
            font-size: 0.82rem; font-weight: 500;
            margin-bottom: 32px;
        }
        .status-dot {
            width: 8px; height: 8px; border-radius: 50%;
            animation: pulse 2s ease-in-out infinite;
        }
        .status.ok { background: rgba(34,197,94,0.15); color: #4ade80; border: 1px solid rgba(34,197,94,0.3); }
        .status.ok .status-dot { background: #4ade80; box-shadow: 0 0 6px #4ade80; }
        .status.bad { background: rgba(239,68,68,0.15); color: #f87171; border: 1px solid rgba(239,68,68,0.3); }
        .status.bad .status-dot { background: #f87171; }

        @keyframes pulse { 0%,100%{opacity:1;} 50%{opacity:0.4;} }

        .actions {
            display: flex; flex-direction: column; gap: 12px;
        }

        .btn {
            display: flex; align-items: center; justify-content: center; gap: 10px;
            padding: 14px 24px;
            border-radius: 12px;
            font-size: 0.95rem; font-weight: 600;
            text-decoration: none;
            transition: all 0.2s ease;
            border: none; cursor: pointer;
        }
        .btn-primary {
            background: linear-gradient(135deg, #7828c8, #006FEE);
            color: white;
            box-shadow: 0 4px 24px rgba(120,40,200,0.35);
        }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 8px 32px rgba(120,40,200,0.5); }

        .btn-secondary {
            background: rgba(255,255,255,0.06);
            color: rgba(255,255,255,0.75);
            border: 1px solid rgba(255,255,255,0.1);
        }
        .btn-secondary:hover { background: rgba(255,255,255,0.1); color: white; transform: translateY(-2px); }

        .footer { margin-top: 28px; font-size: 0.78rem; color: rgba(255,255,255,0.2); }
    </style>
</head>
<body>
<div class="bg"></div>
<div class="orb orb1"></div>
<div class="orb orb2"></div>
<div class="orb orb3"></div>

<div class="container">
    <div class="logo">📸</div>
    <h1>SnapVault</h1>
    <p class="tagline">Your personal cloud photo gallery</p>

    <c:choose>
        <c:when test="${applicationScope.dbConnected}">
            <div class="status ok">
                <span class="status-dot"></span>
                Database connected
            </div>
        </c:when>
        <c:otherwise>
            <div class="status bad">
                <span class="status-dot"></span>
                Database not connected
            </div>
        </c:otherwise>
    </c:choose>

    <div class="actions">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/gallery" id="btn-gallery">
            🖼️ View Gallery
        </a>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/login" id="btn-admin">
            🔐 Admin Login
        </a>
    </div>

    <p class="footer">Powered by Cloudinary · MySQL · Apache Tomcat</p>
</div>
</body>
</html>
