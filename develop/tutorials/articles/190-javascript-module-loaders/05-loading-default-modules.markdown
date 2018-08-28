# Loading MetalJS Modules in Liferay

Metal.js, jQuery, and Lodash JavaScript libraries are included out-of-the-box. 
Both 
[JQuery](https://portalmigration.wedeploy.io/docs/javascript/jQuery.html) 
and 
[Lodash](https://portalmigration.wedeploy.io/docs/javascript/lodash.html), 
however, are being migrated away from in @product@ in favor of MetalJS or 
vanilla JavaScript. In some instances, you may need to load a MetalJS module as 
a replacement, or there may be a MetalJS module that you would like to use in 
your theme. 

To load a MetalJS module in your JavaScript files follow these steps:

1.  Open the JavaScript file you want to load the module in 
    (for example, you could load it in your theme's `main.js` file).

2.  Call the Liferay Loader's `require()` method to require the 
    [module(s)](https://github.com/metal/metal-plugins/tree/master/packages) 
    you need. Note that the relative path to the module is used. The example 
    below uses the 
    [`Clipboard.js` module](https://github.com/metal/metal-plugins/blob/master/packages/metal-clipboard/src/Clipboard.js):

        Liferay.Loader.require("metal-clipboard/src/Clipboard", 
        function(metalClipboardSrcClipboard) {
            //add module code here
        }, function(error) {
            console.error(error)
        });

3.  Use the module in your code. If you've 
    [loaded ES2015+ modules in your portlet](/develop/tutorials/-/knowledge_base/7-1/using-esplus-modules-in-your-portlet), 
    the `<aui:script>` tag's `require` attribute follows the same approach; 
    References to the module within the `Liferay.Loader.require()` method are 
    named after the `require` value, in camel-case and with all invalid 
    characters removed. The `Clipboard.js` module's reference 
    `metalClipboardSrcClipboard` is derived from the module's relative path 
    value `metal-clipboard/src/Clipboard`. The value is stripped of its dash and 
    slash characters and converted to camel case:

        Liferay.Loader.require("metal-clipboard/src/Clipboard", 
        function(metalClipboardSrcClipboard) {  
          
            //example code
            var clipboard = new metalClipboardSrcClipboard.default();
            
            clipboard.on('success', function(e) {
            e.clearSelection();

            console.info('Copied!');
            console.info('Action:', e.action);
            console.info('Text:', e.text);
            });
            
        }, function(error) {
            console.error(error)
        });
 
Now you know how to load MetalJS modules in your JavaScript!

## Related Topics [](id=related-topics)

[Using External JavaScript Libraries](/develop/tutorials/-/knowledge_base/7-1/using-external-javascript-libraries)

[Loading AMD Modules](/develop/tutorials/-/knowledge_base/7-1/loading-amd-modules-in-liferay)
