<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ page import="java.lang.Object" %>
<%@ page import="java.util.List" %>
<%@ page import="com.liferay.docs.guestbook.model.Entry" %>


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
<%
List<Entry> entryList = (List<Entry>) request.getAttribute("entries");
int lastIndex = entryList.size() - 1;
%>
<p>Attribute entries is null: <%= entryList == null %></p>
<p>Name: <%= entryList.get(lastIndex).getName() %></p>
<p>Place: <%= entryList.get(lastIndex).getPlace() %></p>