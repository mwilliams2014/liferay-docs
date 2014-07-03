# Configuring the Resources Importer

A theme without content is like an empty house. If you're trying to sell an
empty house, it may be difficult for prospective buyers to see its full beauty.
However, staging the house with some furniture and decorations helps prospective
buyers imagine what the house might look like with their belongings. Liferay's
resources importer application is a tool that allows a theme developer to have
files and web content automatically imported into the portal when a theme is
deployed. Usually, the resources are imported into a site template but they can
also be imported directly into a site. Portal administrators can use the site or
site template created by the resources importer to showcase the theme. This is a
great way for theme developers to provide a sample context that optimizes the
design of their theme. In fact, all standalone themes that are uploaded to
Liferay Marketplace must use the resources importer. This ensures a uniform
experience for Marketplace users: a user can download a theme from Marketplace,
install it on their portal, go to Sites or Site Templates in the Control Panel
and immediately see their new theme in action.

---

![Note](../../images/tip-pen-paper.png) **Note:** The resources importer can be
used in any type of plugin project to import resources. Importing resources
within a theme plugin is just one of the more common use cases.

---

In this tutorial, you'll learn how to configure the resources importer for your 
theme. Time to get started.

## Liferay's Welcome Theme

Liferay's welcome theme includes resources that the resources importer
automatically deploys to the default site. (Note: The welcome theme is only
applied out-of-the-box in Liferay CE.) The welcome theme and the pages and
content that it imports to the default site provide a good example of the
resources importer's functionality.

![Figure 1: The welcome theme uses the resources importer to import pages and content to the default site of a fresh Liferay installation.](../../images/welcome-theme.png)

If it's not already installed, you can download the resources importer
application from Liferay Marketplace. Search for either *Resources Importer CE*
or *Resources Importer EE*, depending on your Liferay Portal platform, and
download the latest version. Install and deploy the resources importer to your
Liferay instance the same way you would deploy any other Liferay plugin or
Marketplace app.

---

![Tip](../../images/tip-pen-paper.png) **Tip:** If you deploy a theme to your
Liferay Portal instance and don't have the resources importer already deployed,
you might see a message like this:
 
    19:21:12,224 INFO  [pool-2-thread-2][HotDeployImpl:233] Queuing test-theme for deploy because it is missing resources-importer-web

Such a message appears if the resources importer is declared as a dependency in
your theme's `liferay-plugin-package.properties` file but is not deployed. You
can deploy the resources importer application to satisfy the dependency or you
can remove or comment out the dependency declaration if you're not going to use
the resources importer with your theme (see below).

---

Take a look at the properties files for the resources importer next.

## The Resources Importer in liferay-plugin-package.properties

When you create a new theme project using the Liferay Plugins SDK, check your
theme's `docroot/WEB-INF/liferay-plugin-package.properties` file for two entries
related the resources importer. One or both of these might be commented out or
missing, depending on the version of your Plugins SDK:

    required-deployment-contexts=\
        resources-importer-web

    resources-importer-developer-mode-enabled=true

The first entry, `required-deployment-contexts=resources-importer-web`, declares
your theme's dependency on the resources importer plugin. If you're not going to
use the resources importer with your theme and don't want to deploy the
resources importer, you can remove or comment out this entry. The second entry,
`resources-importer-developer-mode-enabled=true`, is a convenience feature for
theme developers. With this setting enabled, if the resources are to be imported
to a site template that already exists, the site template is recreated and
reapplied to sites using the site template. Otherwise, you have to manually
delete the sites built using the resource importer's site template each time you
change anything in your theme's `docroot/WEB-INF/src/resources-importer` folder.

If you'd like to import your theme's resources directly into a site, instead of
into a site template, you can specify the following in your
`liferay-plugin-package.properties` file:

    resources-importer-target-class-name=com.liferay.portal.model.Group

    resources-importer-target-value=<site-name>

---

![warning](../../images/tip-pen-paper.png) **Warning:** If you're developing
themes for Liferay Marketplace, don't configure your theme to import resources
directly into a site. Instead, use the default: import the resources into a
site template. Do this by commenting out the
`resources-importer-target-class-name` property. This way, it'll be much safer
to deploy your theme to a production Liferay instance.

---

All of the resources a theme uses with the resources importer go in the
`<theme-name>/docroot/WEB-INF/src/resources-importer` folder. The assets to be
imported by your theme should be placed in the following directory structure:

- `<theme-name>/docroot/WEB-INF/src/resources-importer/`
    - `sitemap.json` - defines the pages, layout templates, and portlets
    - `assets.json` - (optional) specifies details on the assets
    - `document_library/`
        - `documents/` - contains documents and media files
    - `journal/`
        - `articles/` - contains web content (HTML) and folders grouping web
          content articles (XML) by template. Each folder name must match the
          file name of the corresponding template. For example, create folder
          `Template 1/` to hold an article based on template file
          `Template 1.vm`. 
        - `structures/` - contains structures (XML) and folders of child
          structures. Each folder name must match the file name of the
          corresponding parent structure. For example, create folder
          `Structure 1/` to hold a child of structure file `Structure 1.xml`.
        - `templates/` - groups templates (VM or FTL) into folders by structure.
          Each folder name must match the file name of the corresponding
          structure. For example, create folder `Structure 1/` to hold a
          template for structure file `Structure 1.xml`.

When you create a new theme using the Liferay Plugins SDK
(liferay-plugins-sdk-6.1.1-ce-ga2-20121004092655026 or later), this folder
structure is created automatically. Also, a default `sitemap.json` file is
created and a default `liferay-plugin-package.properties` file is created in the 
`WEB-INF` folder.

Hopefully now you have a better understanding of how to configure the resources
importer and where to place the resources to be imported.

## Next Steps
<!-- URL will probably need updated when added to the new devsite-->
 [Specifying Resources to Be Imported in Your Theme](/tutorials/-/knowledge_base/specifying-resources)

 [Choosing the Resources For Your Theme](/tutorials/-/knowledge_base/choosing-resources)
