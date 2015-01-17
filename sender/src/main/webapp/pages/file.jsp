<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Sending reservation requests from file</title>
</head>
<body>
<br/><br/>

<form action="${pageContext.request.contextPath}/file/send"
      enctype="multipart/form-data" method="post">
    <p>Please specify a JSON file:<br>
        <br/>
        <input type="file" name="file" size="40">
    </p>
    <br/>
    <div>
        <input type="submit" value="Upload">
    </div>
</form>
<br/>
<br/>
<br/>
<c:if test="${message != null && message != ''}">
    <span style="color: green; ">Message: ${message}</span>
</c:if>

<c:if test="${error != null}">
    <span style="color: red; ">Error: ${error}</span>
</c:if>


<br/><br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>
