<%@ include file="/WEB-INF/views/include.jsp" %>
<div class="section">
    <div class="sectionHeader"><fmt:message key="venues.heading"/></div>
    <div class="sectionContent">
        <p><fmt:message key="venues.greeting"></fmt:message><p/>
        <table>
            <c:forEach items="${venues}" var="venue">
                <tr>
                    <td>
                        <p>
                                <c:out value="${venue.name}"/><br>
                                <c:out value="${venue.address}"/><br>
                                ${venue.description.active.content}<br><br>
                                
                        <p/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

