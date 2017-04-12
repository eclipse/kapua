###############################################################################
# Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
#
###############################################################################

Feature: Access Info Service CRUD tests

Scenario: Simple create
    Create a simple access info entry. Only a user is supplied. The entry must 
    match the creator parameters.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    And An access info entity was created
    And The entity matches the creator

Scenario: Create with permissions
    Create an access info entry. In addition to a user a list of permissions is 
    supplied too. The entry must match the creator parameters.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    And The permissions "read, write, execute"
    When I create the access info entity
    Then No exception was thrown
    And An access info entity was created
    And The entity matches the creator
    When I search for the permissions of the last access info entity
    Then The permissions match the creator

Scenario: Create with permissions and a role
    Create a full access info entry. A user,  a list of permissions and
    an access role are supplied. The entry must match the creator parameters.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    And The permissions "read, write, execute"
    And The role "test_role_1"
    When I create the access info entity
    Then No exception was thrown
    And An access info entity was created
    And The entity matches the creator
    When I search for the permissions of the last access info entity
    Then The permissions match the creator
    When I search for the roles of the last access info entity
    Then The access info roles match the creator

Scenario: Try to create an access into entity with an invalid role id
    It must not be possible to create an access info entity if the given role is invalid.
    Such an attempt must result in an exception.
    
    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    And The permissions "read, write, execute"
    And An invalid role ID
    When I create the access info entity
    Then An exception was thrown

Scenario: Find an access info entity
    It must be possible to find an existing access info entity in the database 
    based on its ID.
    
    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    And An access info entity was created
    When I search for the last created access info entity
    Then I find an accessinfo entity

Scenario: Find an access info entity by user ID
    It must be possible to find an existing access info entity in the database 
    based on the entity user ID. 

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    When I search for an access info entity by user ID
    Then I find an accessinfo entity

Scenario: Search for an access info entity by an incorrect user ID
    If the user ID supplied for a search is invalid, only a null result 
    must be returned. No exception must be thrown.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    Given A user such as
          | name     | displayName     | email              | phoneNumber     |
          | kapua-u2 | Kapua User 2    | kapua_u2@kapua.com | +386 31 323 555 |
    When I search for an access info entity by user ID
    Then I find no access info entity

Scenario: Delete an existing access info entity
    It must be possible to delete an existing entity from the database.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    When I delete the existing access info entity
    And I search for the last created access info entity
    Then No exception was thrown
    And I find no access info entity

Scenario: Delete a access info entity with permissions and roles
    It must be possible to delete an existing entity from the database.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    And The permissions "read, write, execute"
    And The role "test_role_1"
    And I create the access info entity
    When I delete the existing access info entity
    And I search for the last created access info entity
    Then I find no access info entity

Scenario: Delete an access info entity twice
    An attempt to delete the same entity multiple times should result 
    in an exception.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    And I create the access info entity
    When I delete the existing access info entity
    And I delete the existing access info entity
    Then An exception was thrown

Scenario: Delete an access info entity using the wrong scope ID
    An attempt to delete an entity using the wrong scope ID must result 
    in an exception.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    And I create the access info entity
    Given A scope with ID 20
    When I delete the existing access info entity
    Then No exception was thrown

Scenario: Count access info entities in a specific scope
    It must be possible to count the existing access info entities in various 
    scopes.

    Given A scope with ID 10
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    Then I create the access info entity
    Then I create the access info entity
    Then I create the access info entity
    Given A scope with ID 20
    Then I create the access info entity
    Then I create the access info entity
    Given A scope with ID 30
    Then I create the access info entity
    When I count the access info entities for scope 10
    Then I get 3 as result
    When I count the access info entities for scope 20
    Then I get 2 as result
    When I count the access info entities for scope 30
    Then I get 1 as result
    When I count the access info entities for scope 42
    Then I get 0 as result

Scenario: Query for all the access info entities of a specific user
    It must be possible to find all the access info entities that belong 
    to a specific user.

    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    And I create the access info entity
    And I create the access info entity
    And I create the access info entity
    And I create the access info entity
    When I query for the access info entities for the last user
    Then I get 4 as result
    Given A user such as
          | name     | displayName     | email              | phoneNumber     |
          | kapua-u2 | Kapua User 2    | kapua_u2@kapua.com | +386 31 323 555 |
    And I create the access info entity
    And I create the access info entity
    And I create the access info entity
    When I query for the access info entities for the last user
    Then I get 3 as result

Scenario: Query for all the access info entities of an invalid user
    If an invalid user is supplied for an entity count, the result list must be empty.
    No exceptions should be thrown.
 
    Given A scope with ID 1
    And A user such as
        | name     | displayName     | email              | phoneNumber     |
        | kapua-u1 | Kapua User 1    | kapua_u1@kapua.com | +386 31 323 555 |
    And I create the access info entity
    Given An invalid user
    When I query for the access info entities for the last user
    Then I get 0 as result

