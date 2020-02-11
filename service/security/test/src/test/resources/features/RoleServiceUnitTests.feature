###############################################################################
# Copyright (c) 2020 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech
###############################################################################
@role
@unit
Feature: Role Service
  Role Service is responsible for CRUD operations on Roles. This service is currently
  used to attach roles to Users.

   Scenario: Creating a valid role
     Create a role entry with specified name and description. Once created, search for it - it should have been created.
     Kapua should not return any errors.

     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I find a role with name "roleName"
     And No exception was thrown

   Scenario: Creating a role with null name
     Create a role entry, with null name and description e.g "roleDescription".
     Kapua should return an error.

     Given I prepare a role creator with name "" and description "roleDescription"
     But I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument roleCreator.name."
     When I create a new role entity from the existing creator
     And An exception was thrown

   Scenario: Creating a role with name only
     Create a role with name "roleName" only.
     Kapua should not return any errors.

     Given I prepare a role creator with name "roleName" and description ""
     When I create a new role entity from the existing creator
     Then I find a role with name "roleName"
     And No exception was thrown

   Scenario:  Creating a role with too short name
     Create a role with too short name e.g "ro" and description "roleDescription"
     Kapua should return an error.

     Given I prepare a role creator with name "ro" and description "roleDescription"
     But I expect the exception "KapuaIllegalArgumentException" with the text "Value less than allowed min length. Min length is 3."
     When I create a new role entity from the existing creator
     And An exception was thrown

   Scenario: Creating a role with regular name and very short description" as description cannot be too short, as even one character is enough
     Creating a role with regular name and very short description.
     Kapua should not return any errors, "description" cannot be too short.

     Given I prepare a role creator with name "roleName" and description "d"
     When I create a new role entity from the existing creator
     Then I find a role with name "roleName"
     And No exception was thrown
     
   Scenario: Creating a role wtih 255 characters long name
     Create a role with 255 characters long name and with a regular description e.g "roleDescription".
     Kapua should not return any errors.
     
     Given I prepare a role creator with name "roleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNam" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I find a role with name "roleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNam"
     And No exception was thrown

   Scenario: Creating a role with too long name
     Create a role with too long name and a regular description.
     Kapua should return an error.

     Given I prepare a role creator with name "roleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleName" and description "roleDescription"
     But I expect the exception "KapuaIllegalArgumentException" with the text "Value over than allowed max length. Max length is 255."
     Then I create a new role entity from the existing creator
     And An exception was thrown

   Scenario: Creating a role with 255 characters long description
     Creating a role with a valid name and with 255 character long description.
     Kapua should not return any errors.
     
     Given I prepare a role creator with name "roleName" and description "roleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescription"
     When I create a new role entity from the existing creator
     Then I find a role with name "roleName"
     And No exception was thrown

   Scenario: Creating a role with valid name and with too long description
     Create a role with valid name e.g "roleName" and with too long description.
     Kapua should return an error.

     Given I prepare a role creator with name "roleName" and description "roleDescrsdsdsdsdsdsiptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescriptionroleDescription"
     When I create a new role entity from the existing creator

   Scenario: Creating a role with the name that contains digits
     Create a role with name that contains digits (e.g "role123") and with description (e.g "roleDescription").
     Kapua should not return any error.

     Given I prepare a role creator with name "role123" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I find a role with name "role123"
     And No exception was thrown

   Scenario: Creating a role with forbidden symbols in its name
     Create a role with name containing special symbols (e.g. "roleName%") and with a valid description (e.g "roleDescription").
     Kapua should return an error.

     Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
     Then I try to create roles with invalid characters "!#$%&'()=»Ç>:;<.,⁄@‹›€*ı–°·‚±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«" in name
     And An exception was thrown

   Scenario: Creating a role with special characters in the description
     Create a role with a valid name (e.g "roleName") and with description containing special symbols (e.g "descripti@n!@#").
     Kapua should not return any errors.

     Given I try to create roles with invalid characters "!#$%&'()=»Ç>:;<.,⁄@‹›€*ı–°·‚±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«" in description
     Then I find a role with name "roleName0"
     And No exception was thrown

   Scenario: Creating a role with a name and description that contain digits only
     Create a role with name e.g "123" and with description e.g "123".
     Kapua should not return any errors.

     Given I prepare a role creator with name "123" and description "123"
     When I create a new role entity from the existing creator
     Then I find a role with name "123"
     And No exception was thrown

   Scenario: Creating two roles with the same name
     Create first role with name e.g "roleName" and with description "roleDescription".
     After that create another role with same name but different description."
     Kapua should return an error.

     Given I prepare a role creator with name "roleName" and description "roleDescription123"
     When I create a new role entity from the existing creator
     Then I prepare a role creator with name "roleName" and description "roleDescription"
     But I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name roleName already exists."
     Then I create a new role entity from the existing creator
     And An exception was thrown

   Scenario: Creating two roles with the same description
     Create first role with name e.g "roleName" and with description "roleDescription".
     After that create another role with same description but different name."
     Kapua should not return an error.

     Given I prepare a role creator with name "roleName1" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I prepare a role creator with name "roleName2" and description "roleDescription"
     Then I create a new role entity from the existing creator
     And No exception was thrown

   Scenario: Creating a role name with allowed symbols in its name
     Create a role with a name that contains allowed symbol (e.g "roleName-123").
     Kapua should not return any errors.

     Given I prepare a role creator with name "roleName-123_" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I find a role with name "roleName-123_"
     And No exception was thrown

   Scenario: Counting created roles items in the DB
     Create 15 roles and find all of them in the DB.
     Kapua should not return any errors.

     Given I create 15 roles
     When I count the roles in the database
     Then I count 15
     And No exception was thrown

   Scenario: Changing role's name to a valid one
     Create a role with name e.g "roleName". After that try to edit it to e.g "roleName2.
     Kapua should not return any errors.
     
     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     And I update the role name to "roleName2"
     Then I find a role with name "roleName2"
     And No exception was thrown

   Scenario: Changing role description to a valid one
     Create a role with a valid name and valid description (e.g "roleDescription").
     After that try to edit role description to e.g "roleDescription123.
     Kapua should not return any errors.

     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     And I update the role description to "roleDescription123"
     Then I search for a role with description "roleDescription"
     And I find no roles

   Scenario: Changing name of a nonexisting role
     Create a role with a valid name (e.g "roleName") and with a valid description (e.g "roleDescription").
     After that delete it and try to update it.
     Kapua should return an error.
     
     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I delete the role with name "roleName"
     But I expect the exception "KapuaEntityNotFoundException" with the text "*"
     And I update the last created role name to "roleName2"
     Then An exception was thrown

   Scenario: Changing description of a nonexisting role
     Create a role with a valid name (e.g "roleName") and with a valid description (e.g "roleDescription").
     After that delete it and try to update role description.
     Kapua should return an error.

     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I delete the role with name "roleName"
     But I expect the exception "KapuaEntityNotFoundException" with the text "*"
     And I update the role description to "roleDescription123"
     And An exception was thrown

   Scenario: Changing role name to null
     Create a role with a valid name (e.g "roleName") and with a valid description (e.g "roleDescription").
     After that try to update role name to "null".
     Kapua should return an error.

     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     But I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument role.name."
     Then I update the role name to ""
     And An exception was thrown

   Scenario: Changing role name to contain special character
     Create a role with name e.g "roleName" and with description e.g "roleDescription",
     then try to update role name with special characters ( e.g "roleName%").
     Kapua should return an errors.

     But I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument role.name"
     Then I update the role name with special characters "!#$%&'()=»Ç>:;<.,⁄@‹›€*ı–°·‚±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«"
     And An exception was thrown
     
   Scenario:  Change role name so it is too short
     Create a role with a valid name (e.g "roleName") and with a valid description (e.g "roleDescription").
     After that try to change role name to "ro".
     Kapua should return an error.

     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     But I expect the exception "KapuaIllegalArgumentException" with the text "Value less than allowed min length. Min length is 3."
     Then I update the role name to "ro"
     And An exception was thrown

   Scenario: Changing role name so it is too long
     Create a role with a valid name (e.g "roleName") and with a valid description (e.g "roleDescription"),
     then try to update role name so it contains more than 255 characters.
     Kapua should return an error.

     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     But I expect the exception "KapuaIllegalArgumentException" with the text "Value over than allowed max length. Max length is 255."
     Then I update the role name to "roleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleNameroleName"
     And An exception was thrown

   Scenario: Deleting an existing role
     Create a role with a valid name (e.g "roleName") and with a valid description (e.g "roleDescription").
     After that try to delete it.
     Kapua should not return any errors.

     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I delete the role with name "roleName" and description "roleDescription"
     And I search for the role with name "roleName"
     But I find no roles

   Scenario: Deleting a role twice
     Create a role with a valid name (e.g "roleName") and with a valid description (e.g "roleDescription"),
     then try to delete role with name "roleName" twice - Kapua should return an error on the second try.

     Given I prepare a role creator with name "roleName" and description "roleDescription"
     When I create a new role entity from the existing creator
     Then I delete the role with name "roleName" and description "roleDescription"
     But I expect the exception "KapuaEntityNotFoundException" with the text "*"
     Then I delete the role with name "roleName" and description "roleDescription"
     And An exception was thrown