# Using Liferay Auto Fields

The `liferay-auto-fields` module allows you to duplicate form fields. Using the
module is a piece of cake. As this is an AUI module, it is for use within the
JSP of a portlet. You can quickly create an MVC portlet following the steps
covered in this [tutorial](). 

Follow these steps to make your form fields repeatable:

1.  Wrap each repeatable form row with the `lfr-form-row` and 
    `lfr-form-row-inline` classes. Below is an example configuration:

        <aui:fieldset id="info">
                <div class="lfr-form-row lfr-form-row-inline">
                        <div class="form-fields">
                        ...
                        </div>
                </div>
                
                ...
        </aui:fieldset>

2. Add the following `<aui:script>` to your JSP, replacing the `contentBox` 
   value with the name of your forms content wrapper:

        <aui:script use="liferay-auto-fields">
                new Liferay.AutoFields(
                        {
                                contentBox: '#<portlet:namespace />info',
                                namespace: '<portlet:namespace />'
                        }
                ).render();
        </aui:script>

the `liferay-auto-fields` module has the following configurable attributes:

**contentBox:** Specfies the container that the auto fields are within.

**fieldIndexes:** The name of the POST parameter that will contain a list of the 
order for the fields. Every row that has a `name` attribute with the 
`fieldIndexes` value will be indexed.

**namespace:** Specifies the namespace.
<!-- Namespace within autofields? generated autofields? -->
   
