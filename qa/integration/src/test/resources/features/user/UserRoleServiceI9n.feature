###############################################################################
# Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@user
@integration
@userRole
Feature: User role service integration tests

  Scenario: Start datastore for all scenarios

    Given Start Datastore

  Scenario: Start event broker for all scenarios

    Given Start Event Broker

  Scenario: Adding existing roles to user
    Adding user more than one role

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And I create user with name "user1"
    And Credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I configure the role service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And I create the domain
      | name | actions             |
      | test | read,write, execute |
    And The permissions "read, write, execute"
    And I create the access info entity
    And I create the roles
      | name  |
      | role1 |
      | role2 |
      | role3 |
    And I add permissions to the role
    And I add access roles to user
    Then Access role with name "role3" is found
    And I logout

  Scenario: Add user permissions to the role
  Creating user "user1" and role "test_role", adding permissions with user domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to find, delete or create users as user "user1"

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And I create user with name "user1"
    And Credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "user"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permission
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role to user
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create user with name "new-user"
    And I find user with name "new-user"
    And I try to edit user to name "new-user"
    And I try to delete user "new-user"
    Then No exception was thrown
    And I logout

  Scenario: Add device permissions to the role
  Creating user "user1" and role "test_role", adding permissions with device domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to find, delete or create devices as user "user1"

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And I create user with name "user1"
    And Credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "device"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permission
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role to user
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create a device with name "device1"
    And I find device with client id "device1"
    And I try to edit device to clientId "device2"
    And I delete the device with the client id "device1"
    Then No exception was thrown
    And I logout

  Scenario: Add device event permissions to the role
  Creating user "user1" and role "test_role", adding permissions with device and device_event domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to find device events"

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Scope with ID 1
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | user1 | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I select the domain "device"
    And I create the following role permission
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I select the domain "account"
    And I create the following role permission
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role to user
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And A birth message from device "device_1"
    And I expect the exception "SubjectUnauthorizedException" with the text "Missing permission: device_event:read:"
    When I search for events from device "device_1" in account "kapua-sys"
    Then An exception was thrown
    And I logout
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select the domain "device_event"
    And I create the following role permission
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I logout
    Given I login as user with name "user1" and password "User@10031995"
    When I search for events from device "device_1" in account "kapua-sys"
    Then I find 1 device events
    And The type of the last event is "BIRTH"
    And No exception was thrown
    And I logout

  Scenario: Stop broker after all scenarios

    Given Stop Broker

  Scenario: Stop event broker for all scenarios

    Given Stop Event Broker

  Scenario: Stop datastore after all scenarios

    Given Stop Datastore












