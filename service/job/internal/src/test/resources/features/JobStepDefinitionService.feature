###############################################################################
# Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@jobs
Feature: Job step definition service CRUD tests
    The Job Step Definition service is responsible for maintaining job step definitions.
    During regular runtime the step definitions are automatically extracted from the various
    service implementations.

Scenario: Regular step definition creation

    Given A regular definition creator with the name "TestDefinition" and 3 properties
    When I create a new step definition entity from the existing creator
    Then No exception was thrown
    When I search for the step definition in the database
    Then No exception was thrown
    And The step definition entity matches the creator

Scenario: Regular step definition with a property list

    Given A regular step definition creator with the name "TestDefinition" and the following properties
    | name  | type | value |
    | prop1 | t1   | 123   |
    | prop2 | t1   | 123   |
    | prop3 | t1   | 123   |
    When I create a new step definition entity from the existing creator
    Then No exception was thrown

Scenario: Step definition with a null scope ID

    Given A null scope
    Given A regular step definition creator with the name "TestDefinition"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "scopeId"
    When I create a new step definition entity from the existing creator
    Then An exception was thrown

Scenario: Step definition with a null name

    Given A regular step definition creator with the name "TestDefinition"
    And I set the step definition creator name to null
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "name"
    When I create a new step definition entity from the existing creator
    Then An exception was thrown

Scenario: Step definition with an empty name

    Given A regular step definition creator with the name ""
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "name"
    When I create a new step definition entity from the existing creator
    Then An exception was thrown

Scenario: Step definition with a duplicate name

    Given A regular step definition creator with the name "TestDefinition"
    When I create a new step definition entity from the existing creator
    Given I expect the exception "KapuaException" with the text "Persistence"
    And I create a new step definition entity from the existing creator
    Then An exception was thrown

Scenario: Delete a step definition

    Given A regular step definition creator with the name "TestDefinition"
    When I create a new step definition entity from the existing creator
    When I search for the step definition in the database
    And The step definition entity matches the creator
    When I delete the step definition
    And I search for the step definition in the database
    Then There is no such step definition item in the database

Scenario: Delete a step definition twice

    Given A regular step definition creator with the name "TestDefinition"
    When I create a new step definition entity from the existing creator
    When I delete the step definition
    Given I expect the exception "KapuaEntityNotFoundException" with the text "stepDefinition"
    When I delete the step definition
    Then An exception was thrown

Scenario: Update a step definition name

    Given A regular step definition creator with the name "TestDefinition"
    When I create a new step definition entity from the existing creator
    When I change the step definition name to "SomeRandomNewName"
    Then No exception was thrown
    When I search for the step definition in the database
    Then The step definition name is "SomeRandomNewName"

Scenario: Update a step definition target type
    The step target type field is not changeable. Any attempt to update the
    field will be silently ignored.

    Given A regular step definition creator with the name "TestDefinition"
    When I create a new step definition entity from the existing creator
    Then The step definition type is "TARGET"
    When I change the step definition type to "GENERIC"
    Then No exception was thrown
    When I search for the step definition in the database
    Then The step definition type is "TARGET"

Scenario: Update a step definition processor name
    The processor name field is not changeable. Any attempt to update the
    field will be silently ignored.

    Given A regular step definition creator with the name "TestDefinition"
    And I set the step definition creator processor name to "SimpleProcessor"
    Then I create a new step definition entity from the existing creator
    When I change the step definition processor name to "AnotherProcessor"
    Then No exception was thrown
    When I search for the step definition in the database
    Then The step definition processor name is "SimpleProcessor"

Scenario: Update a nonexistent step definition

    Given A regular step definition creator with the name "TestDefinition"
    When I create a new step definition entity from the existing creator
    When I delete the step definition
    Given I expect the exception "KapuaEntityNotFoundException" with the text "JobStepDefinitionImpl"
    When I change the step definition name to "SomeRandomNewName"
    Then An exception was thrown

Scenario: Count step definition items

    Given I create 10 step definition items
    When I count the step definition in the database
    Then There are exactly 10 items

Scenario: Count step definitions in wrong (empty) scope

    Given I create 10 step definition items
    Given Scope with ID 20
    When I count the step definition in the database
    Then There are exactly 0 items

Scenario: Query for step definitions

    Given Scope with ID 10
    Then I create 10 step definition items
    Given Scope with ID 20
    Then I create 20 step definition items
    When I query for step definitions in scope 10
    Then There are exactly 10 items

Scenario: Step definition factory sanity checks

    Given I test the sanity of the step definition factory
