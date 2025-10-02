<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>
<br/>
<br>
<form action="hello-servlet" method="post">
    <label for="value_form">Choose a value</label> <br>
    <select id="page" name="page">
        <option value="page1">page1</option>
        <option value="page2">page2</option>
    </select>
    <br>
    <button type="submit">Submit</button>
</form>
</body>
</html>