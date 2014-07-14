<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<portlet:defineObjects />

<jsp:useBean id="entries" class="java.util.ArrayList"  scope="request"/>

<aui:button-row cssClass="guestbook-buttons">

	<portlet:renderURL var="addEntryURL">
		<portlet:param name="mvcPath" value="/html/guestbook/edit_entry.jsp"></portlet:param>
	</portlet:renderURL>
	
	<aui:button onClick="<%= addEntryURL.toString() %>" value="Add Entry"></aui:button>
	
</aui:button-row>
<script>



</script>
<p id="madlib"></p>