# Kapua Permissions Explained

Kapua has a lot of different permissions for various purposes, all of which can be mixed to get different combinations for different users and purposes - this naturally adds complexity to them but also opens a world of possibilities. This way we can have users that can see and edit Devices but cannot delete them or users that can only see Tags or Roles with "Permissions" tab...

This document was created because adding, editing and using permissions in Kapua can be rather difficult some times, so end users like you will not waste time with searching and debugging which permission(s) is/are needed for certain operation but will rather focus on the primary task itself.

Below there is a description for every service/grid in Kapua and which permissions need to be granted in order to see certain tabs or to enable certain buttons or features. For most of them the process and logic are pretty straightforward but some of them must be "studied" a bit in detail.

## Forwardable Permissions

But before we dive into Kapua's permissions we have to mention one important thing that can be more confusing than others - so called **__Forwardable__** option in permissions.

**__Forwardable__** permission is a link between a parent account and child account; It enables parent account to edit child account's settings, view its Users... In short, this permission can limit certain user to viewing users only in his "scope". The best way to explain this is through an example.
Imagine you have account named __account0__ and in this account you have the following users:

 - __user0__ (permissions: _**Account:Read**_, _**Account:Write**_, _**User:Read**_ - forwardable set to False)
 - __user1__ (permissions: _**Account:Read**_, _**Account:Write**_, _**User:Read**_ - forwardable set to True)

Create a child account within __account0__ named __account0_1__. Now login as __user0__ and go to __Child Accounts__ view - try to see a__ccount0_1's__ __Users__ - Kapua will return an error. This is because this user does not have __Forwardable__ option on __User__ permission set to true and thus user cannot see __Child Account's__ users. Same thing happens if you navigate to upper right corner where the __**[username]@[account name]**__ button is, you click __Switch to Account...__, select __account0_1__ and then navigate through the GUI - Kapua will start reporting errors that you do not have proper permissions to do this.

Same thing happens with __Account:Write__ permission - if this user tries to change account's parameters, Kapua will return an error saying that user does not have proper permissions, which is again OK, since user's **_Account:Write_** permission is not **_Forwardable_**. This may be a bit odd for you, but we will talk more about this in a moment.

If you now login as __user1__, there will not be such problems, since all the permissions are forwardable - as user1 you will be able to see all of the child account's settings and also all of the users and their parameters.

If we now take a deeper dive into this, you can see, that **_Forwardable_** permission opens several options - you can _close_ user in his so called **_scope_** - so he can only see his account and his users, but if he also has **_Forwardable_** permission, he can also see Users in Child Accounts and all of their settings.
This is also explained in __**Child Accounts**__ section of this text.

In this document only specific permissions and permission mix will be described, simple **_Read/Write/Delete_** permissions will be skipped if their functionalities are straightforward - but if you still have any questions after reading this document, please post a question/issue on Github or join us on one of community meetings!


## Welcome and About

**__Welcome__** and **__About__** are used to show some basic information about Kapua. These two tabs are enabled by default, so user can always see and use them (even if the user has no permissions). User can also always change its password (upper right corner).


## Connections

**__Connections__** in Kapua are one of the special items in Main menu grid. Because user cannot add or delete connections (that can only __Broker__ do), user can only edit parameters of certain connection (that is why table bellow does not describe how to enable **__Add__** or **__Delete__** buttons).

Connections view is also closely related to __**Users**__ in Kapua - connections can be connected/reserved to/for a certain user only (**__Reserved User__** and __**Allow user Change**__ options) or can be __open__ to all users. Because of this, Connections view has two "User" filter options - __Reserved User__ and __Last User__ which are enabled only if user has **_User:Read_** permission - otherwise these filter options are not available.


| Feature                                             | Needed Permissions                                       | Forwardable |
|-----------------------------------------------------|----------------------------------------------------------|-------------|
| **See Connections tab in main menu**                | Device_connection:Read                                   | No          |
| Enabled **Edit** button                             | Device_connection:Read                                   | No          |
| Enabled **_Refresh_** button in Connections         | Device_connection:Read Device_connection:Write           | No          |
| See **_Reserved User_** option                      | Device_connection:Read Device_connection:Write User:Read | No          |
| See **_Allow User Change_** option                  | Device_connection:Read Device_connection:Write User:Read | No          |
| See **_Last User_** filter option                   | Device_connection:Read Device_connection:Write User:Read | No          |
| See **_Reserved User_** filter option               | Device_connection:Read Device_connection:Write User:Read | No          |

