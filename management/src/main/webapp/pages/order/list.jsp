<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<html>
<head>
    <title>History</title>
</head>
<body>
<h2>History:</h2>
<a href="${pageContext.request.contextPath}">Back</a>
<br/><br/>
<h3>
    <a href="history" style="color: #00008b; text-decoration: none; background-color: white">All</a> --
    <a href="filtered?status=NEW" style="color: #00008b; text-decoration: none; background-color: #dbffaa">New</a> --
    <a href="filtered?status=SENT" style="color: #00008b; text-decoration: none; background-color: #dbffaa">Sent</a> --
    <a href="filtered?status=DECLINED" style="color: #00008b; text-decoration: none; background-color: #FF9494">Declined</a> --
    <a href="filtered?status=PROCESSED" style="color: #00008b; text-decoration: none; background-color: #58FF58">Processed</a> --
    <a href="filtered?status=FINISHED" style="color: #00008b; text-decoration: none; background-color: #C3C3FF">Finished</a>
    <a href="filtered?status=EXPIRED" style="color: #00008b; text-decoration: none; background-color: #C3C3FF">Expired</a>
    <a href="filtered?status=CANCELED" style="color: #00008b; text-decoration: none; background-color: #FF7878">Canceled</a>
</h3>
<c:set var="i" value="${0}"/>
<c:forEach var="order" items="${orders}">
    <c:set var="i" value="${i+1}"/>
    <c:if test="${order.status == 'NEW' || order.status == 'SENT'}">
        <c:set var="color" value="#dbffaa"/>
    </c:if>
    <c:if test="${order.status == 'PROCESSED'}">
        <c:set var="color" value="#58FF58"/>
    </c:if>
    <c:if test="${order.status == 'DECLINED'}">
        <c:set var="color" value="#FF9494"/>
    </c:if>
    <c:if test="${order.status == 'CANCELED'}">
        <c:set var="color" value="#FF7878"/>
    </c:if>
    <c:if test="${order.status == 'EXPIRED'}">
        <c:set var="color" value="#C3C3FF"/>
    </c:if>
    <div style='background-color: ${color}; border: solid 1px black; display:inline-block; margin: 8px;'>
        <table>
            <tr>
                <td width="50px">${i}.</td>
                <td width="400px">id: <a href="order/${order.id}">${order.id}</a>
                </td>
                <td width="400px">from ${order.bookingRequest.startPosition}
                    to ${order.bookingRequest.finishPosition}</td>
            </tr>
            <tr>
                <td></td>
                <td>Delivery time: <joda:format value="${order.bookingRequest.deliveryTime}" pattern="HH:mm, dd MMM"/></td>
                <td>
                    Status: ${order.status}
                    <c:if test="${order.reason.length == 0}">
                        Reason: ${order.reason}
                    </c:if>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    Vechicle type: ${order.bookingRequest.vehicleType}
                </td>
                <td>
                    Payment: \$${order.bookingRequest.payment}
                </td>
            </tr>

        </table>

    </div>
</c:forEach>

<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>
