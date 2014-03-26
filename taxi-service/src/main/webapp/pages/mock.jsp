<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<html>
<head>
    <title>Mock</title>
</head>
<body>


Current status is ${status}
<c:if test="${status == 'disabled'}">
    <form method="post">
        Delay: <input type="text" name="delay" value="${delay}" /> ms <br/>
        Reject every <input type="text" name="rejectEveryNthOrder" value="${rejectEveryNthOrder}" /> order <br/>

        <input type="hidden" name="action" value="Enable">
        <input type="submit" value="Run">
    </form>
</c:if>

<c:if test="${status != 'disabled'}">
    <form method="post">
        Delay: <input type="text" name="delay" value="${delay}" disabled /> ms <br/>
        Reject every <input type="text" name="rejectEveryNthOrder" value="${rejectEveryNthOrder}"
                            <c:if test="${status != 'disabled'}">disabled</c:if> /> order <br/>

        <input type="hidden" name="action" value="Disable">
        <input type="submit" value="Stop">
    </form>
</c:if>

<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>

</body>
</html>