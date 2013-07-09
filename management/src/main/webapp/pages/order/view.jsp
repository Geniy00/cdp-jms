<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
  "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<h2>Order</h2>
</body>
	<br/><br/>
	
	<c:if test="${order != null}">
	
		<p>Order information:</p> <br/>
		<div style="border: solid 1px green; margin: 15px; display:inline-block;">
		<table>
			<tr>
				<td>id:</td>
				<td>${order.id }</td>
			</tr>
			<tr>
				<td>Customer:</td>
				<td>name: ${order.customer.name} <br/>phone: ${order.customer.phone}</td>
			</tr>
			<tr>
				<td>Start position:</td>
				<td>${order.startPosition}</td>
			</tr>
			<tr>
				<td>Finish position:</td>
				<td>${order.finishPosition}</td>
			</tr>
			<tr>
				<td>Date&time:</td>
				<td>${order.dateTime}</td>
			</tr>
			<tr>
				<td>Order type:</td>
				<td>${order.orderType}</td>
			</tr>
			<tr>
				<td>Price:</td>
				<td>${order.price}</td>
			</tr>
		</table>
		</div>
		<br/>
	</c:if>
	
	
	<br/><br/>
	<a href="javascript:" onclick="history.go(-1); return false">Back</a>
</html>
