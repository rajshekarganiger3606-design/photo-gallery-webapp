<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Upload</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            margin: 20px;
        }

        h1 {
            text-align: center;
        }

        .nav {
            text-align: center;
            margin-bottom: 20px;
        }

        .nav a {
            color: #4285f4;
            text-decoration: none;
            margin: 0 10px;
        }

        .upload-box {
            max-width: 520px;
            margin: 0 auto;
            padding: 24px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 8px gray;
            text-align: center;
        }

        .welcome {
            margin-bottom: 16px;
            color: #333;
        }

        input[type="file"] {
            margin-bottom: 16px;
        }

        button {
            padding: 10px 20px;
            background: #4285f4;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .message {
            margin-top: 16px;
            padding: 10px;
            border-radius: 5px;
        }

        .success {
            color: #137333;
            background: #e6f4ea;
        }

        .error {
            color: #d93025;
            background: #fce8e6;
        }

        .warning {
            color: #b06000;
            background: #fef7e0;
            margin-bottom: 16px;
            padding: 12px;
            border-radius: 5px;
            text-align: left;
        }

        .info-box {
            color: #0c5460;
            background: #d1ecf1;
            margin-bottom: 16px;
            padding: 12px;
            border-radius: 5px;
            text-align: left;
            border: 1px solid #bee5eb;
            font-size: 14px;
        }
    </style>
</head>
<body>

<h1>Admin Upload</h1>

<div class="nav">
    <a href="${pageContext.request.contextPath}/">Home</a>
    <a href="${pageContext.request.contextPath}/gallery">View Gallery</a>
    <a href="${pageContext.request.contextPath}/admin/logout">Logout</a>
</div>

<div class="upload-box">
    <p class="welcome">Logged in as <strong>${adminUsername}</strong></p>

    <c:if test="${not cloudinaryConfigured}">
        <div class="info-box">
            <strong>Local Upload Mode Active:</strong> Cloudinary is not configured. Photos will be saved locally on the Tomcat server. To persist photos in production, configure Cloudinary in <code>src/main/resources/application.properties</code>.
        </div>
    </c:if>

    <c:if test="${not empty message}">
        <p class="message ${messageType}">${message}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/upload"
          method="post"
          enctype="multipart/form-data">

        <input type="file" name="photo" accept="image/*" required>
        <br><br>
        <button type="submit">
            Upload Photo
        </button>
    </form>
</div>

</body>
</html>
