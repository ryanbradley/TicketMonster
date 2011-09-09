<%@ include file="/WEB-INF/views/include.jsp"%>

<div class="section">
	<div class="sectionHeader">Check Out</div>
	<div class="sectionContent" id="checkOut">
		Checking out of TicketMonster after purchasing $${total} in tickets.
		
		<c:forEach items="${allocations}" var="allocation">
			<c:if test="${allocation.quantity > 0}"/>
				<p/>Allocation: ${allocation.quantity} seats in Row ${allocation.row.name}, Section ${allocation.row.section.name}.
		</c:forEach>
	</div>
</div>