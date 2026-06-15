<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gallery – SnapVault</title>
    <meta name="description" content="Browse your cloud photo gallery.">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: 'Inter', sans-serif;
            min-height: 100vh;
            background: #0a0a0f;
            color: rgba(255,255,255,0.85);
        }

        /* ── Navbar ── */
        .navbar {
            position: sticky; top: 0; z-index: 100;
            display: flex; align-items: center; justify-content: space-between;
            padding: 16px 32px;
            background: rgba(10,10,15,0.8);
            backdrop-filter: blur(20px);
            border-bottom: 1px solid rgba(255,255,255,0.07);
        }
        .brand {
            display: flex; align-items: center; gap: 10px;
            font-size: 1.2rem; font-weight: 700;
            background: linear-gradient(135deg, #a855f7, #3b82f6);
            -webkit-background-clip: text; -webkit-text-fill-color: transparent;
            background-clip: text;
            text-decoration: none;
        }
        .brand-icon {
            width: 36px; height: 36px;
            background: linear-gradient(135deg, #7828c8, #006FEE);
            border-radius: 10px;
            display: flex; align-items: center; justify-content: center;
            font-size: 18px;
        }
        .nav-links { display: flex; gap: 8px; }
        .nav-link {
            padding: 8px 16px;
            border-radius: 8px;
            font-size: 0.85rem; font-weight: 500;
            text-decoration: none;
            color: rgba(255,255,255,0.6);
            transition: all 0.2s ease;
        }
        .nav-link:hover { background: rgba(255,255,255,0.08); color: white; }
        .nav-link.active {
            background: rgba(120,40,200,0.2);
            color: #c084fc;
        }

        /* ── Hero ── */
        .hero {
            text-align: center; padding: 60px 20px 40px;
            background: radial-gradient(ellipse at 50% 0%, rgba(120,40,200,0.2) 0%, transparent 70%);
        }
        .hero h1 {
            font-size: 2.8rem; font-weight: 700;
            background: linear-gradient(135deg, #fff 40%, rgba(255,255,255,0.4));
            -webkit-background-clip: text; -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 12px;
        }
        .hero p { color: rgba(255,255,255,0.4); font-size: 1rem; }

        .photo-count {
            display: inline-block;
            margin-top: 16px;
            padding: 6px 16px;
            background: rgba(255,255,255,0.06);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 50px;
            font-size: 0.8rem; color: rgba(255,255,255,0.5);
        }

        /* ── Gallery Grid ── */
        .gallery-container { padding: 24px 32px 60px; max-width: 1400px; margin: 0 auto; }

        .gallery {
            columns: 4 280px;
            column-gap: 16px;
        }

        .card {
            break-inside: avoid;
            margin-bottom: 16px;
            border-radius: 16px;
            overflow: hidden;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
            cursor: pointer;
            transition: all 0.3s cubic-bezier(0.4,0,0.2,1);
            animation: fadeIn 0.5s ease both;
            position: relative;
        }
        .card:hover { transform: translateY(-6px) scale(1.02); border-color: rgba(168,85,247,0.4); box-shadow: 0 20px 60px rgba(0,0,0,0.6), 0 0 0 1px rgba(168,85,247,0.2); }

        @keyframes fadeIn { from { opacity:0; transform: scale(0.96); } to { opacity:1; transform: scale(1); } }

        .card img {
            width: 100%; display: block;
            transition: transform 0.4s ease;
        }
        .card:hover img { transform: scale(1.04); }

        .card-overlay {
            position: absolute; inset: 0;
            background: linear-gradient(to top, rgba(0,0,0,0.8) 0%, transparent 50%);
            opacity: 0;
            transition: opacity 0.3s ease;
            display: flex; align-items: flex-end;
            padding: 16px;
        }
        .card:hover .card-overlay { opacity: 1; }
        .card-filename {
            font-size: 0.78rem; font-weight: 500;
            color: rgba(255,255,255,0.9);
            overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
        }

        /* ── Empty State ── */
        .empty-state {
            text-align: center; padding: 80px 20px;
            color: rgba(255,255,255,0.3);
        }
        .empty-icon { font-size: 64px; margin-bottom: 20px; }
        .empty-state h2 { font-size: 1.4rem; font-weight: 600; margin-bottom: 8px; color: rgba(255,255,255,0.5); }
        .empty-state p { font-size: 0.9rem; margin-bottom: 24px; }
        .btn-primary {
            display: inline-flex; align-items: center; gap: 8px;
            padding: 12px 24px;
            background: linear-gradient(135deg, #7828c8, #006FEE);
            color: white; font-weight: 600; font-size: 0.9rem;
            border-radius: 10px; text-decoration: none;
            transition: all 0.2s ease;
            box-shadow: 0 4px 24px rgba(120,40,200,0.35);
        }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 8px 32px rgba(120,40,200,0.5); }

        /* ── Lightbox ── */
        .lightbox {
            display: none; position: fixed; inset: 0; z-index: 999;
            background: rgba(0,0,0,0.92);
            align-items: center; justify-content: center;
            backdrop-filter: blur(10px);
        }
        .lightbox.open { display: flex; }
        .lightbox img {
            max-width: 90vw; max-height: 88vh;
            border-radius: 12px;
            box-shadow: 0 40px 100px rgba(0,0,0,0.8);
            animation: zoomIn 0.25s ease;
        }
        @keyframes zoomIn { from { transform: scale(0.85); opacity:0; } to { transform: scale(1); opacity:1; } }
        .lightbox-close {
            position: fixed; top: 24px; right: 32px;
            font-size: 32px; color: rgba(255,255,255,0.6);
            cursor: pointer; background: none; border: none;
            transition: color 0.2s;
        }
        .lightbox-close:hover { color: white; }

        .delete-btn {
            position: absolute;
            top: 12px;
            right: 12px;
            width: 36px;
            height: 36px;
            border-radius: 50%;
            background: rgba(239, 68, 68, 0.2);
            border: 1px solid rgba(239, 68, 68, 0.4);
            color: #ef4444;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            transition: all 0.2s ease;
            z-index: 10;
            opacity: 0;
            font-size: 14px;
            backdrop-filter: blur(8px);
            -webkit-backdrop-filter: blur(8px);
        }
        .card:hover .delete-btn {
            opacity: 1;
        }
        .delete-btn:hover {
            background: rgba(239, 68, 68, 0.9);
            border-color: #ef4444;
            color: white;
            transform: scale(1.1);
            box-shadow: 0 0 15px rgba(239, 68, 68, 0.5);
        }
    </style>
</head>
<body>

<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/">
        <div class="brand-icon">📸</div>
        SnapVault
    </a>
    <div class="nav-links">
        <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
        <a class="nav-link active" href="${pageContext.request.contextPath}/gallery">Gallery</a>
        <a class="nav-link" href="${pageContext.request.contextPath}/admin/login">Admin</a>
    </div>
</nav>

<div class="hero">
    <h1>📸 Photo Gallery</h1>
    <p>All your memories, beautifully curated in the cloud</p>
    <c:if test="${not empty photos}">
        <span class="photo-count">${fn:length(photos)} photos</span>
    </c:if>
</div>

<div class="gallery-container">
    <c:choose>
        <c:when test="${empty photos}">
            <div class="empty-state">
                <div class="empty-icon">🖼️</div>
                <h2>No photos yet</h2>
                <p>Upload your first photo to get started!</p>
                <a class="btn-primary" href="${pageContext.request.contextPath}/admin/login">
                    ✦ Upload Photos
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="gallery" id="gallery">
                <c:forEach items="${photos}" var="photo">
                    <div class="card" onclick="openLightbox('${photo.imageUrl}', '${photo.fileName}')" id="photo-card-${photo.id}">
                        <img src="${photo.imageUrl}" alt="${photo.fileName}" loading="lazy">
                        <div class="card-overlay">
                            <span class="card-filename">${photo.fileName}</span>
                        </div>
                        <c:if test="${sessionScope.adminLoggedIn}">
                            <button class="delete-btn" onclick="confirmDelete(event, ${photo.id})" title="Delete Photo">
                                🗑️
                            </button>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Lightbox -->
<div class="lightbox" id="lightbox" onclick="closeLightbox()">
    <button class="lightbox-close" onclick="closeLightbox()">✕</button>
    <img id="lightbox-img" src="" alt="">
</div>

<script>
    function openLightbox(url, name) {
        document.getElementById('lightbox-img').src = url;
        document.getElementById('lightbox-img').alt = name;
        document.getElementById('lightbox').classList.add('open');
        document.body.style.overflow = 'hidden';
    }
    function closeLightbox() {
        document.getElementById('lightbox').classList.remove('open');
        document.body.style.overflow = '';
    }
    document.addEventListener('keydown', e => { if (e.key === 'Escape') closeLightbox(); });

    function confirmDelete(event, id) {
        event.stopPropagation(); // Prevent opening the lightbox
        
        if (confirm("Are you sure you want to delete this photo? This action cannot be undone.")) {
            const btn = event.currentTarget;
            btn.disabled = true;
            btn.innerHTML = '⌛';
            
            const formData = new URLSearchParams();
            formData.append('id', id);

            fetch('${pageContext.request.contextPath}/admin/delete', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const card = document.getElementById('photo-card-' + id);
                    card.style.transition = 'all 0.4s ease';
                    card.style.opacity = '0';
                    card.style.transform = 'scale(0.9) translateY(20px)';
                    
                    setTimeout(() => {
                        card.remove();
                        const countEl = document.querySelector('.photo-count');
                        if (countEl) {
                            const currentCount = parseInt(countEl.textContent);
                            if (!isNaN(currentCount)) {
                                const newCount = currentCount - 1;
                                if (newCount <= 0) {
                                    location.reload();
                                } else {
                                    countEl.textContent = newCount + ' ' + (newCount === 1 ? 'photo' : 'photos');
                                }
                            }
                        }
                    }, 400);
                } else {
                    alert('Error: ' + data.message);
                    btn.disabled = false;
                    btn.innerHTML = '🗑️';
                }
            })
            .catch(err => {
                console.error('Error deleting photo:', err);
                alert('An unexpected error occurred while deleting the photo.');
                btn.disabled = false;
                btn.innerHTML = '🗑️';
            });
        }
    }
</script>
</body>
</html>
