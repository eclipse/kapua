###############################################################################
# Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech
###############################################################################
@group
@unit
Feature: Access Groups
  This feature file contains Unit tests for Access Groups (CRUD tests).

  Scenario: Creating a valid Access Group with unique name
  Create an Access Group with valid name. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description ""
    When I search for the group with name "NewAccessGroup"
    Then I find the group with name "NewAccessGroup"
    And No exception was thrown

  Scenario: Creating an Access Group with empty name
  Create an Access Group without name.
  Kapua should return an error.
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create the group with name "" and description ""
    Then An exception was thrown

  Scenario: Creating non-unique Access Group
  Create an Access Group with valid name. Then create a new Access Group with the same name.
  Kapua should return an error
    Given I create the group with name "NewAccessGroup" and description ""
    And I expect the exception "KapuaDuplicateNameException" with the text "*"
    And I create the group with name "NewAccessGroup" and description ""
    Then An exception was thrown

  Scenario: Creating an Access Group with short name
  Create an Access Group with short but still valid name, name that contains 3 characters. Once created, search for it - it should have been created.
  Kapua should not return any error.
    Given I create the group with name "abc" and description ""
    When I search for the group with name "abc"
    Then I find the group with name "abc"
    And No exception was thrown

  Scenario: Creating an Access Group with too short name
  Create an Access Group with too short name, name that contains less than 3 characters.
  Kapua should return an error.
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create the group with name "ag" and description ""
    Then An exception was thrown

  Scenario: Creating an Access Group with long name
  Create an Access Group with long but still valid name, name that contains 255 characters. Once created, search for it - it should have been created.
  Kapua should not return any error.
    Given I create the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA" and description ""
    When I search for the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    Then I find the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    And No exception was thrown

  Scenario: Creating an Access Group with too long name
  Create an Access Group with too long name, name that contains more than 255 characters.
  Kapua should return an error.
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroup" and description ""
    Then An exception was thrown

  Scenario: Creating a valid Access Group with valid special symbols in name
  Create an Access Group with valid name that contains valid special symbols. Valid special symbols for name are: "_", "-" and " "
  Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "New Access_Group-ag" and description ""
    When I search for the group with name "New Access_Group-ag"
    Then I find the group with name "New Access_Group-ag"
    And No exception was thrown

  Scenario: Creating a Access Group with invalid special symbols in name
  Create an Access Group with with invalid special symbols in name, all except "_", "-" and " ". Use one symbol every time.
  Kapua should return an error.
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I try to create the group with invalid characters "!#$%&'()=»Ç>:;<.,⁄@‹›€*ı–°·‚±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«" in name and description ""
    Then An exception was thrown

  Scenario: Creating a valid Access Group with numbers in name
  Create an Access Group with numbers in name. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "Group123" and description ""
    When I search for the group with name "Group123"
    Then I find the group with name "Group123"
    And No exception was thrown

  Scenario: Creating a valid Access Group with only numbers in name
  Create an Access Group with only numbers in name. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "1234" and description ""
    When I search for the group with name "1234"
    Then I find the group with name "1234"
    And No exception was thrown

  Scenario: Creating unique Access Group with unique description
  Create an Access Group with unique name and with unique description. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "description"
    When I search for the group with description "description"
    Then I find the group with description "description"
    And No exception was thrown

  Scenario: Creating unique Access Group with non-unique description
  Create an Access group with description. Then create second Access Group with different name but the same description.
  Kapua should not return any errors.
    Given I create the group with name "AccessGroup" and description "description"
    And I find the group with name "AccessGroup"
    And I create the group with name "NewAccessGroup" and description "description"
    Then I find the group with name "NewAccessGroup"
    Then No exception was thrown

  Scenario: Creating non-unique Access Group with unique description
  Create an Access Group with valid name and description. Then create a new Access Group with the same name but different description.
  Kapua should return an error
    Given I create the group with name "NewAccessGroup" and description "description"
    And I expect the exception "KapuaDuplicateNameException" with the text "*"
    And I create the group with name "NewAccessGroup" and description "groupDescription"
    Then An exception was thrown

  Scenario: Creating unique Access Group with numbers in description
  Create an Access Group with unique name and with numbers in description. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "description123"
    When I search for the group with description "description123"
    Then I find the group with description "description123"
    And No exception was thrown

  Scenario: Creating unique Access Group with only numbers in description
  Create an Access Group with unique name and with only numbers in description. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "12345"
    When I search for the group with description "12345"
    Then I find the group with description "12345"
    And No exception was thrown

  Scenario: Creating unique Access Group with special symbols in description
  Create an Access Group with unique name and with special symbols in description. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I try to create the group with special characters "!#$%&'()=»Ç>:;<.,⁄@‹›€*ı–°·‚±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«" in description
    Then I find the group with name "GroupName53"
    And No exception was thrown

  Scenario: Creating an Access Group without name and with description
  Create an Access Group without name and with description.
  Kapua should return an error.
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create the group with name "" and description "description"
    Then An exception was thrown

  Scenario: Creating unique Access Group with short description
  Create unique Access Group with short description, description that contains 3 characters.
  Kapua should not return any error.
    Given I create the group with name "NewAccessGroup" and description "a"
    When I search for the group with description "a"
    Then I find the group with description "a"
    And No exception was thrown

  Scenario: Creating unique Access Group with long description
  Create unique Access Group with long description, description that contains 255 characters.
  Kapua should not return any error.
    Given I create the group with name "NewAccessGroup" and description "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    When I search for the group with description "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    Then I find the group with description "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    And No exception was thrown

