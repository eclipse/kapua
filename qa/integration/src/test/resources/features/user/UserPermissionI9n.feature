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
@userPermission
@integration

Feature: User Permission tests

  Scenario: Start datastore for all scenarios
    Given Start Datastore

  Scenario: Start event broker for all scenarios
    Given Start Event Broker

  Scenario: Start broker for all scenarios
    Given Start Broker

  Scenario: Adding One Permission To User
    Create a new user kapua-a, with only one permission - user:read.
    After login kapua-a user should be able to search and find himself.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    And I search for user with name "kapua-a"
    Then I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I logout

  Scenario: Adding Multiple Permissions To User
    Create a new kapua_a user with all permissions in the User domain.
    After login kapua_a should be able to read, add, edit and delete users.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    Then I search for user with name "kapua-b"
    And I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I change name to "updated-kapua-a"
    Then I search for user with name "kapua-b"
    And I find no user
    Then I search for user with name "updated-kapua-a"
    And I find user
      | name            | displayName  | email             | phoneNumber     | status  | userType |
      | updated-kapua-a | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I try to delete user "updated-kapua-a"
    Then I search for user with name "updated-kapua-a"
    And I find no user
    And I logout

  Scenario: Deleting a Permission
    Create a new user kapua-a, with only one permission - user:read.
    Login as kapua-a user, and verify that the permission is added correctly.
    As kapua-sys user delete the only added permission to the kapua_a user.
    After login, kapua_a user should get SubjectUnauthorizedException when doing a user search.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    And I search for user with name "kapua-a"
    Then I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I logout
    When I login as user with name "kapua-sys" and password "kapua-password"
    Then I query for the last permission added to the new User
    And I find the last permission added to the new user
    And I delete the last permission added to the new User
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission"
    And I search for user with name "kapua-a"
    And An exception was thrown
    And I logout

  Scenario: Adding Previously Deleted Permission
    Create a new user kapua-a, with only one permission - user:read.
    Login as kapua_a and verify that the permission is added correctly.
    As the kapua-sys user remove the only permission added to the kapua_a user.
    After login, kapua_a user should get SubjectUnauthorizedException when doing a user search.
    As kapua-sys user add the previously removed permission to the kapua-a user.
    Login as kapua-a user and verify that the permission is correctly added again.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    And I search for user with name "kapua-a"
    Then I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I logout
    When I login as user with name "kapua-sys" and password "kapua-password"
    Then I query for the last permission added to the new User
    And I find the last permission added to the new user
    And I delete the last permission added to the new User
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission"
    And I search for user with name "kapua-a"
    Then An exception was thrown
    And I logout
    When I login as user with name "kapua-sys" and password "kapua-password"
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then I search for user with name "kapua-a"
    And I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I logout

  Scenario: Adding Permissions To Parallel User
    Create two users, kapua-a and kapua-b in the same kapua-sys account.
    Add the needed user, account and access_info permissions to the kapua_a user.
    After login, kapua_a user should be able to add user:read permission to the kapua_b user.
    Login as kapua-b user and verify that the permission is correctly.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain      | action |
      | user        | read   |
      | user        | write  |
      | account     | read   |
      | access_info | read   |
      | access_info | write  |
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-b | ToManySecrets123# | true    |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    And I search for user with name "kapua-b"
    Then I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Add permissions to the last created user
      | domain      | action |
      | user        | read   |
    And I logout
    When I login as user with name "kapua-b" and password "ToManySecrets123#"
    Then I search for user with name "kapua-a"
    And I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I logout

  Scenario:  Adding Permissions To Child User
    Create a new kapua-a user with user, account and access_info domain permissions in the kapua-sys account.
    Add a new account account-b, with a new kapua-b user with no permissions.
    After login, kapua_a user should be able to add user:read permission to the kapua_b user.
    Login as kapua-b user and verify that the permission is correctly.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain      | action |
      | user        | read   |
      | user        | write  |
      | account     | read   |
      | access_info | read   |
      | access_info | write  |
    And Account
      | name      |
      | account-b |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 323 555 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-b | ToManySecrets123# | true    |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then Add permissions to the last created user
      | domain | action |
      | user   | read   |
    And I logout
    When I login as user with name "kapua-b" and password "ToManySecrets123#"
    Then I search for user with name "kapua-b"
    And I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 323 555 | ENABLED | INTERNAL |
    And I logout

    Scenario: Add User domain permissions to new user
      Create a new kapua-a user in the kapua-sys account.
      Add all permissions from the User domain to the kapua-a user.
      Login as kapua-a user and verify that all the permissions are added correctly.

      When I login as user with name "kapua-sys" and password "kapua-password"
      And I select account "kapua-sys"
      And A generic user
        | name    | displayName  | email             | phoneNumber     | status  | userType |
        | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
      And Credentials
        | name    | password          | enabled |
        | kapua-a | ToManySecrets123# | true    |
      And Add permissions to the last created user
        | domain      | action |
        | user        | read   |
        | user        | write  |
        | user        | delete |
      Then I logout
      When I login as user with name "kapua-a" and password "ToManySecrets123#"
      And A generic user
        | name    | displayName  | email             | phoneNumber     | status  | userType |
        | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
      Then I search for user with name "kapua-b"
      And I find user
        | name    | displayName  | email             | phoneNumber     | status  | userType |
        | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
      And I change name to "kapua-ab"
      Then I search for user with name "kapua-b"
      And I find no user
      Then I search for user with name "kapua-ab"
      And I find user
        | name     | displayName  | email             | phoneNumber     | status  | userType |
        | kapua-ab | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
      And I try to delete user "kapua-ab"
      Then I search for user with name "kapua-ab"
      And I find no user
      And I logout

  Scenario: Add Device domain permissions to new user
      Create a new kapua-a user in the kapua-sys account.
      Add all permissions from the Device domain to the kapua-a user.
      Login as kapua-a user and verify that all the permissions are added correctly.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain      | action |
      | device      | read   |
      | device      | write  |
      | device      | delete |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then A device named "device-client-1"
    And I search for a device with the remembered ID
    Then I find the device
    When I update some device parameters
    Then The device was correctly updated
    And I delete the device with the remembered ID
    And I search for a device with the remembered ID
    Then There is no such device
    And I logout

  Scenario: Add Group domain permissions to new user
    Create a new kapua-a user in the kapua-sys account.
    Add all permissions from the Group domain to the kapua-a user.
    Login as kapua-a user and verify that all the permissions are added correctly.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain      | action |
      | group       | read   |
      | group       | write  |
      | group       | delete |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Given I create the group
      | scope | name         |
      | 1     | test_group_1 |
    Then I search for the last created group
    And The group was found
    Then I update the group name to "updated_test_group_1"
    Then The group was correctly updated
    When I delete the last created group
    Then I search for the last created group
    And No group was found
    And I logout

  Scenario: Add Tag domain permissions to new user
    Create a new kapua-a user in the kapua-sys account.
    Add all permissions from the Tag domain to the kapua-a user.
    Login as kapua-a user and verify that all the permissions are added correctly.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain  | action |
      | tag     | read   |
      | tag     | write  |
      | tag     | delete |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Given Tag with name "tag_a"
    When Tag with name "tag_a" is searched
    Then Tag with name "tag_a" is found
    Then Tag with name "tag_a" is found and deleted
    And Tag with name "tag_a" is searched
    And No tag was found
    And I logout

  Scenario: Add Job domain permissions to new user
    Create a new kapua-a user in the kapua-sys account.
    Add permissions from the Job domain to the kapua-a user.
    Login as kapua-a user and verify that all the permissions are added correctly.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action  |
      | job    | read    |
      | job    | write   |
      | job    | delete  |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Given A regular job creator with the name "job-a"
    When I create a new job entity from the existing creator
    When I search for the job in the database
    And The job entity matches the creator
    And I change the job name to "updated-job-a"
    When I search for the job in the database
    Then The job name is "updated-job-a"
    When I delete the job
    And I search for the job in the database
    Then There is no such job item in the database
    And I logout

  Scenario: Add Access Info domain permissions to new user
    Create a new kapua-a and kapua-b users in the kapua-sys account.
    Add permissions from the access_info domain to the kapua-a user.
    Login as kapua-a user and verify that all the permissions are added correctly
    by performing permission add, search and delete on the kapua-b user.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain        | action |
      | access_info   | read   |
      | access_info   | write  |
      | access_info   | delete |
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-b | ToManySecrets123# | true    |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
    And I logout
    When I login as user with name "kapua-b" and password "ToManySecrets123#"
    Then I search for user with name "kapua-b"
    And I find user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then I query for the last permission added to the new User
    And I find the last permission added to the new user
    And I delete the last permission added to the new User
    And I logout
    When I login as user with name "kapua-b" and password "ToManySecrets123#"
    Then I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission"
    And I search for user with name "kapua-b"
    And An exception was thrown
    And I logout

  Scenario: Add Role domain permissions to new user
    Create a new kapua-a user in the kapua-sys account.
    Add permissions from the Role domain to the kapua-a user.
    Login as kapua-a user and verify that all the permissions are added correctly.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | role   | read   |
      | role   | write  |
      | role   | delete |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Given I create the following role
      | scopeId | name      |
      | 1       | test_role |
    Then I search for the last created role
    And The correct role entry was found
    When I update the last created role name to "updated_test_role"
    And I search for the last created role
    Then The role was successfully updated
    When I delete the last created role
    And I search for the last created role
    Then I find no roles
    And I logout

  Scenario: Add Datastore domain permissions to new user
    Create a new kapua-a user in the kapua-sys account.
    Add permissions from the Datastore domain to the kapua-a user.
    Login as kapua-a user and verify that all the permissions are added correctly.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And The device "test-device-1"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain     | action |
      | datastore  | read   |
      | datastore  | write  |
      | datastore  | delete |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    And I prepare a random message and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When I search for a data message with ID "RandomDataMessageId" and remember it as "DataStoreMessage"
    Then The datastore message "DataStoreMessage" matches the prepared message "RandomDataMessage"
    When I delete the datastore message with ID "RandomDataMessageId"
    And I refresh all indices
    When I search for a data message with ID "RandomDataMessageId" and remember it as "ShouldBeNull"
    Then Message "ShouldBeNull" is null
    And I logout

    Scenario: Add Domain domain permissions to kapua-sys user
    Login as the kapua-sys user and select the kapua-sys account.
    Verify that the kapua-sys user has all of the permissions from the Credential domain.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    When I count the domain entries in the database
    Then I count 21
    And I create the domain
      | name          | actions             |
      | test_domain   | read, write, delete |
    When I search for the last created domain
    Then The domain matches the creator
    When I count the domain entries in the database
    Then I count 22
    When I delete the last created domain
    And I search for the last created domain
    Then There is no domain
    When I count the domain entries in the database
    Then I count 21
    And I logout

  Scenario: Add Domain domain permissions to new user
  Create a new kapua-a user in the kapua-sys account.
  Add all permissions from the Domain domain to the kapua-a user.
  After login the kapua-a user should get the SubjectUnauthorizedException when trying to create a new domain.
  Kapua-a user should be able to perform queries for the already created domain with no exceptions.
  After trying to perform a domain delete kapua-a user should get the SubjectUnauthorizedException.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain   | action |
      | domain   | read   |
      | domain   | write  |
      | domain   | delete |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Given I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission"
    When I create the domain
      | name           | actions             |
      | test_domain1   | read, write, delete |
    Then An exception was thrown
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I search for the last created domain
    And An exception was thrown
    When I count the domain entries in the database
    Then I count 21
    And I logout
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I create the domain
      | name           | actions             |
      | test_domain2   | read, write, delete |
    When I count the domain entries in the database
    Then I count 22
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then I search for the last created domain
    And The domain matches the creator
    Given I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission"
    When I delete the last created domain
    Then An exception was thrown
    When I count the domain entries in the database
    Then I count 22
    And I logout

  Scenario: Add Credential domain permissions to new user
  Create a new kapua-a user in the kapua-sys account.
  Add all permissions from the Credential domain to the kapua-a user.
  Login as kapua-a user and verify that all the permissions are added correctly.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain       | action |
      | credential   | read   |
      | credential   | write  |
      | credential   | delete |
      | user         | read   |
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    When I search for last created user's credentials
    Then I find 0 credentials
    And Credentials
      | name    | password           | enabled |
      | kapua-b | ToManySecrets123#2 | true    |
    Then I search for last created user's credentials
    And I find 1 credentials
    And I delete the last created user's credential
    When I search for last created user's credentials
    Then I find 0 credentials
    And I logout

  Scenario: Add Device Event domain permissions to new user
  Create a new kapua-a user in the kapua-sys account.
  Add all permissions from the Broker domain to the kapua-a user, as well as the needed
  permissions from the device domain.
  Login as kapua-a user and verify that all device_event permissions are added correctly.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A device named "test_client1"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain       | action |
      | device_event | write  |
      | device_event | read   |
      | device_event | delete |
      | device       | read   |
      | device       | write  |
    And I logout
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And A "CREATE" event from device "test_client1"
    When I search for an event with the remembered ID
    Then The event matches the creator parameters
    And I delete the event with the remembered ID
    When I search for an event with the remembered ID
    Then There is no such event
    And I logout

  Scenario: Add Device Connection domain permissions to kapua-sys user
    Login as the kapua-sys user and select the kapua-sys account.
    Verify that the kapua-sys user has all of the permissions from the device_connection domain.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I have the following connection
      | clientId    | clientIp    | serverIp   | protocol | allowUserChange   |
      | testClient1 | 127.0.0.101 | 127.0.0.10 | tcp      | true              |
    When I search for a connection with the client ID "testClient1"
    Then The connection details match
      | clientId    | clientIp    | serverIp   | protocol | allowUserChange   |
      | testClient1 | 127.0.0.101 | 127.0.0.10 | tcp      | true              |
    When I modify the connection details to
      | clientIp    | serverIp   | protocol | allowUserChange   |
      | 127.0.0.109 | 127.0.0.25 | udp      | true              |
    And I delete the existing connection
    When I search for a connection with the client ID "testClient1"
    Then No connection was found
    And I logout

  Scenario: Add Device Connection domain permissions to new user
  Create a new kapua-a user in the kapua-sys account.
  Add all permissions from the device_connection domain to the kapua-a user.
  Login as kapua-a user and verify that the permissions are added correctly.
  Kapua-a user should be able to perform queries for the already created connections with no exceptions.
  After trying to perform a connection edit or delete kapua-a user should get the SubjectUnauthorizedException.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain              | action  |
      | device_connection   | read    |
      | device_connection   | write   |
      | device_connection   | delete  |
    And I have the following connection
      | clientId    | clientIp    | serverIp   | protocol | allowUserChange   |
      | testClient1 | 127.0.0.101 | 127.0.0.10 | tcp      | true              |
    And I logout
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    When I search for a connection with the client ID "testClient1"
    Then The connection details match
      | clientId    | clientIp    | serverIp   | protocol | allowUserChange   |
      | testClient1 | 127.0.0.101 | 127.0.0.10 | tcp      | true              |
    Given I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission"
    And I delete the existing connection
    And An exception was thrown
    Given I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission"
    When I modify the connection details to
      | clientIp    | serverIp   | protocol | allowUserChange   |
      | 127.0.0.109 | 127.0.0.25 | udp      | true              |
    And An exception was thrown
    Given I expect the exception "SubjectUnauthorizedException" with the text "User does not have permission"
    And I have the following connection
      | clientId    | clientIp    | serverIp   | protocol | allowUserChange   |
      | testClient2 | 127.0.0.101 | 127.0.0.10 | tcp      | true              |
    Then An exception was thrown
    When I search for a connection with the client ID "testClient2"
    Then No connection was found
    And I logout

  Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker

  Scenario: Stop datastore after all scenarios
    Given Stop Datastore
