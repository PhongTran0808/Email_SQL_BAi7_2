<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="css/main.css" type="text/css"/>


</head>
<body>

<%
    String sqlStatement = (String) session.getAttribute("sqlStatement");
    if (sqlStatement == null) {
        sqlStatement = "select * from users";
        request.setAttribute("sqlStatement", sqlStatement);
    }

%>


<h1>The SQL Gateway</h1>
<p>Enter an SQL statement and click the Execute button.</p>
<p><b>SQL statement:</b></p>
<form action="sqlGateway" method="post">
    <textarea name="sqlStatement" cols="60" rows="8">${sqlStatement}</textarea>
    <input type="submit" value="Execute">
</form>

<p><b>SQL result:</b></p>
${sessionScope.sqlResult}


</body>
</html>