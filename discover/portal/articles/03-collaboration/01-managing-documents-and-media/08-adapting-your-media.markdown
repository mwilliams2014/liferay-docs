Adaptive Media overview

Adaptive Media is a set of OSGi modules that are responsible of
providing media (images, video, audio…) in the best format attending to
the channel and the device that will consume it.

In the current times there are more and more devices that need to access
to information. Those devices have different characteristics like screen
size, bandwidth, processing capabilities, etc. Up until now, media has
been delivered to those devices without considering these
characteristics, so the delivery wasn’t optimized at all.

Adaptive Media applies several improvements to achieve better
performance and optimize the delivery to those devices. We have started
with optimizing the images delivery to the users.

Deploying Adaptive Media {#h.wfhyz7nr1j7u .c1 .c12}
========================

Development of Adaptive Media modules is done in the master branch of an
independent repository of Liferay in the following url:
[https://github.com/liferay/com-liferay-adaptive-media](https://www.google.com/url?q=https://github.com/liferay/com-liferay-adaptive-media&sa=D&ust=1482880889869000&usg=AFQjCNHzWR-uBL7ROpGnHzG2agFqs89miQ)

Periodically this repository will be synchronized with the official
liferay-portal repository that will contain the latest stable version of
Adaptive Media in the following url:
[https://github.com/liferay/liferay-portal/tree/master/modules/apps/adaptive-media](https://www.google.com/url?q=https://github.com/liferay/liferay-portal/tree/master/modules/apps/adaptive-media&sa=D&ust=1482880889870000&usg=AFQjCNHfNqLv5_JXqMiTwLavewkaSg53VQ)

If you want to test the latest features of Adaptive Media, even if they
are not “official” yet, you should deploy the independent
com-liferay-adaptive-media repository. This will contain all the latest
features, but it might contain some unfinished or not stable features.

If you want to test the latest official Adaptive Media, you should use
the one in the liferay-portal repository. Keep in mind that this
repository will be sync from time to time, so latest features might not
be available in this repository.

In order to test the full Adaptive Media features, please make sure that
you deploy all the modules contained in Adaptive Media, except the
testing ones (the ones with the \`-test\` suffix).

Adaptive Media API {#h.wgyn7q2i5lxu .c1 .c12}
==================

Media content (images, audio, video…) is represented with the interface
com.liferay.adaptive.media.AdaptiveMedia. This interface exposes the
following data about a media:

-   A set of attributes (content length, content type, dimensions,
    etc.).
-   The raw content of the media.
-   A URI that uniquely identifies the media.

The actual set of attributes allowed for a media depend on its type --
for example, images have different characteristics than videos or audio
files -- and the API tries to enforce this statically. All attributes
are implementations of the AdaptiveMediaAttribute, so users can extend
the set of supported attributes simply creating a new object. The only
requirement is that the value of an attribute must be serializable into
a String (i.e. it must provide a way to convert the value to and from
String).

The contents of a media are exposed via an InputStream. Once requested
with the getInputStream() method, consumers are responsible of closing
it when finished.

Finally, the getURI() method returns a URI that uniquely identifies the
media in a particular portal instance. This URI should be treated as an
opaque value, as the particular format may change; the only guarantee is
that the URI for a media will be constant as long as that media exists.

Adaptive Media Images {#h.4pfzvu6mta1t .c1 .c12}
=====================

Adaptive media optimizes the image delivery according to the size of the
image and the screen device. Portal Administrators define a set of image
sizes that will be send to the devices that best matches the screen
size. For every image uploaded to a Documents and Media repository that
supports Adaptive Media, a new image will be created based on the
maximum width and maximum height provided by the portal administrator.

So, let’s think that the Portal Administrator has the following
configuration for the image variants:

-   Very High Resolution  max-width: 3000px; max-height: 2400px;
-   High Resolution  max-width: 2400px; max-height: 1800px;
-   Medium Resolution  max-width: 1200px; max-height: 800px;
-   Low Resolution  max-width: 400px; max-height: 200px;

With this configuration, for every image that is added to any Documents
and Media repository that supports Adaptive Media (such as Documents and
Media repository, Blogs repository, etc) 4 new images will be created
with the max-width and max-height specified in the configuration. Note
that the actual width/height of the generated images may or may not be
the maximum specified; the only guarantee is that images will be scaled
so that the width/height values are less than or equal to the ones
requested.

The API clients of Adaptive Media can choose which of the above image
variants is more appropriate for the device that will access consume the
media. If the image is going to be displayed in a smart watch, Low
Resolution image will be probably the choosen one. If it’s going to be
displayed in a desktop with a large screen, Very High Resolution will be
choosen.

The election of the appropriate image can be done in lot’s of different
ways:

-   Choosing the image whose width is smaller than the screen width.
-   Choosing the image whose height is smaller than the screen height.
-   Choosing the image whose width and height are smaller than the
    screen width and height.
-   Choosing the image whose width and height are more similar to the
    screen size.
-   Choosing the image whose width and height are slightly smaller than
    the screen size

As you can see, there are many ways of choosing the “best” image for the
different devices. The consumer of the Adaptive Media API will decide
which image will be best for the particular use case.

Liferay portlets like Blogs will display images whose width is the
closest to the screen width but not larger. We don’t consider the height
of the image in order to choose the “optimal” image. Let’s see some
examples according to the configuration below:

-   If the screen width of the device is 700px, a Low Resolution image
    will be chosen.
-   If the screen width of the device is 1800px, a High Resolution image
    will be chosen.
-   If the screen width of the device is 3500px, the RAW image will be
    chosen (since \`there is no image variant that can be displayed.

However, this is just a particular use case of the Adaptive Media API in
the Blogs portlet in Liferay. Any other application or client can have a
different policy and choose the optimal according to other policies.

Configuring Adaptive Media Images {#h.jfhe2di7pr77 .c1 .c12}
---------------------------------

This section will change in the future because we will change the
configuration to have a better UX and improve the parameters and the way
the parameters are included as part of
[https://issues.liferay.com/browse/LPS-68587](https://www.google.com/url?q=https://issues.liferay.com/browse/LPS-68587&sa=D&ust=1482880889887000&usg=AFQjCNHU8WvUnm_CTegd3z4jVzJHQ8zaYw)

In order to enable Adaptive Images we need to add some configuration to
define the parameters to create the image variants. That configuration
is accessible from Control Panel -\> System Settings -\> Other -\>
adaptive.media.image.configuration.name.

![](images/image00.png)

There is a repeatable Image Variants field that accepts the parameters
of one image variant. If the portal admin needs to create several image
variants for each of the images uploaded, he can as many Image Variants
fields as needed.

The Image Variant fields needs to follow the following convention:

name:uuid:attribute=value;attribute=value

The name is an arbitrary human readable name. The framework does not use
it in any way, so its purpose is to help users identify the image
variant. The UUID must be a unique identifier for that particular
variant; there must be no two variants with the same UUID. Finally, the
set of attribute/value pairs must be consistent with the attributes
defined in the subtypes of AdaptiveMediaAttribute; for example, in the
case of adaptive images the valid attributes are “widht” and “height”.

This is an example of a valid configuration that will create images with
a maximum height of 100px and a maximum width of 200px:

lowresolution:12345:height=100;width=200

Adaptive Media Image Attributes {#h.lkjxewowpv4h .c1 .c12}
-------------------------------

The information of the adaptive images can be obtained using Adaptive
Media Attributes. There are a few generic Adaptive Media attributes that
are common to every “media” and there are a few specific attribute for
images:

com.liferay.adaptive.media.AdaptiveMediaAttribute contains the
attributes common to every media. They are:

-   Content Length
-   Content Type
-   File Name

com.liferay.adaptive.media.image.processor.ImageAdaptiveMediaAttribute
contains the attributes specific to images:

-   Height
-   Width

This attributes can be used to obtain the value of a particular media as
follows:

adaptiveMedia.getAttributeValue(

  ImageAdaptiveMediaAttribute.IMAGE\_HEIGHT)

This method will return an Option that will indicate the height of the
image if present, or an empty optional if not. The return type of each
attribute is encoded in its type, so there’s no need to cast it to the
correct type. In this particular example, the return type will be
Option\<Integer\>.

Obtaining and searching Adaptive Media Images {#h.8waecosadzac .c1 .c12}
---------------------------------------------

The Adaptive Media API provides a way of searching and obtaining
Adaptive Media by providing a query with the parameters that should be
used to perform the search. The interface
com.liferay.adaptive.media.finder.AdaptiveMediaFinder is the responsible
of this search.

If the search is specific to Adaptive Images, we can use the interface
com.liferay.adaptive.media.image.finder.ImageAdaptiveMediaFinder to
obtain a list of adaptive images.

This interface is implemented by the class
com.liferay.adaptive.media.image.internal.finder.ImageAdaptiveMediaFinderImpl and
is registered as an OSGi component using the interface
ImageAdaptiveMediaFinder and the property
"model.class.name=com.liferay.portal.kernel.repository.model.FileVersion".
This property specifies the kind of model the finder expects. All
current implementations expect FileVersion models.

We can obtain the OSGI component ImageAdaptiveMediaFinder using
Declarative Services:

@Reference(

  target =
"(model.class.name=com.liferay.portal.kernel.repository.model.FileVersion)"

)

private ImageAdaptiveMediaFinder \_finder;

The finder accepts a function that when invoked will receive a
queryBuilder to specify the criteria that will be used to perform the
search. The methods accepted by this image queryBuilder can be seen in
the interface
com.liferay.adaptive.media.image.finder.ImageAdaptiveMediaQueryBuilder and
it allows to do powerful queries as follows:

Stream\<AdaptiveMedia\<ImageAdaptiveMediaProcessor\>\> stream =

  \_finder.getAdaptiveMedia(

     queryBuilder -\>

        queryBuilder.allForFileEntry(fileEntry).

        orderBy(ImageAdaptiveMediaAttribute.IMAGE\_WIDTH, true).

        done());

This will return a stream of the image variants created for the image
fileEntry ordered by the width ascendently.

Stream\<AdaptiveMedia\<ImageAdaptiveMediaProcessor\>\> stream =

  \_finder.getAdaptiveMedia(

     queryBuilder -\>

        queryBuilder.

           forVersion(\_fileVersion).

           with(ImageAdaptiveMediaAttribute.IMAGE\_HEIGHT, 100).

           done());

This will return a stream of the image variants created for the image
version fileVersion whose width is closer to 100px. This is using a
fuzzy query so the results are ordered based on the proximity of the
height to 100px, the closer they are to 100px the first they will
appear.

You can see the list of all supported methods of the queryBuilder in the
class
com.liferay.adaptive.media.image.finder.ImageAdaptiveMediaQueryBuilder.

Obtaining URLs and information for image variants {#h.j11iruiu72a .c1 .c12}
-------------------------------------------------

Usually after getting a stream of AdaptiveMedia you will want to do
something with them. One of the most common actions is to obtain the URL
so you can create \<img\> tags or use the link to display the image.
Additionally you might want to get additional information on the media
using the Adaptive Media Attributes.

If you want to know the exact height of an adaptive media image you can
do this:

Optional\<Integer\> widthOptional = adaptiveMedia.getAttributeValue(

  ImageAdaptiveMediaAttribute.IMAGE\_WIDTH);

If you want to obtain the URI for a particular adaptive media image you
can do this:

adaptiveMedia.getURI().
