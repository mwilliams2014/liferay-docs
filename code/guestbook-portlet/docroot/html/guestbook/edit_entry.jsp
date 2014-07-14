<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ include file="../../js/main.js" %>
<portlet:defineObjects />

<portlet:renderURL var="viewURL">
	<portlet:param name="mvcPath" value="/html/guestbook/view.jsp"></portlet:param>
</portlet:renderURL>



<portlet:actionURL name="addEntry" var="addEntryURL"></portlet:actionURL>

<aui:form action="<%= addEntryURL %>" name="<portlet:namespace />fm">

        <aui:fieldset>

            <aui:input name="name" id="name"></aui:input>
            <aui:input name="message"></aui:input>
			<aui:input name="noun"></aui:input>
            <aui:input name="person"></aui:input>
            <aui:input name="place"></aui:input>
        </aui:fieldset>

        <aui:button-row>

			<aui:button type="submit"></aui:button>
			<aui:button id="create" value="Create!" onClick="<%= renderResponse.getNamespace() + \"test()\"%>"></aui:button>
			<aui:button type="cancel" onClick="<%= viewURL %>"></aui:button>

        </aui:button-row>
</aui:form>
<script type="text/javascript" src="main.js"></script>
