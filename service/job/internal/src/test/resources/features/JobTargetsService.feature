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

Scenario: Job target factory sanity checks

    When I test the sanity of the job target factory

