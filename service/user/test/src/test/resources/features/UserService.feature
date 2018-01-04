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
@default
Feature: User Service
    User Service is responsible for CRUD operations on User objects in Kapua
    database.

Scenario: Creating user
    Create user with all User entity fields set and persist it in database. Then try to
    find it by name and check all the fields.

    Given Using kapua-sys account
    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given The generic user
        | name     | displayName  | email              | phoneNumber     | status  |
        | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    When I search for a user with name "kapua-u1"
    Then The user matches the data
        | name     | displayName  | email              | phoneNumber     | status  |
        | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |

Scenario: Create user with short name
    Create user that has less than required 3 characters in name.

    Given Using kapua-sys account
    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided"
    And The generic user
        | name | displayName        | email              | phoneNumber     | status  |
        | u1   |    Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    Then An exception was thrown

Scenario: Create user that has more than DB allowed length
    Create user with name that contains 280 characters.

    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given I expect the exception "KapuaException" with the text "*"
    And The generic user
        | name | displayName        | email              | phoneNumber     | status  |
        | uuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaa  |    Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    Then An exception was thrown


Scenario: Create user with special characters in his name
    Create user with #$% characters in his name

    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided"
    And The generic user
        | name      | displayName        | email              | phoneNumber     | status  |
        | ###$$$%%% | Kapua User 1       | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    Then An exception was thrown

Scenario: Update user
    First create user with all User entity fields set. Then persist this user in database.
    After that find that same user and modify all the fields by appending modified.
    Persist changes to database. At the end check that changes ware persisted

    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given The generic user
        | name     | displayName        | email              | phoneNumber     | status  |
        | kapua-u1 |    Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    And I search for a user with name "kapua-u1"
    And I change user to
        | name     | displayName        | email              | phoneNumber     | status  |
        | kapua-u1-mod |    Kapua User 1 mod    | kapua_u1_mod@kapua.com | +386 31 323 444 | DISABLED |
    And I search for a user with name "kapua-u1-mod"
    Then The user matches the data
        | name     | displayName        | email              | phoneNumber     | status  |
        | kapua-u1-mod |    Kapua User 1 mod    | kapua_u1_mod@kapua.com | +386 31 323 444 | DISABLED |

Scenario: Delete user
    Create user with name kapua-user. Then delete this user and check it is
    deleted. This means that if trying to search user, no such user is found.

    Given I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    And The generic user
        | name     | displayName        | email              | phoneNumber     | status  |
        | kapua-u1 |    Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    When I delete the user "kapua-u1"
    And I search for a user with name "kapua-u1"
    Then There is no such user

Scenario: Query user
    Create user with name kapua-user, than issue query for user based on scopeId.
    List of matching users should match single user.

    Given I set the current scope id to 42
    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given I create user "kapua-user" in scope with id 42
    And I query for users in scope with id 42
    Then There is 1 user in the query result list

Scenario: Count user
    Create user with name kapua-user, than issue count based on query that has
    scopeId specified. It is same as Query user, just that it only retrieves count
    of results and not list of users. Count should match just one user.

    Given I set the current scope id to 42
    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given I create user "kapua-user" in scope with id 42
    And I count users in scope 42
    Then I get 1

Scenario: Find user by id
    Create user with all User entity fields set. When user is created it receives
    user id. Use this user id to search for user. Then check that user found is the same
    user as the one created. Check all fields.

    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given The generic user
        | name     | displayName        | email              | phoneNumber     | status  |
        | kapua-u1 |    Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    When I search for the last created user by id
    Then The user matches the data
        | name     | displayName     | email              | phoneNumber     | status  |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |

Scenario: Find user by name
    Create user with all User entity fields set. Use user name to search for user.
    Check that user found is the same user as the one created. Check all fields.

    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given The generic user
        | name     | displayName     | email              | phoneNumber     | status  |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    When I search for a user with name "kapua-u1"
    Then The user matches the data
        | name     | displayName     | email              | phoneNumber     | status  |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |

Scenario: Create user that already exist
    Create user whit name kapua-user and than try to persist it two times.
    KapuaException should be thrown in such scenario.

    Given I set the current scope id to 42
    When I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given I create user "kapua-user" in scope with id 42
    And I expect the exception "KapuaException" with the text "Error during Persistence Operation"
    When I create user "kapua-user" in scope with id 42
    Then An exception was thrown

Scenario: Update user that doesn't exist
    Create user that is not persisted and than run update statement on that user.
    As user doesn't exist KapuaException should be thrown.

    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And A fake user with random IDs
    When I update the user
    Then An exception was thrown

Scenario: Delete user that doesn't exist
    Create user that is not persisted and than try to delete that user. As user
    doesn't exist KapuaException should be thrown.

    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type user with id/name"
    And A fake user with random IDs
    When Delete the last user
    Then An exception was thrown

Scenario: Find user with id and scope id that doesn't exist
    Try to find user that doesn't exist in database. As no user is present for current scopeId,
    issuing find by scopeId and unused user id, should return no user.

    When I search for a user with id 123 in scope with id 456
    Then There is no such user

Scenario: Find user by name that doesn't exist
    Search for user with name kapua-user. That user doesn't exist in database. As a result no
    user should be returned.

    When I search for a user with name "kapua-user-123"
    Then There is no such user

Scenario: Delete Kapua system user
    Deletion of user with name "kapua-sys" should not be allowed. This is system user. So search
    for "kapua-sys" user and than delete it. KapuaException should be thrown.

    Given I search for a user with name "kapua-sys"
    And I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided"
    When Delete the last user
    Then An exception was thrown

Scenario: Create multiple users
    Create three ordinary users in same scopeId and then count to see if there are 3 users in
    that scopeId.

    Given I set the current scope id to 42
    Then I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given The generic users
        | name     | displayName     | email              | phoneNumber     | status  |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 111 | ENABLED |
        | kapua-u2 | Kapua User 2    | kapua_u2@kapua.com | +386 31 323 222 | ENABLED |
        | kapua-u3 | Kapua User 3    | kapua_u3@kapua.com | +386 31 323 333 | ENABLED |
    When I count users in scope 42
    Then I get 3

Scenario: Find multiple users
    Create three ordinary users in same scopeId and then find all users and see if there are
    users with same data as those created.

    Given I set the current scope id to 42
    Then I configure user service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
        | boolean | lockoutPolicy.enabled      | false |
        | integer | lockoutPolicy.maxFailures  | 3     |
        | integer | lockoutPolicy.resetAfter   | 300   |
        | integer | lockoutPolicy.lockDuration | 3     |
    Given The generic users
        | name     | displayName  | email              | phoneNumber     | status  |
        | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED |
        | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 222 | ENABLED |
        | kapua-u3 | Kapua User 3 | kapua_u3@kapua.com | +386 31 323 333 | ENABLED |
    When I query for users in scope with id 42
    Then There are 3 users in the query result list
    And The users in the list match the data
        | name     | displayName  | email              | phoneNumber     | status  |
        | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED |
        | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 222 | ENABLED |
        | kapua-u3 | Kapua User 3 | kapua_u3@kapua.com | +386 31 323 333 | ENABLED |

Scenario: Get metadata
    Query for service specific metadata.

    When I retrieve metadata
    Then I have metadata
