<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
  "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
<head>
	<title>History</title>
</head>
<script type="text/javascript">
	function handleClick(thisObj, i){
		if(thisObj.checked){
			showRefuseBlock(i);
		} else {
			hideRefuseBlock(i);
		}
	}

	function showRefuseBlock(i){
		var label = document.getElementById("label"+i);
		var reason = document.getElementById("reason"+i);
		var send = document.getElementById("send"+i);
		
		label.style.visibility="visible";
		reason.style.visibility="visible";
		send.style.visibility="visible";
		
	}
	
	function hideRefuseBlock(i){
		var label = document.getElementById("label"+i);
		var reason = document.getElementById("reason"+i);
		var send = document.getElementById("send"+i);
		
		label.style.visibility="hidden";
		reason.style.visibility="hidden";
		send.style.visibility="hidden";
	}
</script>
<body>
<h2>History:</h2>
	<a href="${pageContext.request.contextPath}">Back</a>
	<br/><br/>
	
	<c:set var="i" value="${0}"/>
	<c:forEach var="item" items="${reportList}">
		<c:set var="i" value="${i+1}"/>
		<c:if test="${item.status == 'ACCEPTED'}">
			<c:set var="color" value="#C8FFA8" />
		</c:if>
		<c:if test="${item.status == 'REJECTED'}">
			<c:set var="color" value="#FFBFC8" />
		</c:if>
		<c:if test="${item.status == 'REFUSED'}">
			<c:set var="color" value="#8984B3" />
		</c:if>
		<div style='background-color:${color}; border: solid 1px black; display:inline-block; margin: 8px;' >
			<table>
				<tr>
					<td width="50px">${i}.</td>
					<td width="400px">id: ${item.id}</td>
					<td width="400px">from ${item.order.startPosition} to ${item.order.finishPosition}</td>
				</tr>
				<tr>
					<td></td>
					<td>date: ${item.order.dateTime}</td>
					<td>status: ${item.status}</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<c:if test="${item.status == 'REFUSED'}">
							<font color="yellow">Reason: ${item.reason}</font>
						</c:if>
					</td>
					<td>
					
						<form action="order/${item.id}/refuse" method="post" <c:if test="${item.status != 'ACCEPTED'}">style="visibility:hidden;" </c:if> ><input type="checkbox" onchange="handleClick(this, ${i})"> Refuse
						<span id="label${i}" style="visibility:hidden;">Reason:</span><input id="reason${i}" type="text" name="reason" style="visibility:hidden;"/>
							<input id="send${i}" type="submit" value="Send" style="visibility:hidden;"/>
						</form>
					
					</td>
				</tr>
				
			</table>
		
		</div>
	</c:forEach>
	
	<br/><br/>
	<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>
