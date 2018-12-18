###############################################################################
# Copyright (c) 2017 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@security
@integration
Feature: Group Service tests

  Scenario: Count groups in a blank database
  The default group table must be empty.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I count the group entries in the database
    Then I count 0
    And I logout

  Scenario: Regular group in root scope
  Create a regular group entry. The newly created entry must match the
  creator parameters.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 1       | 1        |
      | integer | maxNumberChildEntities | 5     | 1       | 1        |
    Given I create the group
      | scope | name        |
      | 1     | test_name_1 |
    Then A group was created
    And The group matches the creator
    And I logout

  Scenario: Regular group in random scope
  Create a regular group entry. The newly created entry must match the
  creator parameters.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 1234    | 1        |
      | integer | maxNumberChildEntities | 5     | 1234    | 1        |
    Given I create the group
      | scope | name        |
      | 1234  | test_name_1 |
    Then A group was created
    And The group matches the creator
    And I logout

  Scenario: Duplicate group name in root scope
  Create a regular group entry. The newly created entry must match the
  creator parameters. Try to create a second group entry with the same name.
  An exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I create the group
      | scope | name        |
      | 0     | test_name_2 |
    Then A group was created
    And The group matches the creator
    Given I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name test_name_2 already exists."
    When I create the group
      | scope | name        |
      | 0     | test_name_2 |
    Then An exception was thrown
    And I logout

  Scenario: Group with a null name
  Try to create a second group entry with a null name.
  An exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 1234    | 1        |
      | integer | maxNumberChildEntities | 5     | 1234    | 1        |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    Given I create the group
      | scope |
      | 1234  |
    Then An exception was thrown
    And I logout

  Scenario: Update a group entry in the database
  Create a regular group entry. Change the entry name. The updated entry
  parameters should match the original group.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I create the group
      | scope | name        |
      | 0     | test_name_3 |
    Then A group was created
    And The group matches the creator
    When I update the group name to "test_name_new"
    Then The group was correctly updated
    And I logout

  Scenario: Update a group entry with a false ID
  Create a regular group entry. Modify the group object ID to a random value.
  Trying to update such an group object should raise an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I create the group
      | scope | name        |
      | 0     | test_name_4 |
    Then A group was created
    And The group matches the creator
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type group"
    When I update the group with an incorrect ID
    Then An exception was thrown
    And I logout

  Scenario: Find a group entry in the database
  It must be possible to find a specific group entry based on the group
  entry ID.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I create the group
      | scope | name        |
      | 0     | test_name_5 |
    Then A group was created
    And The group matches the creator
    When I search for the last created group
    Then The group was correctly found
    And I logout

  Scenario: Delete a group from the database
  It must be possible to delete a group entry from the database. In this test
  the last created entry is deleted.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I create the groups
      | scope | name        |
      | 0     | test_name_6 |
      | 0     | test_name_7 |
    When I delete the last created group
    And I search for the last created group
    Then No group was found
    And I logout

  Scenario: Delete a group from the database - Unknown group ID
  Trying to delete a group record that does not exist (unknown group ID) should
  raise an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type group"
    When I try to delete a random group id
    Then An exception was thrown
    And I logout

  Scenario: Count groups
  It must be possible to count all the groups belonging to a certain scope.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 1       | 1        |
      | integer | maxNumberChildEntities | 5     | 1       | 1        |
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 2       | 1        |
      | integer | maxNumberChildEntities | 5     | 2       | 1        |
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 3       | 1        |
      | integer | maxNumberChildEntities | 5     | 3       | 1        |
    Given I create the groups
      | scope | name         |
      | 0     | test_name_8  |
      | 1     | test_name_9  |
      | 2     | test_name_10 |
      | 2     | test_name_11 |
      | 2     | test_name_12 |
      | 3     | test_name_13 |
      | 3     | test_name_14 |
      | 0     | test_name_15 |
    When I count all the groups in scope 2
    Then I count 3
    When I count all the groups in scope 3
    Then I count 2
    When I count all the groups in scope 15
    Then I count 0
    And I logout

  Scenario: Query for a specific group by name
  It must be possible to query the database for a specific group by name. Since the
  scope ID / name pairs must be unique, only a single entry can be returned by such a query.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 1       | 1        |
      | integer | maxNumberChildEntities | 5     | 1       | 1        |
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 2       | 1        |
      | integer | maxNumberChildEntities | 5     | 2       | 1        |
    When I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 3       | 1        |
      | integer | maxNumberChildEntities | 5     | 3       | 1        |
    Given I create the groups
      | scope | name         |
      | 0     | test_name_16 |
      | 1     | test_name_17 |
      | 2     | test_name_18 |
      | 2     | test_name_19 |
      | 2     | test_name_20 |
      | 3     | test_name_21 |
      | 3     | test_name_22 |
      | 0     | test_name_23 |
    When I query for the group "test_name_16" in scope 0
    Then I count 1
    And The group name is "test_name_16"
    When I query for the group "test_name_18" in scope 2
    Then I count 1
    And The group name is "test_name_18"
    When I query for the group "test_name_25" in scope 1
    Then I count 0
    And I logout
