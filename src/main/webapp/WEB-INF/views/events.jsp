<%@ include file="/WEB-INF/views/include.jsp"%>

<div class="section">
	<div class="sectionHeader"><fmt:message key="events.heading"/></div>
	<div class="sectionContent">
		<p><fmt:message key="events.greeting"/><p/>
		<table>
			<c:forEach items="${events}" var="event">
				<tr>
					<td>
						<c:out value=""/>
						<p>${event.description.active.content}<p/><br>
					</td>
				</tr>
				<tr>
					<td>
						<input type="submit" value="View Event" onclick='viewEvent(${event.id})'/>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>

<script type="text/javascript">

	function viewEvent(id) {
		window.location = '<c:url value="/events/"/>' + id;		
	}

</script>