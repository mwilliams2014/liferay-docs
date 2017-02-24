# Adaptive Media Images REST API [](id=adaptive-media-images-rest-api)

The Adaptive Media Images Rest API allows developers to use several of the
features available in the Adaptive Media Liferay App.

This tutorial explains how to use these APIs to retrieve information about
Adaptive Media images. This tutorial is organized by the available endpoints of
the API. Each endpoint is listed with examples, HTTP status codes, and query
parameters (if the endpoint has any).

In order to use these APIs you must deploy all the Adaptive Media modules,
excluding the *-test* ones, found in the [https://github.com/liferay/com-liferay-adaptive-media](https://github.com/liferay/com-liferay-adaptive-media)
repository.

+$$$

**Note:** If you want to auto-generate test data you can use the
[*adaptive-media-demo* module](https://github.com/liferay/com-liferay-adaptive-media/tree/master/adaptive-media-demo).
To use this demo module, you'll need to deploy the following additional modules:
[*users-admin-demo-data-creator-api* module](https://github.com/liferay/liferay-portal/tree/master/modules/apps/foundation/users-admin/users-admin-demo-data-creator-api),
*users-admin-demo-data-creator-impl* module](https://github.com/liferay/liferay-portal/tree/master/modules/apps/foundation/users-admin/users-admin-demo-data-creator-impl), [*document-library-demo-data-creator-api* module](https://github.com/liferay/liferay-portal/tree/master/modules/apps/collaboration/document-library/document-library-demo-data-creator-api), and [*document-library-demo-data-creator-impl* module](https://github.com/liferay/liferay-portal/tree/master/modules/apps/collaboration/document-library/document-library-demo-data-creator-impl).

$$$

Go ahead and get started.

## API Endpoints [](id=api-endpoints)

Once the Adaptive Media app is running, you can consume the data using the
endpoints listed in the
[API Endpoints](#api-endpoints) section using curl, a browser based client, such as
Google Chrome's [Advance Rest Client](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo)
or Firefox's [REST Easy](https://addons.mozilla.org/en-US/firefox/addon/rest-easy/),
or another HTTP client of your choice.

The root endpoint for Adaptive Media is `/o/adaptive-media-rest/images` by
default. So, for example, if your server address is `http://localhost:8080/`
the API you’ll need to consume is:
`http://localhost:8080/o/adaptive-media-rest/images`

Adaptive Media's API endpoints are divided by functionality, resulting in two
main parts:

- */configuration:* Create, read, update, and delete existing Adaptive Media
image configurations.

- */content:* Retrieve both Adaptive Media data and metadata about existing
Adaptive Media images.

You can learn how to use the */configuration* endpoint next.

## Configuration [](id=configuration)

The following requests are possible through the */configuration* endpoint:

### List All Available Configurations [](id=list-all-available-configurations)

Below is an example request pattern:

    GET [server address]/o/adaptive-media-rest/images/configuration

**Status Codes**

200<br />
500

Below is an example response object:

    [
    {
    "name": "Extra large demo size",
    "id": "demo-xlarge",
    "max-width": "1200",
    "max-height": "1200"
    },
    {
    "name": "Extra small demo size",
    "id": "demo-xsmall",
    "max-width": "50",
    "max-height": "50"
    },
    {
    "name": "Large demo size",
    "id": "demo-large",
    "max-width": "800",
    "max-height": "800"
    }
    ]

### Get a Configuration by its ID [](id=get-a-configuration-by-its-id)

Below is an example request pattern:

    GET [server address]/o/adaptive-media-rest/images/[configurationId]

**Status Codes**

200<br />
404 (Non-existent configuration)

Below is an example response object:

    {
    "name": "Small demo size",
    "id": "demo-small",
    "max-width": "100",
    "max-height": "100"
    }

### Add a New Configuration [](id=add-a-new-configuration)

Below is an example request pattern:

    PUT [server address]/o/adaptive-media-rest/images/[configurationId]

**Status Codes**

200<br />
400 (wrong body)<br />
403 (user without permission)

Below is an example request:

    PUT [server address]/o/adaptive-media-rest/images/small

    BODY:
    {
    "name": "Small Size",
    "max-height": 100,
    "max-width": 100
    }

Here is the resulting configuration:

    {
    "name": "Small Size",
    "id": "small",
    "max-height": "100",
    "max-width": "100"
    }

### Delete a Configuration [](id=delete-a-configuration)

Below is an example request pattern:

    DELETE [server address]/o/adaptive-media-rest/images/[configurationId]

**Status Codes**

200<br />
403 (user without permission)

Below is an example request:

    DELETE [server address]/o/adaptive-media-rest/images/small

    BODY:
    {
    "name": "Small Size",
    "max-height": 100,
    "max-width": 100
    }

Here is the resulting deleted configuration:

    {
    "name": "Small Size",
    "id": "small",
    "max-height": "100",
    "max-width": "100"
    }

Now that you know how to use the */configuration* endpoint, you can learn how to
use the */content* endpoint next.

## Content Endpoint [](id=content-endpoint)

To use the */content* endpoint you must first choose the Adaptive Media
image you want to recover. You can specify an adaptive media image via two
possible forms of ID: `fileEntryId` or `fileVersionId`.
<!-- Where can I find this information for the Adaptive Media images? -->

If you use the `fileEntryId`, You’ll need to provide the `fileEntryId` along
with the version name. The version name is a string (1.0, 1.2, 2.3, etc.).
<!-- Where can I find the version name? -->

Below is an example request pattern that uses the `fileEntryId`:

  [server address]/o/adaptive-media-rest/images/content/file/**fileEntryId**/version/**version**

You can also use the keyword `last` to retrieve the last version of the file
entry:

  [server address]/o/adaptive-media-rest/images/content/file/**fileEntryId**/version/last

Using the `fileVersionId` allows you to directly retrieve the Adaptive
Media image, without the need of a version name:

  [server address]/o/adaptive-media-rest/images/content/**fileVersionId**

Once you've specified a file version, you can use a few additional endpoints to
get data on the image variants.

You can learn how to use the */config* endpoint next.

### Config Endpoint [](id=config-endpoint)

The */config* endpoint uses a `fileVersion` to retrieve an image generated by a
certain configuration. If no data is found, the API will return the original
image. You can prevent this behavior by passing an *original* query param with
value *false* (*?query=original:false*).

**Status Codes**

200<br />
403 (user without permission)<br />
404 (fileVersion, fileEntry or configuration not found)

**Query params**

`original:boolean`: If the value is `true`, the original image is used as
fallback.

Below is an example request pattern using the `fileEntryId`:

    GET [server address]/o/adaptive-media-rest/images/content/file/[fileEntryId]/version/[version]/config/[configId]

Here is a request pattern that uses the `fileVersionId`:

    GET [server address]/o/adaptive-media-rest/images/content/[fileVersionId]/config/[configId]

Now that you know how to use the */config* endpoint, you can learn about the
*/data* endpoint next.

### Data Endpoint [](id=data-endpoint)

If you don’t know the config ID, or you don’t want to specify it, you can use
the */data* endpoint. This endpoint receives a query array param
(*query=parameter:value*) that specifies the properties of the image you want
(e.g. width near 200px and height near 500px).

**Status Codes**

200<br />
403 (user without permission)<br />
400 (wrong or undefined query)<br />
404 (fileVersion, fileEntry or no data found)

**Query params**

`original:boolean`: If the value is `true`, the original image is used as
fallback.

`string`: Used to pass properties of the image in the form of *property:value*.
Queries can be passed for each image property.

For example, if you want to retrieve an Adaptive Media version of the image
close to 200px width, using 500px height to resolve ambiguities, you would use
the following list of query params:

    ?query=width:200&query=height:500

Below is an example request pattern using the `fileEntryId`:

    GET [server address]/o/adaptive-media-rest/images/content/file/[fileEntryId]/version/[version]/data?query=width:200&query=height:500

Here is a request pattern that uses the `fileVersionId`:

    GET [server address]/o/adaptive-media-rest/images/content/[fileVersionId]/data?query=width:200&query=height:500

Here is a request pattern that adds the *original* query to the previous request:

    GET [server address]/o/adaptive-media-rest/images/content/[fileVersionId]/data?query=width:200&query=height:500&query=original:true

You can learn how to use the */variants* endpoint next.

### Variants Endpoint [](id=variants-endpoint)

The */variants* endpoint lets you retrieve the list of Adaptive Medias created
from a file version, with all its metadata in a JSON format. In addition to the
*query* param, and *original* param, you also have an *order* param
(opposite to query, so you can't use both at the same time) that
allows you to perform a strict order of the data: *order=property:true|false*
(ascending or descending).

**Status Codes**

200<br />
403 (user without permission)<br />
400 (wrong or undefined query, wrong or undefined order or both provided)<br />
404 (fileVersion, fileEntry not found)

**Query params**

`original:boolean`: If the value is `true`, the original image is used as
fallback.

`query`, `string`: Used to pass properties of the image in the form of
*query=property:value*. Queries can be passed for each image property.

`order`, `string:boolean`: Sets the order of the requested data in the form of
*order=property:true|false* (`true` for ascending, or `false` for descending).
This parameter can be passed for each image property.
<!-- Is that correct? -->

For example, if you want to retrieve image variants of an image in ascending
order by height, you would use the following query:

    ?order=height:true

Below is an example request pattern that uses the `fileEntryId`:

    GET [server address]/o/adaptive-media-rest/images/content/file/[fileEntryId]/version/[version]/variants

Below is an example request pattern that uses the `fileVersionId`:

    GET [server address]/o/adaptive-media-rest/images/content/[fileVersionId]/variants

Here is a request that adds the *order* parameter to the previous request:

GET [server address]/o/adaptive-media-rest/images/content/[fileVersionId]/variants?order=height:true

Below is an example response JSON object:

    [
    {
    "url": "http://localhost:8080/o/adaptive-media-rest/images/content/version/37278/config/demo-medium",
    "configuration": {
    "name": "Medium size",
    "id": "demo-medium",
    "max-width": "400",
    "max-height": "400"
    },
    "content-length": 15024,
    "width": 400,
    "content-type": "image/jpeg",
    "file-name": "d3998933-8a4a-46bb-babd-fd5f8f72de23.jpeg",
    "configuration-uuid": "demo-medium",
    "height": 225
    },
    {
    "url": "http://localhost:8080/o/adaptive-media-rest/images/content/version/37278/config/demo-small",
    "configuration": {
    "name": "Small demo size",
    "id": "demo-small",
    "max-width": "100",
    "max-height": "100"
    },
    "content-length": 2093,
    "width": 100,
    "content-type": "image/jpeg",
    "file-name": "d3998933-8a4a-46bb-babd-fd5f8f72de23.jpeg",
    "configuration-uuid": "demo-small",
    "height": 56
    }
    ]

Now you know how to use Adaptive Media images rest APIs to get the information
you're looking for!

## Related Topics [](id=related-topics)

[Item Selector](/develop/tutorials/-/knowledge_base/7-0/item-selector)

<!--[Adaptive Media UserGuide](link to go here)-->
