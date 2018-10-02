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
Feature: Access Info Service CRUD tests

  Scenario: Simple create
  Create a simple access info entry. Only a user is supplied. The entry must
  match the creator parameters.

    Given A scope with ID 1
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    And An access info entity was created
    And The entity matches the creator

  Scenario: Create with permissions
  Create an access info entry. In addition to a user a list of permissions is
  supplied too. The entry must match the creator parameters.

    Given A scope with ID 1
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
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
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
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
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    And The permissions "read, write, execute"
    And An invalid role ID
    Given I expect the exception "KapuaAuthorizationException" with the text "Error: Role not found in the scope"
    When I create the access info entity
    Then An exception was thrown

  Scenario: Find an access info entity
  It must be possible to find an existing access info entity in the database
  based on its ID.

    Given A scope with ID 1
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
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
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    When I search for an access info entity by user ID
    Then I find an accessinfo entity

  Scenario: Search for an access info entity by an incorrect user ID
  If the user ID supplied for a search is invalid, only a null result
  must be returned. No exception must be thrown.

    Given A scope with ID 1
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    Given A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 555 |
    When I search for an access info entity by user ID
    Then I find no access info entity

  Scenario: Delete an existing access info entity
  It must be possible to delete an existing entity from the database.

    Given A scope with ID 1
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
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
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
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
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    And I create the access info entity
    Then I delete the existing access info entity
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type accessInfo"
    When I delete the existing access info entity
    Then An exception was thrown

  Scenario: Delete an access info entity using the wrong scope ID
  An attempt to delete an entity using the wrong scope ID must result
  in an exception.

    Given A scope with ID 1
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    And I create the access info entity
    Given A scope with ID 20
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type accessInfo"
    When I delete the existing access info entity
    Then An exception was thrown

  Scenario: Count access info entities in a specific scope
  It must be possible to count the existing access info entities in various
  scopes.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
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
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    And I create the access info entity
    And I create the access info entity
    And I create the access info entity
    And I create the access info entity
    When I query for the access info entities for the last user
    Then I get 4 as result
    Given A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 555 |
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
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    And I create the access info entity
    Given An invalid user
    When I query for the access info entities for the last user
    Then I get 0 as result

  Scenario: Create regular access permissions
  It must be possible to create sets of regular permissions.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And I create the permissions
    Given A scope with ID 20
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 555 |
    When I create a clean access info entity
    And The permissions "read, execute"
    And I create the permissions
    When I count the permissions in scope 10
    Then I get 3 as result
    When I count the permissions in scope 20
    Then I get 2 as result

  Scenario: Find last created permision
  It must be possible to find a specific permission entry based on its ID.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And I create the permissions
    When I search for the last created permission
    Then I find an access permission entity

  Scenario: Delete an existing access permission entity
  It must be possibel to delete a specific permission entry.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And I create the permissions
    When I count the permissions in scope 10
    Then I get 3 as result
    When I delete the last created access permission
    And I count the permissions in scope 10
    Then I get 2 as result

  Scenario: Delete a non existing permission entity
  Atttempting to delete a non existing permission entry must result in
  an exception. In this case this is achieved by deleting the same entry twice.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permission "read"
    And I create the permission
    When I count the permissions in scope 10
    Then I get 1 as result
    Then I delete the last created access permission
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type accessPermission"
    When I delete the last created access permission
    Then An exception was thrown

  Scenario: Regular creation of Access Role entity
  It must be possible to create a regular role entity in the dataabse.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    Then No exception was thrown
    Given The permissions "read, write, execute"
    And The role "test_role_1"
    When I create the access role
    Then No exception was thrown
    And An access role entity was created

  Scenario: Creation of access role without an acess info entity
  The creation of an access role entity without an already existing access info
  entry is permitted.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And The role "test_role_1"
    When I create the access role
    Then No exception was thrown

  Scenario: Creation of access role with neither acess info and role entities
  The creation of an access role entity with neither access info nor role
  entries is permitted.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And The role "test_role_1"
    When I create the access role
    Then No exception was thrown

  Scenario: Find an existing access role entity
  It must be possible to find an existing access role entry in the database
  based on its ID.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And The role "test_role_1"
    When I create the access role
    When I search for the last created access role entity
    Then I find an access role entity

  Scenario: Count access role entities by scope
  It must be possible to count all the access role entries in specific scopes. If the
  requested scope doesn't contain a role or doesn't exist altogether, only an empty
  result list must be returned. No exception must be thrown.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And The role "test_role_1"
    And I create the access role
    And The role "test_role_2"
    And I create the access role
    And The role "test_role_3"
    And I create the access role

    Given A scope with ID 20
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 555 |
    When I create a clean access info entity
    And The permissions "read, write, execute"
    And The role "test_role_1"
    And I create the access role
    And The role "test_role_2"
    And I create the access role

    Given A scope with ID 30
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u3 | Kapua User 3 | kapua_u3@kapua.com | +386 31 323 555 |
    When I create a clean access info entity
    And The permissions "read, write, execute"
    And The role "test_role_1"
    And I create the access role

    When I count the access roles in scope 10
    Then I get 3 as result
    When I count the access roles in scope 20
    Then I get 2 as result
    When I count the access roles in scope 30
    Then I get 1 as result
    When I count the access roles in scope 42
    Then I get 0 as result

  Scenario: Delete an existing access role entry
  It must be possible to delete an existing access role entry from the database.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And The role "test_role_1"
    And I create the access role
    And The permissions "read, write, execute"
    And The role "test_role_2"
    And I create the access role
    And The permissions "read, write, execute"
    And The role "test_role_3"
    And I create the access role
    When I count the access roles in scope 10
    Then I get 3 as result
    When I delete the last created access role entry
    And I count the access roles in scope 10
    Then I get 2 as result

  Scenario: Delete an existing role twice
  Attempting to delete a non existing access role entry must result in an exception. In
  this case this is achieved by trying to delete the same entry twice.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    When I create the access info entity
    And The permissions "read, write, execute"
    And The role "test_role_1"
    And I create the access role
    Then I delete the last created access role entry
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type accessRole"
    When I delete the last created access role entry
    Then An exception was thrown

  Scenario: Access info service sanity checks
  Check the sanity of the various access info related factories.

    Then I check the sanity of the access info factory
    Then I check the sanity of the access permission factory
    Then I check the sanity of the access role factory

  Scenario: Access service comparison sanity checks
  Check the correctnes of the various object comparison functions.

    Then I can compare access role objects
    Then I can compare access permission objects

  Scenario: Create with permissions and a role in the wrong scope
  Such a scenario must cause an exception to be thrown.

    Given A scope with ID 10
    And A user such as
      | name     | displayName  | email              | phoneNumber     |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 |
    And The permissions "read, write, execute"
    Given A scope with ID 20
    And The role "test_role_1"
    Given A scope with ID 10
    Given I expect the exception "KapuaAuthorizationException" with the text "Role not found in the scope:"
    When I create the access info entity
    Then An exception was thrown
