# @product@ JavaScript Utilities [](id=javascript-utilities)

This tutorial explains some of the utility methods and objects inside the 
`Liferay` global JavaScript object.

## Liferay Browser [](id=liferay-browser)

The `Liferay.Browser` object contains methods that expose the current user agent 
characteristics without the need of accessing and parsing the global 
`window.navigator` object.

The available methods for the `Liferay.Browser` object are listed in the table 
below:

| Method | Type | Description |
| --- | --- | --- |
| acceptsGzip | boolean | |
| getMajorVersion | number | |
| getRevision | number | |
| getVersion | number | |
| isAir | boolean | |
| isChrome | boolean | |
| isFirefox | boolean | |
| isGecko | boolean | |
| isIe | boolean | |
| isIphone | boolean | |
| isLinux | boolean | |
| isMac | boolean | |
| isMobile | boolean | |
| isMozilla | boolean | |
| isOpera | boolean | |
| isRtf | boolean | |
| isSafari | boolean | |
| isSun | boolean | |
| isWebKit | boolean | |
| isWindows | boolean | |

## Liferay Util [](id=liferay-util)

The `Liferay.Util` object contains methods that expose the current user agent 
characteristics as well as other helpful information, without the need of 
accessing and parsing the global `window` and `window.screen` objects.

Several handy methods for the `Liferay.Util` object are listed in the table 
below:

| Method | Type | Description |
| --- | --- | --- |
| addInputCancel | | Adds a button search cancel icon in order to clear the text</br> on inputs and textareas |
| addParams | string | Returns a URL with parameters |
| checkAll || Checks all the checkboxes for a form |
| checkAllBox | number | Returns the total number of checked checkboxes for a form |
| checkTab || Checks if the Tab key is pressed in a box and then focuses the field |
| disableElements | | Disables all the child elements for a node |
| disableEsc | | Disables the escape key |
| disableFormButtons | | Disables a set of inputs in a form |
| disableToggleBoxes | | Toggles the `disabled` state for a togglebox when a checkbox is clicked |
| enableFormButtons | | Enables form button(s) |
| escapeCDATA | string | Escapes the CDATA tags in a string |
| focusFormField | | Focuses a form field when the page loads |
| forcePost |||
| getAttributes | object | Returns the attributes for an element |
| getColumnId | string | Returns the columnId for a column |
| getDOM | object | Returns the underlying DOM element for a node |
| getGeolocation|| Gets the user's geographic coordinates |
| getLexiconIcon | object | Returns a SVG node element for a Lexicon icon.<br/>The Lexicon icons are listed</br>on [Lexicon's Site](http://liferay.github.io/lexicon/content/icons-lexicon/). |
| getLexiconTpl | string | Returns a SVG element for a Lexicon icon<br/>as a string |
| getOpener | object | Returns the window object that opened the current dialog |
| getPortletId | string | Returns the portlet ID |
| getPortletNamespace | string | Returns the namespace for a portlet ID |
| getTop | object | Returns the parent Window object |
| getURLWithSessionId | string | Returns a URL with the session ID |
| getWindow | object | Returns the window object for an ID |
| getWindowName | string | Returns the window name |
| getWindowWidth | number | Returns the width of the window in pixels |
| inBrowserView | boolean | Whether a node is currently visible in the browser window |
| isphone | boolean | Whether the device viewing the site is a phone |
| isTablet | boolean | Whether the device viewing the site is a tablet |
| listCheckboxesExcept | string | Returns a string list of checkboxes for a form,<br/>with some exclusions. Matches are returned as 'on.' |
| listCheckedExcept | string | Using the `listCheckBoxesExcept` method, this returns<br/>a list of checkboxes for a form that are checked,<br/>with some exceptions. Matches are returned as 'on.'  |
| listSelect | string | Returns a delimited list containing the options for the specified select box. If no<br/>`delimeter` is specified, `,` is used. |
| listUncheckedExcept | string | Using the `listCheckBoxesExcept` method, this<br/>returns a list of checkboxes in the given form that<br/>are unchecked, excluding the specified exception.<br/>Matches are returned as 'on.' |
| ns | object | Returns an object containing the namespace |
| openInDialog | | Opens the provided configuration in a dialog |
| openWindow | | Opens a window with the specified configuration |
| processTab |||
| randomInt | number | Generates a random integer |
| removeEntitySelection || Removes an entity and disables the associated button |
| reorder || Reorders input boxes |
| rowCheckerCheckAllBox |||
| savePortletTitle | | Saves the portlet title via AJAX with parameters |
| selectEntityHandler | | Fires an event, passing the entity's `data-`<br/>attributes when the entity is selected in a `container` |
| selectFolder | | Handles the data and UI attributes for selecting a folder |
| setCursorPostion | | Sets the cursor's position in an element's text |
| setSelectionRange | | Selects the range of text for an element |
| showCapsLock | | Notifies the user when the caps lock key is on while entering input.<br/>See the example below for more information. |
| sortByAscending | | Used in the AUI `liferay-input-move-boxes` module,<br/>sorts boxes by ascending order |
| submitForm | | Submits a form |
| toCharCode | string | Returns the character code for a character |
| toggleBoxes | | Toggles the visibility of content via a checkbox |
| toggleDisabled | | Toggles the `disabled` state of the given button node |
| toggleRadio | | Toggles the visibility of content via a radio button |
| toggleSearchContainerButton | | Toggles the `disabled` state of a search container<br/>button via a checkbox |
| toggleSelectBox | | Toggles the visibility of content based on a select box's value |
| toNumber | number | Casts the given value as a number |

### Using the ShowCapsLock Method [](id=using-the-showcapslock-method)

The `showCapsLock` method lets you notify the user that the caps lock key is on
when they fill in a form field.

Follow these steps to use the `showCapsLock` method:

1.  Within an `<aui:script>` or plain HTML, create a `span` element that
    contains the caps lock message, and set the `display` style to `none`. Below 
    is an example configuration:

        var form = AUI.$(document.<portlet:namespace />myForm);
    
        <span id="<portlet:namespace />passwordCapsLockSpan" style="display: none;">
            <liferay-ui:message key="caps-lock-is-on" />
        </span>

2.  Attach a `keypress` event to the form field that the caps lock message is 
    associated with. For example, the configuration below attaches a `keypress` 
    event a `password` form field:
        
        form.fm('password').on(
                'keypress',
                function(event) {
                    ...
                }
        );

3.  Finally, configure the `showCapsLock` method. The `showCapsLock` method has 
    two parameters: `event`, the event that triggers the caps lock message, and 
    `span`, the element that contains the text to display the caps lock message:
    
        form.fm('password').on(
                'keypress',
                function(event) {
                        Liferay.Util.showCapsLock(event, '<portlet:namespace />
                        passwordCapsLockSpan');
                }
        );

![Figure 1: The caps lock message is useful for password fields.](../../images/caps-lock-on.png)
        
When the caps lock key is pressed while entering the field, the message
displays.

## Related Topics [](id=related-topics)

[Liferay Theme Display](https://dev.liferay.com/develop/tutorials/-/knowledge_base/7-0/liferay-themedisplay)
