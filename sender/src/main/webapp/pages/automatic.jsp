<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
           
<html>
<head>
	<title>Sending random reservation requests automatically</title>
</head>
<body>	
	<br /><br />

	<c:if test="${status == 'STOPPED'}">
		<form action="${pageContext.request.contextPath}/automatic/toggle"  method="post">
		       <p>Delay: <input type="text" name="delay" value="${delay }"/> ms</p>
		       <div>
		           <input type="submit" value="Start sending"/>
		       </div>
		   </form>
    </c:if>
    
    <c:if test="${status == 'SENDING'}">
		<form action="${pageContext.request.contextPath}/automatic/toggle"  method="post">
		       <p>Delay: ${delay} ms </p>
		       <div>
		           <input type="submit" value="Stop sending"/>
		       </div>
		   </form>
    </c:if>
    
	<br/>
	<br/>
	<br/>
	<c:if test="${messageCount != null && messageCount > 0}">
		<span style="color: green; ">Message: ${messageCount} messages were sent</span>
	</c:if>
	
	<c:if test="${error != null}">
	    <span style="color: red; ">Error: ${error}</span>
	</c:if>
	
	
	<br/><br/><br/>
	<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>
