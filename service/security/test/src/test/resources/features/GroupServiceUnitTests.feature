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
@group
@unit

Feature: Access Groups

  Scenario: Creating a valid Access Group
  Create an Access Group with valid name. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup"
    When I search for the group with name "NewAccessGroup"
    Then I find the group with name "NewAccessGroup"
    And No exception was thrown

  Scenario: Creating an Access Group with empty name
  Create an Access Group with null name.
  Kapua should return an errors.
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create the group with name " "
    Then An exception was thrown

  Scenario: Creating non-unique Access Group
  Create an Access Group with valid name. Then create a new Access Group with the same name.
  Kapua should return an error
    Given I create the group with name "NewAccessGroup"
    And I expect the exception "KapuaDuplicateNameException" with the text "*"
    And I create the group with name "NewAccessGroup"
    Then An exception was thrown

  Scenario: Creating a Access Group with short name
  Create an Access Group with short but still valid name, name that contains 3 characters.
  Kapua should not return any error.
    Given I create the group with name "abc"
    When I search for the group with name "abc"
    Then I find the group with name "abc"
    And No exception was thrown

  Scenario: Creating a Access Group with too short name
  Create an Access Group with too short name, name that contains less than 3 characters.
  Kapua should return an error.
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create the group with name "ag"
    Then An exception was thrown

  Scenario: Creating a Access Group with long name
  Create an Access Group with long but still valid name, name that contains 255 characters.
  Kapua should not return any error.
    Given I create the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    When I search for the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    Then I find the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    And No exception was thrown

  Scenario: Creating a Access Group with too long name
  Create an Access Group with too long name, name that contains more than 255 characters (e.g. 267).
  Kapua should return an error.
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroup"
    Then An exception was thrown

  Scenario: Creating a valid Access Group with valid special symbols in name
  Create an Access Group with valid name that contains valid special symbols. Valid special symbols for name are: "_", "-" and " "
  Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "New Access_Group-ag"
    When I search for the group with name "New Access_Group-ag"
    Then I find the group with name "New Access_Group-ag"
    And No exception was thrown

  Scenario: Creating a Access Group with invalid special symbols in name
  Create an Access Group with with invalid special symbols in name, all except "_", "-" and " ". Use one symbol every time.
  Kapua should return an error.
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create the group with name "NewAccessGroup%"
    Then An exception was thrown

  Scenario: Creating a valid Access Group with numbers in name
  Create an Access Group with name that contain numbers. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "Group123"
    When I search for the group with name "Group123"
    Then I find the group with name "Group123"
    And No exception was thrown

  Scenario: Creating a valid Access Group with only numbers in name
  Create an Access Group with name that contain only numbers. Once created, search for it - it should have been created.
  Kapua should not return any errors.
    Given I create the group with name "1234"
    When I search for the group with name "1234"
    Then I find the group with name "1234"
    And No exception was thrown

  Scenario: Editing Access Group name to valid one
  Crete an Access Group with valid name. Edit the group name to different one, and try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup"
    When I update the group name to "NewGroup"
    And I search for the group with name "NewGroup"
    Then I find the group with name "NewGroup"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name non-unique one
  Crete two Access Groups with different valid names. Edit one name of one group to the same name of the second group.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup"
    And I create the group with name "AccessGroup"
    And I expect the exception "KapuaDuplicateNameException" with the text "*"
    When I update the group name to "NewAccessGroup"
    Then An exception was thrown

  Scenario: Editing Access Group name to short one
  Crete an Access Group with valid name. Edit the group name to short one, that contains 3 characters, and try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup"
    When I update the group name to "New"
    And I search for the group with name "New"
    Then I find the group with name "New"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to too short one
  Crete an Access Group with valid name. Edit the group name to a too short one, name that contains less than 3 characters.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I update the group name to "NG"
    Then An exception was thrown

  Scenario: Editing Access Group name to a long one
  Crete an Access Group with valid name. Edit the group name to long one, that contains 255 characters, and try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup"
    When I update the group name to "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    And I search for the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    Then I find the group with name "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNA"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to a too long one
  Crete an Access Group with valid name. Edit the group name to too long one, name that contains more than 255 characters.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I update the group name to "NewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroupNewAccessGroup"
    Then An exception was thrown

  Scenario: Editing Access Group name to name that contains valid special symbols
  Crete an Access Group with valid name. Edit the group name to contain valid special symbols: "_", "-" and " ". Try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup"
    When I update the group name to "New Access_Group-ag"
    And I search for the group with name "New Access_Group-ag"
    Then I find the group with name "New Access_Group-ag"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to name with invalid special symbols in name
  Crete an Access Group with valid name. Edit the group name to name with with invalid special symbols in name, all except "_", "-" and " ". Use one symbol every time.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I update the group name to "NewAccessGroup@"
    Then An exception was thrown

  Scenario: Editing Access Group name to empty name
  Crete an Access Group with valid name. Edit the group name to empty name.
  Kapua should return an error.
    Given I create the group with name "NewAccessGroup"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I update the group name to " "
    Then An exception was thrown

  Scenario: Editing Access Group name to name that contains numbers
  Crete an Access Group with valid name. Edit the group name to contain numbers. Try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup"
    When I update the group name to "Group123"
    And I search for the group with name "Group123"
    Then I find the group with name "Group123"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Editing Access Group name to name that contains numbers
  Crete an Access Group with valid name. Edit the group name to contain only numbers. Try to search for it - it should have been changed.
  Kapua should not return any errors.
    Given I create the group with name "NewAccessGroup"
    When I update the group name to "1234"
    And I search for the group with name "1234"
    Then I find the group with name "1234"
    And The group was correctly updated
    And No exception was thrown

  Scenario: Deleting an existing Access Group
  Crete an Access Group with valid name. Then delete it and try to find that Access Group.
  Group with that name should not be find.
    Given I create the group with name "NewAccessGroup"
    When I delete the group with name "NewAccessGroup"
    And I search for the group with name "NewAccessGroup"
    Then No group was found

  Scenario: Deleting a non-existing Access Group
  Create an Access Group with valid name, then try to delete that Access Group twice.
  Kapua should return an error on the second try.
    Given I create the group with name "NewAccessGroup"
    And I delete the group with name "NewAccessGroup"
    Given I expect the exception "KapuaEntityNotFoundException" with the text "*"
    Then I delete the group with name "NewAccessGroup"
    And An exception was thrown

  Scenario: Deleting an existing Access Group and creating it again with the same name
  Crete an Access Group with valid name. Then delete it and create new Access Group with the same name.
  Kapua should not return any error
    Given I create the group with name "NewAccessGroup"
    When I delete the group with name "NewAccessGroup"
    Given I create the group with name "NewAccessGroup"
    When I search for the group with name "NewAccessGroup"
    Then I find the group with name "NewAccessGroup"
    And No exception was thrown










