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
@jobsIntegrationBase
@jobService
@env_docker_base

Feature: Job service CRUD tests
  The Job service is responsible for executing scheduled actions on various targets.

  @setup
  Scenario: Init Security Context for all scenarios
    Given Init Jaxb Context
    And Init Security Context
    And Start base docker environment

  Scenario: Regular job creation

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A regular job creator with the name "TestJob_1"
    When I create a new job entity from the existing creator
    Then No exception was thrown
    When I search for the job in the database
    Then No exception was thrown
    And The job entity matches the creator
    Then I logout

  Scenario: Job with a null scope ID

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A null scope
    And A regular job creator with the name "TestJob_1"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "scopeId"
    When I create a new job entity from the existing creator
    Then An exception was thrown
    Then I logout

  Scenario: Job with a null name

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And A job creator with a null name
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "name"
    When I create a new job entity from the existing creator
    Then An exception was thrown
    Then I logout

  Scenario: Job with an empty name

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A job creator with an empty name
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "name"
    When I create a new job entity from the existing creator
    Then An exception was thrown
    Then I logout

  Scenario: Job with a duplicate name

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A regular job creator with the name "TestJob_2"
    Then I create a new job entity from the existing creator
    Given I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name TestJob_2 already exists."
    When I create a new job entity from the existing creator
    Then An exception was thrown
    Then I logout

  Scenario: Delete a job

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A regular job creator with the name "TestJob_3"
    When I create a new job entity from the existing creator
    When I search for the job in the database
    And The job entity matches the creator
    When I delete the job
    And I search for the job in the database
    Then There is no such job item in the database
    Then I logout

  Scenario: Delete a job twice

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A regular job creator with the name "TestJob_4"
    And I create a new job entity from the existing creator
    And I delete the job
    Given I expect the exception "KapuaEntityNotFoundException" with the text "type job"
    And I delete the job
    Then An exception was thrown
    Then I logout

  Scenario: Update a job name

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A regular job creator with the name "TestJob_5"
    Then I create a new job entity from the existing creator
    When I change the job name to "SomeRandomNewName"
    Then No exception was thrown
    When I search for the job in the database
    Then The job name is "SomeRandomNewName"
    Then I logout

  Scenario: Update a job description

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A regular job creator with the name "TestJob_6"
    Then I create a new job entity from the existing creator
    When I change the job description to "SomeRandomNewDescription"
    Then No exception was thrown
    When I search for the job in the database
    Then The job description is "SomeRandomNewDescription"
    Then I logout

  Scenario: Update a job XML definition

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A regular job creator with the name "TestJob_7"
    Then I create a new job entity from the existing creator
    When I change the job XML definition to "SomeRandomNewDefinition"
    Then No exception was thrown
    When I search for the job in the database
    Then The job XML definition is "SomeRandomNewDefinition"
    Then I logout

  Scenario: Update a nonexistent job

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given A regular job creator with the name "TestJob_8"
    Then I create a new job entity from the existing creator
    And I delete the job
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type job"
    When I change the job name to "SomeRandomNewName"
    Then An exception was thrown
    Then I logout

  Scenario: Count job items

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create 10 job items
    When I count the jobs in the database
    Then I count 10
    Then I logout

  Scenario: Count job items in wrong - empty - scope

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create 10 job items
    Given Scope with ID 20
    When I count the jobs in the database
    Then I count 0
    Then I logout

  Scenario: Query for jobs with specified name

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I create 10 job items with the name "TestJobA"
    And I create 15 job items with the name "TestJobB"
    And I create 20 job items with the name "TestJobC"
    When I count the jobs with the name starting with "TestJobB"
    Then I count 15
    Then I logout

  Scenario: Job factory sanity checks

    When I test the sanity of the job factory
    Then No exception was thrown

  @teardown
  Scenario: Stop test environment
    Given Stop full docker environment
    And Reset Security Context