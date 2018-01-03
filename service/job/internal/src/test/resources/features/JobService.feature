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
Feature: Job service CRUD tests
    The Job service is responsible for executing scheduled actions on various targets.

Scenario: Regular job creation

    Given A regular job creator with the name "TestJob"
    When I create a new job entity from the existing creator
    Then No exception was thrown
    When I search for the job in the database
    Then No exception was thrown
    And The job entity matches the creator

Scenario: Job with a null scope ID

    Given A null scope
    And A regular job creator with the name "TestJob"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "scopeId"
    When I create a new job entity from the existing creator
    Then An exception was thrown

Scenario: Job with a null name

    And A job creator with a null name
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "name"
    When I create a new job entity from the existing creator
    Then An exception was thrown

Scenario: Job with an empty name

    Given A job creator with an empty name
    When I create a new job entity from the existing creator
    Then No exception was thrown
    When I search for the job in the database
    Then The job entity matches the creator

Scenario: Job with a duplicate name

    Given A regular job creator with the name "TestJob"
    Then I create a new job entity from the existing creator
    When I create a new job entity from the existing creator
    Then No exception was thrown

Scenario: Delete a job

    Given A regular job creator with the name "TestJob"
    When I create a new job entity from the existing creator
    When I search for the job in the database
    And The job entity matches the creator
    When I delete the job
    And I search for the job in the database
    Then There is no such job item in the database

Scenario: Delete a job twice

    Given A regular job creator with the name "TestJob"
    And I create a new job entity from the existing creator
    And I delete the job
    Given I expect the exception "KapuaEntityNotFoundException" with the text "type job"
    And I delete the job
    Then An exception was thrown

Scenario: Update a job name

    Given A regular job creator with the name "TestJob"
    Then I create a new job entity from the existing creator
    When I change the job name to "SomeRandomNewName"
    Then No exception was thrown
    When I search for the job in the database
    Then The job name is "SomeRandomNewName"

Scenario: Update a job description

    Given A regular job creator with the name "TestJob"
    Then I create a new job entity from the existing creator
    When I change the job description to "SomeRandomNewDescription"
    Then No exception was thrown
    When I search for the job in the database
    Then The job description is "SomeRandomNewDescription"

Scenario: Update a job XML definition

    Given A regular job creator with the name "TestJob"
    Then I create a new job entity from the existing creator
    When I change the job XML definition to "SomeRandomNewDefinition"
    Then No exception was thrown
    When I search for the job in the database
    Then The job XML definition is "SomeRandomNewDefinition"

#Scenario: Update job steps
#
#    Given A regular job creator with the name "TestJob"
#    Then I create a new job entity from the existing creator
#    And A regular step definition with the name "TestDefinition" and the following properties
#        | name  | type |
#        | prop1 | t1   |
#        | prop2 | t2   |
#        | prop3 | t3   |
#    And A regular step creator with the name "TestStep" and the following properties
#        | name  | type | value |
#        | prop1 | t1   | v1    |
#    Then I create a new step entity from the existing creator
#    When I add the current step to the last job
#    And A regular step creator with the name "TestStep" and the following properties
#        | name  | type | value |
#        | prop1 | t1   | v1    |
#    Then I create a new step entity from the existing creator
#    When I add the current step to the last job
#    And I search for the job in the database
#    # This should be 2!!! For some reason the update method does not update the job entity steps.
#    Then The job has 0 steps

Scenario: Update a nonexistent job

    Given A regular job creator with the name "TestJob"
    Then I create a new job entity from the existing creator
    And I delete the job
    Given I expect the exception "KapuaEntityNotFoundException" with the text "JobImpl"
    When I change the job name to "SomeRandomNewName"
    Then An exception was thrown

Scenario: Count job items

    Given I create 10 job items
    When I count the jobs in the database
    Then There are exactly 10 items

Scenario: Count job items in wrong (empty) scope

    Given I create 10 job items
    Given Scope with ID 20
    When I count the jobs in the database
    Then There are exactly 0 items

#Scenario: Query for job items
#
#    Given Scope with ID 10
#    Then I create 10 job items
#    Given Scope with ID 20
#    Then I create 20 job items
#    When I query for jobs in scope 10
#    Then There are exactly 10 items

Scenario: Query for jobs with specified name

    Given I create 10 job items with the name "TestJob1"
    And I create 15 job items with the name "TestJob2"
    And I create 20 job items with the name "TestJob3"
    When I count the jobs with the name "TestJob2"
    Then There are exactly 15 items

Scenario: Job factory sanity checks

    When I test the sanity of the job factory
    Then No exception was thrown
