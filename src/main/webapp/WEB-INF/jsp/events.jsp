<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="/WEB-INF/jsp/include.jsp"%>

<html>
	<head><title><fmt:message key="title"></fmt:message></title></head>
	
	<body>
		<h1><fmt:message key="heading"></fmt:message></h1>
		<p><fmt:message key="greeting"></fmt:message></p>
		
		<h3>Upcoming Events</h3>
		<c:forEach items="${events}" var="event">
		<c:out value="${event.name}"/><br><br>
        <p/>
		${event.description.active.content}
        <p/>
		</c:forEach>
	</body>

</html>