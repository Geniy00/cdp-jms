<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<html>
<body>
<h3>Booking info</h3>
<br/>

<div>
    Booking id: <strong>${booking.id}</strong> </br>
    Status: <strong>${booking.status}</strong>
</div>
</br>

<c:if test="${message != null}">
    <span style="color: #ff4500;">Message: ${message}</span>
</c:if>
</br>

<div style="border: solid 1px green; margin: 15px; display:inline-block; text-align: left">
    <table>
        <tr>
            <td>BookingRequest id:</td>
            <td>${booking.bookingRequest.bookingRequestId }</td>
        </tr>
        <c:if test="${booking.client != null}">
            <tr>
                <td><b>Customer:</b></td>
                <td><b>name: ${booking.client.name} <br/>phone: ${booking.client.phone}</b></td>
            </tr>
        </c:if>
        <tr>
            <td>Start position:</td>
            <td>${booking.bookingRequest.startPosition}</td>
        </tr>
        <tr>
            <td>Finish position:</td>
            <td>${booking.bookingRequest.finishPosition}</td>
        </tr>
        <tr>
            <td>Delivery time:</td>
            <td><joda:format value="${booking.bookingRequest.deliveryTime}" pattern="HH:mm, dd MMM"/></td>
        </tr>
        <tr>
            <td>Vehicle type:</td>
            <td>${booking.bookingRequest.vehicleType}</td>
        </tr>
        <tr>
            <td>Payment:</td>
            <td><b>$ ${booking.bookingRequest.payment}</b></td>
        </tr>
        <tr>
            <td>Expiry time:</td>
            <td><joda:format value="${booking.bookingRequest.expiryTime}" pattern="HH:mm, dd MMM"/></td>
        </tr>
    </table>
</div>
<br/>
<c:if test="${booking.status == 'ASSIGNED' && booking.assignToExpiryTime != null}">
    <span style="color: orange; ">Assigned till <joda:format value="${booking.assignToExpiryTime}"
                                                             pattern="HH:mm, dd MMM"/> </span>
</c:if>
<br/>

<c:if test="${booking != null && booking.status != 'EXPIRED' && booking.status != 'REJECTED' && booking.status != 'REFUSED'}">
    <div style="border: solid 1px green; margin: 15px; display:inline-block;">
        Actions:
        <table>
            <c:if test="${booking.status == 'NEW' || booking.status == 'REVOKED'}">
                <tr>
                    <td width="100px">
                        <form action="booking/${booking.id}/assigned" method="post">
                            <input type="hidden" name="action" value="ASSIGN_TO_ME"/>
                            <input type="submit" value="Assign to me"/>
                        </form>
                    </td>
                </tr>
            </c:if>
            <c:if test="${booking.status == 'ASSIGNED'}">
                <tr>
                    <td width="100px">
                        <form action="${pageContext.request.contextPath}/booking/${booking.id}" method="post">
                            <input type="hidden" name="action" value="ACCEPT"/>
                            <input type="submit" value="Accept"/>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td width="100px">
                        <form action="${pageContext.request.contextPath}/booking/${booking.id}" method="post">
                            <input type="hidden" name="action" value="REJECT"/>
                            <input type="submit" value="Reject"/>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td width="100px">
                        <form action="${pageContext.request.contextPath}/booking/${booking.id}/assigned" method="post">
                            <input type="hidden" name="action" value="UNASSIGN"/>
                            <input type="submit" value="Unassign"/>
                        </form>
                    </td>
                </tr>
            </c:if>
            <c:if test="${booking.status == 'ACCEPTED'}">
                <tr>
                    <td width="100px">
                        <form action="${pageContext.request.contextPath}/booking/${booking.id}" method="post">
                            <input type="hidden" name="action" value="REFUSE"/>
                            <input type="submit" value="Refuse"/>
                        </form>
                    </td>
                </tr>
            </c:if>
        </table>
    </div>
</c:if>

<br/><br/>
<a href="${pageContext.request.contextPath}/bookings">Back</a>

</body>
</html>
