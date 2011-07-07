<%@ include file="/WEB-INF/views/include.jsp"%>

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

        function refreshTimes(venueId, eventId) {
            baseUrl = '<c:url value="/shows.htm?"/>';
            jQuery.getJSON(baseUrl + "eventId=" + eventId + "&venueId=" + venueId, function (result) {
                $("select#times").empty();
                jQuery.each(result, function (index, value) {
                    $("select#times").append("<option value='" + value.showId + "'>" + prettyDate( new Date(value.date)) + "</option>");
                });
            });
        }

		function getVenueDetails(venueId) {
            baseUrl = '<c:url value= "/venues/[venueId].htm" />';
        	jQuery.getJSON(baseUrl.replace("[venueId]", venueId), function (result) {
				$("div#venueDetails").empty();
	            $("div#venueDetails").append(result.address + "<p/>" + result.description.active.content);		
		    });
        }

        function getPriceCategories(eventId, venueId) {
			baseUrl = '<c:url value= "/categories.htm?"/>';
			jQuery.getJSON(baseUrl + "eventId=" + eventId + "&venueId=" + venueId, function (result) {
				$("div#priceCategories").empty();
				jQuery.each(result, function (index, value) {
					$("div#priceCategories").append("<p/>" + value.section.name + ": " + "$" + value.price);
					});
				});
        }

</script>
    
<div class="section">

	<div class="sectionHeader"><fmt:message key="event.heading"/></div>
	<div class="sectionContent">
		<c:out value=""/>
		<p>${event.description.active.content}<p/>
			<h3>Venues</h3>
            <p/>
            <select id="venues">
            <c:forEach items="${venues}" var="venue">
				<option value='${venue.id}'>${venue.name}</option>
			</c:forEach>
            </select>
            <div class="sectionContent" id="venueDetails"></div>
            <p/>
            <h3>Show Times</h3>
            <select id="times"></select>
            <p/>
            <div class="sectionContent" id="priceCategories"></div>
	</div>
</div>

<script type="text/javascript">
$("select#venues").change(function () {
	$("select#venues option:selected ").each(function() {
        refreshTimes($(this).val(), <c:out value="${event.id}"/>);
        getVenueDetails($(this).val());
        getPriceCategories(<c:out value="${event.id}"/>, $(this).val());
    });
}).change();

	$(function() {
		$( "#accordion" ).accordion();
	});


</script>


<div id="accordion">
    <h3 class="sectionHeader"><a href="#" >S1 - Balcony</a></h3>

    <div class="sectionContent">
        <p/>
        <ul>
            <li> Adult - 10^6</li>
            <li> Child - 0.4</li>
        </ul>
    </div>
    <h3><a href="#">S2 - Roof</a></h3>

    <div>
        <p/>
        <ul>
            <li> Adult - 99</li>
            <li> Child - 102</li>
        </ul>
    </div>
</div>


