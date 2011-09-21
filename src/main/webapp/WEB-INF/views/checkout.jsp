<%@ include file="/WEB-INF/views/include.jsp"%>

<div class="section">
	<div class="sectionHeader">Check Out</div>
	<div class="sectionContent" id="checkOut">
		<c:if test="${user.username != NULL}">
		User ${user.username} is checking out of TicketMonster after purchasing $${total} in tickets.
		</c:if>
		<c:forEach items="${allocations}" var="allocation">
			<c:if test="${allocation.quantity > 1}">
				<p/>Allocation: ${allocation.quantity} seats in Row ${allocation.row.name}, Section ${allocation.row.section.name}.  Allocated seats are from ${allocation.startSeat} to ${allocation.endSeat}.
			</c:if>
			<c:if test="${allocation.quantity == 1}">
				<p/>Allocation: ${allocation.quantity} seats in Row ${allocation.row.name}, Section ${allocation.row.section.name}.  Allocated seat ${allocation.startSeat}.
			</c:if>
		</c:forEach>
	</div>
</div>