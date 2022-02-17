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
@device
Feature: Device Registry CRUD tests
    The Device registry Service is responsible for CRUD operations for devices in the Kapua
    database.

    Scenario: Create a single device with null clientID value
        Try to create a single device with ClientID set to NULL.
        An exception should be thrown.

        Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument deviceCreator.clientId."
        When I create a device with null clientID
        Then An exception was thrown

    Scenario: Creating a single device with valid clientID
        Create a single test device. The resulting device must have a unique clientID assigned
        by the creation process.

        Given I create a device with name "device"
        When I search for a device with name "device"
        Then I find device with clientId "device"
        And No exception was thrown

    Scenario: Creating a single device with case sensitive clientID
        Create a single device with case sensitive clientID name.
        Kapua should not return any error.

        Given I create a device with name "testDevice"
        When I search for a device with name "testDevice"
        Then I find device with clientId "testDevice"
        And No exception was thrown

    Scenario: Creating a single device with spaces in clientID
        Create a single device with space in clientID.
        Kapua should not return any error.

        Given I create a device with name "test Device"
        When I search for a device with name "test Device"
        Then I find device with clientId "test Device"
        And No exception was thrown

    Scenario: Create a single device with an empty string for clientID
        Create a single device with clientID that contains an empty string for it.
        Kapua should return an error.

        Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument deviceCreator.clientId."
        When I create a device with name ""
        Then An exception was thrown

    Scenario: Creating a single device with clientID that contains 255 characters
        Create a single device with clientID that contains 255 characters.
        Kapua should not return any error.

        Given I create a device with name "validDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDeviceva"
        When I search for a device with name "validDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDeviceva"
        Then I find device with clientId "validDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDeviceva"
        And No exception was thrown

    Scenario: Creating a single device with clientID that contains 256 characters
        Create a single device with clientID that contains 256 characters.
        Kapua should return an error.

        Given I expect the exception "KapuaIllegalArgumentException" with the text "Value over than allowed max length. Max length is: 255."
        When I create a device with name "validDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDevicevalidDeviceval"
        Then An exception was thrown

    Scenario: Creating a single device with clientID that contains invalid characters
        Create a single device with clientID that contains invalid characters.
        Kapua should return an error.

        Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.clientId"
        When I try to create devices with invalid symbols "#+*&,?>/" in name
        Then An exception was thrown

    #This scenario must be separated from the above scenario because upper scenario cannot contain (::) characters in a row.
    Scenario: Creating a single device with clientID that contains invalid character
        Create a single device with clientID invalid special character ::.
        Kapua should return an error.

        Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.clientId"
        When I create a device with name "testDevice::"
        Then An exception was thrown

    Scenario: Creating two device with the same clientID
        Try to create two devices with same clientID.
        Kapua should return an error.

        Given I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name testDevice already exists."
        When I create a device with name "testDevice"
        And I create a device with name "testDevice"
        Then An exception was thrown

    Scenario: All device parameters must match the device creator
        Create a test device and check whether it was created correctly. All the device
        parameters must match the device creator specifications.

        Given A device named "testDevice"
        Then The device matches the creator parameters

    Scenario: Case sensitiveness of named device searches
        Searching by client ID is case sensitive.

        Given A device named "CaseSensitiveTestName"
        When I search for a device with the client ID "CaseSensitiveTestName"
        Then I find device with clientId "CaseSensitiveTestName"

    Scenario: Find device by registry ID
        It must be possible to find a device in the registry by its registry ID.

        Given A device named "TestDevice"
        When I search for a device with the remembered ID
        Then The device matches the creator parameters

    Scenario: Find device by client ID
        It must be possible to find a device in the registry by its Client ID.

        Given A device named "TestDevice"
        Then I find the device based on clientID "TestDevice"

    Scenario: Try to find a device with an invalid registry ID
        Searching for a nonexistent device should not raise any exception.
        Empty list should be returned.

        When I search for a device with a nonexisting registry ID
        Then There is no such device

    Scenario: Try to find a device with an invalid client ID
        Searching for a nonexistent device should not raise any exception.
        Empty list should be returned.

        When I search for a device with a nonexisting registry ID
        Then There is no such device

    Scenario: Device query - find by BIOS version
        It must be possible to construct arbitrary device registry queries. In this case
        several test devices are created with different BIOS version values.
        A query based on the BIOS version must only return the correct device.

        Given A device with BIOS version "1.1.0" named "TestDevice1"
        Given A device with BIOS version "1.2.0" named "TestDevice2"
        Given A device with BIOS version "1.3.0" named "TestDevice3"
        When I query for devices with BIOS version "1.1.0"
        And I extract the device with correct BIOS version
        Then The device client id is "TestDevice1"

    Scenario: Device queries
        Test several variants of device registry queries.
        Kapua should not return any error.

        Given I create 100 randomly named devices with BIOS version "1.1.0"
        Given I create 201 randomly named devices with BIOS version "1.2.0"
        Given I create 98 randomly named devices with BIOS version "1.3.0"
        When I query for devices with BIOS version "1.1.0"
        Then I find 100 devices
        When I query for devices with BIOS version "1.3.0"
        Then I find 98 devices
        When I query for devices with BIOS different from "1.2.0"
        Then I find 201 devices

    Scenario: Count devices with a specific BIOS version
        It must be possible to count devices based on arbitrary rules.
        To this end several devices are created with different BIOS version.
        A device count based on the BIOS version value is performed. Only
        the number of devices that match the specified BIOS version is
        returned.

        Given I create 15 randomly named devices with BIOS version "1.1.0"
        Given I create 25 randomly named devices with BIOS version "1.2.0"
        Given I create 35 randomly named devices with BIOS version "1.3.0"
        When I count devices with BIOS version "1.2.0"
        Then I count 25

    Scenario: Try to update the device client ID
        The Client ID of a device cannot be changed after creation. Any attempt to
        after this ID must be silently ignored. No exception should be raised.

        Given A device named "TestDevice"
        When I update the device clientID from "TestDevice" to "NewClientId"
        Then I find device with clientId "TestDevice"

    Scenario: Update a non existing device
        An attempt to update a non existing device should raise an exception.

        Given A device named "TestDevice"
        And I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type device with id/name"
        When I update a device with an invalid ID
        Then An exception was thrown

    Scenario: Delete an existing device from the registry
        It must be possible to delete a device from the registry. To this
        end a test device is created and subsequently deleted.
        A search for this device should yield a null but no exception.

        Given A device named "TestDevice"
        When I delete the device with the clientId "TestDevice"
        Then There is no device with the client ID "TestDevice"

    Scenario: Try to delete a non existing device from the registry
        If a user tries to delete a non existing device from the registry an
        exception must be raised.

        Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type DeviceImpl with id/name"
        When When I delete a device with nonexisting ID
        Then An exception was thrown

    Scenario: Device factory sanity checks
        The Account factory must instantiate and return valid items. For this test it is enough
        that the items returned are not null.

        Then All device factory functions must return non null values