## Devices

Kapua is a device management software and therefore it is logical that we have __**Device**__ view. With it, we can monitor the device's condition, status and many other things. If user has __Command__ permissions, we can even send some simple commands to it remotely or reboot it, if necessary.
**__Devices__** view is the most complex view in Kapua, since it has more than 10 tabs and subtabs all of which need some special permissions for viewing/editing.
If user has only _**Device:Read**_ permission, the __Description__ tab will be enabled and user will be able to see all the info of a certain device (if there are any, since user cannot add devices without **_Device:Write_** permission). __**Export to CSV**__ button and refresh button are also enabled (even if there are no devices in the list).

One of the specialities is in __Tags__ tab - user needs **_Device:Write_** permission to be able to add and delete tags and not **_Device:Delete_** or **_Tag:Write/Delete_** since there permissions are used for deleting devices and editing/deleting tags respectively.

Other buttons/features do no require any special permissions as you can see from the table bellow.


| Feature                                                                  | Needed Permissions                                                 | Forwardable |
|--------------------------------------------------------------------------|--------------------------------------------------------------------|-------------|
| **See Devices tab in main menu**                                         | Device:Read                                                        | No          |
| Enabled **_Refresh_** button                                             | Device:Read                                                        | No          |
| Enabled **_Export to CSV_** button                                       | Device:Read                                                        | No          |
| Enabled **_Add_** button                                                 | Device:Read Device:Write                                           | No          |
| Enabled **_Access Group_** option in **Add** window                      | Device:Read Device:Write Group:Read                                | No          |
| Enabled **_Edit_** button                                                | Device:Read Device:Write                                           | No          |
| Enabled **_Access Group_** option in **Edit** window                     | Device:Read Device:Write Group:Read                                | No          |
| Enabled **_Delete_** button                                              | Device:Read Device:Delete                                          | No          |
| Enabled **_Group_** option in filter menu                                | Device:Read Group:Read                                             | No          |
| Enabled **_Tag_** option in filter menu                                  | Device:Read Tag:Read                                               | No          |
| **TAG TAB**                                                              |                                                                    |             |
| Enabled **_Tag_** tab                                                    | Device:Read Tag:Read                                               | No          |
| Enabled **_Refresh_** button in **Tag** tab                              | Device:Read Tag:Read                                               | No          |
| Enabled **_Apply_** button in **Tag** tab                                | Device:Read Device:Write Tag:Read                                  | No          |
| Enabled **_Remove_** button in **Tag** tab                               | Device:Read Device:Write Tag:Read                                  | No          |
| **EVENTS TAB**                                                           |                                                                    |             |
| Enabled **_Events_** tab                                                 | Device:Read Device_event:Read                                      | No          |
| Enabled **_Refresh_** button in **Events** tab                           | Device:Read Device_event:Read                                      | No          |
| Enabled **_Export to CSV_** button in "Events" tab                       | Device:Read Device_event:Read                                      | No          |
| **PACKAGES TAB**                                                         |                                                                    |             |
| Enabled **_Packages_** tab (**Installed** and **In Progress** subtabs)   | Device:Read Device_management:Read                                 | No          |
| Enabled **_Refresh_** button in **Packages** tab                         | Device:Read Device_management:Read                                 | No          |
| Enabled **_Install_** button in **Packages** tab                         | Device:Read Device_management:Read Device_management:Write         | No          |
| Enabled **_Uninstall_** button in **Packages** tab                       | Device:Read Device_management:Read Device_management:Write         | No          |
| Enabled **_History_** subtab in **Packages** tab                         | Device:Read Device_management:Read Device_management-registry:Read | No          |
| **BUNDLES TAB**                                                          |                                                                    |             |
| Enabled **_Bundles_** tab                                                | Device:Read Device_management:Read                                 | No          |
| Enabled **_Refresh_** button in **Bundles** tab                          | Device:Read Device_management:Read                                 | No          |
| Enabled **_Start_** button in **Bundles** tab                            | Device:Read Device_management:Read Device_management:Execute       | No          |
| Enabled **_Stop_** button in **Bundles** tab                             | Device:Read Device_management:Read Device_management:Execute       | No          |
| **CONFIGURATION TAB**                                                    |                                                                    |             |
| Enabled **_Configuration_** tab (**Services** and **Snapshots** subtabs) | Device:Read Device_management:Read                                 | No          |
| Enabled **_Refresh_** button in **Configuration** tab                    | Device:Read Device_management:Read                                 | No          |
| Enabled **_Save_** button in **Configuration** tab                       | Device:Read Device_management:Read Device_management:Write         | No          |
| Enabled **_Discard_** button in **Configuration** tab                    | Device:Read Device_management:Read Device_management:Write         | No          |
| Enabled **_Refresh_** button in **Snapshots** subtab                     | Device:Read Device_management:Read Device_management:Write         | No          |
| Enabled **_Download_** button in **Snapshots** subtab                    | Device:Read Device_management:Read Device_management:Write         | No          |
| Enabled **_Rollback to_** button in **Snapshots** subtab                 | Device:Read Device_management:Read Device_management:Write         | No          |
| Enabled **_Upload And Apply_** button in **Snapshots** subtab            | Device:Read Device_management:Read Device_management:Write         | No          |
| **COMMAND TAB**                                                          |                                                                    |             |
| Enabled **_Command_** tab                                                | Device:Read Device_management:Read Device_management:Execute       | No          |
| **ASSETS TAB**                                                           |                                                                    |             |
| Enabled **_Assets_** tab                                                 | Device:Read Device_management:Read                                 | No          |
| Enabled **_Refresh_** button in **Assets** tab                           | Device:Read Device_management:Read                                 | No          |
| Enabled **_Save_** button in **Assets** tab                              | Device:Read Device_management:Read Device_management:Write         | No          |
| Enabled **_Discard_** button in **Assets** tab                           | Device:Read Device_management:Read Device_management:Write         | No          |


