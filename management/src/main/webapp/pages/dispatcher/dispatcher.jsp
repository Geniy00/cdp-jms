<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<html>
<head>
    <title>Taxi dispatcher info</title>
</head>
<body>
<a href="${pageContext.request.contextPath}">Back</a>
<br/><br/>


    <table width="30%">
        <tr>
            <td>Id:</td>
            <td>${dispatcher.id}</td>
        </tr>
        <tr>
            <td>Name:</td>
            <td>${dispatcher.name}</td>
        </tr>
        <tr>
            <td>Jms queue name:</td>
            <td>${dispatcher.jmsQueue}</td>
        </tr>
        <tr>
            <td>Jms queue capacity:</td>
            <td>${dispatcher.jmsQueueCapacity}</td>
        </tr>
        <tr>
            <td>Email:</td>
            <td>${dispatcher.email}</td>
        </tr>
        <tr>
            <td>
                Active:
            </td>
            <td>
                <c:if test="${dispatcher.disabled}"><span style="color: red; font-weight: bold;">X</span></c:if>
                <c:if test="${!dispatcher.disabled}"><span style="color: green; font-weight: bold;">V</span></c:if>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <br/>
                <form action="${pageContext.request.contextPath}/dispatcher/edit/${dispatcher.id}" method="GET">
                    <input type="submit" value="Edit" />
                </form>
                <form action="${pageContext.request.contextPath}/dispatcher/remove/${dispatcher.id}" method="POST">
                    <input type="submit" value="Remove" />
                </form>
            </td>
        </tr>
    </table>


<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>