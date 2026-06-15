<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
           prefix="c" %>

<!DOCTYPE html>
<html>

<head>

<title>Photo Gallery</title>

<style>

body{
    font-family:Arial;
    background:#f5f5f5;
    margin:20px;
}

h1{
    text-align:center;
}

.gallery{
    display:grid;
    grid-template-columns:
    repeat(auto-fill,minmax(250px,1fr));

    gap:20px;
}

.card{
    background:white;
    border-radius:10px;
    overflow:hidden;

    box-shadow:0 2px 8px gray;
}

.card img{
    width:100%;
    height:250px;
    object-fit:cover;
}

.info{
    padding:10px;
}

.top-links{
    text-align:center;
    margin-bottom:20px;
}

.top-links a{
    color:#4285f4;
    text-decoration:none;
}

.empty-state{
    text-align:center;
    color:#666;
    margin-top:40px;
}

</style>

</head>

<body>

<h1>📸 My Photo Gallery</h1>

<div class="top-links">
    <a href="${pageContext.request.contextPath}/">Home</a>
    &nbsp;|&nbsp;
    <a href="${pageContext.request.contextPath}/admin/login">Admin Login</a>
</div>

<div class="gallery">

<c:choose>
    <c:when test="${empty photos}">
        <p class="empty-state">No photos yet. Check back soon!</p>
    </c:when>
    <c:otherwise>
        <c:forEach items="${photos}" var="photo">
            <div class="card">
                <img src="${photo.imageUrl}"
                     alt="${photo.fileName}"
                     loading="lazy">

                <div class="info">
                    <strong>${photo.fileName}</strong>
                </div>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>

</div>

</body>
</html>
