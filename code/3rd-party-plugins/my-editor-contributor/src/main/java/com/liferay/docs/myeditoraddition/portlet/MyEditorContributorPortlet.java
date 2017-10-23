package com.liferay.docs.myeditoraddition.portlet;

import com.liferay.docs.myeditoraddition.constants.MyEditorContributorPortletKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author liferay
 */
@Component(
	immediate = true,
	property = {
		"editor.config.key=contentEditor",
		"editor.name=alloyeditor",
		"editor.name=ckeditor",
		"javax.portlet.name=com_liferay_blogs_web_portlet_BlogsPortlet",
		"javax.portlet.name=com_liferay_blogs_web_portlet_BlogsAdminPortlet",
		"service.ranking:Integer=100"
	},
	service = EditorConfigContributor.class
)
public class MyEditorContributorPortlet extends BaseEditorConfigContributor {
	
		@Override
		public void populateConfigJSONObject(
		    JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		    ThemeDisplay themeDisplay,
		    RequestBackedPortletURLFactory requestBackedPortletURLFactory) {
				
					JSONObject toolbars = jsonObject.getJSONObject("toolbars");
					String extraPlugins = jsonObject.getString("extraPlugins");
	
					if(Validator.isNotNull(extraPlugins)){
						extraPlugins +=",youtube,videodetector";
					}
					else{
						extraPlugins = "youtube,videodetector";
					}
					
					jsonObject.put("extraPlugins", extraPlugins);
	
					if (toolbars != null) {
					    JSONObject toolbarAdd = toolbars.getJSONObject("add");
					
					    if (toolbarAdd != null) {
					        JSONArray addButtons = toolbarAdd.getJSONArray("buttons");
					
									addButtons.put("camera");
					        addButtons.put("youtube");
									addButtons.put("videodetector");
					    }
							
					}

		}
	
}