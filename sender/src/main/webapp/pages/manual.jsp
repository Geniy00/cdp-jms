<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
           
<html>
<head>
	<title>New order</title>
</head>
<body>	
	<a href="manual/random">Generate random order</a>
	<br /><br />

	<sf:form action="${pageContext.request.contextPath}/send" method="post" modelAttribute="order">
		<table>
			<tr>
				<td>Id: </td><td><sf:input path="id"/> </td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr><td colspan="2">Customer</td></tr>
			<tr>
				<td>Name: </td><td><sf:input path="customer.name"/></td>
			</tr>
			<tr>
				<td>Phone: </td><td><sf:input path="customer.phone"/></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td>Start: </td><td><sf:input path="startPosition"/></td>
			</tr>
			<tr>
				<td>Finish: </td><td><sf:input path="finishPosition"/></td>
			</tr>
			<tr>
				<td>Date: </td><td><sf:input path="dateTime"/></td>
			</tr>
			<tr>
				<td>Order type: </td>
				<td><sf:select path="orderType" items="${orderTypeValues}"/></td>
			</tr>
			<tr>
				<td>Price: </td><td><sf:label path="price"/></td>
			</tr>
			<tr>
				<td colspan="2"><sf:button>Send</sf:button></td>
			</tr>
		</table>
	</sf:form>
	<br/>
	<br/>
	<br/>
	<c:if test="${sentOrder.id != null}">
	<font color="green">Order with id <b>${sentOrder.id}</b> was sent successfully</font>
	</c:if>
	
	
	<br/><br/><br/>
	<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>
