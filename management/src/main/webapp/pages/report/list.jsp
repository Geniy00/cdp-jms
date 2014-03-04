<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Report list</title>
</head>
<body>
<br/>
<a href="${pageContext.request.contextPath}">Back</a>
<br/><br/>

<h2>Reports:</h2>
<c:choose>
    <c:when test="${reports == null }">(0 items)</c:when>
    <c:otherwise>(${reports.size()} items)</c:otherwise>
</c:choose>
<br/>

<c:forEach var="report" items="${reports}">
    <div style="border: solid 1px black; display:inline-block; margin: 8px; width: 60%">
        <table>
            <tr>
                <td width="30%">Id: ${report.id}</td>
                <td width="30%">Status: ${report.getReportStatus()}</td>
            </tr>
            <tr>
                <td><a href="${pageContext.request.contextPath}/report/${report.id}">report details</a></td>
                <td><a href="${pageContext.request.contextPath}/order/${report.id}">order details</a></td>
            </tr>
        </table>
    </div>
</c:forEach>

</body>
</html>