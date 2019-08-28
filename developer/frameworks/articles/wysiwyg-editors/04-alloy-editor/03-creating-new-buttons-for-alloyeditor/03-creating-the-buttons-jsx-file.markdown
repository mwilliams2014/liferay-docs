---
header-id: creating-the-alloyeditor-buttons-jsx-file
---

# Creating the Button's JSX File

[TOC levels=1-4]

| **Note:** AlloyEditor was updated to V 2.0.0. which resulted in some 
| [breaking changes](/docs/7-2/reference/-/knowledge_base/r/breaking-changes#updated-alloyeditor-v20-includes-new-major-version-of-react) 
| due to a version change in React. If you created a button using the older 
| `createClass()` method, you must update your code to use the ES6 syntax, as 
| described below.

Follow these steps to create your button for AlloyEditor:

1.  Create a `.jsx` file in your OSGi bundle's `resources\META-INF\resources\js` 
    folder. This file defines your button's configuration.

2.  Inside the JSX file, import the packages, including the [mixins](/docs/7-2/reference/-/knowledge_base/r/alloyeditor-button-reference-guide#mixins) 
    that your button requires. These provide additional functionality, making it 
    easy to add features to your button, such as binding a shortcut key to your 
    button. The log text button uses AlloyEditor's `React` and `ReactDom` and 
    the mixins `ButtonStateClasses` and `ButtonKeystroke`:

    ```javascript
    import React from 'react'
    import ReactDOM from 'react-dom'
    import ButtonStateClasses from '../base/button-state-classes';
    import ButtonKeyStroke from '../base/button-key-stroke';
    import EditorContext from '../../adapter/editor-context';
    ```

3.  Extend the `React.Component` class to create your button's class:

    ```javascript
    class LogSelectedTextButton extends React.Component {
        {
            //button configuration goes here
        }
    );
    ```

6.  Optionally define any default properties your button has for each instance 
    using the `getDefaultProps` method. The example below uses the 
    `ButtonKeystroke` mixin's required `command` and `keystroke` properties to 
    set the shortcut keys for the button's `logText()` function:

    ```javascript
    /**
     * Lifecycle. Returns the default values of the properties used in the widget.
     *
     * @instance
     * @memberof LogSelectedTextButton
     * @method getDefaultProps
     * @return {Object} the default properties.
     */
    static getDefaultProps = {
      command: 'logText',
      keystroke: {
          fn: 'logText',
          keys: CKEDITOR.CTRL + CKEDITOR.SHIFT + 89 /*Y*/
      },
    };
    ```

7.  Define the `key` to specify the button's alias name to use in 
    [AlloyEditor's configuration](/docs/7-2/frameworks/-/knowledge_base/f/adding-buttons-to-alloyeditor-toolbars). 
    The `my-log-text-button` module's key is shown below:

    ```javascript
        /**
         * The name which is used as an alias of the button in the configuration.
         *
         * @default LogSelectedTextButton
         * @memberof LogSelectedTextButton
         * @property {String} key
         * @static
         */
        static key = 'logSelectedText';
    ```

8.  Define the HTML markup to render for your button. The example below uses the 
    `getStateClasses()` method to retrieve the state class information provided 
    by the `ButtonStateClasses` mixin and add it to the current `cssClass` 
    value. It also uses Liferay Util's `getLexiconIconTpl()` method to retrieve 
    a Lexicon icon to use for the button. See 
    [Lexicon's Design Site](https://clayui.com/docs/components/icons.html) 
    for a full list of the available icons. 

    ```javascript
    /**
     * Lifecycle. Renders the UI of the button.
     *
     * @instance
     * @memberof ButtonOrderedList
     * @method render
     * @return {Object} The content which should be rendered.
     */
    render() {
      const cssClass = `ae-button ${this.getStateClasses()}`;

      return (
        <button
          aria-label="log Text Button"
          aria-pressed={cssClass.indexOf('pressed') !== -1}
          className={cssClass}
          data-type="button-log-text"
          onClick={this._logText}
          tabIndex={this.props.tabIndex}
          title= "Log the selected text in the console">
          <ButtonIcon symbol="view" />
        </button>
      );
    }
    ```

9.  Define your button's main action. Retrieving the 
    [`nativeEditor`](https://alloyeditor.com/api/1.5.0/Core.html#nativeEditor), 
    as shown in the example below, gives you access to the full API of 
    CKEditor. From there, you can use any of the available 
    [`CKEditor.editor` methods](https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_editor.html#methods) 
    to interact with the editor's content. The example below chains the editor's 
    [`getSelection()`](https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_editor.html#method-getSelection) 
    and 
    [`getSelectedText()`](https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_dom_selection.html#method-getSelectedText) 
    methods to retrieve the user's highlighted text, and then it logs it to the 
    browser's console:

    ```javascript
    /**
     * @protected
     * @method  _logText
     */
    _logText = () => {
        const editor = this.context.editor.get('nativeEditor');
        const selectedText = editor.getSelection().getSelectedText();

        console.log("Your selected text is " + selectedText);
    }
    ```

10. Finally, export the button:

    ```javascript
    export default ButtonCommand(
    	ButtonStateClasses(ButtonStyle(LogSelectedTextButton))
    );
    ```

Now you know how to create a button for AlloyEditor! 

## Related Topics

- [Adding New Behavior to an Editor](/docs/7-2/frameworks/-/knowledge_base/f/adding-new-behavior-to-an-editor)
- [Creating the AlloyEditor Button's OSGi Bundle](/docs/7-2/frameworks/-/knowledge_base/f/creating-the-alloyeditor-buttons-osgi-bundle)
- [Contributing the Button to AlloyEditor](/docs/7-2/frameworks/-/knowledge_base/f/contributing-the-button-to-alloyeditor)
