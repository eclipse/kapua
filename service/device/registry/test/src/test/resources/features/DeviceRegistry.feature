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
@device
Feature: Device Registry CRUD tests
    The Device registry Service is responsible for CRUD operations for devices in the Kapua
    database.

Scenario: Create a single device
    Create a single test device. The resulting device must have a unique ID assigned
    by the creation process.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "test_device"
    Then The device has a non-null ID

Scenario: All device parameters must match the device creator
    Create a test device and check whether it was created correctly. All the device
    parameters must match the device creator specifications.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "test_device"
    Then The device matches the creator parameters

Scenario: Case sensitivness of named device searches
    Searching by client ID is case sensitive.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "CaseSensitiveTestName"
    Then Named device registry searches are case sensitive

Scenario: Find device by registry ID
    It must be possible to find a device in the registry by its registry ID.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "TestDevice"
    When I search for a device with the remembered ID
    Then The device matches the creator parameters

Scenario: Find device by client ID
    It must be possible to find a device in the registry by its Client ID.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "TestDevice"
    Then It is possible to find the device based on its client ID

Scenario: Try to find a device with an invalid registry ID
    Searching for a nonexistent device should not raise any exception. Only a null
    device should be returned.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    When I search for a device with a random ID
    Then There is no such device

Scenario: Try to find a device with an invalid client ID
    Searching for a nonexistent device should not raise any exception. Only a null
    device should be returned.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    When I search for a device with a random client ID
    Then There is no such device

Scenario: Device query - find by BIOS version
    It must be possible to construct arbitrary device registry queries. In this case
    several test devices are created with different BIOS version values.
    A query based on the BIOS version must only return the corect device.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device with BIOS version "1.1.0" named "TestDevice1"
    Given A device with BIOS version "1.2.0" named "TestDevice2"
    Given A device with BIOS version "1.3.0" named "TestDevice3"
    When I query for devices with BIOS version "1.2.0"
    And I extract the first device
    Then The device client id is "TestDevice2"

Scenario: Device queries
    Test several variants of device registry queries.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "TestDevice"
    Given I create 100 randomly named devices with BIOS version "1.1.0"
    Given I create 100 randomly named devices with BIOS version "1.2.0"
    Given I create 100 randomly named devices with BIOS version "1.3.0"
    When I query for devices with BIOS version "1.1.0"
    Then I find 100 devices
    When I query for devices with BIOS version "1.3.0"
    Then I find 100 devices
    When I query for devices with BIOS different from "1.2.0"
    Then I find 201 devices
    When I query for devices with Client Id "TestDevice"
    Then I find 1 device

Scenario: Count devices in a specific scope
    It must be possible to count all the devices in a specific scope.
    To this end several devices are created in 3 different scopes. When
    counted, only the number of devices in the specified scope must be returned.

    Given The KAPUA-SYS scope
    When I configure the device registry service
        | type    | name                       | value | scopeId |
        | boolean | infiniteChildEntities      | true  |    5    |
        | integer | maxNumberChildEntities     | 50    |    5    |
    Given I create 20 randomly named devices in scope 5
    When I configure the device registry service
        | type    | name                       | value | scopeId |
        | boolean | infiniteChildEntities      | true  |    6    |
        | integer | maxNumberChildEntities     | 5     |    6    |
    Given I create 30 randomly named devices in scope 6
    When I configure the device registry service
        | type    | name                       | value | scopeId |
        | boolean | infiniteChildEntities      | true  |    7    |
        | integer | maxNumberChildEntities     | 5     |    7    |
    Given I create 45 randomly named devices in scope 7
    When I count the devices in scope 6
    Then I count 30
    When I count the devices in scope 5
    Then I count 20

Scenario: Count devices with a specific BIOS version
    It must be possible to count devices based on arbitrary rules.
    To this end several devices are created with different BIOS version.
    A device count based on the BIOS version value is performed. Only
    the number of devices that match the specified BIOS version is
    returned.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given I create 15 randomly named devices with BIOS version "1.1.0"
    Given I create 25 randomly named devices with BIOS version "1.2.0"
    Given I create 35 randomly named devices with BIOS version "1.3.0"
    When I count devices with BIOS version "1.2.0"
    Then I count 25

Scenario: Update an existing device
    Most of the parameters of an existing device are updatable.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "TestDevice"
    When I update some device parameters
    Then The device was correctly updated

Scenario: Try to update the device client ID
    The Client ID of a defice cannot be changed after creation. Any attempt to
    alter this ID must be silently ignored. No exception must be raised.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "TestDevice"
    When I update the device cleint ID to "NewClientId"
    Then The client ID was not changed

Scenario: Update a non existing device
    An attempt to update a non existing device should raise an exception.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "TestDevice"
    And I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type device with id/name"
    When I update a device with an invalid ID
    Then An exception was thrown

Scenario: Delete an existing device from the registry
    It must be possible to delete a device from theregistry. To this
    end a test device is created and subsequently deleted.
    A search for this device should yield a null but no exception.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given A device named "TestDevice"
    When I delete the device with the cleint id "TestDevice"
    Then There is no device with the client ID "TestDevice"

Scenario: Try to delete a non existing device from the registry
    If a user tries to delete a non existing device from the registry an
    exception must be raised.

    Given The KAPUA-SYS scope
    And I configure the device registry service
        | type    | name                   | value |
        | boolean | infiniteChildEntities  | true  |
        | integer | maxNumberChildEntities |  10   |
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type DeviceImpl with id/name"
    When I delete a device with random IDs
    Then An exception was thrown

Scenario: Device factory sanity checks
    The Account factory must instantiate and return valid items. For this test it is enough
    that the items returned are not null.

    Then All device factory functions must return non null values
