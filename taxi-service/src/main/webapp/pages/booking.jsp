<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<h2>Booking</h2>
</body>
<br/><br/>

<c:if test="${booking != null}">

    <p>Booking information:</p> <br/>

    <div style="border: solid 1px green; margin: 15px; display:inline-block;">
        <table>
            <tr>
                <td>id:</td>
                <td>${booking.id }</td>
            </tr>
            <tr>
                <td>Customer:</td>
                <td>name: ${customerName} <br/>phone: ${customerPhone}</td>
            </tr>
            <tr>
                <td>Start position:</td>
                <td>${booking.startPosition}</td>
            </tr>
            <tr>
                <td>Finish position:</td>
                <td>${booking.finishPosition}</td>
            </tr>
            <tr>
                <td>Delivery time:</td>
                <td>${booking.deliveryTime}</td>
            </tr>
            <tr>
                <td>Vehicle type:</td>
                <td>${booking.vehicleType}</td>
            </tr>
            <tr>
                <td>Payment:</td>
                <td><b>$ ${booking.payment}</b></td>
            </tr>
            <tr>
                <td>Expiry time:</td>
                <td>${booking.expiryTime}</td>
            </tr>
        </table>
    </div>
    <br/>
    <c:if test="${customerName == null || customerPhone == null}">
        <div style="border: solid 1px green; margin: 15px; display:inline-block;">Resolution:
            <table>
                <tr>
                    <td width="100px">
                        <form action="order/${order.id}/accept" method="post">
                            <input type="submit" value="Accept"/>
                        </form>
                    </td>
                    <td width="100px">
                        <form action="order/${order.id}/reject" method="post">
                            <input type="submit" value="Reject"/>
                        </form>
                    </td>
                </tr>
            </table>


        </div>
    </c:if>
</c:if>
<c:if test="${booking == null}">
    <span style="color: red; ">The queue is empty</span>
</c:if>

<br/><br/>
<input type="button" value="Reload Page" onClick="location.href='${pageContext.request.contextPath}/booking'">

<br/><br/>
<span style="color: green; ">The queue has ${bookingCount} bookings</span>

<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>
</html>
