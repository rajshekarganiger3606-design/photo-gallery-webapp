<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            margin: 0;
            padding: 40px 20px;
        }

        .card {
            max-width: 420px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 8px gray;
        }

        h1 {
            text-align: center;
            margin-top: 0;
        }

        .hint {
            text-align: center;
            color: #666;
            font-size: 14px;
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 6px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-bottom: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #4285f4;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        .message {
            margin-bottom: 16px;
            text-align: center;
            padding: 10px;
            border-radius: 5px;
        }

        .error {
            color: #d93025;
            background: #fce8e6;
        }

        .success {
            color: #137333;
            background: #e6f4ea;
        }

        .links {
            text-align: center;
            margin-top: 20px;
        }

        .links a {
            color: #4285f4;
            text-decoration: none;
            margin: 0 8px;
        }
    </style>
</head>
<body>

<div class="card">
    <h1>Admin Login</h1>
    <p class="hint">Default login: <strong>admin</strong> / <strong>admin123</strong></p>

    <c:if test="${not empty error}">
        <p class="message error">${error}</p>
    </c:if>

    <c:if test="${param.loggedOut == '1'}">
        <p class="message success">You have been logged out.</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/admin/login" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" value="admin" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Login</button>
    </form>

    <div class="links">
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/gallery">Gallery</a>
    </div>
</div>

</body>
</html>
