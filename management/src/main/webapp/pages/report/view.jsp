<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Report</title>
</head>
<body>
	<br/><br/>
	Report:
	<table>
		<tr>
			<td>id:</td>
			<td>${report.id}</td>
		</tr>
		<tr>
			<td>Status:</td>
			<td>${report.getReportStatus()}</td>
		</tr>
		<tr>
			<td colspan="2">	
				<br/>		
				History:
				<br/>==========================<br/>
				<c:forEach var="item" items="${report.history}" varStatus="theCount">
				<div>
					${theCount.count}) Status: ${item.reportStatus.name()} <br/>
					Reason: ${item.reason} <br/>
					taxiId: ${item.taxiId } <br/>
					dateTime: ${item.dateTime} <br/>
				</div>
				</c:forEach>
			</td>
		</tr>
	</table>
	
	<a href="javascript:" onclick="history.go(-1); return false">Back</a>
</body>
</html>