<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<html>
<head>
    <title>Taxi dispatcher list</title>
</head>
<body>
<br/><br/>
<a href="dispatcher/new">New</a>
<br/><br/>

    <table width="80%" border="1" cellspacing="0" style="text-align: center">
        <thead style="font-weight: bold;">
            <tr>
                <td>Id</td>
                <td>Name</td>
                <td>Email</td>
                <td>Active</td>
                <td colspan="3" width="20%">Actions</td>
            </tr>
        </thead>
        <c:set var="i" value="${0}"/>
        <c:forEach var="dispatcher" items="${dispatchers}">
        <c:set var="i" value="${i+1}"/>
        <tr>
            <td><a href="dispatcher/show/${dispatcher.id}">${dispatcher.id}</a></td>
            <td>${dispatcher.name}</td>
            <td>${dispatcher.email}</td>
            <td>
                <c:if test="${dispatcher.disabled}"><span style="color: red; font-weight: bold;">X</span></c:if>
                <c:if test="${!dispatcher.disabled}"><span style="color: green; font-weight: bold;">V</span></c:if>
            </td>
            <td>
                <form action="dispatcher/show/${dispatcher.id}" method="GET">
                    <input type="submit" value="Show" />
                </form>
            </td>
            <td>
                <form action="dispatcher/edit/${dispatcher.id}" method="GET">
                    <input type="submit" value="Edit" />
                </form>
            </td>
            <td>
                <form action="dispatcher/remove/${dispatcher.id}" method="POST">
                    <input type="submit" value="Remove" />
                </form>
            </td>
        </tr>
        </c:forEach>
    </table>
    <br/>


<c:if test="${dispatchers == null && dispatchers.size == 0}">
    Taxi dispatcher list is empty.
</c:if>


<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>