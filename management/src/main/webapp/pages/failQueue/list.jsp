<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<html>
<head>
    <title>Fail queue messages</title>

</head>
<body>
<script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
<br/><br/>
<br/><br/>

<c:forEach var="message" items="${messages}">
    <hr/>
    <table width="80%">
        <tr>
            <td>Message id: ${message.id}</td>
            <td><joda:format value="${message.created}" pattern="HH:mm:ss, dd MMM yyy"/></td>
        </tr>
        <tr>
            <td colspan="2">
                <pre class="prettyprint lang-html"><c:out value="${message.message}"/></pre>
            </td>
        </tr>
        <tr>
            <td colspan="2">

                <form action="failQueue/edit/${message.id}" method="GET">
                    <input type="submit" value="[Edit]" style="float: left"/>
                </form>
                <form action="failQueue/remove/${message.id}" method="POST">
                    <input type="submit" value="Remove" style="float: left"/>
                </form>
                <form action="failQueue/resend/${message.id}" method="POST">
                    <input type="submit" value="[Resend]" style="float: right"/>
                </form>
            </td>
        </tr>
    </table>
    <br/><br/>
</c:forEach>
<br/>


<c:if test="${dispatchers == null && dispatchers.size == 0}">
    Taxi dispatcher list is empty.
</c:if>


<br/><br/>
<a href="${pageContext.request.contextPath}">Back</a>
</body>
</html>