---
header-id: custom-selecting-of-information-with-infoitemselector
---

# Selecting Custom information Types with `InfoItemSelector`

[TOC levels=1-4]

The [Content fragment](/docs/7-2/user/-/knowledge_base/u/content-page-management-interface#section-builder) and [itemSelector fragment configuration](/docs/7-2/reference/-/knowledge_base/r/fragment-configuration-types#item-selector-configuration) let you display a single Web Content, Blog Entry, or document in a fragment. See the [Item Selector Introduction](https://portal.liferay.dev/docs/7-2/frameworks/-/knowledge_base/f/item-selector) for more information on how the Item Selector works. To make your custom content type available for selection, follow these steps:

1. Create an [`InfoItemRenderer`](/docs/7-2/frameworks/-/knowledge_base/f/custom-rendering-of-information-with-infoitemrenderer) to render the content.

2.  Create an `*ItemSelectorView` Component class that implements the `ItemSelectorView.class` service and both the [`InfoItemSelectorView`](https://github.com/liferay/liferay-portal/blob/master/modules/apps/info/info-api/src/main/java/com/liferay/info/item/selector/InfoItemSelectorView.java) interface and the [`ItemSelectorView`](https://github.com/liferay/liferay-portal/blob/master/modules/apps/item-selector/item-selector-api/src/main/java/com/liferay/item/selector/ItemSelectorView.java) interface (with an [`InfoItemItemSelectorCriterion`](https://github.com/liferay/liferay-portal/blob/master/modules/apps/item-selector/item-selector-criteria-api/src/main/java/com/liferay/item/selector/criteria/info/item/criterion/InfoItemItemSelectorCriterion.java) type). It must also have an `InfoItemItemSelectorReturnType` as the Return Type. The example below is the implementation for the [`BlogsEntryItemSelectorView` Class](https://github.com/liferay/liferay-portal/blob/master/modules/apps/blogs/blogs-web/src/main/java/com/liferay/blogs/web/internal/item/selector/BlogsEntryItemSelectorView.java):

```java
@Component(
	property = "item.selector.view.order:Integer=200",
	service = ItemSelectorView.class
)
public class BlogsEntryItemSelectorView
	implements InfoItemSelectorView,
			   ItemSelectorView<InfoItemItemSelectorCriterion> {

	@Override
	public String getClassName() {
		return BlogsEntry.class.getName();
	}

	@Override
	public Class<InfoItemItemSelectorCriterion>
		getItemSelectorCriterionClass() {

		return InfoItemItemSelectorCriterion.class;
	}

	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public List<ItemSelectorReturnType> getSupportedItemSelectorReturnTypes() {
		return _supportedItemSelectorReturnTypes;
	}

	@Override
	public String getTitle(Locale locale) {
		return _language.get(locale, "blogs");
	}

	@Override
	public boolean isVisible(ThemeDisplay themeDisplay) {
		return true;
	}

	@Override
	public void renderHTML(
			ServletRequest servletRequest, ServletResponse servletResponse,
			InfoItemItemSelectorCriterion infoItemItemSelectorCriterion,
			PortletURL portletURL, String itemSelectedEventName, boolean search)
		throws IOException, ServletException {

		BlogEntriesItemSelectorDisplayContext
			blogEntriesItemSelectorDisplayContext =
				new BlogEntriesItemSelectorDisplayContext(
					this, (HttpServletRequest)servletRequest,
					itemSelectedEventName, portletURL);

		servletRequest.setAttribute(
			BlogsWebKeys.BLOGS_ITEM_SELECTOR_DISPLAY_CONTEXT,
			blogEntriesItemSelectorDisplayContext);

		ServletContext servletContext = getServletContext();

		RequestDispatcher requestDispatcher =
			servletContext.getRequestDispatcher(
				"/blogs/item/selector/select_entries.jsp");

		requestDispatcher.include(servletRequest, servletResponse);
	}

	private static final List<ItemSelectorReturnType>
		_supportedItemSelectorReturnTypes = Collections.singletonList(
			new InfoItemItemSelectorReturnType());

	@Reference
	private Language _language;

	@Reference(target = "(osgi.web.symbolicname=com.liferay.blogs.web)")
	private ServletContext _servletContext;

}
```

3.  Deploy your bundle to your app server and either add a Content fragment to 
    the page or create a custom fragment that includes an 
    [`itemSelector` configuration](/docs/7-2/reference/-/knowledge_base/r/fragment-configuration-types#item-selector-configuration) 
    and add it to the page.
    
4.  Open the fragment's configuration and click the *Add button* to select your 
    custom content.

Great! Now you know how to make your custom content type available for selection in the fragment's content selector and `itemSelector` configuration.