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
<form method="post">
    Delay: <input type="text" name="delay" value="${delay}"
                  disabled="${status == 'enabled'}"> ms <br/>
    Reject every <input type="text" name="rejectEveryNthOrder"
                        value="${rejectEveryNthOrder} disabled="${status == 'enabled'}"> order <br/>

    <input type="hidden" name="action" value="${action}">
    <input type="submit" value="${action}">
</form>


<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>

</body>
</html>