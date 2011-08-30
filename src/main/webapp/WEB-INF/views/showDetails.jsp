<%@ include file="/WEB-INF/views/include.jsp" %>

<script type="text/javascript">
        var dayNames = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
        var monthNames = ["January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"];

        function prettyDate(val) {
            return dayNames[val.getDay()] + " " +
                    val.getDate() + " " +
                    monthNames[val.getMonth()] + " " +
                    val.getFullYear() + " @ " +
                    zeropad(val.getHours(), 2) + ":" +
                    zeropad(val.getMinutes(), 2);
        }

        function zeropad(val, digits) {
            val = val + "";
            while (val.length < digits) val = "0" + val;
            return val;
        }

        function updateAllocation(showId, priceCategoryId, quantity) {
			baseUrl = '<c:url value="/allocate?"/>';
			jQuery.getJSON(baseUrl + "showId=" + showId + "&priceCategoryId=" + priceCategoryId + "&quantity=" + quantity, function (result) {
				if(result == true)
					$("div#sectionContent").append("Available seats have been allocated.");
				else
					$("div#sectionContent").append("Insufficient available seats.");
			});			        	
        }
        
</script>

<div class="section">
	<div class="sectionHeader"><fmt:message key="bookings.heading"/></div>
	<div class="sectionContent" id="showDate"></div>
	<div class="sectionContent">
			<c:out value=""/>
			<p>${show.event.description.active.content}</p>
			
			<p>${show.venue.name}<br>
			${show.venue.address}
			${show.venue.description.active.content}</p>
	</div>
	<div class="sectionContent" id="priceCategories">
		<table>
		<c:forEach items="${bookingRequest.categoryRequests}" var="categoryRequest" varStatus="categoryStatus">
			<c:if test="${categoryRequest.priceCategory.section.name != lastSection}">
				<tr>			
				<td><h3><a href="#"><c:out value="${categoryRequest.priceCategory.section.name}"/></a></h3></td>
				</tr>
			</c:if>
			<c:set var="lastSection" value="${categoryRequest.priceCategory.section.name}"/>			
			<tr>
				<td><c:out value="${categoryRequest.priceCategory.category.description} - $${categoryRequest.priceCategory.price}"/></td>
				<td>
					<spring:bind path="bookingRequest.categoryRequests[${categoryStatus.index}].quantity">
						<input name="<c:out value="${status.expression}"/>" id="${status.expression}" value="${status.value}"
							onchange="updateAllocation(${show.id}, ${categoryRequest.priceCategoryId}, ${status.value})">
					</spring:bind>
					
					<spring:bind path="bookingRequest.categoryRequests[${categoryStatus.index}].priceCategoryId">
						<input type="hidden" name="<c:out value="${status.expression}"/>" id="${status.expression}" value="${status.value}">
					</spring:bind>
				</td>
			</tr>
		</c:forEach>
		</table>
		<input type="submit" value="Execute">	
	</div>
</div>

<script type="text/javascript">
	$("div#showDate").append(prettyDate(new Date(<c:out value="${show.showDate.time}"/>)));
</script>