#This unit test will be commented until issue #2800 is fixed
#  Scenario: Creating unique Access Group with too long description
 # Create unique Access Group with too long description, description that contains more than 255 characters.
  #Kapua should not return any error.
   # Given I create the group with name "NewAccessGroup" and description "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroup"
    #When I search for the group with description "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroup"
    #Then I find the group with description "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroup"
    #And No exception was thrown

  Scenario: Editing Access Group name to valid one
  Create an Access Group with valid name. Edit the group name to different one, and try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description ""
    When I update the group name from "NewAccessGroup" to "NewGroup"
    And I search for the group with name "NewGroup"
    Then I find the group with name "NewGroup"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to non-unique one
  Create two Access Groups with different valid names. Edit one name of one group to the same name of the second group.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup" and description ""
    And I create a group with name "AccessGroup"
    And I expect the exception "KapuaDuplicateNameException" with the text "*"
    When I update the group name from "AccessGroup" to "NewAccessGroup"
    Then An exception was thrown

  Scenario: Editing Access Group name to short one
  Create an Access Group with valid name. Edit the group name to short one, name that contains 3 characters, and try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description ""
    When I update the group name from "NewAccessGroup" to "New"
    And I search for the group with name "New"
    Then I find the group with name "New"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to too short one
  Create an Access Group with valid name. Edit the group name to too short one, name that contains less than 3 characters.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup" and description ""
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I update the group name from "NewAccessGroup" to "NG"
    Then An exception was thrown

  Scenario: Editing Access Group name to a long one
  Create an Access Group with valid name. Edit the group name to long one, name that contains 255 characters, and try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description ""
    When I update the group name from "NewAccessGroup" to "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    And I search for the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    Then I find the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to a too long one
  Create an Access Group with valid name. Edit the group name to too long one, name that contains more than 255 characters.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup" and description ""
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I update the group name from "NewAccessGroup" to "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroup"
    Then An exception was thrown

  Scenario: Editing Access Group name to name with valid special symbols
  Create an Access Group with valid name. Edit the group name to contain valid special symbols: "_", "-" and " ". Try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description ""
    When I update the group name from "NewAccessGroup" to "New Access_Group-ag"
    And I search for the group with name "New Access_Group-ag"
    Then I find the group with name "New Access_Group-ag"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to name with invalid special symbols in name
  Create an Access Group with valid name. Edit the group name to name with invalid special symbols in name, all except "_", "-" and " ". Use one symbol every time.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup" and description ""
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I update the group name from "NewAccessGroup" to name with special characters "!#$%&'()=»Ç>:;<.,⁄@‹›€*ı–°·‚±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«"
    Then An exception was thrown

  Scenario: Editing Access Group name to empty name
  Create an Access Group with valid name. Edit the group name to empty name.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup" and description ""
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I update the group name from "NewAccessGroup" to ""
    Then An exception was thrown

  Scenario: Editing Access Group name to name that contains numbers
  Create an Access Group with valid name. Edit the group name to contain numbers. Try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description ""
    When I update the group name from "NewAccessGroup" to "Group123"
    And I search for the group with name "Group123"
    Then I find the group with name "Group123"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to name that contains only numbers
  Create an Access Group with valid name. Edit the group name to contain only numbers. Try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description ""
    When I update the group name from "NewAccessGroup" to "1234"
    And I search for the group with name "1234"
    Then I find the group with name "1234"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group description to unique one
  Create an Access Group with unique name and with unique description. Once created, search for that description - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "description"
    When I update the group description from "description" to "NewDescription"
    When I search for the group with description "NewDescription"
    Then I find the group with description "NewDescription"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group description to non-unique one
  Create two Access Groups with different valid names and different description. Edit description of one group to the same description of the second group.
  Kapua should not return any errors.
    Given I create the group with name "AccessGroup" and description "description"
    And I create the group with name "NewAccessGroup" and description "description2"
    Then I update the group description from "description2" to "description"
    Then No exception was thrown

  Scenario: Editing Access Group description to description with special symbols
  Create unique Access Group with description. Edit the group description to contain valid special symbols, enter all possible characters.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "description"
    When I update the group description to description with special characters "!#$%&'()=»Ç>:;<.,⁄@‹›€*ı–°·‚±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«"
    Then No exception was thrown

  Scenario: Editing Access Group description to description with numbers
  Create unique Access Group with description. Edit the group description to contain numbers.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "description"
    When I update the group description from "description" to "description123"
    Then The group was correctly updated
    Then No exception was thrown

  Scenario: Editing Access Group description to description with only numbers
  Create unique Access Group with description. Edit the group description to contain only numbers.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "description"
    When I update the group description from "description" to "12345"
    Then The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group description to short description
  Create unique Access Group with description. Edit the group description to short description, description that contains 3 characters.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "description"
    When I update the group description from "description" to "a"
    Then The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group description to long description
  Create unique Access Group with description. Edit the group description to long description, description that contains 255 characters.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup" and description "description"
    When I update the group description from "description" to "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    Then The group was correctly updated
    And No exception was thrown

