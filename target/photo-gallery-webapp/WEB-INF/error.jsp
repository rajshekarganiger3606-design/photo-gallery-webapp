<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            margin: 40px;
        }

        .card {
            max-width: 700px;
            margin: 40px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 8px gray;
        }

        h1 {
            color: #d93025;
            margin-top: 0;
        }

        .message {
            background: #fce8e6;
            padding: 15px;
            border-radius: 5px;
            color: #333;
            word-break: break-word;
        }

        .steps {
            margin-top: 20px;
            line-height: 1.6;
        }

        a {
            color: #4285f4;
            text-decoration: none;
            margin-right: 12px;
        }
    </style>
</head>
<body>

<div class="card">
    <h1>Something went wrong</h1>

    <div class="message">
        ${errorMessage}
    </div>

    <div class="steps">
        <p><strong>Common fixes:</strong></p>
        <ol>
            <li>Make sure MySQL is running on your computer.</li>
            <li>Check username and password in <code>application.properties</code>.</li>
            <li>Restart Tomcat after changing configuration.</li>
            <li>Look at the Tomcat console for database startup messages.</li>
        </ol>
    </div>

    <p>
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/gallery">Try Gallery Again</a>
        <a href="${pageContext.request.contextPath}/admin/login">Admin Login</a>
    </p>
</div>

</body>
</html>
