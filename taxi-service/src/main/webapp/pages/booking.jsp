<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<html>
<body>
<h2>Booking</h2>

<br/><br/>

<c:if test="${booking != null}">

    <p>Booking information:</p> <br/>

    <div style="border: solid 1px green; margin: 15px; display:inline-block;">
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
                <td><joda:format value="${booking.bookingRequest.deliveryTime}" pattern="HH:mm, dd MMM"/> </td>
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
    <c:if test="${booking.client == null}">
        <div style="border: solid 1px green; margin: 15px; display:inline-block;">Resolution:
            <table>
                <tr>
                    <td width="100px">
                        <form action="booking/${booking.id}" method="post">
                            <input type="hidden" name="action" value="ACCEPT" />
                            <input type="submit" value="Accept"/>
                        </form>
                    </td>
                    <td width="100px">
                        <form action="booking/${booking.id}" method="post">
                            <input type="hidden" name="action" value="REJECT" />
                            <input type="submit" value="Reject"/>
                        </form>
                    </td>
                </tr>
            </table>

            <c:if test="${message != null}">
                <strong>Message: ${message}</strong>
            </c:if>

        </div>
    </c:if>
</c:if>
<c:if test="${booking == null}">
    <span style="color: red; ">The queue is empty or all request are in processing</span>
</c:if>

<br/><br/>
<input type="button" value="Reload Page" onClick="location.href='${pageContext.request.contextPath}/booking'">

<br/><br/>
<span style="color: green; ">The queue has ${bookingCount} bookings</span>

<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>

</body>
</html>
