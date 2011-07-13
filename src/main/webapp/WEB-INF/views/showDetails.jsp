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

		function getPriceCategories(eventId, venueId) {
			baseUrl = '<c:url value= "/categories.htm?"/>';
	        jQuery.getJSON(baseUrl + "eventId=" + eventId + "&venueId=" + venueId, function (result) {
	        	$("#priceCategories").empty();
	            currentSection = 0;
	            var html = "";
	            jQuery.each(result, function (index, value) {
	            	if (currentSection != value.section.id) {
	            		if (currentSection != 0) {
	                    	html += ("</ul></div>");
	                    }
	                    html += ("<h3><a href='#'>" + value.section.name + "</a></h3>");
	                    html += ("<div><ul>");
	                    currentSection = value.section.id;
	                }
	                html += ("<li>" + value.category.description + " - $" + value.price + "</li>");
				});
	            if (currentSection != 0) {
	            	html += ("</ul></div>");
	            }
	            $("#priceCategories").append($(html));
			});
		 }
</script>

<div class="section">
	<div class="sectionHeader"><fmt:message key="bookings.heading"/></div>
	<div class="sectionContent" id="showDate"></div>
	<div class="sectionContent">
			<c:out value=""/>
			<p>${show.event.description.active.content}</p>
			
			<p>${show.venue.name}</p>
			<p>${show.venue.address}</p>
			<p>${show.venue.description.active.content}</p><br>		
	</div>
	<div class="sectionContent" id="priceCategories">
		<c:forEach items="${categories}" var="category">
			<h3><a href="#"><c:out value="${category.section.name}"/></a></h3>
			<ul>
				<li><c:out value="${category.category.description} - ${category.price}"/></li>
			</ul><br>
		</c:forEach>			
	</div>
</div>

<script type="text/javascript">
	$("div#showDate").append(prettyDate(new Date(<c:out value="${show.showDate.time}"/>)));
</script>
