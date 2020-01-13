###############################################################################
# Copyright (c) 2019 Eurotech and/or its affiliates and others
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
@userRole
@integration

Feature: User role service integration tests

  Scenario: Start datastore for all scenarios

    Given Start Datastore

  Scenario: Start event broker for all scenarios

    Given Start Event Broker

  Scenario: Adding existing roles to user
  Adding several different roles to one user

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
    And I add credentials
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
    And I add access roles to user "user1"
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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "user"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "device"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create a device with name "device1"
    And I find device with clientId "device1"
    And I try to edit device to clientId "device2"
    And I delete the device with the clientId "device1"
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
    And I add credentials
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
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I select the domain "account"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And A birth message from device "device_1"
    And I expect the exception "SubjectUnauthorizedException" with the text "Missing permission: device_event:read:"
    When I search for events from device "device_1" in account "kapua-sys"
    Then An exception was thrown
    And I logout
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select the domain "device_event"
    And I create the following role permissions
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

  Scenario: Add group permissions to the role
  Creating user "user1" and role "test_role", adding permissions with group domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to find, delete or create groups as user "user1"

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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "group"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create the group with name "group1"
    And I find the group with name "group1"
    And I update the group name to "group2"
    And I delete the group with name "group2"
    Then No exception was thrown
    And I logout

  Scenario: Add role permissions to the role
  Creating user "user1" and role "test_role", adding permissions with role domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to find, delete or create roles as user "user1"

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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "role"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create the following role
      | scopeId | name       |
      | 1       | test_role1 |
    And I find a role with name "test_role"
    And I update the last created role name to "role-ana"
    And I delete the role with name "test_role"
    Then No exception was thrown
    And I logout

  Scenario: Add tag permissions to the role
  Creating user "user1" and role "test_role", adding permissions with tag domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to find, delete or create tags as user "user1"

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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "tag"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create a tag with name "tag1"
    And I find a tag with name "tag1"
    And I try to edit tag to name "tag2"
    And I delete the tag with name "tag2"
    Then No exception was thrown
    And I logout

  Scenario: Add account permissions to the role
  Creating user "user1" and role "test_role", adding permissions with account domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to find, delete or create accounts as user "user1"

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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "account"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create an account with name "account1", organization name "organization" and email adress "organization@gmail.com"
    And I find account with name "account1"
    And I try to edit description to "account in child account"
    And I delete account "account1"
    Then No exception was thrown
    And I logout

  Scenario: Add job permissions to the role
  Creating user "user1" and role "test_role", adding permissions with job domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to find, delete or create jobs as user "user1"

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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "job"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create a job with the name "job1"
    And I find a job with name "job1"
    And I try to edit job to name "job2"
    And I try to delete the job with name "job2"
    Then No exception was thrown
    And I logout

  Scenario: Add domain, user and access_info permissions to the role
  Creating user "user1" and role "test_role", adding permissions with domain, access_info and user domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to add permission as user "user1"

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
    And User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | user1 | Kapua User B | kapua_b@kapua.com | +386 31 323 555 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I select the domain "domain"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I select the domain "access_info"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I select the domain "user"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I create the domain
      | name | actions             |
      | test | read,write, execute |
    And The permissions "read, write, execute"
    And I create the permissions
    Then No exception was thrown
    And I logout

  Scenario: Add datastore permissions to the role
  Creating user "user1" and role "test_role", adding permissions with tag datastore and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to work with data as "user1"

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I select account "kapua-sys"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And I create user with name "user1"
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I select the domain "device"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And The device "test-device-1"
    And I expect the exception "SubjectUnauthorizedException" with the text "Missing permission: datastore:read:"
    And I search for data message with id "fake-id"
    And I logout
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select the domain "datastore"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I logout
    When I login as user with name "user1" and password "User@10031995"
    And I search for data message with id "fake-id"
    Then I don't find message
    And No exception was thrown
    And I logout

  Scenario: Delete access role from user
  Login as kapua-sys user, adding role and create access role.
  If user deletes existing role, access role has to be null.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I create user with name "user1"
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I add access role "test_role" to user "user1"
    Then Access role with name "test_role" is found
    When I delete the role with name "test_role"
    Then Access role is not found
    And I logout

  Scenario: Add deleted role again
  First add deleted role, and after that create access role

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I create user with name "user1"
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    When I add access role "test_role" to user "user1"
    Then Access role with name "test_role" is found
    And I logout

  Scenario: Delete permissions from role
  After access role permissions are removed, user also should not have that permissions

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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "user"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
    And I add access role "test_role" to user "user1"
    And I delete role permissions
    And I logout
    And I login as user with name "user1" and password "User@10031995"
    And I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission to perform this action. Missing permission: user:write:1:*. Please perform a new login to refresh users permissions."
    And I create user with name "user-test"
    Then An exception was thrown
    And I logout


  Scenario: Add same role to user twice
  If user tries to add same role two times, he gets exception

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
    And I create the access info entity
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I add access role "test_role" to user "user1"
    And I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name test_role already exists."
    And I add access role "test_role" to user "user1"
    And An exception was thrown
    Then I logout

  Scenario: Add same permission twice to the same role
  If user wants to add user:read permission two times to the same role,
  than exception was thrown

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
    And I select the domain "user"
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
    Then No exception was thrown
    And I expect the exception "KapuaEntityUniquenessException"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
    Then An exception was thrown
    And I logout

  Scenario: Add scheduler permissions to the role
  Creating user "user1" and role "test_role", adding permissions with scheduler domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to work with schedules as "user1"

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I select account "kapua-sys"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And I create user with name "user1"
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I select the domain "job"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I select the domain "scheduler"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I find scheduler properties with name "Interval Job"
    And A regular trigger creator with the name "TestSchedule" and following properties
      | name     | type              | value |
      | interval | java.lang.Integer | 1     |
    And I try to create a new trigger entity from the existing creator
    And I try to edit trigger name "TestSchedule1"
    And I try to delete last created trigger
    Then No exception was thrown
    And I logout
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I delete the last created role permissions
    And I logout
    Given I login as user with name "user1" and password "User@10031995"
    And I expect the exception "SubjectUnauthorizedException" with the text "Missing permission: scheduler:write:"
    And I try to create a new trigger entity from the existing creator
    Then An exception was thrown
    And I logout

  Scenario: Add endpoint_info permissions to the role
  Creating user "user1" and role "test_role", adding permissions with endpoint_info domain and read, write and delete actions to the "test_role",
  adding "test_role" to "user1" and after that trying to work with endpoints as "user1"

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
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I create endpoint with schema "endpoint1", domain "com" and port 8000
    And I create the access info entity
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I select the domain "endpoint_info"
    And I create the following role permissions
      | scopeId | actionName | targetScopeId |
      | 1       | read       | 1             |
      | 1       | write      | 1             |
      | 1       | delete     | 1             |
    And I add access role "test_role" to user "user1"
    And I logout
    Then I login as user with name "user1" and password "User@10031995"
    And I try to find endpoint with schema "endpoint1", domain "com" and port 8000
    Then No exception was thrown
    When I expect the exception "SubjectUnauthorizedException" with the text "Missing permission: endpoint_info:write:"
    And I create endpoint with schema "endpoint2", domain "dns" and port 20000
    Then An exception was thrown
    When I expect the exception "SubjectUnauthorizedException" with the text "Missing permission: endpoint_info:delete:"
    And I delete the endpoint with schema "endpoint1", domain "com" and port 8000
    Then An exception was thrown
    And I logout
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I delete the last created role permissions
    And I logout
    Given I login as user with name "user1" and password "User@10031995"
    And I expect the exception "SubjectUnauthorizedException" with the text "Missing permission: endpoint_info:read:"
    And I try to find endpoint with schema "endpoint1", domain "com" and port 8000
    Then An exception was thrown
    And I logout

  Scenario: Try to add two different roles with same permissions
  Adding the same permissions from user domain with read, write and delete actions to both roles.
  Afterwards add both roles to the user and try to work with Users as user "TestUser".

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I create user with name "TestUser"
    And I add credentials
      | name     | password      | enabled |
      | TestUser | User@10031995 | true    |
    And I create the access info entity
    And I create the roles
      | scopeId | name       |
      | 1       | test_role  |
      | 1       | test_role1 |
    And I select the domain "user"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access roles to user "TestUser"
    And I count the access roles from user "TestUser"
    And I count 2
    And No exception was thrown
    And I logout
    And I login as user with name "TestUser" and password "User@10031995"
    And I create user with name "TestUser1"
    And I find user with name "TestUser1"
    And I try to edit user to name "TestUser2"
    And I try to delete user "TestUser2"
    Then No exception was thrown
    And I logout

  Scenario: Add admin role to the user
  Creating user "TestUser" and adding "admin" role to him. Login as user "TestUser" and
  try to find, add, edit or delete users, devices, tags, jobs, groups, roles, endpoints.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I create user with name "TestUser"
    And I add credentials
      | name     | password      | enabled |
      | TestUser | User@10031995 | true    |
    And I search for user with name "TestUser"
    And I find user with name "TestUser"
    And I find a role with name "admin"
    And I search for the permissions of role "admin"
    And I count 1
    And I create the access info entity
    And I add access role "admin" to user "TestUser"
    And I logout
    And I login as user with name "TestUser" and password "User@10031995"
    And I create user with name "user"
    And I find user with name "user"
    And I try to edit user to name "user1"
    And I try to delete user "user1"
    And I create a device with name "TestDevice"
    And I find device with clientId "TestDevice"
    And I try to edit device to clientId "TestDevice1"
    And I delete the device with the clientId "TestDevice1"
    And I create a job with the name "TestJob"
    And I find a job with name "TestJob"
    And I try to edit job to name "TestJob1"
    And I try to delete the job with name "TestJob1"
    And I create a tag with name "Tag"
    And I find a tag with name "Tag"
    And I try to edit tag to name "Tag1"
    And I delete the tag with name "Tag1"
    And I create the following role
      | scopeId | name      |
      | 1       | test_role |
    And I find a role with name "test_role"
    And I update the last created role name to "role1"
    And I delete the role with name "test_role"
    And I create the group with name "TestGroup"
    And I find the group with name "TestGroup"
    And I update the group name to "TestGroup1"
    And I delete the group with name "TestGroup1"
    And I create endpoint with schema "TestEndpoint", domain "com" and port 8000
    And I try to find endpoint with schema "TestEndpoint", domain "com" and port 8000
    And I try to edit endpoint schema to "TestEndpoint1"
    And I delete the endpoint with schema "TestEndpoint1", domain "com" and port 8000
    Then No exception was thrown
    And I logout

  Scenario: Deleting admin role
  Login as kapua-sys user and try to delete "admin" role.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I find a role with name "admin"
    And I expect the exception "KapuaException" with the text "Operation not allowed on admin role"
    When I delete the role with name "admin"
    Then An exception was thrown
    And I logout

  Scenario: Deleting default permissions from admin role
  Login as kapua-sys user and try to find and delete default permission
  from "admin" role. An exception with proper text should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I find a role with name "admin"
    And I search for the permissions of role "admin"
    And I count 1
    And I expect the exception "KapuaException" with the text "Operation not allowed on this specific permission"
    And I delete the default admin role permission
    Then An exception was thrown
    And I search for the permissions of role "admin"
    And I count 1
    And I logout

  Scenario: Add and delete User permissions from the "admin" role
  Login as user kapua-sys, go to Roles and add User permissions with Read, Write and Delete actions to the "admin" role.
  Try to delete added permissions. After deleting, no exception should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I find a role with name "admin"
    And I search for the permissions of role "admin"
    Then I count 1
    And I select the domain "user"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I search for the permissions of role "admin"
    And I count 4
    When I delete all admin role permissions except default permission
    Then No exception was thrown
    And I count the role permissions in scope 1
    And I count 1
    And I logout

  Scenario: Add and delete Device permissions from the "admin" role
  Login as user kapua-sys, go to roles and add Device permissions with Read, Write and Delete actions to the "admin" role.
  Try to delete added permissions. After deleting, no exception should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I find a role with name "admin"
    And I search for the permissions of role "admin"
    Then I count 1
    And I select the domain "device"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I search for the permissions of role "admin"
    And I count 4
    When I delete all admin role permissions except default permission
    Then No exception was thrown
    And I search for the permissions of role "admin"
    And I count 1
    And I logout

  Scenario: Add and delete Job permissions from the "admin" role
  Login as user kapua-sys, go to Roles and add Job permissions with Read, Write, Delete and Execute actions to the "admin" role.
  Try to delete added permissions. After deleting, no exceptions should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I find a role with name "admin"
    And I search for the permissions of role "admin"
    Then I count 1
    And I select the domain "job"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
      | 1       | execute    |
    And I search for the permissions of role "admin"
    And I count 5
    When I delete all admin role permissions except default permission
    Then No exception was thrown
    And I search for the permissions of role "admin"
    And I count 1
    And I logout

  Scenario: Add and delete Account permissions from the "admin" role
  Login as user kapua-sys, go to Roles and add Account permissions with Read, Write and Delete actions to the "admin" role.
  Try to delete added permissions. After deleting, no exception should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I find a role with name "admin"
    And I search for the permissions of role "admin"
    Then I count 1
    And I select the domain "account"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I search for the permissions of role "admin"
    And I count 4
    When I delete all admin role permissions except default permission
    Then No exception was thrown
    And I search for the permissions of role "admin"
    And I count 1
    And I logout

  Scenario: Stop broker after all scenarios

    Given Stop Broker

  Scenario: Stop event broker for all scenarios

    Given Stop Event Broker

  Scenario: Stop datastore after all scenarios

    Given Stop Datastore












