<%@ include file="/WEB-INF/views/include.jsp"%>
<div class="section">
	<div class="sectionHeader"><fmt:message key="events.heading"/></div>
	<div class="sectionContent">
		<p><fmt:message key="events.greeting"/><p/>
		<table border="0">
			<c:forEach items="${events}" var="event">
				<tr>
					<td>
						<c:out value=""/>
						<p>${event.description.active.content}<p/><br>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>