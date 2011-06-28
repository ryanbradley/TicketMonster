<%@ include file="/WEB-INF/jsp/include.jsp"%>

<html>
	<head><title><fmt:message key="title"></fmt:message></title></head>
	
	<body>
		<h1><fmt:message key="events.heading"></fmt:message></h1>
		<p><fmt:message key="events.greeting"></fmt:message></p>
		
		<h3>Upcoming Events</h3>
		<table border="0">
		<c:forEach items="${events}" var="event">
		<tr>
		<td>		
		<c:out value=""/>
        <p>
		${event.description.active.content}
        <p/>
   		</td>
        </tr>
		</c:forEach>
		</table>
	</body>

</html>