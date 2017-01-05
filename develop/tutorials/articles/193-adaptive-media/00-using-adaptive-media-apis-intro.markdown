# Configuring Adaptive Media APIs

The Adaptive Media APIs give you lots of control over your media. You can create
unique attributes for your media types, search and obtain information about
adaptive media such as the width and height of an image, and obtain URLs for an 
adaptive media. This tutorial demonstrates these features.

<!-- Are there any other things you can do with the API that we should mention? -->

First, you must deploy the proper Adaptive Media OSGi modules. This is covered
next.

## Deploying Adaptive Media

Development of Adaptive Media modules is done in the master branch of an
independent repository of Liferay at the following URL:
[https://github.com/liferay/com-liferay-adaptive-media](https://www.google.com/url?q=https://github.com/liferay/com-liferay-adaptive-media&sa=D&ust=1482880889869000&usg=AFQjCNHzWR-uBL7ROpGnHzG2agFqs89miQ)

<!-- Shouldn't we just point to the Liferay-portal repo and consider that what
is in Portal is always stable? I know at the moment the repo is up to date in 
Portal. -->

<!--
Periodically this repository will be synchronized with the official
liferay-portal repository that will contain the latest stable version of
Adaptive Media in the following url:
[https://github.com/liferay/liferay-portal/tree/master/modules/apps/adaptive-media](https://www.google.com/url?q=https://github.com/liferay/liferay-portal/tree/master/modules/apps/adaptive-media&sa=D&ust=1482880889870000&usg=AFQjCNHfNqLv5_JXqMiTwLavewkaSg53VQ)

If you want to test the latest features of Adaptive Media, even if they
are not “official” yet, you should deploy the independent
`com-liferay-adaptive-media repository`. This contains all the latest features, 
but it might contain some unfinished or not stable features.

If you want to test the latest official Adaptive Media, you should use
the one in the liferay-portal repository. Keep in mind that this
repository will be synced from time to time, so latest features might not
be available in this repository. -->

To test the Adaptive Media features, you must deploy all the modules contained 
in the Adaptive Media repo, except the testing ones 
(the ones with the `-test` suffix).

<!-- Note: there is currently an issue with the `adaptive-media-image-jax-rs` 
module that causes images to break. -->

Once the Adaptive Media modules are deployed, you can interact with the APIs.

## Adaptive Media API

Media content (images, audio, video…) is represented with the 
[`com.liferay.adaptive.media.AdaptiveMedia` interface](https://github.com/liferay/com-liferay-adaptive-media/blob/6356d57a76050764d52a8dccce07578cc53b72b1/adaptive-media-api/src/main/java/com/liferay/adaptive/media/AdaptiveMedia.java). 
This interface exposes the following data about a media:

-   A set of attributes (content length, content type, dimensions,
    etc.).
-   The raw content of the media.
-   A URI that uniquely identifies the media.

The set of attributes allowed for a media depend on its type. For example, 
images have different characteristics from videos or audio files, and the API 
tries to enforce this statically.

<!-- How do you create a media type? Should we mention that in this tutorial, or 
just focus this one on Adpative Media Images? -->

All attributes are implementations of the `AdaptiveMediaAttribute`, so to extend 
the set of supported attributes you just need to create a new 
[`AdaptiveMediaAttribute` object](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-api/src/main/java/com/liferay/adaptive/media/AdaptiveMediaAttribute.java). 
The only requirement is that the value of an attribute must be serializable into 
a String (i.e. it must provide a way to convert the value to and from a String).

The example configuration below shows how the 
[`ImageAdaptiveMediaAttribute` class](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/processor/ImageAdaptiveMediaAttribute.java) 
creates the `height` and `width` attributes for Adaptive Media images:

    public final class ImageAdaptiveMediaAttribute {
    
            public static final AdaptiveMediaAttribute
                    <ImageAdaptiveMediaProcessor, Integer> IMAGE_HEIGHT =
                            new AdaptiveMediaAttribute<>(
                                    "height", 
                                    AdaptiveMediaAttributeConverterUtil::parseInt,
                                    ImageAdaptiveMediaAttribute::_intDistance);
    
            public static final AdaptiveMediaAttribute
                    <ImageAdaptiveMediaProcessor, Integer> IMAGE_WIDTH =
                            new AdaptiveMediaAttribute<>(
                                    "width", 
                                    AdaptiveMediaAttributeConverterUtil::parseInt,
                                    ImageAdaptiveMediaAttribute::_intDistance);
    
            /**
             * Returns a string-attribute map containing the available
             * name-attribute pairs.
             *
             * @return the list of available attributes
             */
            public static Map<String, AdaptiveMediaAttribute<?, ?>>
                    allowedAttributes() {
    
                    return _allowedAttributes;
            }
    
            private static int _intDistance(int i1, int i2) {
                    return i1 - i2;
            }
    
            private ImageAdaptiveMediaAttribute() {
            }
    
            private static final Map<String, AdaptiveMediaAttribute<?, ?>>
                    _allowedAttributes = new HashMap<>();
    
            static {
                    _allowedAttributes.put(
                            ImageAdaptiveMediaAttribute.IMAGE_WIDTH.getName(),
                            ImageAdaptiveMediaAttribute.IMAGE_WIDTH);
                    _allowedAttributes.put(
                            ImageAdaptiveMediaAttribute.IMAGE_HEIGHT.getName(),
                            ImageAdaptiveMediaAttribute.IMAGE_HEIGHT);
            }
    
    }

First, the attributes are individually added to a new `AdaptiveMediaAttribute`
object. The `AdaptiveMediaAttribute` class takes a processor type and
attribute type as parameters. The example above uses 
`ImageAdaptiveMediaProcessor` and an `Integer` for the attribute type. A new 
instance of the `AdaptiveMediaAttribute` is then created passing the 
`String name`, `Function<String, V> converter`, and a `Comparator<V> comparator` 
arguments. For example, the `height` attribute above uses `height` as the name, 
`AdaptiveMediaAttributeConverterUtil::parseInt` as the converter function, and 
`ImageAdaptiveMediaAttribute::_intDistance` as the comparator.
Finally, the new `height` and `width` attributes for the 
`ImageAdaptiveMediaAttribute` are added to the `_allowedAttributes` HashMap.

The contents of a media are exposed via an InputStream. Once requested
with the [`getInputStream()` method](https://github.com/liferay/com-liferay-adaptive-media/blob/6356d57a76050764d52a8dccce07578cc53b72b1/adaptive-media-api/src/main/java/com/liferay/adaptive/media/AdaptiveMedia.java#L45-L52), 
consumers are responsible for closing it when finished.

<!-- Is there an example of using and closing an InputStream? Is this 
ImageProcessor class an example of one? [https://github.com/liferay/com-liferay-adaptive-media/blob/5220d012bc037a1736746eddc9a0ab9aa90ea8d4/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/internal/util/ImageProcessor.java](https://github.com/liferay/com-liferay-adaptive-media/blob/5220d012bc037a1736746eddc9a0ab9aa90ea8d4/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/internal/util/ImageProcessor.java)-->

Finally, the [`getURI()` method](https://github.com/liferay/com-liferay-adaptive-media/blob/6356d57a76050764d52a8dccce07578cc53b72b1/adaptive-media-api/src/main/java/com/liferay/adaptive/media/AdaptiveMedia.java#L54-L61) 
returns a URI that uniquely identifies the media in a particular portal instance. 
This URI should be treated as an opaque value, as the particular format may 
change; the only guarantee is that the URI for a media will be constant as long 
as that media exists.

### Adaptive Media Image Attributes

The adaptive image information can be obtained using Adaptive Media Attributes. 
There are a few generic Adaptive Media attributes that are common to every media 
type and there are a few specific attributes for images:

The [`com.liferay.adaptive.media.AdaptiveMediaAttribute` class](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-api/src/main/java/com/liferay/adaptive/media/AdaptiveMediaAttribute.java) 
contains the attributes common to every media, shown below:

-   Content Length
-   Content Type
-   File Name

The [`com.liferay.adaptive.media.image.processor.ImageAdaptiveMediaAttribute` class](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/processor/ImageAdaptiveMediaAttribute.java) 
contains these attributes specific to images:

-   Height
-   Width

These attributes can be used to obtain the value of a particular media as
follows:

    adaptiveMedia.getAttributeValue(ImageAdaptiveMediaAttribute.IMAGE_HEIGHT)

This method returns an Option that indicates the height of the image if present, 
or an empty optional if not. The return type of each attribute is encoded in its 
type, so there’s no need to cast it to the correct type. In this particular 
example, the return type is `Option<Integer>`.

### Obtaining and searching Adaptive Media Images

The Adaptive Media API provides a way to search and obtain Adaptive Media by 
providing a query with the search parameters. The
[`com.liferay.adaptive.media.finder.AdaptiveMediaFinder` interface](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-api/src/main/java/com/liferay/adaptive/media/finder/AdaptiveMediaFinder.java) 
provides the APIs needed for this search.

If the search is specific to Adaptive Images, you can use the 
[`com.liferay.adaptive.media.image.finder.ImageAdaptiveMediaFinder` interface](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/finder/ImageAdaptiveMediaFinder.java) 
to obtain a list of adaptive images.

This interface is implemented by the 
[`com.liferay.adaptive.media.image.internal.finder.ImageAdaptiveMediaFinderImpl` class](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/internal/finder/ImageAdaptiveMediaFinderImpl.java) 
and is registered as an OSGi component using the 
[`ImageAdaptiveMediaFinder` interface](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/finder/ImageAdaptiveMediaFinder.java) 
and the property 
`model.class.name=com.liferay.portal.kernel.repository.model.FileVersion`. This 
property specifies the kind of model the finder expects. All current 
implementations expect `FileVersion` models.

You can obtain the `ImageAdaptiveMediaFinder` OSGI component using Declarative 
Services:

    @Reference(
    
      target =
    "(model.class.name=com.liferay.portal.kernel.repository.model.FileVersion)"
    
    )
    
    private ImageAdaptiveMediaFinder _finder;

The finder accepts a function that, when invoked, receives a `queryBuilder` that
specifies the criteria for the search. The methods accepted by the image 
`queryBuilder` can be seen in the 
[`com.liferay.adaptive.media.image.finder.ImageAdaptiveMediaQueryBuilder` interface](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/finder/ImageAdaptiveMediaQueryBuilder.java). 
The `queryBuilder` allows you to run powerful queries, as shown below:

    Stream<AdaptiveMedia<ImageAdaptiveMediaProcessor>> stream =
    
      _finder.getAdaptiveMedia(
    
         queryBuilder ->
    
            queryBuilder.allForFileEntry(fileEntry).
    
            orderBy(ImageAdaptiveMediaAttribute.IMAGE_WIDTH, true).
    
            done());

This returns a stream of the image variants created for the image fileEntry 
in ascending order by width.

<!-- Is this using an InputStream to get the content or is this completely 
different? -->

    Stream<AdaptiveMedia<ImageAdaptiveMediaProcessor>> stream =
    
      _finder.getAdaptiveMedia(
    
         queryBuilder ->
    
            queryBuilder.
    
               forVersion(_fileVersion).
    
               with(ImageAdaptiveMediaAttribute.IMAGE_HEIGHT, 100).
    
               done());

This returns a stream of the image variants created for the image version 
`fileVersion` whose height is closer to 100px. This is using a fuzzy query, so 
the results are ordered based on the proximity of the height to 100px. The 
closer the results are to 100px, the sooner they will appear in the order of 
results.

You can see the list of all supported queryBuilder methods in the 
[`com.liferay.adaptive.media.image.finder.ImageAdaptiveMediaQueryBuilder` class](https://github.com/liferay/com-liferay-adaptive-media/blob/master/adaptive-media-image-impl/src/main/java/com/liferay/adaptive/media/image/finder/ImageAdaptiveMediaQueryBuilder.java).

### Obtaining URLs and information for image variants

Usually after getting a stream of AdaptiveMedia you will want to do
something with them. One of the most common actions is to obtain the URL
so you can create `<img>` tags or use the link to display the image. 
You may also want additional information on the media using the Adaptive Media 
Attributes.

If you want to know the exact height of an adaptive media image you can
do this:

    Optional<Integer> widthOptional = adaptiveMedia.getAttributeValue(
    
      ImageAdaptiveMediaAttribute.IMAGE_WIDTH);

If you want to obtain the URI for a particular adaptive media image you
can do this:

    adaptiveMedia.getURI()
    
Now you know how to use the Adaptive Media APIs!

## Related Topics

<!-- Related topic links to go here -->

