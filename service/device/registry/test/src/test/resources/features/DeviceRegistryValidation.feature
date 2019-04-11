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
@unit
@deviceRegistry
@deviceRegistryValidation
Feature: Device Registry Validation Tests
    The Device Registry Validation helper is responsible for validating parameters
    and permissions before any operation is performed on the database.

Scenario: Validate a regular creator
    Create a regular device creator. The validator should OK it.

    Given A regular device creator
    When I create a device from the existing creator
    Then No exception was thrown

Scenario: Validate a null creator
    Create a null device creator. The validator should throw an exception.

    Given A null device creator
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I create a device from the existing creator
    Then An exception was thrown

Scenario: Validate a device creator with a null scope ID
    Create a regular device creator. Assign a null scope ID. The validator
    should throw an exception.

    Given A regular device creator
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I set the creator scope ID to null
    And I create a device from the existing creator
    Then An exception was thrown

Scenario: Validate a device creator with a null client ID
    Create a regular device creator. Assign a null client ID. The validator
    should throw an exception.

    Given A regular device creator
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I set the creator client ID to null
    And I create a device from the existing creator
    Then An exception was thrown

Scenario: Validate a regular device
    Create a regular device object. the update validator should OK it.

    Given A regular device
    When I update some device parameters
    Then No exception was thrown

Scenario: Validate a null device
    Create a null device object. The validator should throw an
    exception.

    Given A null device
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I update some device parameters
    Then An exception was thrown

Scenario: Validate a regular device search
    Validate the parameters for a device search. Both ScopeID and DeviceID
    are not null. The validator should be OK with it.

    Given Scope with ID 15
    And The device ID 4321
    When I search for a device with the remembered ID
    Then No exception was thrown

Scenario: Validate a device search with a null device ID
    Validate the parameters for a device search. ScopeID is valid, but the DeviceID
    is null. The validator should throw an exception.

    Given Scope with ID 15
    And The device ID null
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I search for a device with the remembered ID
    Then An exception was thrown

Scenario: Validate a device search with a null scope ID
    Validate the parameters for a device search. DeviceID is valid, but ScopeID
    is null. The validator should throw an exception.

    Given A null scope
    And The device ID 4321
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I search for a device with the remembered ID
    Then An exception was thrown

Scenario: Validate a regular device deletion
    Validate the parameters for deleting a device. Both ScopeID and DeviceID
    are not null. The validator should be OK with it.

    Given Scope with ID 15
    And The device ID 4321
    When I delete the device with the remembered ID
    Then No exception was thrown

Scenario: Validate deleting a device with a null device ID
    Validate the parameters for deleting a device. ScopeID is valid, but the DeviceID
    is null. The validator should throw an exception.

    Given Scope with ID 15
    And The device ID null
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I delete the device with the remembered ID
    Then An exception was thrown

Scenario: Validate deleting a device with a null scope ID
    Validate the parameters for deleting a device. DeviceID is valid, but ScopeID
    is null. The validator should throw an exception.

    Given A null scope
    And The device ID 4321
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I delete the device with the remembered ID
    Then An exception was thrown

Scenario: Validate a regular device client search
    Validate the parameters for a client id based search. Both the Scope ID and the
    Client ID are valid. The validator should OK it.

    Given Scope with ID 42
    When I search for a device with the client ID "test_client"
    Then No exception was thrown

Scenario: Validate a device client search with null scope
    Validate the parameters for a client id based search. The Scope ID is null and the
    Client ID is valid. The validator should throw an exception.

    Given A null scope
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I search for a device with the client ID "test_client"
    Then An exception was thrown

Scenario: Validate a device client based search with a null client ID
    Validate the parameters for a client id based search. The Scope ID is valid but the
    Client ID is null. The validator should throw an exception.
    Note: a string with the content 'null' is taken as a null string. Just a cucumber workaround.

    Given Scope with ID 42
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I search for a device with the client ID "null"
    Then An exception was thrown

Scenario: Validate a device client based search with an empty client ID
    Validate the parameters for a client id based search. The Scope ID is valid while the
    Client ID is an empty string. The validator should throw an exception.

    Given Scope with ID 42
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I search for a device with the client ID ""
    Then An exception was thrown

Scenario: Validate a regular device query
    Validate the parameters for a regulat device query. Neither the query nor the query
    Scope ID is null. The validator should be OK with it.

    Given A regular query
    When I perform the remembered query
    Then No exception was thrown

Scenario: Validate a null device query
    Validate a null device query. The validator should throw an exception.

    Given A null query
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I perform the remembered query
    Then An exception was thrown

Scenario: Validate a device query with a null Scope ID
    Validate a faulty device query. The query is not null, but the query Scope ID
    is null. The validator should throw an exception.

    Given A query with a null Scope ID
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I perform the remembered query
    Then An exception was thrown

Scenario: Validate a regular device count
    Validate the parameters for a regulat device count. Neither the query nor the query
    Scope ID is null. The validator should be OK with it.

    Given A regular query
    When I count the devices based on the remembered query
    Then No exception was thrown

Scenario: Validate a null device count
    Validate a device count with a null query. The validator should throw an exception.

    Given A null query
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I count the devices based on the remembered query
    Then An exception was thrown

Scenario: Validate a device count with a null Scope ID
    Validate a device count with a faulty query. The query is not null, but the query Scope ID is.
    The validator should throw an exception.

    Given A query with a null Scope ID
    And I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided"
    When I count the devices based on the remembered query
    Then An exception was thrown
