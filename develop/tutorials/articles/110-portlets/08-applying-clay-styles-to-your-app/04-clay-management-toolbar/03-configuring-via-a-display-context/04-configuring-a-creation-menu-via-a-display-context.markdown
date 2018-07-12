# Configuring the Creation Menu via a Display Context

1.  If you've 
    [extended a base class](sdsds), 
    override the `getCreationMenu()` method, and create a new instance of 
    [`CreationMenu()`](sdsd), as shown in the example below:
    
        @Override
        public CreationMenu getCreationMenu() {
          CreationMenu creationMenu = new CreationMenu();

        }

Follow the steps below to configure the Creation Menu:

1.  If you've 
    [extended a base `*ManagementToolbarDisplayContext` class](sds), 
    override the `getCreationMenu()` method, and create a new instance of 
    [`CreationMenu`](sdsd), as shown in the example below:
    
        @Override
        public CreationMenu getCreationMenu() {
          CreationMenu creationMenu = new CreationMenu();

        }

2.  Still inside the `getCreationMenu()` method, use one of the CreationMenu 
    methods below to add a dropdown item:

    - `addDropdownItem()`:
    - `addFavoriteDropdownItem()`:
    - `addPrimaryDropdownItem()`:
    - `addRestDropdownItem()`:

    The example below adds a primary dropdown item:

        @Override
        public CreationMenu getCreationMenu() {

          CreationMenu creationMenu = new CreationMenu();

          creationMenu.addPrimaryDropdownItem(
          );
            
        }

3.  Safely consume the dropdown item:

        @Override
        public CreationMenu getCreationMenu() {
          CreationMenu creationMenu = new CreationMenu();

          creationMenu.addPrimaryDropdownItem(
            SafeConsumer.ignore(
              dropdownItem -> {

              }));
        }

4.  Configure the dropdown item's attributes. The example below sets the URL and 
    label:

        @Override
        public CreationMenu getCreationMenu() {
          CreationMenu creationMenu = new CreationMenu();

          creationMenu.addPrimaryDropdownItem(
            SafeConsumer.ignore(
              dropdownItem -> {
                User selectedUser = PortalUtil.getSelectedUser(request);

                dropdownItem.setHref(
                  liferayPortletResponse.createRenderURL(),
                  "mvcRenderCommandName", "/add_uad_export_processes",
                  "backURL", PortalUtil.getCurrentURL(request), "p_u_i_d",
                  String.valueOf(selectedUser.getUserId()));

                dropdownItem.setLabel(
                  LanguageUtil.get(request, "add-export-processes"));
              }));

        }
        
    The full list of available attributes is shown below:

    DropdownItem Methods

    | Method | Type | Description |
    | --- | --- | --- |
    | setIcon | Boolean | Sets a Clay icon to display for the creation menu item |
    | setQuickAction | Boolean | ? |
    | setSeparator | Boolean | Adds a horizontal line separation after the item |

    Inherited Methods from NavigationItem

    | Method | | Type | Description |
    | --- | --- | --- |
    | setActive | Boolean | Sets whether the item is selected |
    | putData | String | Adds a hashmap of key, value pairs |
    | setData | String | Sets value for a given data hashmap |
    | setDisabled | Boolean | Sets whether the item is disabled |
    | setHref | String,  | Adds a horizontal line separation after the item |
    | setLabel | String | Adds a horizontal line separation after the item |

5.  Finally, return the completed creation menu:

        @Override
        public CreationMenu getCreationMenu() {
          CreationMenu creationMenu = new CreationMenu();

          creationMenu.addPrimaryDropdownItem(
            SafeConsumer.ignore(
              dropdownItem -> {
                User selectedUser = PortalUtil.getSelectedUser(request);

                dropdownItem.setHref(
                  liferayPortletResponse.createRenderURL(),
                  "mvcRenderCommandName", "/add_uad_export_processes",
                  "backURL", PortalUtil.getCurrentURL(request), "p_u_i_d",
                  String.valueOf(selectedUser.getUserId()));

                dropdownItem.setLabel(
                  LanguageUtil.get(request, "add-export-processes"));
              }));

          return creationMenu;
        }

The available methods for the creation menu and dropdown items are shown below.

CreationMenu Methods

- addDropdownItem()
- addPrimaryDropdownItem()
- setCaption() 
- setHelpText() -> sets text to display on mouseover hover
- setViewMoreURL()

DropdownItem Methods

| Method | Type | Description |
| --- | --- | --- |
| setIcon | Boolean | Sets a Clay icon to display for the creation menu item |
| setQuickAction | Boolean | ? |
| setSeparator | Boolean | Adds a horizontal line separation after the item |

Inherited Methods from NavigationItem

| Method | | Type | Description |
| --- | --- | --- |
| setActive | Boolean | Sets whether the item is selected |
| putData | String | Adds a hashmap of key, value pairs |
| setData | String | Sets value for a given data hashmap |
| setDisabled | Boolean | Sets whether the item is disabled |
| setHref | String,  | Adds a horizontal line separation after the item |
| setLabel | String | Adds a horizontal line separation after the item |

---------------------------
- setIcon() // Where are the icon options? Clay, FontAwesome, Glyphicons, etc?
- setQuickAction() //Boolean true or false // What does this do?
- setSeparator() //Boolean -> add horizontal line separation after item
  Inherited Methods from NavigationItem
 ------------------------------------------
 - setActive() -> set whether the item is selected
 - putData() -> add data hashmap of key, value pairs
  - setData()v-. Set an existing data hashmap key, va
  - setDisabled()-> disabled Item
  - setHref() -> set the portlet URL for the creationmenu item
  - setLabel() -> Set the creation Menu Item's label

CreationMenu Example with primary item
-------------------
Note: This overrides the getCreationMenu() method inherited by the BaseManagementToolbarDisplayContext


@Override
public CreationMenu getCreationMenu() {
  CreationMenu creationMenu = new CreationMenu();

  creationMenu.addPrimaryDropdownItem(
    SafeConsumer.ignore(
      dropdownItem -> {
        User selectedUser = PortalUtil.getSelectedUser(request);

        dropdownItem.setHref(
          liferayPortletResponse.createRenderURL(),
          "mvcRenderCommandName", "/add_uad_export_processes",
          "backURL", PortalUtil.getCurrentURL(request), "p_u_i_d",
          String.valueOf(selectedUser.getUserId()));

        dropdownItem.setLabel("add-export-processes");
      }));

  return creationMenu;
}

CreationMenu Example with standard Item
------------------------
  public CreationMenu getCreationMenu() {
    CreationMenu creationMenu = new CreationMenu();

    creationMenu.addDropdownItem(
      dropdownItem -> {
        dropdownItem.setHref(
          _liferayPortletResponse.createRenderURL(),
          "mvcRenderCommandName",
          "/adaptive_media/edit_image_configuration_entry",
          "redirect", _currentURLObj.toString());
        dropdownItem.setLabel(
          LanguageUtil.get(_request, "add-image-resolution"));
      });

    return creationMenu;

showCreationMenu() -> Whether to render the Creation Menu button
  }