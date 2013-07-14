<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Report list</title>
</head>
<body>
	<br/><br/>
	<h2>Reports:</h2>
	<c:forEach var="report" items="${reports}">
	<div style="border: solid 1px black; display:inline-block; margin: 8px;">
		<table>
			<tr>
				<td>Id: <a href="${pageContext.request.contextPath}/report/${report.id}">${report.id}</a></td>
				<td>Status: ${report.getReportStatus()}</td>	
			</tr>
			<tr>
				<td><a href="${pageContext.request.contextPath}/order/${report.id}">order details</a></td>
				<td></td>
			</tr>
		</table>
	</div>
	</c:forEach>
	
</body>
</html>