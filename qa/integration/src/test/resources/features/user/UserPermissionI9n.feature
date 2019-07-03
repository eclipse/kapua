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
@permission
@integration
Feature: User Permission tests

  Scenario: Start event broker for all scenarios
    Given Start Event Broker

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
    And Permissions
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
    And Permissions
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
    And Permissions
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
    And Permissions
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
    And Permissions
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
    Create two users, kapua-a and kapua-b on the same kapua-sys account.
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
    And Permissions
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
    And Permissions
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
    Create a new kapua-a user with user, account and access_info domain permissions on the kapua-sys account.
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
    And Permissions
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
    Then Permissions
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
      Create a new kapua-a user on the kapua-sys account.
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
      And Permissions
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
      Create a new kapua-a user on the kapua-sys account.
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
    And Permissions
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
    Create a new kapua-a user on the kapua-sys account.
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
    And Permissions
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
    Create a new kapua-a user on the kapua-sys account.
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
    And Permissions
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
    Create a new kapua-a user on the kapua-sys account.
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
    And Permissions
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

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker
