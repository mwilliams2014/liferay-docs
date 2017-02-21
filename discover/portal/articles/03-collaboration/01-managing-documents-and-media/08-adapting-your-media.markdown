# Adapting Your Media

Desktops, tablets, phones, smart watches and more; The world is full of devices.
Adapting your sites media (images, video, audio, etc.) to a device's unique
specifications, like screen size, bandwidth, processing capabilities, etc. can
be tricky. Thankfully, @product@ provides a set of Adaptive Media OSGi modules
that makes this process much easier.

Adaptive Media applies several improvements to achieve better performance and
optimize your media's delivery to a device. So far, these optimizations are
only available for images, but more media types will be supported in the future.

<!-- At the time of this writing this is only implemented for blog images. -->

## Adaptive Media Images

Adaptive media optimizes the image delivery according to the size of the image
and the screen device. Portal Administrators define a set of image sizes that
are sent to the devices that best matches the screen size.

For example, consider the following resolutions:

-   Very High Resolution  max-width: 3000px; max-height: 2400px;
-   High Resolution  max-width: 2400px; max-height: 1800px;
-   Medium Resolution  max-width: 1200px; max-height: 800px;
-   Low Resolution  max-width: 400px; max-height: 200px;

With this configuration, for every image that is added to a Documents and Media
repository that supports Adaptive Media (such as Documents and Media repository,
Blogs repository, etc), four new image variants will be created with the
max-width and max-height specified. Note that generated images are scaled to
best fit the desired resolutions while still maintaining their original aspect
ratio, so the generated images may not match the exact width/height specified.

The Adaptive Media's API determines which image variant is best to use for the
given device's specifications. For example, if a smart watch is viewing the
image, given the configuration above, the Low Resolution image is displayed. If
the image is viewed on a desktop with a large screen instead, the Very High
Resolution image is displayed.

The Blogs portlet displays images whose width is the closest to the screen width
but not larger. The image's height isn't considered when choosing the optimal
image. Consider the use cases below:

-   If the screen width of the device is 700px, a Low Resolution image is
    displayed.
-   If the screen width of the device is 1800px, a High Resolution image is
    displayed.
-   If the screen width of the device is 3500px, the RAW image is displayed
    (since there is no image variant that can be displayed with the example
    configuration).

You can see the image variants when you view the source code for the image in a
Blog entry.

<!--![Figure 1: The adaptive resolutions can be seen in the image source code while editing a Blog Entry.](../../../images/adaptive-media-blog-entry)-->

However, this is just a particular use case of the Adaptive Media API in the
Blogs portlet in @product@. Another application or client can have a different
policy and choose the optimal settings according to other policies.

## Configuring Adaptive Media Images

<!--This section will change in the future because we will change the
configuration to have a better UX and improve the parameters and the way
the parameters are included as part of
[https://issues.liferay.com/browse/LPS-68587](https://www.google.com/url?q=https://issues.liferay.com/browse/LPS-68587&sa=D&ust=1482880889887000&usg=AFQjCNHU8WvUnm_CTegd3z4jVzJHQ8zaYw)
-->

You can configure Adaptive Media settings at the Portal level and at the
instance level. Both of these configurations are covered in this section.

### Configuring Adaptive Media Settings for the Portal

To set the default configuration across the Portal, open the Control Panel and
goto *Configuration* &rarr; *System Settings* and select the
*Adaptive Media Images* configuration under the *Other* tab. The Image Variant
fields must follow the convention below:

    name:uuid:attribute=value;attribute=value

**Name:** An arbitrary name used for identification purposes only (the framework
does not use it in any way).

**UUID:** A unique identifier for this particular image variant. Note that no
two variants can share the same UUID.

**Attribute/Value pair:** A set of attribute/value pairs that must be consistent
with the attributes defined in the subtypes of the
[`AdaptiveMediaAttribute` class](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-api/src/main/java/com/liferay/adaptive/media/AdaptiveMediaAttribute.java).
For example, in the case of adaptive images the valid attributes are `width` and
`height`. You can learn more about the Adaptive Media APIs in the
[Using Adaptive Media APIs]() tutorial.

Below is an example configuration that creates images with a maximum height of
100px and a maximum width of 200 px:

    lowresolution:12345:height=100;width=200

![Figure 1: Click the plus sign to add a new image variant.](../../../images/adaptive-media-image-variant-form.png)

Click the plus sign to add a new image variant. Fill out the form with the image
variant information and click *Save*.

These image variants are now the default values across the Portal. You can
override these default values by configuring the Adaptive Media settings at the
instance level.

### Configuring Adaptive Media Settings for an Instance

To configure Adaptive Media for the current instance, open the Control Panel and
navigate to *Configuration* &rarr; *Adaptive Media*.

At the moment, you don't have any image resolutions available, so you must add
one. Click the plus button at the bottom right corner to add an image resolution.

The New Image Resolution form presents you with the following fields:

**Name:** An arbitrary name used for identification purposes only (the framework
does not use it in any way).

**UUID:** A unique identifier for this particular image variant. Note that no
two variants can share the same UUID.

**Max. Width (px):** The maximum width for the image resolution.

**Max. Height (px):** The maximum height for the image resolution.

Fill out the form with the resolution information and click *Save*.

![Figure 2:](../../../images/adaptive-media-image-resolution-form.png)

Repeat these steps for each resolution that you need. Once you've added some
resolutions, they're displayed under the *Image Resolutions* tab.

![Figure 3:](../../../images/adaptive-media-image-resolutions.png)

Clicking on the Actions menu next to a resolution gives you the option to *Edit*
or *Delete* the resolution. You can also delete multiple resolutions at once.
Click the checkbox next to each resolution that you want to delete, or click the
*All* checkbox at the upper left corner to select all resolutions, and click the
X button that appears in the upper right corner to delete them.
