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
Feature: Job Target service CRUD tests
    The Job service is responsible for maintaining a list of job targets.

Scenario: Regular target creation

    Given I create a job with the name "TestJob"
    And A regular job target item
    Then No exception was thrown
    And The job target matches the creator

Scenario: Target with a null scope ID

    Given A null scope
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "scopeId"
    When I create a job with the name "TestJob"
    Then An exception was thrown

Scenario: Delete a job target

    Given I create a job with the name "TestJob"
    And A regular job target item
    When I delete the last job target in the database
    And I search for the last job target in the database
    Then There is no such job target item in the database

Scenario: Delete a job target twice

    Given I create a job with the name "TestJob"
    And A regular job target item
    When I delete the last job target in the database
    Given I expect the exception "KapuaEntityNotFoundException" with the text "type jobTarget"
    When I delete the last job target in the database
    Then An exception was thrown

Scenario: Create and count multiple job targets

    Given I create a job with the name "TestJob"
    And A regular job target item
    And A regular job target item
    And A regular job target item
    And A regular job target item
    When I count the targets in the current scope
    Then There are exactly 4 items

Scenario: Query for the targets of a specific job

    Given I create a job with the name "TestJob1"
    And A regular job target item
    And A regular job target item
    And A regular job target item
    And A regular job target item
    Given I create a job with the name "TestJob2"
    And A regular job target item
    And A regular job target item
    Given I create a job with the name "TestJob3"
    And A regular job target item
    And A regular job target item
    And A regular job target item
    Given I query for the job with the name "TestJob2"
    When I query the targets for the current job
    Then There are exactly 2 items
    Given I query for the job with the name "TestJob1"
    When I query the targets for the current job
    Then There are exactly 4 items

Scenario: Update a job target TargetId

    Given I create a job with the name "TestJob1"
    And A regular job target item
    When I update the job target target id
    Then No exception was thrown
    And The job target matches the creator

Scenario: Update a job target step index

    Given I create a job with the name "TestJob1"
    And A regular job target item
    When I update the job target step number to 3
    Then The target step index is indeed 3

Scenario: Update a job target status

    Given I create a job with the name "TestJob1"
    And A regular job target item
    When I update the job target step status to "PROCESS_OK"
    Then The target step status is indeed "PROCESS_OK"
    When I update the job target step status to "PROCESS_AWAITING"
    Then The target step status is indeed "PROCESS_AWAITING"

#Scenario: Update a job target step exception
#
#    Given I create a job with the name "TestJob1"
#    And A regular job target item
#    When I update the job target step exception message to "RandomExceptionText"
#    Then The target step exception message is indeed "RandomExceptionText"

Scenario: Job target factory sanity checks

    When I test the sanity of the job target factory

