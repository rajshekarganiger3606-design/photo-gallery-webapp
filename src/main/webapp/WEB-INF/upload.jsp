<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Upload – SnapVault</title>
    <meta name="description" content="Upload new photos to the gallery.">
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

        /* ── Upload Card ── */
        .card {
            position: relative; z-index: 1;
            width: 100%; max-width: 520px;
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

        .welcome {
            text-align: center;
            color: rgba(255,255,255,0.4);
            font-size: 0.85rem;
            margin-bottom: 28px;
        }
        .welcome strong { color: rgba(255,255,255,0.75); }

        /* ── File Input Area ── */
        .file-drop-area {
            position: relative;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 32px 20px;
            border: 2px dashed rgba(255,255,255,0.15);
            border-radius: 16px;
            background: rgba(255,255,255,0.02);
            transition: all 0.2s ease;
            cursor: pointer;
            margin-bottom: 24px;
        }
        .file-drop-area:hover {
            border-color: #a855f7;
            background: rgba(255,255,255,0.05);
        }
        .file-icon {
            font-size: 36px;
            margin-bottom: 12px;
            opacity: 0.7;
        }
        .file-msg {
            font-size: 0.9rem;
            color: rgba(255,255,255,0.6);
            margin-bottom: 4px;
            text-align: center;
        }
        .file-hint {
            font-size: 0.75rem;
            color: rgba(255,255,255,0.3);
        }
        .file-input {
            position: absolute;
            left: 0; top: 0; width: 100%; height: 100%;
            opacity: 0;
            cursor: pointer;
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
        }
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 32px rgba(120,40,200,0.5);
        }

        /* ── Info & Messages ── */
        .info-box {
            font-size: 0.8rem;
            line-height: 1.4;
            padding: 12px 16px;
            border-radius: 10px;
            margin-bottom: 24px;
            text-align: left;
            color: #93c5fd;
            background: rgba(59,130,246,0.1);
            border: 1px solid rgba(59,130,246,0.2);
        }
        .message {
            margin-bottom: 24px;
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

        /* ── Nav Links ── */
        .links {
            display: flex;
            justify-content: center;
            gap: 16px;
            margin-top: 32px;
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
    <h1>Upload Photo</h1>
    <p class="welcome">Logged in as <strong>${adminUsername}</strong></p>

    <c:if test="${not cloudinaryConfigured}">
        <div class="info-box">
            ⚡ <strong>Local Upload Mode:</strong> Cloudinary is not configured. Photos will be saved locally on the Tomcat server. To persist photos in production, configure Cloudinary settings in application properties.
        </div>
    </c:if>

    <c:if test="${not empty message}">
        <div class="message ${messageType}">${message}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/upload"
          method="post"
          enctype="multipart/form-data">

        <div class="file-drop-area" id="drop-area">
            <span class="file-icon">📁</span>
            <span class="file-msg" id="file-msg">Choose an image file or drag it here</span>
            <span class="file-hint">Accepts JPG, PNG, GIF, WEBP up to 20MB</span>
            <input class="file-input" type="file" name="photo" id="photo-input" accept="image/*" required>
        </div>

        <button type="submit">
            ✦ Upload Photo
        </button>
    </form>

    <div class="links">
        <a href="${pageContext.request.contextPath}/">Home</a>
        <span style="color: rgba(255,255,255,0.1)">|</span>
        <a href="${pageContext.request.contextPath}/gallery">View Gallery</a>
        <span style="color: rgba(255,255,255,0.1)">|</span>
        <a href="${pageContext.request.contextPath}/admin/logout" style="color: #f87171;">Logout</a>
    </div>
</div>

<script>
    const input = document.getElementById('photo-input');
    const msg = document.getElementById('file-msg');
    const area = document.getElementById('drop-area');

    input.addEventListener('change', () => {
        if (input.files && input.files[0]) {
            msg.textContent = 'Selected: ' + input.files[0].name;
            area.style.borderColor = '#a855f7';
            area.style.background = 'rgba(168,85,247,0.05)';
        }
    });

    ['dragenter', 'dragover'].forEach(eventName => {
        area.addEventListener(eventName, e => {
            e.preventDefault();
            area.style.borderColor = '#a855f7';
            area.style.background = 'rgba(168,85,247,0.08)';
        }, false);
    });

    ['dragleave', 'drop'].forEach(eventName => {
        area.addEventListener(eventName, e => {
            e.preventDefault();
            if (!input.files || !input.files[0]) {
                area.style.borderColor = 'rgba(255,255,255,0.15)';
                area.style.background = 'rgba(255,255,255,0.02)';
            }
        }, false);
    });
</script>
</body>
</html>