## Batch Jobs

Another interesting and generally useful feature in Kapua are **Jobs**. User can start/stop bundles, write assets or configuration, download and install a package and lots more on remote devices - they can even be scheduled in advance! All in all it is a powerful tool for device management.

Everything you need to start working with Jobs is in bottom table - it is pretty straightforward. If in doubt, do not hesitate to ask us for more clarification.

| Feature                                 | Needed Permissions                       | Forwardable |
|-----------------------------------------|------------------------------------------|-------------|
| **See Batch Jobs tab in main menu**     | Job:Read                                 | No          |
| Enabled **_Refresh_** button            | Job:Read                                 | No          |
| Enabled **_Add_** button                | Job:Read Job:Write                       | No          |
| Enabled **_Edit_** button               | Job:Read Job:Write                       | No          |
| Enabled **_Delete_** button             | Job:Read Job:Delete                      | No          |
| Enabled **_Start_** button              | Job:Read Job:Execute                     | No          |
| Enabled **_Stop_** button               | Job:Read Job:Execute                     | No          |
| Enabled **_Restart_** button            | Job:Read Job:Execute                     | No          |
| **TARGETS TAB**                         |                                          |             |
| Enabled **_Targets_** tab               | Job:Read Device:Read                     | No          |
| Enabled **_Refresh_** button            | Job:Read Device:Read                     | No          |
| Enabled **_Add_** button                | Job:Read Job:Write Device:Read           | No          |
| Enabled **_Delete_** button             | Job:Read Job:Delete Device:Read          | No          |
| Enabled **_Start_** button              | Job:Read Job:Execute Device:Read         | No          |
| **STEPS TAB**                           |                                          |             |
| Enabled **_Steps_** tab                 | Job:Read                                 | No          |
| Enabled **_Refresh_** button            | Job:Read                                 | No          |
| Enabled **_Add_** button                | Job:Read Job:Write                       | No          |
| Enabled **_Edit_** button               | Job:Read Job:Write                       | No          |
| Enabled **_Delete_** button             | Job:Read Job:Delete                      | No          |
| **SCHEDULES TAB**                       |                                          |             |
| Enabled **_Refresh_** button            | Job:Read Scheduler:Read                  | No          |
| Enabled **_Add_** button                | Job:Read Scheduler:Read Scheduler:Write  | No          |
| Enabled **_Delete_** button             | Job:Read Scheduler:Read Scheduler:Delete | No          |
| **EXECUTIONS TAB**                      |                                          |             |
| Enabled **_Refresh_** button            | Job:Read                                 |             |


## Data

