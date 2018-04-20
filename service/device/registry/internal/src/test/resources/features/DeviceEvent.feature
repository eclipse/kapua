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
@default
Feature: Device Event CRUD tests
    The Device Event service is responsible for handling the incoming device
    events.

Background:
    Given Scope 12
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    And A "first" device
    And A "second" device

Scenario: Create a regular event
    Create a regular event. The event should not be null and should
    have a regular entity ID. Also the event entity shoould match the event
    creator parameters.

    Given Scope 12
    And User 5
    And A "CREATE" event from device "first"
    Then There was no exception
    And The event matches the creator parameters

Scenario: Create an event with a null scope ID
    It should be impossible to create an event with a null scope ID. Such attempt
    must raise an exception.

    Given Null scope ID
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    And User 5
    When A "CREATE" event from device "first"
    Then An exception was raised

Scenario: Create an event with a null action
    The database should reject any ebvent entity that has a null action parameter.
    In such cases an exception must be thrown.

    Given Scope 12
    And I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type device with id/name"
    And User 5
    And An event creator with null action
    When I create an event from the existing creator
    Then An exception was raised

Scenario: Find an event by its ID
    It must be possible to find an event entity based on the event ID.

    Given Scope 12
    And User 5
    And A "CREATE" event from device "first"
    When I search for an event with the remembered ID
    Then The event matches the creator parameters

Scenario: Find a non existing event
    Searching for an event with a non existing entity ID should return null. No
    exception must be thrown.

    Given Scope 12
    And User 5
    And A "CREATE" event from device "first"
    When I search for an event with a random ID
    Then There is no such event

Scenario: Delete an existing event
    It must be possible to delete an existing event entity from the database.

    Given Scope 12
    And User 5
    And A "CREATE" event from device "first"
    When I search for an event with the remembered ID
    Then The event matches the creator parameters
    When I delete the event with the remembered ID
    And I search for an event with the remembered ID
    Then There is no such event

Scenario: Delete a non existent event
    Trying to delete a non existent event (no matching entity ID) must cause an
    exception to be thrown.

    Given Scope 12
    And I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type deviceEvent with id/name"
    And User 5
    And A "CREATE" event from device "first"
    When  I delete an event with a random ID
    Then An exception was raised

Scenario: Count events in scope
    It must be possible to count all events in a given scope. Only the events for this
    scope should counted, all other events must be ignored.

    Given Scope 12
    And User 5
    And I have 15 "CREATE" events from device "first"

    Given Scope 42
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    And A "third" device
    And I have 25 "READ" events from device "third"
    When I count events for scope 12
    Then There are 15 events

Scenario: Count events in empty scope
    Counting events in an empty (non existing) scope must return a count of 0. No
    exception must be thrown.

    Given Scope 12
    And User 5
    And I have 15 "CREATE" events from device "first"
    When I count events for scope 42
    Then There are 0 events

Scenario: Basic Device Event queries
    It must be possible to perform basic event entity queries.

    Given Scope 12
    And User 5
    And I have 10 "WRITE" events from device "first"
    And I have 15 "CREATE" events from device "first"
    And I have 20 "EXECUTE" events from device "first"
    When I query for "CREATE" events
    Then I find 15 events
    When I query for "WRITE" events
    Then I find 10 events

Scenario: Event factory sanity checks
    Then All device event factory functions must return non null objects

Scenario: Event service domain check
    Then The device event domain data can be updated
