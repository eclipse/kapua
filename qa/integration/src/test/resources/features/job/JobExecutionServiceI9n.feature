###############################################################################
# Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@integration
@jobs
@jobExecutionService
Feature: Job Execution service CRUD tests
    The Job service is responsible for maintaining the status of the target step executions.

Scenario: Regular job execution creation

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    And I configure the job execution service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    Given I create a job with the name "TestJob"
    And A regular job execution item
    Then No exception was thrown
    And The job execution matches the creator

Scenario: Update job id of an existing execution item

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    And I configure the job execution service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    Given I create a job with the name "TestJob"
    And A regular job execution item
    Then I create a job with the name "TestJob2"
    When I update the job id for the execution item
    Then No exception was thrown

Scenario: Update the end time of an existing execution item

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    And I configure the job execution service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    Given I create a job with the name "TestJob"
    And A regular job execution item
    When I update the end time of the execution item
    Then No exception was thrown
    When I search for the last job execution in the database
    Then The job execution items match

Scenario: Delete a job execution item

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    And I configure the job execution service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    Given I create a job with the name "TestJob"
    And A regular job execution item
    Then I delete the last job execution in the database
    When I search for the last job execution in the database
    Then There is no such job execution item in the database

Scenario: Delete a job execution item twice

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    And I configure the job execution service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    Given I create a job with the name "TestJob"
    And A regular job execution item
    Then I delete the last job execution in the database
    Given I expect the exception "KapuaEntityNotFoundException" with the text "jobExecution"
    Then I delete the last job execution in the database
    Then An exception was thrown

Scenario: Create and count several execution items for a job

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    And I configure the job execution service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    Given I create a job with the name "TestJob"
    And A regular job execution item
    And A regular job execution item
    And A regular job execution item
    And A regular job execution item
    Then No exception was thrown
    When I count the execution items for the current job
    Then I count 4

Scenario: Query for executions of a specific job

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the job service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    And I configure the job execution service
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |
    Given I create a job with the name "TestJob1"
    And A regular job execution item
    And A regular job execution item
    And A regular job execution item
    And A regular job execution item
    Given I create a job with the name "TestJob2"
    And A regular job execution item
    And A regular job execution item
    And A regular job execution item
    Given I create a job with the name "TestJob3"
    And A regular job execution item
    And A regular job execution item
    Given I query for the job with the name "TestJob2"
    When I query for the execution items for the current job
    Then I count 3

#Scenario: Target with a null scope ID
#
#    Given A null scope
#    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "scopeId"
#    When I create a job with the name "TestJob"
#    Then An exception was thrown
#
#Scenario: Delete a job target twice
#
#    Given I create a job with the name "TestJob"
#    And A regular job target item
#    When I delete the last job target in the database
#    Given I expect the exception "KapuaEntityNotFoundException" with the text "type jobTarget"
#    When I delete the last job target in the database
#    Then An exception was thrown
#
#Scenario: Create and count multiple job targets
#
#    Given I create a job with the name "TestJob"
#    And A regular job target item
#    And A regular job target item
#    And A regular job target item
#    And A regular job target item
#    When I count the targets in the current scope
#    Then There are exactly 4 items
#
#Scenario: Query for the targets of a specific job
#
#    Given I create a job with the name "TestJob1"
#    And A regular job target item
#    And A regular job target item
#    And A regular job target item
#    And A regular job target item
#    Given I create a job with the name "TestJob2"
#    And A regular job target item
#    And A regular job target item
#    Given I create a job with the name "TestJob3"
#    And A regular job target item
#    And A regular job target item
#    And A regular job target item
#    Given I query for the job with the name "TestJob2"
#    When I query the targets for the current job
#    Then There are exactly 2 items
#    Given I query for the job with the name "TestJob1"
#    When I query the targets for the current job
#    Then There are exactly 4 items
#
#Scenario: Update a job target TargetId
#
#    Given I create a job with the name "TestJob1"
#    And A regular job target item
#    When I update the job target target id
#    Then No exception was thrown
#    And The job target matches the creator

Scenario: Job execution factory sanity checks

    And I test the sanity of the job execution factory

