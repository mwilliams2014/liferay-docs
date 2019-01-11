# Loading Bundled npm Modules in Your Portlets [](id=referencing-a-bundled-npm-modules-package)

Once you've 
[exposed your modules](/develop/tutorials/-/knowledge_base/7-1/preparing-your-javascript-files-for-esplus), 
you can use them in your portlet via the `aui:script` tag's `require` attribute. 
You can load the npm module in your portlet using the 
`npmResolvedPackageName` variable, which is available by default since 
@product-ver@. You can then create an alias to reference it in your portlet. 

Follow these steps:

1.  Make sure the `<liferay-frontend:defineObjects />` tag is included in your 
    portlet's `init.jsp`. This makes the `npmResolvedPackageName` variable 
    available, setting it to your project module's resolved name. For instance, 
    if your module is called `my-module` and is at version `2.3.0`, the implicit 
    variable `npmResolvedPackageName` is set to `my-module@2.3.0`. This lets you 
    prefix any JS module require or soy component rendering with this variable.
    
2.  Use the `npmResolvedPackageName` variable along with the relative path to 
    your JavaScript module file to create an alias in the `<aui:script>`'s 
    `require` attribute. An example configuration is shown below:
    
        <aui:script 
          require='<%= npmResolvedPackageName + 
          "/js/my-module.es as myModule" %>'>

        </aui:script>

3.  Now you can use the alias inside the `aui:script`:

        <aui:script 
          require='<%= npmResolvedPackageName + 
          "/js/my-module.es as myModule" %>'>
          
            myModule.default();
        
        </aui:script>

Now you know how to use an npm module's package!

## Related Topics [](id=related-topics)

[Obtaining an OSGi bundle's Dependency npm Package Descriptors](/develop/tutorials/-/knowledge_base/7-1/obtaining-dependency-npm-package-descriptors)

[liferay-npm-bundler](/develop/reference/-/knowledge_base/7-1/liferay-npm-bundler)

[How @product@ Publishes npm Packages](/develop/reference/-/knowledge_base/7-1/how-liferay-portal-publishes-npm-packages)