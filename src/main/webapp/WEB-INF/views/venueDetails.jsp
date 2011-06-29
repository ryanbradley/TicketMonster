<%@ include file="/WEB-INF/views/include.jsp"%>
<div class="section">
	<div class="sectionHeader"><fmt:message key="venue.heading"></fmt:message></div>
	<div class="sectionContent">
		<c:out value="${venue.name}"/><br>
		<c:out value="${venue.address}"/>
		<p>${venue.description.active.content}<p/>
			<table border="0">
				<c:forEach items="${events}" var="event">
					<tr>
						<td>
							<a href="<c:url value="../events/${event.id}.htm"/>">${event.name}</a>
							<p>${event.description.active.content}<p/>
						</td>
					</tr>
				</c:forEach>
			</table>
	</div>
</div>