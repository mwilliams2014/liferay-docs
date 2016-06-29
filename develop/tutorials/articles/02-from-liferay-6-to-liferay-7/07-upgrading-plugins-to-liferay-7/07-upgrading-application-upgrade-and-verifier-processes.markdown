# Upgrading Application Upgrade and Verifier Processes

Since Liferay 7.0 and Lifery DXP, a new upgrade framework and verifier framework 
is available for you to use for your application. You can upgrade your existing 
upgrade and verify processes to use the new framework in just a few steps.

This tutorial demonstrates how to:

- Upgrade an existing upgrade process to the new upgrade framework
- Upgrade an existing verify process to the new verify framework

Before you get started, you can review the older upgrade process next.


<!-- I'll most likely merge some of the info in this section into the Upgrading
your Upgrade Process section-->
## Previous Upgrade Process Review [](id=previous-upgrade-process-review)

Following the prior upgrade process, you have to define the property 
`upgrade.processes`, a list of `UpgradeProcesses` representing the different 
upgrades for a specific version of your module, in your `portal-ext.properties`.

<!-- Was `portal-ext.properties` the correct file to add these properties to?-->

For instance, the code below shows the previous process for upgrading 
Calendar-service module from v1.0.0 to 1.0.1 and then to 1.0.2. 

    upgrade.processes=
        com.liferay.calendar.hook.upgrade.UpgradeProcess_1_0_0,
        com.liferay.calendar.hook.upgrade.UpgradeProcess_1_0_1,
        com.liferay.calendar.hook.upgrade.UpgradeProcess_1_0_2

Each step between versions was represented by a single class extending
`UpgradeProcess`, using a method called `doUpgrade`. This method was responsible
for executing the internal steps to update the database to that concrete
version. A method `getThreshold` is provided also to specify the schema version
where the upgrade starts.

The following example represents the required operations to update the database 
to v1.0.0 using the old framework:

    public class UpgradeProcess_1_0_0 extends UpgradeProcess {
    
        @Override
        public int getThreshold() {
            return 100;
        }
    
        @Override
        protected void doUpgrade() throws Exception {
            upgrade(UpgradeCalendarBooking.class);
        }
        
    }

The following example represents the required operations to update the database 
to v1.0.1 using the old framework:

    public class UpgradeProcess_1_0_1 extends UpgradeProcess {
    
        @Override
        public int getThreshold() {
            return 101;
        }
    
        @Override
        protected void doUpgrade() throws Exception {
            upgrade(UpgradeCalendar.class);
            upgrade(UpgradeCalendarBooking.class);
        }
        
    }


The following example represents the required operations to update the database 
to v1.0.2 using the old framework:

    public class UpgradeProcess_1_0_1 extends UpgradeProcess {
    
        @Override
        public int getThreshold() {
            return 102;
        }
    
        @Override
        protected void doUpgrade() throws Exception {
             upgrade(UpgradePortletPreferences.class);
        }
        
    }

Whenever you needed another internal step, you added another 
`upgrade(new UpgradePortletPreferences());` etc. after the existing ones.

Now that you are familiarized with the older framework, you can learn how to
migrate your code to the new upgrade framework next.

## Upgrading Your Upgrade Process to the New Framework

Follow the steps below to migrate your code to the new framework.

1.  If your application has any dependencies, add a dependency on the 
    `portal-upgrade` module to your `build.gradle` file:
    
        compile project(":portal:portal-upgrade")

2.  Check your database schema version against your bundle version.

    If the database schema is in a different version than the bundle version, 
    specify the different versions for the bundle and schema in your `bnd.bnd` 
    file, using the `require-SchemaVersion` property:

        require-SchemaVersion: 1.0.2

    If no `Required-SchemaVersion` is found, the `Bundle-Version` header will be 
    used. Now that the build files are configured, you can move on to the 
    upgrade class next.

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

    With the new framework, previous type of classes are represented by upgrade 
    registrations instead.

    Each upgrade is represented by an upgrade registration. An upgrade 
    registration is an abstraction for the changes you need to apply to the 
    database from one version to the next one.

    To define a registration, you need to provide

    - the bundle symbolic name of the module.
    - the schema version your module wants to upgrade from (as a `String`)
    - the schema version your module wants to upgrade to (as a `String`)
    - a list of `UpgradeSteps`

    For example, here is an upgrade process for the 
    `com.liferay.calendar.service` module:

        @Override
    
        public void register(Registry registry) {
        
            registry.register(
                            "com.liferay.calendar.service", "0.0.1", "1.0.0",
                            new com.liferay.calendar.upgrade.
                            v1_0_0.UpgradeCalendarBooking()));    
        }

    In this example, the `com.liferay.calendar.service` module is being upgraded 
    from version 0.0.1 to version 1.0.0. The changes are produced by a list of 
    `UpgradeSteps`, which in this example contains only one step:

        new com.liferay.calendar.upgrade.v1_0_0.UpgradeCalendarBooking());    
    
    The internal steps defined within the intermediate classes, the former 
    `UpgradeProcess` class, as they are indeed `UpgradeSteps`, require no change
    on your part. The new framework will process the steps as they are.
    
