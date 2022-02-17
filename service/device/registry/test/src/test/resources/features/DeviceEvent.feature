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
@unit
@deviceRegistry
@deviceEvents
Feature: Device Event CRUD tests
    The Device Event service is responsible for handling the incoming device
    events.

Scenario: Create a regular event
    Create a regular event. The event should not be null and should
    have a regular entity ID. Also the event entity shoould match the event
    creator parameters.

    Given A device named "first"
    When I search for a device with the remembered ID
    Then I find the device
    And A device named "second"
    And A "CREATE" event from device "first"
    Then No exception was thrown
    And The event matches the creator parameters

Scenario: Create an event with a null scope ID
    It should be impossible to create an event with a null scope ID. Such attempt
    must raise an exception.

    Given A device named "first"
    And A device named "second"
    Given A null scope
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When A "CREATE" event from device "first"
    Then An exception was thrown

Scenario: Find an event by its ID
    It must be possible to find an event entity based on the event ID.

    Given A device named "first"
    And A device named "second"
    And A "CREATE" event from device "first"
    When I search for an event with the remembered ID
    Then The event matches the creator parameters

Scenario: Find a non existing event
    Searching for an event with a non existing entity ID should return null. No
    exception must be thrown.

    Given A device named "first"
    And A device named "second"
    And A "CREATE" event from device "first"
    When I search for an event with a random ID
    Then There is no such event

Scenario: Delete an existing event
    It must be possible to delete an existing event entity from the database.

    Given A device named "first"
    And A device named "second"
    And A "CREATE" event from device "first"
    When I search for an event with the remembered ID
    Then The event matches the creator parameters
    When I delete the event with the remembered ID
    And I search for an event with the remembered ID
    Then There is no such event

Scenario: Delete a non existent event
    Trying to delete a non existent event (no matching entity ID) must cause an
    exception to be thrown.

    Given A device named "first"
    And A device named "second"
    And I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type deviceEvent with id/name"
    And A "CREATE" event from device "first"
    When I delete an event with a random ID
    Then An exception was thrown

Scenario: Count events in empty scope
    Counting events in an empty (non existing) scope must return a count of 0.
    No exception should be thrown.

    Given A device named "first"
    And A device named "second"
    And I have 15 "CREATE" events from device "first"
    When I count events for scope 42
    Then I count 0

Scenario: Basic Device Event queries
    It must be possible to perform basic event entity queries.

    Given A device named "first"
    And A device named "second"
    And I have 10 "WRITE" events from device "first"
    And I have 15 "CREATE" events from device "first"
    And I have 20 "EXECUTE" events from device "first"
    When I query for "CREATE" events
    Then I find 15 device events
    When I query for "WRITE" events
    Then I find 10 device events

Scenario: Event factory sanity checks
    Then All device event factory functions must return non null objects

Scenario: Event service domain check
    Then The device event domain data can be updated
