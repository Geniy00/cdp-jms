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
    <a href="filtered?status=ASSIGNED" style="color: #00008b; text-decoration: none; background-color: #dbffaa">Assigned</a> --
    <a href="filtered?status=UNASSIGNED" style="color: #00008b; text-decoration: none; background-color: #dbffaa">Unassigned</a> --
    <a href="filtered?status=ACCEPTED" style="color: #00008b; text-decoration: none; background-color: #58FF58">Accepted</a> --
    <a href="filtered?status=REJECTED" style="color: #00008b; text-decoration: none; background-color: #FF9494">Rejected</a> --
    <a href="filtered?status=REFUSED" style="color: #00008b; text-decoration: none; background-color: #FF7878">Refused</a> --
    <a href="filtered?status=EXPIRED" style="color: #00008b; text-decoration: none; background-color: #C3C3FF">Expired</a>
</h3>
<c:set var="i" value="${0}"/>
<c:forEach var="order" items="${bookings}">
    <c:set var="i" value="${i+1}"/>
    <c:if test="${order.status == 'NEW' || order.status == 'UNASSIGNED' || order.status == 'ASSIGNED'}">
        <c:set var="color" value="#dbffaa"/>
    </c:if>
    <c:if test="${order.status == 'ACCEPTED'}">
        <c:set var="color" value="#58FF58"/>
    </c:if>
    <c:if test="${order.status == 'REJECTED'}">
        <c:set var="color" value="#FF9494"/>
    </c:if>
    <c:if test="${order.status == 'REFUSED'}">
        <c:set var="color" value="#FF7878"/>
    </c:if>
    <c:if test="${order.status == 'EXPIRED'}">
        <c:set var="color" value="#C3C3FF"/>
    </c:if>
    <div style='background-color: ${color}; border: solid 1px black; display:inline-block; margin: 8px;'>
        <table>
            <tr>
                <td width="50px">${i}.</td>
                <td width="400px"><a href="booking/${order.id}">id: ${order.id}</a>
                    orderId:${order.bookingRequest.orderId}
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
