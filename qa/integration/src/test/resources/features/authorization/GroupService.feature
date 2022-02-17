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
#     Eurotech - initial API and implementation
###############################################################################
@security
@groupService
@integration

Feature: Group Service tests

Scenario: Init Security Context for all scenarios

  Given Init Security Context

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
    When I search for the group with name "test_name_5"
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
      | 1     | test_name_6 |
      | 1     | test_name_7 |
    And I search for the group with name "test_name_7"
    Then The group was found
    When I delete the group with name "test_name_7"
    And I search for the group with name "test_name_7"
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

  Scenario: Creating Unique Group Without Description
  Login as kapua-sys, go to groups, try to create a group with valid name without description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create a group with name "ValidGroup"
    Then No exception was thrown
    And I logout

  Scenario: Creating Non-unique Group Without Description
  Login as kapua-sys, go to groups, try to create a group with valid name without description. After that create another group with the same name.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create a group with name "ValidGroup"
    Then No exception was thrown
    Given I expect the exception "Exception" with the text "*"
    When I create a group with name "ValidGroup"
    Then I logout

  Scenario: Creating Group With Short Name Without Description
  Login as kapua-sys, go to groups, try to create a group with short name (3 characters).
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create a group with name "abc"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Group With Too Short Name Without Description
  Login as kapua-sys, go to groups, try to create a group with too short name (1 character).
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I expect the exception "Exception" with the text "*"
    When I create a group with name "a"
    Then An exception was thrown
    Then I logout

  Scenario: Creating Group With Long Name Without Description
  Login as kapua-sys, go to groups, try to create a group with long name (255 characters).
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create a group with name "y6Nzz2qKaF8nbRxaRo8o9mRvH8CEvldYg0kCVq7EPfutbHvaQZf7m8eHcTBiXexxYl8zZI9taDwWGnnyMbnuOmpQp4tdmXeA9tnLx6dtuhu8vIfNU9YKyROkHW6f1RnMV8NhVChvE2eeTYdtczRzmJ5Zqx2MlHWJ68vbRqZ7Jw7BttDxB0bCveEYjg0JVJGsigZBRTifJhcQnXndfMcQ7WKAxXwJplvRx6sasjLKP2ZefVTqbxZQr1tVAuvLJvX"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Group With Too Long Name Without Description
  Login as kapua-sys, go to groups, try to create a group with too long name (256 characters).
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I create a group with name "aaay6Nzz2qKaF8nbRxaRo8o9mRvH8CEvldYg0kCVq7EPfutbHvaQZf7m8eHcTBiXexxYl8zZI9taDwWGnnyMbnuOmpQp4tdmXeA9tnLx6dtuhu8vIfNU9YKyROkHW6f1RnMV8NhVChvE2eeTYdtczRzmJ5Zqx2MlHWJ68vbRqZ7Jw7BttDxB0bCveEYjg0JVJGsigZBRTifJhcQnXndfMcQ7WKAxXwJplvRx6sasjLKP2ZefVTqbxZQr1tVAuvLJvX"
    Then An exception was thrown
    Then I logout

  Scenario: Creating Group With Permitted Symbols And Numbers In Name Without Description
  Login as kapua-sys, go to groups, try to create a group with name that contains permitted symbols.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create a group with name "Valid-group-name_1_2_3"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Group Invalid Symbols In Name Without Description
  Login as kapua-sys, go to groups, try to create a group with name that contains invalid symbols.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I try to create groups with invalid characters in name
    Then An exception was thrown
    Then I logout

  Scenario: Creating Group Without a Name And Without Description
  Login as kapua-sys, go to groups, try to create a group with without a name.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I expect the exception "Exception" with the text "*"
    When I create a group with name ""
    Then An exception was thrown
    Then I logout

  Scenario: Creating Unique Group With Unique Description
  Login as kapua-sys, go to groups, try to create a group with a valid description with all possible symbols.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I create the group with name "Group1" and description "Valid_description-123 !#$%&'()=»Ç>:;<-.,⁄@‹›€*ı–°·‚_±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«◊Ñˆ¯Èˇ¿"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Unique Group With Non-unique Description
  Login as kapua-sys, go to groups, try to create a group with a non-unique description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I create the group with name "Group1" and description "Non-unique description"
    And I create the group with name "Group2" and description "Non-unique description"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Non-Unique Group With Valid Description
  Login as kapua-sys, go to groups, try to create a group with a non-unique description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I create the group with name "Group1" and description "Non-unique description"
    Given I expect the exception "Exception" with the text "*"
    When I create the group with name "Group1" and description "Non-unique description"
    Then An exception was thrown
    Then I logout

  Scenario: Creating Group With Short Name With Valid Description
  Login as kapua-sys, go to groups, try to create a group with short (3 characters) name and valid description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "abc" and description "Valid description 123"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Group With Too Short Name With Valid Description
  Login as kapua-sys, go to groups, try to create a group with too short (1 character) name and valid description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I create the group with name "a" and description "Valid description 123"
    Then An exception was thrown
    Then I logout

  Scenario: Creating Group With Long Name With Valid Description
  Login as kapua-sys, go to groups, try to create a group with long (255 characters) name and valid description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "PxYHG9FEvy3d4CKlLwoA7b5FTk7XptHr1TZoKVft4Qhxe4BwjQE5zGFn5Jh0AGSQjbAv0ooPPBCRasXk0lnjeeHiU5mWM2KxG98ziV5WIxnUaZhIpGVfV8bqhQobJtx4QrIPizEPpG0p6DCcS72ulKqnoVsusqEkDsBBadZjhZzy4NbauKnlZnAoXerKBg23ku0CDDaB1boKqrlZ4yGmoLjgSWz4GMg6SCfjYabOCkt73HOs0SlZowCCmtzb7kF" and description "Valid description 123"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Group With Too Long Name With Valid Description
  Login as kapua-sys, go to groups, try to create a group with too long (256) name and valid description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I create the group with name "aPxYHG9FEvy3d4CKlLwoA7b5FTk7XptHr1TZoKVft4Qhxe4BwjQE5zGFn5Jh0AGSQjbAv0ooPPBCRasXk0lnjeeHiU5mWM2KxG98ziV5WIxnUaZhIpGVfV8bqhQobJtx4QrIPizEPpG0p6DCcS72ulKqnoVsusqEkDsBBadZjhZzy4NbauKnlZnAoXerKBg23ku0CDDaB1boKqrlZ4yGmoLjgSWz4GMg6SCfjYabOCkt73HOs0SlZowCCmtzb7kF" and description "Valid description 123"
    Then An exception was thrown
    Then I logout

  Scenario: Creating Group With Permitted Symbols In Name With Valid Description
  Login as kapua-sys, go to groups, try to create a group with permitted symbols in name and valid description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group-1_2_3" and description "Valid description 123"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Group With Invalid Symbols In Name With Valid Description
  Login as kapua-sys, go to groups, try to create a group with invalid symbols in name and valid description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I create the group with name "Group@123" and description "Valid description 123"
    Then An exception was thrown
    Then I logout

  Scenario: Creating Group Without a Name And With Valid Description
  Login as kapua-sys, go to groups, try to create a group without a name and valid description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I create the group with name "" and description "Valid description 123"
    Then An exception was thrown
    Then I logout

  Scenario: Creating Group With Numbers In Name With Valid Description
  Login as kapua-sys, go to groups, try to create a group with name that contains numbers and valid description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1234567890" and description "Valid description 123"
    Then No exception was thrown
    When I create the group with name "1234567890" and description "Valid description 123"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Unique Group With Short Description
  Login as kapua-sys, go to groups, try to create a group with short description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "a"
    Then No exception was thrown
    Then I logout

  Scenario: Creating Unique Group With Long Description
  Login as kapua-sys, go to groups, try to create a group with long (255 characters) description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "PxYHG9FEvy3d4CKlLwoA7b5FTk7XptHr1TZoKVft4Qhxe4BwjQE5zGFn5Jh0AGSQjbAv0ooPPBCRasXk0lnjeeHiU5mWM2KxG98ziV5WIxnUaZhIpGVfV8bqhQobJtx4QrIPizEPpG0p6DCcS72ulKqnoVsusqEkDsBBadZjhZzy4NbauKnlZnAoXerKBg23ku0CDDaB1boKqrlZ4yGmoLjgSWz4GMg6SCfjYabOCkt73HOs0SlZowCCmtzb7kF"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Group's Name To Unique One
  Login as kapua-sys, go to groups, try to edit group's name to a unique one, leave description as it is.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    And I change the group name from "Group1" to "Group2"
    Then No exception was thrown
    And I logout

  Scenario: Changing Group's Name To Non-Unique One
  Login as kapua-sys, go to groups, try to edit group's name to a non-unique one, leave description as it is.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    And I create the group with name "Group2" and description "Description2"
    Given I expect the exception "KapuaDuplicateNameException" with the text "*"
    When I change the group name from "Group2" to "Group1"
    Then An exception was thrown
    Then I logout

  Scenario: Changing Group's Name To Short One Without Changing Description
  Login as kapua-sys, go to groups, try to edit group's name to a short one, leave description as it is.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    When I change the group name from "Group1" to "abc"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Group's Name To a Too Short One Without Changing Description
  Login as kapua-sys, go to groups, try to edit group's name to a too short one, leave description as it is.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I change the group name from "Group1" to "a"
    Then An exception was thrown
    Then I logout

  Scenario: Changing Group's Name To a Long One Without Changing Description
  Login as kapua-sys, go to groups, try to edit group's name to a long one, leave description as it is.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    When I change the group name from "Group1" to "PxYHG9FEvy3d4CKlLwoA7b5FTk7XptHr1TZoKVft4Qhxe4BwjQE5zGFn5Jh0AGSQjbAv0ooPPBCRasXk0lnjeeHiU5mWM2KxG98ziV5WIxnUaZhIpGVfV8bqhQobJtx4QrIPizEPpG0p6DCcS72ulKqnoVsusqEkDsBBadZjhZzy4NbauKnlZnAoXerKBg23ku0CDDaB1boKqrlZ4yGmoLjgSWz4GMg6SCfjYabOCkt73HOs0SlZowCCmtzb7kF"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Group's Name To a Too Long One Without Changing Description
  Login as kapua-sys, go to groups, try to edit group's name to a long one, leave description as it is.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I change the group name from "Group1" to "aPxYHG9FEvy3d4CKlLwoA7b5FTk7XptHr1TZoKVft4Qhxe4BwjQE5zGFn5Jh0AGSQjbAv0ooPPBCRasXk0lnjeeHiU5mWM2KxG98ziV5WIxnUaZhIpGVfV8bqhQobJtx4QrIPizEPpG0p6DCcS72ulKqnoVsusqEkDsBBadZjhZzy4NbauKnlZnAoXerKBg23ku0CDDaB1boKqrlZ4yGmoLjgSWz4GMg6SCfjYabOCkt73HOs0SlZowCCmtzb7kF"
    Then An exception was thrown
    Then I logout

  Scenario: Changing Group's Name To Contain Permitted Symbols In Name Without Changing Description
  Login as kapua-sys, go to groups, try to edit group's name to contain permitted symbols, leave description as it is.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    When I change the group name from "Group1" to "Group_1-2-3"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Group's Name To Contain Invalid Symbols In Name Without Changing Description
  Login as kapua-sys, go to groups, try to edit group's name to contain invalid symbols, leave description as it is.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I change the group name from "Group1" to "Group@123"
    Then An exception was thrown
    Then I logout

  Scenario: Deleting Group's Name And Leaving It Empty Without Changing Description
  Login as kapua-sys, go to groups, try to edit group's name to empty string, leave description as it is.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I change the group name from "Group1" to ""
    Then An exception was thrown
    Then I logout

  Scenario: Editing Group's Name To Contain Numbers Without Changing Description
  Login as kapua-sys, go to groups, try to edit group's name to one that contains numbers, leave description as it is.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group" and description "Description"
    When I change the group name from "Group" to "1234567890"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Group's Description To Uniqe One
  Login as kapua-sys, go to groups, try to edit group's description, name should remain unchanged.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group" and description "Description"
    And I change the description of group with name "Group" to "Description 123"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Group's Description To Non-Uniqe One
  Login as kapua-sys, go to groups, try to edit group's description to a non-unique one, name should remain unchanged.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group1" and description "Description1"
    And I create the group with name "Group2" and description "Description2"
    And I change the description of group with name "Group2" to "Description1"
    Then I logout

  Scenario: Changing Description On Group With Short Name
  Login as kapua-sys, go to groups, try to edit description on a group that has short name.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "abc" and description "Description1"
    When I change the description of group with name "abc" to "Description 2"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Description On Group With Long Name
  Login as kapua-sys, go to groups, try to edit description on a group that has long name.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "PxYHG9FEvy3d4CKlLwoA7b5FTk7XptHr1TZoKVft4Qhxe4BwjQE5zGFn5Jh0AGSQjbAv0ooPPBCRasXk0lnjeeHiU5mWM2KxG98ziV5WIxnUaZhIpGVfV8bqhQobJtx4QrIPizEPpG0p6DCcS72ulKqnoVsusqEkDsBBadZjhZzy4NbauKnlZnAoXerKBg23ku0CDDaB1boKqrlZ4yGmoLjgSWz4GMg6SCfjYabOCkt73HOs0SlZowCCmtzb7kF" and description "Description1"
    When I change the description of group with name "PxYHG9FEvy3d4CKlLwoA7b5FTk7XptHr1TZoKVft4Qhxe4BwjQE5zGFn5Jh0AGSQjbAv0ooPPBCRasXk0lnjeeHiU5mWM2KxG98ziV5WIxnUaZhIpGVfV8bqhQobJtx4QrIPizEPpG0p6DCcS72ulKqnoVsusqEkDsBBadZjhZzy4NbauKnlZnAoXerKBg23ku0CDDaB1boKqrlZ4yGmoLjgSWz4GMg6SCfjYabOCkt73HOs0SlZowCCmtzb7kF" to "Description 123"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Description On Group With Permitted Symbols
  Login as kapua-sys, go to groups, try to edit description on a group that has name with permited symbols.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group-1_2_3" and description "Description1"
    When I change the description of group with name "Group-1_2_3" to "Description2"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Description On Group With Numbers In Name
  Login as kapua-sys, go to groups, try to edit description on a group that has numbers in name.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group123" and description "Description1"
    When I change the description of group with name "Group123" to "Description2"
    Then No exception was thrown
    When I create the group with name "1234567890" and description "Description1"
    When I change the description of group with name "1234567890" to "Description2"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Description On Group With Short Description
  Login as kapua-sys, go to groups, try to edit description on a group that has short description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group-1_2_3" and description "a"
    When I change the description of group with name "Group-1_2_3" to "b"
    Then No exception was thrown
    Then I logout

  Scenario: Changing Description On Group With Long Description
  Login as kapua-sys, go to groups, try to edit description on a group that to long description (255 characters).
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create the group with name "Group-1_2_3" and description "Valid description"
    When I change the description of group with name "Group-1_2_3" to "PxYHG9FEvy3d4CKlLwoA7b5FTk7XptHr1TZoKVft4Qhxe4BwjQE5zGFn5Jh0AGSQjbAv0ooPPBCRasXk0lnjeeHiU5mWM2KxG98ziV5WIxnUaZhIpGVfV8bqhQobJtx4QrIPizEPpG0p6DCcS72ulKqnoVsusqEkDsBBadZjhZzy4NbauKnlZnAoXerKBg23ku0CDDaB1boKqrlZ4yGmoLjgSWz4GMg6SCfjYabOCkt73HOs0SlZowCCmtzb7kF"
    Then No exception was thrown
    Then I logout

  Scenario: Deleting Existing Group
  Login as kapua-sys, go to groups, create a group, and delete it.
  Check if group has been deleted.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create a group with name "Group1"
    And I delete the group with name "Group1"
    And I search for a group named "Group1"
    Then No group was found
    And I logout

  Scenario: Deleting Unexisitng Group
  Login as kapua-sys, go to groups, create a group, and delete it and then try to delete it once again.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create a group with name "Group1"
    And I delete the group with name "Group1"
    And I search for a group named "Group1"
    Then No group was found
    Given I expect the exception "NullPointerException" with the text "*"
    When I delete the group with name "Group1"
    Then An exception was thrown
    And I logout

  Scenario: Deleting Existing Group And Creating It Again With Same Name
  Login as kapua-sys, go to groups, create a group, delete it and then create it again with the same name.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    When I create a group with name "Group1"
    And I delete the group with name "Group1"
    And I search for a group named "Group1"
    Then No group was found
    When I create a group with name "Group1"
    And I search for a group named "Group1"
    Then The group was found
    And No exception was thrown
    And I logout

  Scenario: Adding Regular Group Without Description to Device
  Login as kapua-sys, go to groups, create a group.
  Go to devices, create a device and add created group to it.
  Check if created device is in Asigned Devices of the group.
  Kapua should not throw any errors

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create a group with name "Group1"
    And I create a device with name "Device1"
    And I add device "Device1" to group "Group1"
    Then Device "Device1" is in Assigned Devices of group "Group1"
    And No exception was thrown
    And I logout

  Scenario: Adding Same Regular Group Without Description to Device
  Login as kapua-sys, go to groups, create a group.
  Go to devices, create a device ("Device1") and add created group to it.
  Check if created device is in Asigned Devices of the group.
  Create another group and add it to "Device1"
  Check both groups Assigned Devices ("Device1" should only be in second group)
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create a group with name "Group1"
    And I create a device with name "Device1"
    And I add device "Device1" to group "Group1"
    Then Device "Device1" is in Assigned Devices of group "Group1"
    When I create a group with name "Group2"
    And I add device "Device1" to group "Group2"
    Then Device "Device1" is in Assigned Devices of group "Group2"
    Then Device "Device1" is not in Assigned Devices of group "Group1"
    And No exception was thrown
    And I logout

  Scenario: Adding Regular Device to a Group Without a Description
  Login as kapua-sys, go to groups, create a group.
  Go to devices, create a device and add it to newly created group.
  Check if created device is in Assigned Devices of the group.
  Remove group from the selected group.
  Check group's Assigned Devices - "device1" should be part of this group.
  Kapua should not throw any errors

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create a group with name "Group1"
    And I create a device with name "Device1"
    And I add device "Device1" to group "Group1"
    Then Device "Device1" is in Assigned Devices of group "Group1"
    When I remove device "Device1" from all groups
    Then Device "Device1" is not in Assigned Devices of group "Group1"
    And No exception was thrown
    And I logout

  Scenario: Have Two Devices in The Same Group Without Description
  Login as kapua-sys, go to groups, create a group.
  Go to devices, create two devices and add them to created group.
  Check if created device is in Assigned Devices of the group.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create a group with name "Group1"
    And I create a device with name "Device1"
    And I add device "Device1" to group "Group1"
    And I create a device with name "Device2"
    And I add device "Device2" to group "Group1"
    Then Device "Device1" is in Assigned Devices of group "Group1"
    Then Device "Device2" is in Assigned Devices of group "Group1"
    And No exception was thrown
    And I logout

  Scenario: Assign 100 Devices to One Group
  Login as kapua-sys, go to groups, create a group "Group1".
  Go to devices, create 100 devices and add them to created group.
  Check if created devices are in Assigned Devices of the group.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create a group with name "Group1"
    And I create 100 devices and add them to group "Group1"
    Then Device "Device00" is in Assigned Devices of group "Group1"
    Then Device "Device55" is in Assigned Devices of group "Group1"
    Then Device "Device99" is in Assigned Devices of group "Group1"
    Then No exception was thrown
    And I logout

  Scenario: Adding Regular Device to a Group With Description
  Login as kapua-sys, go to groups, create a group with description.
  Go to devices, create a device and add it to newly created group.
  Check if created device is in Assigned Devices of the group.
  Kapua should not throw any errors

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create the group with name "Group1" and description "Description 1234"
    And I create a device with name "Device1"
    And I add device "Device1" to group "Group1"
    Then Device "Device1" is in Assigned Devices of group "Group1"
    And No exception was thrown
    And I logout

  Scenario: Moving Device From One Group With Description to Another
  Login as kapua-sys, go to groups, create a group.
  Go to devices, create a device ("Device1") and add it to the created group.
  Check if created device is in Assigned Devices of the group.
  Create another group and add "Device1" to it.
  Check both groups Assigned Devices ("Device1" should only be in second group)
  Kapua should throw Exception when checking for device in "Group1"

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the group service
      | type    | name                   | value | scopeId | parentId |
      | boolean | infiniteChildEntities  | true  | 0       | 1        |
      | integer | maxNumberChildEntities | 5     | 0       | 1        |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create the group with name "Group1" and description "Description 123"
    And I create a device with name "Device1"
    And I add device "Device1" to group "Group1"
    Then Device "Device1" is in Assigned Devices of group "Group1"
    When I create the group with name "Group2" and description "Description 123"
    And I add device "Device1" to group "Group2"
    Then Device "Device1" is in Assigned Devices of group "Group2"
    Then Device "Device1" is not in Assigned Devices of group "Group1"
    And No exception was thrown
    And I logout

  Scenario: Reset Security Context for all scenarios

    Given Reset Security Context
