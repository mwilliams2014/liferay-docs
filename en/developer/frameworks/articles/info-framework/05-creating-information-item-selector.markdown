---
header-id: custom-selecting-of-information-with-infoitemselector
---

# Selecting Custom information Types with `InfoItemSelector`

[TOC levels=1-4]

The [Content fragment](/docs/7-2/user/-/knowledge_base/u/content-page-management-interface#section-builder) 
and [itemSelector fragment configuration](/docs/7-2/reference/-/knowledge_base/r/fragment-configuration-types#item-selector-configuration) 
let you display a single Web Content, Blog Entry, or document in a fragment. To 
make your custom content type available for selection, follow these steps:

1. Create an [`InfoItemRenderer`](/docs/7-2/frameworks/-/knowledge_base/f/custom-rendering-of-information-with-infoitemrenderer) to render the content.

2.  Create an [Item Selector](/docs/7-2/frameworks/-/knowledge_base/f/selecting-entities-with-an-item-selector) 
    to select the content that has a `InfoItemItemSelectorReturnType`.

3.  Deploy your bundle to your app server and either add a Content fragment to 
    the page or create a custom fragment that includes an 
    [`itemSelector` configuration](/docs/7-2/reference/-/knowledge_base/r/fragment-configuration-types#item-selector-configuration) 
    and add it to the page.
    
4.  Open the fragment's configuration and click the *Add button* to select your 
    custom content.

Great! Now you know how to make your custom content type available to the 
fragment's itemSelector configuration.