#This unit test will be commented until issue #2800 is fixed
  #Scenario: Editing Access Group description to too long description
  #Create unique Access Group with description. Edit the group description to too long description, description that contains more than 255 characters.
  #Kapua should not return any errors.
    #Given I create the group with name "NewAccessGroup" and description "description"
    #When I update the group description from "description" to "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroup"
    #Then The group was correctly updated
    #And No exception was thrown

  Scenario: Deleting an existing Access Group
  Create an Access Group with valid name. Then delete it and try to find that Access Group.
  Group with that name should not be found.
    Given I create the group with name "NewAccessGroup" and description "description"
    When I try to delete the group with name "NewAccessGroup"
    And I search for the group with name "NewAccessGroup"
    Then No group was found

  Scenario: Deleting a non-existing Access Group
  Create an Access Group with valid name, then try to delete that Access Group twice.
  Kapua should return an error on the second try.
    Given I create the group with name "NewAccessGroup" and description "description"
    And I try to delete the group with name "NewAccessGroup"
    Given I expect the exception "KapuaEntityNotFoundException" with the text "*"
    Then I try to delete the group with name "NewAccessGroup"
    And An exception was thrown

  Scenario: Deleting an existing Access Group and creating it again with the same name
  Create an Access Group with valid name. Then delete it and create new Access Group with the same name.
  Kapua should not return any error
    Given I create the group with name "NewAccessGroup" and description "description"
    When I try to delete the group with name "NewAccessGroup"
    Given I create the group with name "NewAccessGroup" and description "description"
    When I search for the group with name "NewAccessGroup"
    Then I find the group with name "NewAccessGroup"
    And No exception was thrown
