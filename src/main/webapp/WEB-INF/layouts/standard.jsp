<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Ticket Monster</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<link rel="stylesheet" href="<c:url value="/static/style/default.css"/>"/>

    <script type="text/javascript" src="<c:url value="/static/jquery-1.6.1.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/static/jquery-ui-1.8.14.custom.min.js" />"></script>

</head>

<body>
<div id="container">

    <div id="main">
        <div id="header">

            <div class="headerTitle">
                <a href="<c:url value="/events"/>"><img src="<c:url value="/static/style/ticket.png"/>"/></a>
            </div>

            <div class="headerSearch">
                Search
                <input type="text"/>
            </div>
        </div>


        <div id="menu" class="menu">
            <div id="menuItem1" class="menuItem"><a href="<c:url value="/events?major=true"/>">Major Events</a></div>
            <div id="menuItem2" class="menuItem"><a href="<c:url value="/events?category=1"/>">Concerts</a></div>
            <div id="menuItem3" class="menuItem"><a href="<c:url value="/events?category=2"/>">Theatre</a></div>
            <div id="menuItem4" class="menuItem"><a href="<c:url value="/events?category=3"/>">Musicals</a></div>
            <div id="menuItem5" class="menuItem"><a href="<c:url value="/events?category=4"/>">Sports</a></div>
            <div id="menuItem6" class="menuItem"><a href="<c:url value="/events?category=5"/>">Comedy</a></div>
            <div id="menuItem7" class="menuItem"><a href="<c:url value="/venues"/>">Venues</a></div>
        </div>

        <div id="content">
            <tiles:insertAttribute name="body"/>
        </div>
    </div>
</div>
</body>
</html>