<!-- Manuel can you show an example of what an old internal step would look like
after it is converted to this new process? Would it be something like this?

    old framework:

    public class UpgradeProcess_1_0_1 extends UpgradeProcess {
    
        @Override
        public int getThreshold() {
            return 102;
        }
    
        @Override
        protected void doUpgrade() throws Exception {
             upgrade(UpgradeCalendarBooking.class);
        }
        
    }
    
    new framework version:
    
        @Override
    
        public void register(Registry registry) {
        
            registry.register(
                            "com.liferay.calendar.service", "1.0.1", "1.0.2",
                            upgrade(UpgradeCalendarBooking.class);    
        }
        
        
        
        
        
        would that work? or would the upgrade step have to be 
        `new com.liferay.calendar.upgrade.v1_0_0.UpgradeCalendarBooking());`
        
        based on what is said above, this should work, right? the 
        `upgrade(UpgradeCalendarBooking.class);` step will automatically be
        converted into the correct format?
        
        thx!
-->    

5.  Remove the logger code. It should look similar to the following pattern:

    private static final Log _log = logFactoryUtil.getLog(
        UpgradeProcess_1_0_0.class);
        
<!-- Can you point to an example in Liferay portal that uses this logger code in
its upgrade process?  Does every upgrade process have this line of code?

I found an example of this code in a 7.0 app: 
[https://github.com/liferay/liferay-portal/blob/0b6bbc6d48922f8079c008489304a5427be15e9b/modules/apps/forms-and-workflow/polls/polls-service/src/main/java/com/liferay/polls/upgrade/PollsServiceUpgrade.java](https://github.com/liferay/liferay-portal/blob/0b6bbc6d48922f8079c008489304a5427be15e9b/modules/apps/forms-and-workflow/polls/polls-service/src/main/java/com/liferay/polls/upgrade/PollsServiceUpgrade.java)

Is the logger code suppose to be in a 7.0 upgrade? If so, why are we removing it
here?

thx!
-->        

6.  Finally, use the `@Reference` annotation to reference the services that you 
    need for the upgrade. For example, here is a reference to the
    `expandoRowLocalService` for the [DDMServiceUpgrade.java](https://github.com/liferay/liferay-portal/blob/b46614dab20730319ba4218670e05e1d15fb6443/modules/apps/forms-and-workflow/dynamic-data-mapping/dynamic-data-mapping-service/src/main/java/com/liferay/dynamic/data/mapping/upgrade/DDMServiceUpgrade.java) 
    class:

        @Reference(unbind = "-")
	public void setExpandoRowLocalService(
		ExpandoRowLocalService expandoRowLocalService) {

		_expandoRowLocalService = expandoRowLocalService;
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
						Add Require-SchemaVersion if
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
						    "bundle.name","fromV",
						    "1.0.0",list
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

Next you can learn how to upgrade your application verify processes.

## Upgrading your Verify Process to the new Framework

Follow the steps below to migrate your verify process to the new framework.

1.  Convert your `verify` class into a component, using the `@Component` token  
    and define the following properties:
    
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

2.  Reference any Liferay services the class uses, using the `@Reference`
    annotation. For example, here is a reference to the 
    `dlFileVersionLocalService` for the [Document Library app](https://github.com/liferay/liferay-portal/blob/2960360870ae69360861a720136e082a06c5548f/modules/apps/collaboration/document-library/document-library-service/src/main/java/com/liferay/document/library/workflow/DLFileEntryWorkflowHandler.java)
    :
    
        @Reference(unbind = "-")
        protected void setDLFileVersionLocalService(
          DLFileVersionLocalService dlFileVersionLocalService) {
    
          _dlFileVersionLocalService = dlFileVersionLocalService;
        }
        ...
        private DLFileVersionLocalService _dlFileVersionLocalService;
        
    When another module provides the service implementation, the verify process
    will be able to use it.

Your verify processes are upgraded!

## Related Topics

<!-- Add topics here -->
