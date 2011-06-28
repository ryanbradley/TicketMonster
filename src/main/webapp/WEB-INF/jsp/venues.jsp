<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
	<head><title><fmt:message key="title"></fmt:message></title></head>
	<body>
		<h1><fmt:message key="venues.heading"></fmt:message></h1>
		<p><fmt:message key="venues.greeting"></fmt:message><p/>
		<table border="0">
		<c:forEach items="${venues}" var="venue">
		<tr>
		<td>
		<p>
		<c:out value="${venue.name}"/><br>
		<c:out value="${venue.address}"/><br>
		${venue.description.active.content}<br><br>
		<p/>
		</td>
		</tr>
		</c:forEach>
		</table>
	</body>
</html>