Data tab is one of the simplest tabs in Kapua which is used to see the data that is sent from devices. It literally needs only one permission for showing the Data tab and "Device:Read" permission to show two additional tabs ("By Device" and "By Assets"). Because user cannot write (or delete) data in kapua, there is no need (for now) for user to have "Data:Write/Delete" permissions.

| Feature                           | Needed Permissions               | Forwardable |
|-----------------------------------|----------------------------------|-------------|
| **See Data tab in main menu**     | Datastore:Read                   | No          |
| Enabled **_Refresh_** button      | Datastore:Read                   | No          |
| **"BY DEVICE" TAB**               |                                  |             |
| Enabled **_Refresh_** button      | Device:Read Datastore:Read       | No          |
| **"BY ASSET" TAB**                |                                  |             |
| Enabled **_Refresh_** button      | Device:Read Datastore:Read       | No          |


## Tags

Tags are interesting feature in Kapua, allowing user to tag certain devices with a specific tag for various purposes. This is different than grouping, since user can see all the devices regardless of their tags, but cannot see devices in ceratain groups if he has insufficient permissions (see **Group** option in permissions). Also device can have multiple tags but can be part of only one group.

Tags can be used to check devices that are in certain area or in certain state or anything similar. This gives end-user additional options over device management.
All the permissions are pretty straight forward and do not need any extra explanation.

| Feature                                  | Needed Permissions   | Forwardable |
|------------------------------------------|----------------------|-------------|
| **See Tags tab in main menu**            | Tag:Read             | No          |
| Enabled **_Refresh_** button             | Tag:Read             | No          |
| Enabled **_Add_** button                 | Tag:Read Tag:Write   | No          |
| Enabled **_Edit_** button                | Tag:Read Tag:Write   | No          |
| Enabled **_Delete_** button              | Tag:Read Tag:Delete  | No          |
| **ASSIGNED DEVICES TAB**                 |                      |             |
| Enabled **_Assigned Devices_** tab       | Tag:Read Device:Read | No          |
| Enabled **_Refresh_** button             | Tag:Read Device:Read | No          |


## Users

Users in Kapua are basis for everything - without user you cannot even login!
Nevertheless, let's take a closer look on some of the permissions. To see __**Users**__ tab, end-user needs only _**User:Read**_ permissions and nothing else. To edit and delete them, _**User:Write**_ and _**User:Delete**_ permissions are needed respectively.

For showing **__Credentials__** tab, user needs _**Credentials:Read**_ permission (alongside with _**User:Read**_) and for editing/unlocking/deleting them _**Credentials:Write**_ and _**Credentials:Delete**_ respectively.

