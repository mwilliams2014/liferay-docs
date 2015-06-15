# Using liferay-ui:success and liferay-ui:error Messages [](id=using-liferay-uisuccess-and-liferay-uierror-message)

As users perform different actions in your portlet, it's helpful for them to get
feedback as to whether the portlet's actions are succeeding or failing. For
example, on completing an action successfully, you can reassure the user of the
success. Similarly, if the user supplied invalid input to the action, you can
inform him and even describe why it's invalid or hint what might make it valid;
this kind of feedback helps your portlet's users.  

To facilitate such feedback, Liferay provides the means for you to pass an
attribute to your JSPs to indicate an action's success or failure. In your
portlet class, you simply set an attribute in the `actionRequest` that is then
read from the JSP. What's more, the JSP immediately removes the attribute from
the session so the message is only shown once. Liferay provides a helper class
and taglibs to do this operation easily. Figure 1 shows what a success message
can look like in your portlet. 

![Figure 1: Giving feedback on a user's success is easy using the `liferay-ui:success` tag and the `SessionMessage` helper class.](../../images/liferay-ui-success.png)

In this tutorial, you'll learn how to use session messages and the
`liferay-ui:success` and `liferay-ui:error` tags to provide user feedback in a
sample portlet called the My Greeting portlet. Are you ready to give it a try?
Go ahead and get started. 

## Confirming Success with liferay-ui:success [](id=confirming-success-with-liferay-uisuccess)

It's good to let a user know when a portlet was able to execute his action
successfully. So, you'll add a success message for an action successfully 
completed in My Greeting portlet. 

1.  As a starting point, use the My Greeting portlet which is available in the
<https://github.com/liferay/liferay-docs> 
GitHub repository 
[here](https://github.com/liferay/liferay-docs/tree/master/develop/tutorials/code/liferayui/success/begin/my-greeting-portlet).
You'll need to clone the repository if you haven't already done so. Then copy
the `my-greeting-portlet` folder into the `portlets` folder of your Liferay
Plugins SDK. 

2.  Open `MyGreetingPortlet.java`, found in package `com.liferay.samples`, and
add the attribute value `"success"` to the `actionRequest` via the
`SessionMessages` helper class. You can add it at the end of the `processAction`
method, so that the method looks like this: 

        @Override
        public void processAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws IOException, PortletException {

            PortletPreferences prefs = actionRequest.getPreferences();
            String greeting = actionRequest.getParameter("greeting");

            if (greeting != null) {
                prefs.setValue("greeting", greeting);
                prefs.store();
            }

            SessionMessages.add(actionRequest, "success");
            super.processAction(actionRequest, actionResponse);
        }

    Make sure to import the `com.liferay.portal.kernel.servlet.SessionMessages` 
    class. 

3.  In `view.jsp`, add a `liferay-ui:success` JSP tag with a message for the
user and add the `liferay-ui` taglib declaration, so that the JSP looks like
this: 

        <%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %> 
        <%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %> 
        <%@ page import="javax.portlet.PortletPreferences" %>

        <portlet:defineObjects />

        <liferay-ui:success key="success" message="greeting-saved successfully!"/>

        <% PortletPreferences prefs = renderRequest.getPreferences(); String
        greeting = (String)prefs.getValue(
            "greeting", "Hello! Welcome to our portal."); %>

        <p><%= greeting %></p>

        <portlet:renderURL var="editGreetingURL">
            <portlet:param name="mvcPath" value="/edit.jsp" />
        </portlet:renderURL>

        <p><a href="<%= editGreetingURL %>">Edit greeting</a></p>

4.  Redeploy the portlet, go to the edit screen, edit the
greeting, and save it. Similar to the figure below, the portlet shows your
success message and your new greeting.

    ![Figure 2: The `liferay-ui:success` tag provides the means to confirm the success of portlet actions.](../../images/success-saving-greeting.png)

    The message displayed is derived from the `key` attribute of the tag. At the
    moment you're supplying the message for the user. However, the best practice 
    is to supply the message using a language key. You'll take care of this now.

### Creating a Language Key Hook for the Success Message

Follow the steps below to create the language key hook:
    
1.  Goto *File*&rarr;*New*&rarr;*Liferay Plugin Project*.

2.  Set the *Project name* as `my-greeting-language-hook` and *Display name* as
`My Greeting Language Hook`.

3. Set the proper SDK and runtime, select *Hook* for the *Plugin type*, and
click *Finish*.

4. Right-click the language key hook you just created in the package explorer
on the left and select *New*&rarr;*Liferay Hook Configuration*.

5. With `my-greeting-language-hook` set as the *Hook plugin project* select
*Language properties* for the *hook type* and *click* *Next*.

6. Leave the default *Content folder* and click *Add*.

7. Enter *Language_en.properties* for the property file, click *OK* and
*Finish*.

8. Open the `docroot/WEB-INF/src` folder of the `my-greeting-language-hook` and
open the `Language_en.properties` file in the `content` package.

9. Add the following language key to the file and Save it:

        greeting-saved = Greeting saved successfully!

    Now that you have the language key defined you can update the tag to use it.     
    
10. In `view.jsp` of the *my-greeting-portlet*, update the `liferay-ui:success`
tag to look like the following code:

        <liferay-ui:success key="success" message="greeting-saved"/>
    
11. Redeploy the portlet, go to the edit screen, edit the greeting, and save it. 
The success message now uses the language key you just created! 
    
That was easy enough! Now that you've provided the user some positive feedback,
you need to provide a way to inform him when his action failed to complete
successfully. 

## Flagging Errors with liferay-ui:error [](id=flagging-errors-with-liferay-uierror)

Error notification operates similarly to success notification. There's an
equivalent utility class to `SessionMessages` called `SessionErrors`, to use for
error notification. And there's a `liferay-ui:error` JSP tag in which you can
supply your error message. 

Add error notification to the My Greeting portlet with these steps: 

1.  Add the following `liferay-ui:error` tag to your `view.jsp` after the
`liferay-ui:success` tag: 

    <liferay-ui:error key="error" message="sorry-error" />
    
2. Open the `Language_en.properties` file in the `my-greeting-language-hook`, 
add the language key you just referenced in your error tag, and save the file:

    sorry-error = Sorry, an error prevented saving your greeting
    
2. Modify `MyGreetingPortlet.java`'s `processAction` method to flag an error to
the `actionRequest`, on catching an exception. Your `processAction` method 
should look like this: 

        @Override
        public void processAction(
            ActionRequest actionRequest, ActionResponse actionResponse)
            throws IOException, PortletException {

            PortletPreferences prefs = actionRequest.getPreferences();
            String greeting = actionRequest.getParameter("greeting");

            if (greeting != null) {
                try {
                    prefs.setValue("greeting", greeting);
                    prefs.store();
                }
                catch(Exception e) {
                    SessionErrors.add(actionRequest, "error");
                }
            }

            SessionMessages.add(actionRequest, "success");
            super.processAction(actionRequest, actionResponse);
        }

    Make sure to import the `com.liferay.portal.kernel.servlet.SessionErrors` 
    class.

3.  Redeploy the `my-greeting-portlet` and `my-greeting-language-hook`. 

If an error occurs in processing the action request, your `view.jsp` shows
the error message in your portlet. 

![Figure 3: The sample My Greeting portlet shows an error message on failure to process the portlet action.](../../images/portlet-invalid-data.png)

The final My Greeting portlet implemented in this tutorial, including
its
[`MyGreetingPortlet.java`](https://github.com/liferay/liferay-docs/blob/master/develop/tutorials/code/liferayui/success/end/my-greeting-portlet/docroot/WEB-INF/src/com/liferay/samples/MyGreetingPortlet.java)
,
[`my-greeting-language-hook`](https://github.com/liferay/liferay-docs/blob/master/develop/tutorials/code/liferayui/success/end/my-greeting-portlet/docroot/view.jsp)
and
[`view.jsp`](https://github.com/liferay/liferay-docs/blob/master/develop/tutorials/code/liferayui/success/end/my-greeting-portlet/docroot/view.jsp)
files, is posted on GitHub 
[here](https://github.com/liferay/liferay-docs/tree/master/develop/tutorials/code/liferayui/success/end/my-greeting-portlet). 

To sum things up, you've added a success message for confirming successful
portlet action execution and you've added an error message for notifying when
something's gone wrong; you can implement these messages in your portlets too.
Your portlet users will be glad to get helpful feedback from your portlets. 

## Related Topics

[User Interfaces with AlloyUI](/develop/tutorials/-/knowledge_base/alloyui)

