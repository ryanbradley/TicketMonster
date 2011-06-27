<%@ include file="/WEB-INF/jsp/include.jsp"%>

<html>
	<head><title><fmt:message key="title"></fmt:message></title></head>

	<body>
	<h3>Event Details</h3>
	<c:out value=""/><br>
	<p/>
	${event.description.active.content}
	<p/>
	</body>

</html>