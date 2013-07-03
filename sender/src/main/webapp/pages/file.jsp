<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
           
<html>
<head>
	<title>Sending orders from file</title>
</head>
<body>	
	<br /><br />

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
	<c:if test="${message != null}">
	<font color="green">Message: ${message}</font>
	</c:if>
	
	<c:if test="${error != null}">
	<font color="red">Error: ${error}</font>
	</c:if>
	
	
	<br/><br/><br/>
	<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>
