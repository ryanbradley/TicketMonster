<%@ include file="/WEB-INF/views/include.jsp"%>

<div class="section">
	<div class="sectionHeader">Check Out</div>
	<div class="sectionContent" id="checkOut">
		${user.firstName} ${user.lastName} (Username ${user.username}) is checking out of TicketMonster after purchasing $${total} in tickets.
		<c:forEach items="${allocations}" var="allocation">
			<c:if test="${allocation.quantity > 1}">
				<p/>Purchased ${allocation.quantity} seats from seat ${allocation.startSeat} to seat ${allocation.endSeat} in Row ${allocation.row.name}, Section ${allocation.row.section.name}.
			</c:if>
			<c:if test="${allocation.quantity == 1}">
				<p/>Purchased seat ${allocation.startSeat} in Row ${allocation.row.name}, Section ${allocation.row.section.name}.
			</c:if>
		</c:forEach>
	</div>
</div>