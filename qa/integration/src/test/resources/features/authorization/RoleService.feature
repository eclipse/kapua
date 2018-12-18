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
Feature: Role Service tests

Scenario: Regular role creation
    Create a regular role entry. The entry must match the creator details.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    Then The role matches the creator
    When I examine the permissions for the last role
    Then I find the following actions
    | actions              |
    | write, read, connect |
    And The permissions match

Scenario: Nameless role entry
    It must not be possible to create a role entry without a name.
    Such an attempt must throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I create the following role
    | scopeId | actions              |
    | 1       | write, read, connect |
    Then An exception was thrown

Scenario: Duplicate role names
    It must not be possible to create role entries with duplicate names.
    Such an attempt must throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name test_role already exists."
    When I create the following role
    | scopeId | name      | actions                 |
    | 1       | test_role | write, read, connect    |
    | 1       | test_role | write, execute, connect |
    Then An exception was thrown

Scenario: Role entry with no actions
    It should be possible to create a role entry without a single action.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    Given I create the following role
    | scopeId | name      |
    | 1       | test_role |
    Then No exception was thrown
    And The role matches the creator

Scenario: Find role by ID
    It must be possible to find a specific role entry in the database based
    on the known role ID.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    When I search for the last created role
    Then The correct role entry was found

Scenario: Search the role database for a random ID
    Searching the database for an unknown ID should be silently ignored. No
    exception should be raised.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    When I search for a random role ID
    Then I find no roles

Scenario: Delete an existing role
    It must be possible to delete an existing role item from the database.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    When I delete the last created role
    And I search for the last created role
    Then I find no roles

Scenario: Delete a non existing role entry
    Deleting a non existing role must throw an exception. This scenario explores what
    happens when it is attempted to delete the same role twice.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    When I delete the last created role
    And I search for the last created role
    Then I find no roles
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type role"
    When I delete the last created role
    Then An exception was thrown

Scenario: Update existing role name
    It must be possible to change the name of an already existing role.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    When I update the last created role name to "test_role_changed"
    And I search for the last created role
    Then The role was successfully updated

Scenario: Modify a role that was deleted
    If an update operation is tried on a role that was previously deleted,
    an exception must be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    And I delete the last created role
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type role"
    When I update the last created role name to "test_role_changed"
    Then An exception was thrown

Scenario: Count roles in specific scopes
    It must be possible to count all the roles defined for specific scopes. If
    there is no role for a specific domain a 0 should be returned. No error or
    exception is expected.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    When I configure the role service
    | type    | name                       | value | scopeId | parentId |
    | boolean | infiniteChildEntities      | true  |    2    |       1  |
    | integer | maxNumberChildEntities     | 5     |    2    |       1  |
    When I configure the role service
    | type    | name                       | value | scopeId | parentId |
    | boolean | infiniteChildEntities      | true  |    3    |       1  |
    | integer | maxNumberChildEntities     | 5     |    3    |       1  |
    When I configure the role service
    | type    | name                       | value | scopeId | parentId |
    | boolean | infiniteChildEntities      | true  |    4    |       1  |
    | integer | maxNumberChildEntities     | 5     |    4    |       1  |
    Given I create the following roles
    | scopeId | name        | actions              |
    | 4       | test_role_1 | write, read, connect |
    | 2       | test_role_2 | write, read, connect |
    | 3       | test_role_3 | write, read, connect |
    | 4       | test_role_4 | write, read, connect |
    | 2       | test_role_5 | write, read, connect |
    | 4       | test_role_6 | write, read, connect |
    | 4       | test_role_7 | write, read, connect |
    | 2       | test_role_8 | write, read, connect |
    When I count the roles in scope 4
    Then I count 4
    When I count the roles in scope 2
    Then I count 3
    When I count the roles in scope 3
    Then I count 1
    When I count the roles in scope 15
    Then I count 0

    Scenario: A fresh database must contain 1 default role in the root scope

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I count the roles in scope 1
    Then I count 1

    Scenario: It must be possible to query for specific entries in the role database

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    When I query for the role "test_role" in scope 1
    Then I count 1
    And The correct role entry was found

Scenario: Empty query results are supported
    If a query does not yield any result only an empty list must be returned. This is not
    an error situation.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      | actions              |
    | 1       | test_role | write, read, connect |
    When I query for the role "test_role_unknown" in scope 1
    Then No exception was thrown
    And I count 0

    Scenario: Create some regular role permissions

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      |
    | 1       | test_role |
    And I create the following role permissions
    | scopeId | actionName | targetScopeId |
    | 1       | read       | 1             |
    | 1       | write      | 1             |
    | 1       | connect    | 3             |
    Then No exception was thrown
    When I search for the last created role permission
    Then The correct role permission entry was found

Scenario: Delete role permissions
    It must be possible to delete an existing role permission record from the database.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      |
    | 1       | test_role |
    And I create the following role permission
    | scopeId | actionName |
    | 1       | read       |
    Then No exception was thrown
    When I delete the last created role permission
    And I search for the last created role permission
    Then I find no permissions

Scenario: Delete nonexisting role permission
    An attempt to delete a non existing role permission from the database must result
    in an exception being thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name      |
    | 1       | test_role |
    And  I create the following role permission
    | scopeId | actionName |
    | 1       | read       |
    Then No exception was thrown
    When I delete the last created role permission
    And I search for the last created role permission
    Then I find no permissions
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type rolePermission"
    When I delete the last created role permission
    Then An exception was thrown

Scenario: Count role permissions in specific scopes
    It must be possible to count all the role permissions defined for specific scopes. If
    there is no role permission for a specific domain a 0 should be returned. No error or
    exception is expected.

    When I login as user with name "kapua-sys" and password "kapua-password"
    When I configure the role service
    | type    | name                       | value | scopeId |
    | boolean | infiniteChildEntities      | true  |    1    |
    | integer | maxNumberChildEntities     | 5     |    1    |
    And I create the domain
    | name        | actions             |
    | test        | read,write, execute |
    Given I create the following role
    | scopeId | name        |
    | 2       | test_role_2 |
    And I create the following role permissions
    | scopeId  | actionName | targetScopeId |
    | 2        | read       | 1             |
    | 2        | read       | 2             |
    | 2        | read       | 3             |
    And I create the following role
    | scopeId | name        |
    | 3       | test_role_3 |
    And I create the following role permissions
    | scopeId  | actionName | targetScopeId |
    | 3        | read       | 3             |
    | 3        | read       | 10            |
    And I create the following role
    | scopeId | name        |
    | 4       | test_role_4 |
    And I create the following role permissions
    | scopeId  | actionName | targetScopeId |
    | 4        | read       | 4             |
    When I count the permission in scope 2
    Then I count 3
    When I count the permission in scope 3
    Then I count 2
    When I count the permission in scope 4
    Then I count 1
    When I count the permission in scope 5
    Then I count 0

Scenario: Role creator sanity checks
    Check that the role factory returns non null objects.

    Then The role factory returns sane results

Scenario: Role object equality check
    Check that the role object comparator method correctly compares two objects and returns the
    expected results.

    Then The role comparator does its job

Scenario: Role service related objects sanity checks
    Check that the objects related to the shiro role service behave sanely.

    Then The role permission factory returns sane results
    Then The role permission object constructors are sane
    Then The role permission comparator does its job

