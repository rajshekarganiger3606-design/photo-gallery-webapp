// Fullscreen image viewer lightbox handlers
function openLightbox(imageUrl, title) {
    const modal = document.getElementById('lightboxModal');
    const img = document.getElementById('lightboxImage');
    if (modal && img) {
        img.src = imageUrl;
        img.alt = title || 'Fullscreen Portfolio Capture';
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden'; // Freeze parent page scrolling
    }
}

function closeLightbox() {
    const modal = document.getElementById('lightboxModal');
    if (modal) {
        modal.style.display = 'none';
        document.body.style.overflow = 'auto'; // Restore page scrolling
    }
}

// Key bindings
document.addEventListener('keydown', function(event) {
    if (event.key === 'Escape') {
        closeLightbox();
    }
});

// Fades out and deletes success/warning banners after 4 seconds
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert-dismiss-delay');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            alert.style.transition = 'opacity 0.6s ease';
            alert.style.opacity = '0';
            setTimeout(function() {
                alert.remove();
            }, 600);
        }, 4000);
    });
});
