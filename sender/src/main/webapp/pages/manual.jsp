<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>New reservation request</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/manual/random">Generate random order</a>
<br/><br/>

<sf:form action="${pageContext.request.contextPath}/price" method="post" modelAttribute="reservationRequest">
    <table>
        <tr>
            <td colspan="2">Customer:</td>
        </tr>
        <tr>
            <td>Name:</td>
            <td><sf:input path="customerName"/></td>
        </tr>
        <tr>
            <td>Phone:</td>
            <td><sf:input path="customerPhone"/></td>
        </tr>
        <tr>
            <td colspan="2"><br/></td>
        </tr>
        <tr>
            <td>Start:</td>
            <td><sf:input path="startPosition"/></td>
        </tr>
        <tr>
            <td>Finish:</td>
            <td><sf:input path="finishPosition"/></td>
        </tr>
        <tr>
            <td>Date:</td>
            <td><sf:input path="deliveryTime"/></td>
        </tr>
        <tr>
            <td>Vehicle type:</td>
            <td><sf:select path="vehicleType" items="${vehicleTypeValues}"/></td>
        </tr>
        <tr>
            <td colspan="2"><sf:button>Price an order</sf:button></td>
        </tr>
    </table>
</sf:form>
<br/>
<br/>
<br/>
<c:if test="${sentReservationRequest.customerName != null}">
    <span style="color: green; ">ReservationRequest with name <b>${sentReservationRequest.customerName}</b>
        and from <b>${sentReservationRequest.startPosition} to ${sentReservationRequest.finishPosition}</b> was sent successfully</span>
</c:if>

<br/>
<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>
