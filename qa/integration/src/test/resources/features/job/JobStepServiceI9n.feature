###############################################################################
# Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
@jobs
@jobStepService
@integration

Feature: Job step service CRUD tests
  The Job Step service is responsible for maintaining job steps.

  Scenario: Init Security Context for all scenarios

    Given Init Security Context

  Scenario: Regular step creation

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create a job with the name "TestJob"
    And A regular step definition with the name "TestDefinition" and the following properties
      | name  | type             |
      | prop1 | java.lang.String |
      | prop2 | java.lang.String |
      | prop3 | java.lang.String |
    And A regular step creator with the name "TestStep" and the following properties
      | name  | type             | value |
      | prop1 | java.lang.String | v1    |
      | prop2 | java.lang.String | v2    |
      | prop3 | java.lang.String | v3    |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    When I search for the last step in the database
    And The step item matches the creator
    Then I logout

  Scenario: Step with a null scope ID

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create a job with the name "TestJob"
    And A regular step definition with the name "TestDefinition" and the following properties
      | name  | type             |
      | prop1 | java.lang.String |
      | prop2 | java.lang.String |
      | prop3 | java.lang.String |
    Given A null scope
    And A regular step creator with the name "TestStep" and the following properties
      | name  | type             | value |
      | prop1 | java.lang.String | v1    |
      | prop2 | java.lang.String | v2    |
      | prop3 | java.lang.String | v3    |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "scopeId"
    When I create a new step entity from the existing creator
    Then An exception was thrown
    Then I logout

  Scenario: Change an existing step name

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create a job with the name "TestJob"
    And A regular step definition with the name "TestDefinition" and the following properties
      | name  | type             |
      | prop1 | java.lang.String |
      | prop2 | java.lang.String |
      | prop3 | java.lang.String |
    And A regular step creator with the name "TestStep" and the following properties
      | name  | type             | value |
      | prop1 | java.lang.String | v1    |
    Then I create a new step entity from the existing creator
    When I change the step name to "TestStep2"
    And I query for a step with the name "TestStep2"
    Then I count 1
    Then I logout

  Scenario: Count steps in the database

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create a job with the name "TestJob"
    And A regular step definition with the name "TestDefinition" and the following properties
      | name  | type             |
      | prop1 | java.lang.String |
      | prop2 | java.lang.String |
      | prop3 | java.lang.String |
    Given A regular step creator with the name "TestStep1" and the following properties
      | name  | type             | value |
      | prop1 | java.lang.String | v1    |
    Then I create a new step entity from the existing creator
    Given A regular step creator with the name "TestStep2" and the following properties
      | name  | type             | value |
      | prop2 | java.lang.String | v2    |
    Then I create a new step entity from the existing creator
    Given A regular step creator with the name "TestStep3" and the following properties
      | name  | type             | value |
      | prop3 | java.lang.String | v3    |
    Then I create a new step entity from the existing creator
    When I count the steps in the scope
    Then I count 3
    Then I logout

  Scenario: Delete an existing step

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create a job with the name "TestJob"
    And A regular step definition with the name "TestDefinition" and the following properties
      | name  | type             |
      | prop1 | java.lang.String |
      | prop2 | java.lang.String |
      | prop3 | java.lang.String |
    And A regular step creator with the name "TestStep" and the following properties
      | name  | type             | value |
      | prop1 | java.lang.String | v1    |
    Then I create a new step entity from the existing creator
    When I delete the last step
    And I search for the last step in the database
    Then There is no such step item in the database
    Then I logout

  Scenario: Delete a non-existing step

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create a job with the name "TestJob"
    And A regular step definition with the name "TestDefinition" and the following properties
      | name  | type             |
      | prop1 | java.lang.String |
      | prop2 | java.lang.String |
      | prop3 | java.lang.String |
    And A regular step creator with the name "TestStep" and the following properties
      | name  | type             | value |
      | prop1 | java.lang.String | v1    |
    Then I create a new step entity from the existing creator
    When I delete the last step
    Given I expect the exception "KapuaEntityNotFoundException" with the text "jobStep"
    And I delete the last step
    Then An exception was thrown
    Then I logout

  Scenario: Step factory sanity checks

    Given I test the sanity of the step factory

  Scenario: Reset Security Context for all scenarios

    Given Reset Security Context
