# Converting Widget Pages to Content Pages

In previous versions, While Content Pages provided many benefits, there were 
still reasons to create Widget Pages, such as custom layouts and customizable 
columns. Many of the beneficial features of Widget Pages have been added to 
Content Pages in 7.3, so in most cases, you'll want to create a Content Page. If 
you're upgrading to Liferay Portal 7.3 from a previous version and migrating 
existing Widget Pages, you can convert them to Content Pages. There are two 
approaches you can take to converting individual Widget Pages: You can preview a 
conversion draft and make any manual changes and then convert the page, or you 
can simply convert the Widget Page to a Content Page right away. If you have 
multiple pages you want to convert at once, you can bulk convert them using the 
available APIs. All these approaches are described here.

## Preview and Convert A Widget Page to A Content Page

1. Open the Product Menu and go to *Site Builder* &rarr; *Pages* under your 
   Site's menu.
   
2. Open the Actions Menu next to Widget Page and select the 
   *Preview and convert to Content Page* option.
   
3. Acknowledge any warnings and make any required adjustments to the conversion 
   draft. You can also add any Fragments you'd like to the draft at this point.
   
4. Click *Publish* to publish the preview draft, or click *Discard Conversion 
   Draft* to reset the Widget Page back to its original state. If there are 
   warnings, a best-effort conversion, as described below, is completed.

### Best Effort Conversions

Some features of a Widget Page aren't supported by Content Pages and therefore 
can't be converted exactly as they are. In these cases, the user is warned of 
any issues and a best-effort conversion is processed. These Widget Page features 
aren't supported and therefore can't be converted:

- **Nested Applications:** Nested Applications are instead placed in the same 
  column of the layout during the conversion. You may need to manually 
  reorganize these applications after the best-effort conversion is complete.

- **Customizable Sections:** If the page is [customizable](customizable), any 
  customizations made by the user are lost during conversion.

- **Custom Page Layouts:** The converter may be able to parse your custom 
  layout. If the layout can be converted, the structure of the layout is 
  conserved and the user is warned and given a chance to review the conversion 
  draft before proceeding. If the layout can't be converted, all widgets are 
  placed in a single row and column and you must manually reorganize them after 
  the page is converted.

**Note:** If you've already confirmed that a custom layout template can be 
converted, You can disable the layout template conversion warning for the layout 
template so you don't keep seeing it each time you convert a Widget Page that 
uses the layout. Open the Product Menu and go to *Control Panel* &rarr; 
*Configuration* &rarr; *System Settings*. Select *Pages* under Content and Data 
and add the layout template ID to the list of "Verified Layout Template IDs" 
under the System Scope.

## Convert A Widget Page Directly to A Content Page

1. Open the Product Menu and go to *Site Builder* &rarr; *Pages* under your 
   Site's menu.
   
2. Check the box for the Widget Page and open the Actions Menu in the Management 
   Toolbar and select the *Convert to Content Page* option.
   
3. Click *OK* in the prompt that appears to complete the conversion.

## Bulk Convert Widget Pages to Content Pages

To bulk convert all the Widget Pages for a Site, use the `BulkLayoutConverter` 
OSGi service.

1. Open the Product Menu and go to *Control Panel* &rarr; *Configuration* 
   &rarr; *Server Administration* &rarr; *Script*.
   
2. Enter this code in the script window, making sure to replace the Group ID 
   with your own:
   
    ```groovy
    import com.liferay.layout.util.BulkLayoutConverter
    import com.liferay.portal.kernel.util.ArrayUtil
    import com.liferay.registry.Registry
    import com.liferay.registry.RegistryUtil
    import org.osgi.framework.ServiceReference
    import org.osgi.framework.BundleContext

    Registry registry = RegistryUtil.getRegistry()

    BundleContext bundleContext = registry._bundleContext

    ServiceReference serviceReference = bundleContext.getServiceReference(BulkLayoutConverter.class.getName())

    BulkLayoutConverter bulkLayoutConverter = bundleContext.getService(serviceReference);

    long groupId = 20118L // Use your groupId

    long[] plids = bulkLayoutConverter.getConvertibleLayoutPlids(groupId)

    out.println("Convertible layouts before conversion:" + ArrayUtil.toStringArray(plids))

    long[] convertedLayoutPlids = bulkLayoutConverter.convertLayouts(groupId)

    out.println("Converted layouts:" + ArrayUtil.toStringArray(convertedLayoutPlids))

    plids = bulkLayoutConverter.getConvertibleLayoutPlids(groupId)

    out.println("Convertible layouts after conversion: " + ArrayUtil.toStringArray(plids))
    ```
    
3. Click *Execute* to run the script.

4. The output should look similar to the snippet below:

    ```bash
    Convertible layouts before conversion:[25, 26, 27]
    Converted layouts:[25, 26, 27]
    Convertible layouts after conversion: []
    ```
