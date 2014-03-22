<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<html>
<head>
    <title>New taxi dispatcher</title>
</head>
<body>
<a href="${pageContext.request.contextPath}">Back</a>
<br/><br/>
<br/><br/>


<table>
    <sf:form method="POST" commandName="dispatcher">
    <tr>
        <td>Name:</td>
        <td><sf:input path="name"/></td>
    </tr>
    <tr>
        <td>Jms queue name:</td>
        <td><sf:input path="jmsQueue"/></td>
    </tr>
    <tr>
        <td>Jms queue capacity:</td>
        <td><sf:input path="jmsQueueCapacity"/></td>
    </tr>
    <tr>
        <td>Email:</td>
        <td><sf:input path="email"/></td>
    </tr>
    <tr>
        <td>Disabled:</td>
        <td><sf:checkbox path="disabled" value="disabl"/></td>
    </tr>
    <tr>
        <td></td>
        <td style="text-align: center"><input type="submit" value="${action}"/></td>
    </tr>
    </sf:form>
</table>


<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>