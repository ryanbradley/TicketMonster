<%@ include file="/WEB-INF/jsp/include.jsp"%>

<html>
	<head><title><fmt:message key="title"></fmt:message></title></head>
	
	<body>
	<h3>Venue Details</h3>
	<c:out value="${venue.name}"/><br>
	<c:out value="${venue.address}"/>
	<p>
	${venue.description.active.content}
	<p/>
	<table border="0">
	<thead><strong>Events</strong></thead>
	<c:forEach items="${events}" var="event">
	<tr>
	<td><c:out value=""/>
	<p/>
	${event.description.active.content}
	<p/></td></tr>
	</c:forEach>
	</table>
	</body>
</html>