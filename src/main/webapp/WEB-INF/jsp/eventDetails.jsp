<%@ include file="/WEB-INF/jsp/include.jsp"%>

<html>
	<head><title><fmt:message key="title"></fmt:message></title></head>

	<body>
	<h3>Event Details</h3>
	<c:out value=""/>
	<p>
	${event.description.active.content}
	<p/>
	<table border="0">
	<thead><strong>Venues</strong></thead>
	<c:forEach items="${venues}" var="venue">
	<tr>
	<td><c:out value="${venue.name}" /></td>
	</tr>
	</c:forEach>
	</table>
	</body>

</html>