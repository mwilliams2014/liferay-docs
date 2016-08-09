# Upgrading Application Upgrade and Verifier Processes [](id=upgrading-application-upgrade-and-verifier-processes)

Since Liferay 7.0 and Lifery DXP, a new upgrade framework and verifier framework
is available for you to use for your application. You can upgrade your existing
upgrade and verify processes to use the new framework in just a few steps.

This tutorial demonstrates how to:

- Upgrade an existing upgrade process to the new upgrade framework
- Upgrade an existing verify process to the new verify framework

Go ahead and get started with the upgrade process next.

## Upgrading Your Upgrade Process to the New Framework [](id=upgrading-your-upgrade-process-to-the-new-framework)

Follow the steps below to migrate your code to the new framework.

1.  If your application has any dependencies, add a dependency on the
    `portal-upgrade` module to your `build.gradle` file:

        provided group: "com.liferay", name: "com.liferay.portal.upgrade",
        version: "2.0.0"

2.  Check your database schema version against your bundle version.

    After you make changes to your module you might increment the module
    version, however the database schema version may remain untouched, and
    therefore have a different value. If your module's database schema is a 
    different version than your module's bundle version, you can specify it in 
    the BND file.
    
    Add the`Liferay-Require-SchemaVersion` property to your `bnd.bnd` file to 
    describe that the schema version of the module uses a different version than 
    the module's bundle version.
    
    For example, the [Chat App](https://github.com/liferay/liferay-portal/blob/0d69917/modules/apps/chat/chat-service/bnd.bnd)
    specifies the bundle version and schema version below:

        Bundle-Version: 1.0.2
        
        ...
        
        Liferay-Require-SchemaVersion: 1.0.0

    If no `Liferay-Require-SchemaVersion` value is found, the `Bundle-Version`
    header will be used.
    
    Note that once you make an upgrade to your app, it is best practice to 
    specify your schema version with the `Liferay-Require-SchemaVersion` 
    property from that point on. This way, if the bundle version and schema 
    version ever differ, your database schema version will always be correct.

3.  Convert your `upgrade` class into an OSGi component, using the `@Component`
    token and implement the `UpgradeStepRegistrator` interface, as shown in the
    example below:

        @Component(
                immediate = true,
                service = UpgradeStepRegistrator.class
        )
        public class CalendarServiceUpgrade implements UpgradeStepRegistrator

4.  Remove the intermediate classes that wrapped the internal steps, i.e the
    `public class UpgradeProcess_1_0_1 extends UpgradeProcess...`.

    Following the prior upgrade process in 6.2, you had to define the property
    `upgrade.processes`, a list of `UpgradeProcesses` representing the different
    upgrades for a specific version of your module, in your
    `portal-ext.properties`.

    For instance, the code below shows the previous process for upgrading
    Calendar-service module from v1.0.0 to 1.0.1 and then to 1.0.2.

        upgrade.processes=
            com.liferay.calendar.hook.upgrade.UpgradeProcess_1_0_0,
            com.liferay.calendar.hook.upgrade.UpgradeProcess_1_0_1,
            com.liferay.calendar.hook.upgrade.UpgradeProcess_1_0_2

    Each step between versions was represented by a single class extending
    `UpgradeProcess`, using a method called `doUpgrade`. This method was
    responsible for executing the internal steps to update the database to that
    concrete version. A method `getThreshold` was provided also to specify the
    schema version where the upgrade starts.

    Whenever you needed another internal step, you added another
    `upgrade(new UpgradePortletPreferences());` etc. after the existing ones.

    Classes are now represented by upgrade registrations in the new framework
    instead.

    Each upgrade is represented by an upgrade registration. An upgrade
    registration is an abstraction for the changes you need to apply to the
    database from one version to the next one. The registrations are defined in
    an override of method
    [UpgradeStepRegistrator.register(UpgradeStepRegistrator.Registry)](https://docs.liferay.com/portal/7.0/javadocs/modules/apps/foundation/portal/com.liferay.portal.upgrade/com/liferay/portal/upgrade/registry/UpgradeStepRegistrator.html#register(com.liferay.portal.upgrade.registry.UpgradeStepRegistrator.Registry)).

    To define a registration, you need to provide the information below:

    - the bundle symbolic name of the module
    - the schema version your module wants to upgrade from (as a `String`)
    - the schema version your module wants to upgrade to (as a `String`)
    - a list of `UpgradeSteps` instances

    The internal steps defined within the intermediate classes, the former
    `UpgradeProcess` class, require no change on your part, as they are indeed
    `UpgradeSteps`. The new framework will process the steps as they are. The
    example below shows how the the Document Library app uses an older upgrade
    process in its registration method in the new framework.

    old framework class [UpgradePortletSettings.java](https://github.com/liferay/liferay-portal/blob/master/modules/apps/collaboration/document-library/document-library-web/src/main/java/com/liferay/document/library/web/internal/upgrade/v1_0_0/UpgradePortletSettings.java):

        public class UpgradePortletSettings
                extends com.liferay.portal.upgrade.v7_0_0.UpgradePortletSettings {

                public UpgradePortletSettings(SettingsFactory settingsFactory) {
                        super(settingsFactory);
                }

                @Override
                protected void doUpgrade() throws Exception {
                        DLGroupServiceSettings.registerSettingsMetadata();
                        DLPortletInstanceSettings.registerSettingsMetadata();

                        upgradeMainPortlet(
                                DLPortletKeys.DOCUMENT_LIBRARY, DLConstants.SERVICE_NAME,
                                PortletKeys.PREFS_OWNER_TYPE_GROUP, true);

                        upgradeDisplayPortlet(
                                DLPortletKeys.DOCUMENT_LIBRARY, DLConstants.SERVICE_NAME,
                                PortletKeys.PREFS_OWNER_TYPE_LAYOUT);
                        upgradeDisplayPortlet(
                                DLPortletKeys.MEDIA_GALLERY_DISPLAY, DLConstants.SERVICE_NAME,
                                PortletKeys.PREFS_OWNER_TYPE_LAYOUT);
                }

        }

    If you examine the `UpgradePortletSettings` class, you'll see that it
    extends the
    [com.liferay.portal.upgrade.v7_0_0.UpgradePortletSettings](https://github.com/liferay/liferay-portal/blob/692cff9635bd909ba13fab22574d4c02bae3ba91/portal-impl/src/com/liferay/portal/upgrade/v7_0_0/UpgradePortletSettings.java)
    class, which extends the `UpgradeProcess` class. This follows the old
    upgrade framework.

    new framework [DLWebUpgrade.java](https://github.com/liferay/liferay-portal/blob/master/modules/apps/collaboration/document-library/document-library-web/src/main/java/com/liferay/document/library/web/internal/upgrade/DLWebUpgrade.java):

        @Component(immediate = true, service = UpgradeStepRegistrator.class)
        public class DLWebUpgrade implements UpgradeStepRegistrator {

                @Override
                public void register(Registry registry) {
                        registry.register(
                                "com.liferay.document.library.web", "0.0.0", "1.0.0",
                                new DummyUpgradeStep());

                        registry.register(
                                "com.liferay.document.library.web", "0.0.1", "1.0.0",
                                new UpgradeAdminPortlets(),
                                new UpgradePortletSettings(_settingsFactory));
        }

    As you can see the new framework calls the old `UpgradePortletSettings`
    framework class and uses it in the new registration method without any
    problems.

    The `DLWebUpgrade` new framework example covers two different use cases with 
    two different registrations. If the first registration is not needed, the 
    upgrade process skips it and uses the second upgrade registration instead.

    The first registration is used if your app has never been installed in
    @product@, and therefore does not exist in the data table. If you look at
    the [DummyUpgradeStep](https://github.com/liferay/liferay-portal/blob/master/portal-kernel/src/com/liferay/portal/kernel/upgrade/DummyUpgradeStep.java)
    class, you can see that it is empty. The `DummyUpgradeStep()` creates an
    initial release version(1.0.0 in this case) for your app in the data table,
    so that you can upgrade your app's version later on.
    
    If your app has never been installed in @product@, use the 
    `DummyUpgradeStep()` to create your initial release version.

    If your app has an existing version in @product@, the first registration is
    skipped, and the second registration is used instead. In the example, the
    registration finds the 0.0.1 version in the data table and upgrades it to
    version 1.0.0. The changes are produced by a list of `UpgradeSteps`
    instances, which in this example contains two steps:

        new UpgradeAdminPortlets()
        new UpgradePortletSettings(_settingsFactory)

    You can use this same process to upgrade your 6.2 upgrade processes to use
    the new framework.

5.  Remove the logger code. It should look similar to the following pattern:

    private static final Log _log = logFactoryUtil.getLog(
        UpgradeProcess_1_0_0.class);

    Log code is inside of the framework, so upgrade steps do not need to log
    their operations.

6.  Finally, use the `@Reference` annotation to reference the services that you
    need for the upgrade. For example, here is a reference to the
    `companyLocalService` for the [PrivateMessagingServiceUpgrade.java](https://github.com/liferay/liferay-portal/blob/master/modules/apps/social-private-messaging/social-private-messaging-service/src/main/java/com/liferay/social/privatemessaging/internal/upgrade/PrivateMessagingServiceUpgrade.java)
    class:

	@Reference(unbind = "-")
	protected void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

With that, your application upgrade processes are upgraded!

For your convienience, a summary of the steps for upgrading your application
upgrade process are outlined in the table below for reference:

<style>
.lego-image {
	max-height: 100%;
	max-width: 100%;
}
.max-960 {
	margin: 0 auto;
	max-width: 960px;
}
.no-max
.max-960 {
	max-width: none;
}
.metadata-guidelines-table td {
	border-bottom: 1px solid;
	border-top: 1px solid;
	padding: 10px;
}
.table-header {
	font-weight: bold;
}
.table-header.second {
	width: 70%;
}
.left-header {
	border-right: 1px solid;
}
</style>
<div class="lego-article metadata-guidelines-table" id="article-33460946">
<div class="lego-article-content max-960">
<div class="aui-helper-clearfix lego-section section-1" >
<div class="aui-w100 block-1 content-column lego-block" >
<div class="content-column-content">
<table>
	<thead>
		<td class="table-header left-header">
			File
		</td>
		<td class="table-header second">
			Required Changes
		</td>
	</thead>
	<tbody>
		<tr>
			<td class="table-header left-header">
				build.gradle File
			</td>
			<td class="">
				<ul>
					<li>
						Add the `portal-upgrade` dependency.
					</li>
				</ul>
			</td>
		</tr>
		<tr>
			<td class="table-header left-header">
			bnd.bnd file
			</td>
			<td class="">
				<ul>
					<li>
						Add Liferay-Require-SchemaVersion if
						needed.
					</li>

				</ul>
			</td>
		</tr>
		<tr>
			<td class="table-header left-header">
			MyCustomServiceUpgrade.java
			</td>
			<td class="">
				<ul>
					<li>
						    Declare a UpgradeStepRegistrator component.
					</li>
					<li>
					        It has no loggers.
					</li>
					<li>
					        It has no reference to Release
					        service.
					</li>

				</ul>
			</td>
		</tr>
		<tr>
			<td class="table-header left-header">
			Logger reference
			</td>
			<td class="">
				<ul>
					<li>
						Remove it.
					</li>

				</ul>
			</td>
		</tr>
		<tr>
			<td class="table-header left-header">
			MyCustomServiceUpgrade_1_0_0.java
			</td>
			<td class="">
				<ul>
					<li>
						Disappears. It is a registration
						on the UpgradeStepRegistrator:

						registry.register(
						    "bundle.name","fromVersion",
						    "toVersion",list
						)
					</li>

				</ul>
			</td>
		</tr>
		<tr>
			<td class="table-header left-header">
			package.v1_0_0.UpgradeFoo.java
			</td>
			<td class="">
				<ul>
					<li>
						No changes, it's already an
						UpgradeStep.
					</li>

				</ul>
			</td>
		</tr>
		<tr>
			<td class="table-header left-header">
			package.v1_0_0.UpgradeBar.java
			</td>
			<td class="">
				<ul>
					<li>
						No changes. Passed as a
						parameter in the list of
						UpgradeSteps for the
						registration.
					</li>

				</ul>
			</td>
		</tr>
	</tbody>
</table>
</div>
</div>
</div>
</div>
</div>

Next you can learn how to upgrade your application's verify processes.

## Upgrading your Verify Process to the new Framework [](id=upgrading-your-verify-process-to-the-new-framework)

Follow the steps below to migrate your verify process to the new framework.

1.  Convert your `verify` class into a component, using the `@Component` token
    and define the properties below:

        @Component(
                immediate = true,
                property = {"verify.process.name=
                com.liferay.[package.name.service]"},
                service = VerifyProcess.class
        )

    The `immediate` property specifies that the component will be available
    immediately, rather than the first time it is used. The
    `verify.process.name` property should point to the name of the service
    package of the app. Make sure your replace `[package.name.service]` with the
    service package name for your app. The OSGi service tracker uses this
    information to identify the verifier components. You must use
    `VerifyProcess.class` as the `service` property value, to denote that the
    class is a valid implementation of the `VerifyProcess` interface.

2.  Declare a setter method for each Liferay service the class uses, using the
    `@Reference` annotation. For example, here is a reference to the
    `dlFileVersionLocalService` for the [Document Library app](https://github.com/liferay/liferay-portal/blob/7.0.1-ga2/modules/apps/collaboration/document-library/document-library-service/src/main/java/com/liferay/document/library/workflow/DLFileEntryWorkflowHandler.java)
    :

        @Reference(unbind = "-")
        protected void setDLFileVersionLocalService(
          DLFileVersionLocalService dlFileVersionLocalService) {

          _dlFileVersionLocalService = dlFileVersionLocalService;
        }

        ...

        private DLFileVersionLocalService _dlFileVersionLocalService;

    When another module provides the service implementation, the verify process
    will be able to use it. The optional element assignment `unbind = "_"`
    declares that there's no unbind method.

Your verify processes are upgraded!

## Related Topics [](id=related-topics)

[Creating a Verify Process for Your App](/develop/tutorials/-/knowledge_base/7-0/creating-a-verify-process-for-your-app)

[Creating an Upgrade Process for Your App](/develop/tutorials/-/knowledge_base/7-0/creating-an-upgrade-process-for-your-app)
