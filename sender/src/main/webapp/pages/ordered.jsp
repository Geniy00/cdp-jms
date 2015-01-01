<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Reservation request was ordered</title>
    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.3.min.js"></script>
</head>
<body>
<br/><br/>

<h3>Reservation request was ordered</h3>

<sf:form action="${pageContext.request.contextPath}/order" method="post" modelAttribute="reservationRequest">
    <table>
        <tr>
            <td colspan="2">Customer:</td>
        </tr>
        <tr>
            <td width="200">Name:</td>
            <td width="150">${reservationRequest.customerName}</td>
        </tr>
        <tr>
            <td>Phone:</td>
            <td>${reservationRequest.customerPhone}</td>
        </tr>
        <tr>
            <td colspan="2"><br/></td>
        </tr>
        <tr>
            <td>Request id</td>
            <td><strong>${reservationRequest.requestId}</strong></td>
        </tr>
        <tr>
            <td colspan="2"><br/></td>
        </tr>
        <tr>
            <td>Start:</td>
            <td>${reservationRequest.startPosition}</td>
        </tr>
        <tr>
            <td>Finish:</td>
            <td>${reservationRequest.finishPosition}</td>
        </tr>
        <tr>
            <td>Date:</td>
            <td>${reservationRequest.deliveryTime}</td>
        </tr>
        <tr>
            <td>Vehicle type:</td>
            <td>${reservationRequest.vehicleType}</td>
        </tr>
        <tr>
            <td>Price:</td>
            <td>
                <div id="priceValue" style="color: blue"></div>
            </td>
        </tr>
        <tr>
            <td>Status:</td>
            <td>
                <div id="statusValue" style="font-weight: bold; color: #68ff00"></div>
                <div id="failureReason" style="color: red"></div>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <a href="${pageContext.request.contextPath}/manual/random">
                    <input type="button" value="Create new"/>
                </a>
            </td>
        </tr>
    </table>
    <br/>

    <div id="loading"><span style="color: gray;">Loading...</span></div>
    <div id="errorReason"></div>
</sf:form>
<br/>
<br/>
<br/>
<%--<c:if test="${sentReservationRequest.customerName != null}">--%>
<%--<span style="color: green; ">ReservationRequest with name <b>${sentReservationRequest.customerName}</b>--%>
<%--and from <b>${sentReservationRequest.startPosition} to ${sentReservationRequest.finishPosition}</b> was sent successfully</span>--%>
<%--</c:if>--%>

<br/>
<a href="${pageContext.request.contextPath}/manual/random">Back</a>
</body>

<script>
    $(document).ready(function () {

        $(document).ajaxStart(function () {
            $('#loading').show()
        }).ajaxSuccess(function () {
            $('#loading').hide()
        })

        loadUpdatedInfo();
    });

    var MAX_TIMEOUT = 10000;
    var currentTimeout = 1000;

    function loadUpdatedInfo() {
        $.ajax("${pageContext.request.contextPath}/getAjaxResponseObject?requestId=${reservationRequest.requestId}",
                {
                    success: function (response) {
                        if(currentTimeout < MAX_TIMEOUT) {
                            currentTimeout += 1000
                        }
                        if (response == "") {
                            setTimeout(loadUpdatedInfo, currentTimeout);
                            return;
                        } else if (response.status != "FAILURE"
                                && response.status != "COMPLETED") {
                            setTimeout(loadUpdatedInfo, currentTimeout)
                        }

                        var status = response.status;
                        $('#statusValue').text(status)
                        if (status === 'FAILURE') {
                            $('#failureReason').text(response.failureReason)
                        } else {
                            $('#priceValue').text(response.price + " uah")
                        }
                    }
                }
        )
    }
</script>
</html>
