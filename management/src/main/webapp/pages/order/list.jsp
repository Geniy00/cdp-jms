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
    <a href="history?status=NEW" style="color: #00008b; text-decoration: none; background-color: #dbffaa">New</a> --
    <a href="history?status=SENT" style="color: #00008b; text-decoration: none; background-color: #dbffaa">Sent</a> --
    <a href="history?status=DECLINED" style="color: #00008b; text-decoration: none; background-color: #FF9494">Declined</a>
    --
    <a href="history?status=PROCESSED" style="color: #00008b; text-decoration: none; background-color: #58FF58">Processed</a>
    --
    <a href="history?status=FINISHED" style="color: #00008b; text-decoration: none; background-color: #C3C3FF">Finished</a>
    --
    <a href="history?status=EXPIRED" style="color: #00008b; text-decoration: none; background-color: #C3C3FF">Expired</a>
    --
    <a href="history?status=CANCELED" style="color: #00008b; text-decoration: none; background-color: #FF7878">Canceled</a>
</h3>
<c:set var="i" value="${0}"/>
<c:forEach var="booking" items="${orders}">
    <c:set var="i" value="${i+1}"/>
    <c:if test="${booking.orderStatus == 'NEW' || booking.orderStatus == 'SENT'}">
        <c:set var="color" value="#dbffaa"/>
    </c:if>
    <c:if test="${booking.orderStatus == 'PROCESSED'}">
        <c:set var="color" value="#58FF58"/>
    </c:if>
    <c:if test="${booking.orderStatus == 'DECLINED'}">
        <c:set var="color" value="#FF9494"/>
    </c:if>
    <c:if test="${booking.orderStatus == 'CANCELED'}">
        <c:set var="color" value="#FF7878"/>
    </c:if>
    <c:if test="${booking.orderStatus == 'EXPIRED'}">
        <c:set var="color" value="#C3C3FF"/>
    </c:if>
    <div style='background-color: ${color}; border: solid 1px black; display:inline-block; margin: 8px;'>
        <table>
            <tr>
                <td width="50px">${i}.</td>
                <td width="400px">id: <a href="order/${booking.id}">${booking.id}</a>
                </td>
                <td width="400px">from ${booking.reservationRequest.startPosition}
                    to ${booking.reservationRequest.finishPosition}</td>
            </tr>
            <tr>
                <td></td>
                <td>Delivery time:
                    <joda:format value="${booking.reservationRequest.deliveryTime}" pattern="HH:mm, dd MMM"/></td>
                <td>
                    Status: ${booking.orderStatus}
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    Vechicle type: ${booking.reservationRequest.vehicleType}
                </td>
                <td>
                    Payment: <%--\$${order.reservationRequest.payment}--%>
                </td>
            </tr>

        </table>

    </div>
</c:forEach>

<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>