**Roles** tab is a bit different. For **Roles** tab to be visible user needs _**User:Read**_, _**Role:Read**_ and _**Access_info:Read**_ permissions, but for **Add** and **Revoke** buttons to be enabled user also needs **_Access_info:Write/Delete_** (and not "Role:Write/Delete" "- these are meant for adding, editing and deleting roles).

"Permissions" tab is again a bit different and deserves a bit more explanation then others. If user wants to see **Permissions** tab, **_"Access_info:Read"_**, **_"User:Read"_** and **_"Domain:Read"_** permissions are needed - do not forget on **_Domain:Read_** permission! But why?
If you look at "Grant Permission" window closely, you will see, that we have "Domain", "Action", "Access Group" and "Forwardable" options - and the first one, "Domain" has its own permission just for enabling this dropdown menu.
You can imagine "Domain:Read" permission as "entry point" for all other permissions and this is its only task. It has no other function in Kapua except this. This is also the reason why "Domain:Write" and "Domain:Delete" permissions are not needed - user needs Access_info:Write/Delete to grant and revoke permissions.

Word of caution: Be careful which permissions you grant to users, because if you grant them Access_info:Read/Write in combination with User:Read, they can start granting permissions themselves and therefore do things they are not supposed to.


| Feature                                  | Needed Permissions                                       | Forwardable |
|------------------------------------------|----------------------------------------------------------|-------------|
| **See Users tab in main menu**           | User:Read                                                | No          |
| Enabled **_Refresh_** button             | User:Read                                                | No          |
| Enabled **_Add_** button                 | User:Read User:Write                                     | No          |
| Enabled **_Edit_** button                | User:Read User:Write                                     | No          |
| Enabled **_Delete_** button              | User:Read User:Delete                                    | No          |
| See **_Reserved for Connection_** option | User:Read Connection:Read Device:Read                    | No          |
| **CREDENTIALS TAB**                      |                                                          |             |
| Enabled **_Credentials_** tab            | User:Read Credential:Read                                | No          |
| Enabled **_Refresh_** button             | User:Read Credential:Read                                | No          |
| Enabled **_Add_** button                 | User:Read Credential:Read Credential:Write               | No          |
| Enabled **_Edit_** button                | User:Read Credential:Read Credential:Write               | No          |
| Enabled **_Delete_** button              | User:Read Credential:Read Credential:Delete              | No          |
| Enabled **_Unlock_** button              | User:Read Credential:Read Credential:Write               | No          |
| **ROLES TAB**                            |                                                          |             |
| Enabled **_Roles_** tab                  | User:Read Role:Read                                      | No          |
| Enabled **_Refresh_** button             | User:Read Role:Read Access_info:Read                     | No          |
| Enabled **_Assign_** button              | User:Read Role:Read Access_info:Read Access_info:Write   | No          |
| Enabled **_Remove_** button              | User:Read Role:Read Access_info:Read Access_info:Delete  | No          |
| **PERMISSIONS TAB**                      |                                                          |             |
| Enabled **_Permissions_** tab            | User:Read Access_info:Read Domain:Read                   | No          |
| Enabled **_Refresh_** button             | User:Read Access_info:Read Domain:Read                   | No          |
| Enabled **_Grant_** button               | User:Read Access_info:Read Domain:Read Access_info:Write | No          |
| Enabled **_Revoke_** button              | User:Read Access_info:Read Domain:Read Access_info:Delete| No          |

## Roles

Roles are best to imagine as a "set of permissions"; User can add several permissions to a certain role and name it e.g. "user_roles" and this role can then be assigned to multiple users. End result is that this is much faster than adding every single role to every user - and it is also more elegant.

Roles have no special permission combinations, so user has to have Role:Read/Write/Delete permissions to see, edit and delete permissions and Access_info permissions in combination with Role permissions to see "Permission" tab.

If user wants to see which role is granted to which user, User:Read permission has to be added (see table bellow).


| Feature                          | Needed Permissions                                 | Forwardable |
|----------------------------------|----------------------------------------------------|-------------|
| **See Roles in main menu**       | Role:Read                                          | No          |
| Enabled **_Refresh_** button     | Role:Read                                          | No          |
| Enabled **_Add_** button         | Role:Read Role:Write                               | No          |
| Enabled **_Edit_** button        | Role:Read Role:Write                               | No          |
| Enabled **_Delete_** button      | Role:Read Role:Delete                              | No          |
| **PERMISSIONS TAB**              |                                                    |             |
| Enabled **_Permissions_** tab    | Role:Read Access_info:Read Domain:Read             | No          |
| Enabled **_Refresh_** button     | Role:Read Access_info:Read Domain:Read             | No          |
| Enabled **_Add_** button         | Role:Read Access_info:Read Role:Write Domain:Read  | No          |
| Enabled **_Delete_** button      | Role:Read Access_info:Read Role:Delete Domain:Read | No          |
| **GRANTED USERS TAB**            |                                                    | No          |
| Enabled **_Granted Users_** tab  | Role:Read User:Read                                | No          |
| Enabled **_Refresh_** button     | Role:Read User:Read                                | No          |

## Access Groups

Access Groups have similar purpose in Kapua as Tags but with one important difference - every device can be part only of one group, whereas number of tags is not limited. This adds additional options in Kapua - it may seem obsolete but if there are hundreds of devices, every "sorting" feature like this comes in handy.

There are no special permissions needed for showing Access Groups item in the main menu - user only needs Access_group:Read/Write/Delete permissions and optional Device:Read for viewing "Assigned Devices" tab.


| Feature                                  | Needed Permissions      | Forwardable |
|------------------------------------------|-------------------------|-------------|
| **See Access Groups in main menu**       | Group:Read              | No          |
| Enabled **_Refresh_** button             | Group:Read              | No          |
| Enabled **_Add_** button                 | Group:Read Group:Write  | No          |
| Enabled **_Edit_** button                | Group:Read Group:Write  | No          |
| Enabled **_Delete_** button              | Group:Read Group:Delete | No          |
| **ASSIGNED DEVICES TAB**                 |                         |             |
| Enabled **_Assigned Devices_** tab       | Group:Read Device:Read  | No          |
| Enabled **_Refresh_** button             | Group:Read Device:Read  | No          |

## Child Accounts

Child Accounts are the basis for "extending" Kapua an its functionalities. Just as "Users" can "expand" Kapua "horizontally" (creating multiple users in one account), accounts can do this "vertically" - every account (if its parameters permit this) can have their own child accounts and so on. This way we get a lot of smaller entities/accounts with users that have specific permissions for performing small number of specific tasks or basically anything else.

Here also so called "Forwardable" permissions comes into play - in "Users" tab to be exact. **Account:Read/Write/Delete** permissions are pretty straight forward, but if user wants to see and use "User" tab, forwardable permission has to be set to True (e.g. **User:Read:ALL:YES**), otherwise Kapua will return an error that user needs additional permissions.

"Forwardable" permission is also important if user wants to see and edit Services in Child Accounts -> Account Settings. If "Forwardable" is not set to True, user will not be able to set these parameters for child accounts (but will be able to see them in main menu on the left).

| Feature                                   | Needed Permissions                 | Forwardable           |
|-------------------------------------------|------------------------------------|-----------------------|
| **See Child Accounts in main menu**       | Account:Read                       | No                    |
| Enabled **_Refresh_** button              | Account:Read                       | No                    |
| Enabled **_Add_** button                  | Account:Read Account:Write         | No                    |
| Enabled **_Edit_** button                 | Account:Read Account:Write         | No                    |
| Enabled **_Delete_** button               | Account:Read Account:Delete        | No                    |
| **USERS TAB**                             |                                    |                       |
| Enabled **_Refresh_** button              | Account:Read User:Read             | Yes (User:Read)       |
| Enabled **_Add_** button                  | Account:Read User:Read User:Write  | Yes (User:Read/Write) |
| Enabled **_Edit_** button                 | Account:Read User:Read User:Write  | Yes (User:Read/Write) |
| Enabled **_Delete_** button               | Account:Read User:Read User:Delete | Yes (User:Read/Write) |

## Endpoints

Endpoints are a special feature, that does not behave like a "ordinary" Main menu item (such as Roles, Groups, Users...) - except for kapua-sys user that has full read/write/delete access, other users cannot see this item on the left. Although certain users have _**Endpoint:Read/Write/Delete**_ permissions, this item will not be visible.
Instead the added endpoints will be visible in **Settings -> Deployment Info** (under the Account Information), if user has _**Endpoint:Read**_ permission. As in Connections, _**Endpoint:Write/Delete**_ permissions are not used here.

We should also mention one special feature of Endpoints - if you create new endpoints in a child account, they are not added to old ones, but instead are used as a new "root". Example: If we have three endpoints (e.g. eth1/eth1/123, eth2/eth2/123, eth3/eth3/123) in kapua-sys account and we create one new endpoint in kapua-sys's child account "account0" (e.g. eclipse0/eclipse0/123), the three old ones "disappear from the list and only the last one is visible - all other child accounts created from account0 also see only "eclipse0/eclipse0/123" endpoint, until new ones are created in their scope.
If all the endpoints in certain child account are deleted, endpoints from parent account are shown as these are now the new "root" endpoints.

| Feature                          | Needed Permissions          | Forwardable |
|----------------------------------|-----------------------------|-------------|
| **See Endpoints in main menu**   | /                           | /           |
| See Endpoints in Settings        | Account:Read Endpoints:Read | No          |


## Settings

If user has **Account** permissions, **Settings** tab is enabled in Main menu, which enables user to see account's settings (so called "infiniteChildItem" and "MaxNumberChildItem" parameters). This is particularly useful if user encounters errors while creating/editing/deleting items; Unfortunately these settings cannot be changed, as this can only parent user/account do.

As stated, Settings view is part of **Account** permissions - so this item is visible is user has _**Accoount:Read**_ permission. There are no special features for this view; user needs _**Account:Write**_ permission (_**Forwardable**_ can be set to False) if **Edit** button should be enabled.

| Feature                             | Needed Permissions         | Forwardable |
|-------------------------------------|----------------------------|-------------|
| **See Settings in main menu**       | Account:Read               | No          |
| Enabled **_Edit_** button           | Account:Read Account:Write | No          |

