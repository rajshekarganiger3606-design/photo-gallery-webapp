<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Photo Gallery</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 80px;
            background: #f5f5f5;
        }

        .container {
            width: 560px;
            max-width: 90%;
            margin: auto;
            padding: 40px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 10px gray;
        }

        .status {
            margin: 20px 0;
            padding: 12px;
            border-radius: 5px;
            font-size: 14px;
        }

        .ok {
            background: #e6f4ea;
            color: #137333;
        }

        .bad {
            background: #fce8e6;
            color: #d93025;
        }

        .btn {
            display: inline-block;
            margin: 8px;
            padding: 12px 25px;
            background: #4285f4;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .btn:hover {
            background: #3367d6;
        }

        .btn-secondary {
            background: #5f6368;
        }

        .btn-secondary:hover {
            background: #3c4043;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Photo Gallery</h1>
    <p>Public gallery with Cloudinary images and MySQL metadata.</p>

    <c:choose>
        <c:when test="${applicationScope.dbConnected}">
            <div class="status ok">Database connected</div>
        </c:when>
        <c:otherwise>
            <div class="status bad">
                Database not connected
                <c:if test="${not empty applicationScope.dbError}">
                    : ${applicationScope.dbError}
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>

    <a class="btn" href="${pageContext.request.contextPath}/gallery">
        View Public Gallery
    </a>

    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/login">
        Admin Login
    </a>
</div>

</body>
</html>
