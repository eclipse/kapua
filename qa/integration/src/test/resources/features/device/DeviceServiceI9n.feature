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
@deviceRegistry
@env_docker

Feature: Device Registry Integration
  Device Registry integration test scenarios. These scenarios test higher level device service functionality
  with all services live.

  @setup
  Scenario: Start full docker environment
    Given Init Jaxb Context
    And Init Security Context
    And Start full docker environment

  #
  # Device lifecycle

  Scenario: Birth message handling from a new Device
  A birth message is received. The referenced device does not yet exist and is created on-the-fly. After the
  message is processed a new device must be created and a BIRTH event inserted in the database.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A birth message from device "device_1"
    When I search for the device "device_1" in account "AccountA"
    Then I find 1 device
    When I search for events from device "device_1" in account "AccountA" I find 1 event within 30 seconds
    And The type of the last event is "BIRTH"
    And I logout

  Scenario: Birth message handling from an existing Device
  A BIRTH message is received from an already existing device. A new BIRTH event must be
  inserted into the database.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A device such as
      | clientId | displayName | modelId         | serialNumber |
      | device_1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
    And A birth message from device "device_1"
    When I search for events from device "device_1" in account "AccountA" I find 1 event within 30 seconds
    And The type of the last event is "BIRTH"
    And I logout

  Scenario: Birth and Birth messages handling
  Two BIRTH messages are received from a device. No exception should be thrown and there should be
  2 BIRTH events in the database.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A birth message from device "device_1"
    And A birth message from device "device_1"
    When I search for events from device "device_1" in account "AccountA" I find 2 events within 30 seconds
    And The type of the last event is "BIRTH"
    And I logout

  Scenario: Disconnect message handling from a non existing Device
  Reception of a DISCONNECT message with a nonexistent client ID should result in an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument deviceId"
    When A disconnect message from device "device_1"
    Then An exception was thrown
    And I logout

  Scenario: Birth and Death message handling
  Reception of a BIRTH-DISCONNECT pair. The first message (BIRTH) should cause a device to be
  created in the database, allowing the successive DISCONNECT message to be successfully
  processed. After the messages the database should contain two events (both BIRTH
  and DISCONNECT).

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given A birth message from device "device_1"
    And A disconnect message from device "device_1"
    When I search for events from device "device_1" in account "AccountA" I find 2 events within 30 seconds
    And The type of the last event is "DEATH"
    And I logout

  Scenario: Birth and Missing message handling
  Reception of a BIRTH-MISSING pair. The first message (BIRTH) should cause a device to be
  created in the database, allowing the successive MISSING message to be successfully
  processed. After the messages the database should contain two events (both BIRTH
  and MISSING).

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given A birth message from device "device_1"
    And A missing message from device "device_1"
    When I search for events from device "device_1" in account "AccountA" I find 2 events within 30 seconds
    And The type of the last event is "MISSING"
    And I logout

  Scenario: Birth and Applications event handling
  Reception of a BIRTH-APPLICATION pair. The first message (BIRTH) should cause a device to be
  created in the database, allowing the successive APPLICATION message to be successfully
  processed. After the messages the database should contain two events (both BIRTH
  and APPLICATION).

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given A birth message from device "device_1"
    And An application message from device "device_1"
    When I search for events from device "device_1" in account "AccountA" I find 2 events within 30 seconds
    And The type of the last event is "APPLICATION"
    And I logout

  #
  # tagIds

  Scenario: Creating new Device and tagging it with specific Tag
  Procedure of registering a device is executed and device BIRTH message is sent.
  After that device is tagged with Tag KuraDevice and searched by this same tag.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A birth message from device "device_1"
    And I search for the device "device_1" in account "AccountA"
    And I tag device with "KuraDevice" tag
    When I search for device with tag "KuraDevice"
    Then I find device "device_1"
    And I untag device with "KuraDevice" tag
    And I logout

  Scenario: Creating new Device, tagging it with specific Tag and then deleting this Tag
  Procedure of registering a device is executed and device BIRTH message is sent.
  After that device is tagged with Tag KuraDevice and searched by this same tag, followed
  by deletion of this tag.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given User A
      | name  | displayName  | email             | phoneNumber     | status  | userType |
      | UserA | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | device_2 | testGateway3 | ReliaGate 10-20 | 12541234ABC  |
    And A birth message from device "device_1"
    And I search for the device "device_1" in account "AccountA"
    And I tag device with "KuraDevice2" tag
    When I search for device with tag "KuraDevice2"
    Then I find device "device_1"
    And I untag device with "KuraDevice2" tag
    And I verify that tag "KuraDevice2" is deleted
    And I logout

  #
  # clientId

  Scenario: Creating a Device with unique 'clientId'
  Login as kapua-sys, go to devices, create a device with unique name.
  Kapua should not return any errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | Device1  | testGateway3 | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    When I search for a device with the client ID "Device1"
    Then I find the device
    And I logout

  Scenario: Creating a Device with non-unique 'clientId'
  Login as kapua-sys, go to devices, create a device with non-unique name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | Device1  | testGateway3 | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    Given I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name Device1 already exists."
    When I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | Device1  | testGateway1 | ReliaGate 10-21 | 99541234ABC  |
    Then An exception was thrown
    And I logout

  Scenario: Creating a Device with short 'clientId'
  Login as kapua-sys, go to devices, create a device with short name.
  Kapua should not return any errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | d        | testGateway3 | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    And I logout

  Scenario: Creating a Device with no 'clientId'
  Login as kapua-sys, go to devices, create a device without a name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument deviceCreator.clientId."
    When I create a device with name ""
    Then An exception was thrown
    And I logout

  Scenario: Creating a Device with 'clientId' containing permitted symbols
  Login as kapua-sys, go to devices, create a device with permitted symbols in name.
  Kapua should not return any errors

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId  | displayName  | modelId         | serialNumber |
      | dev-1_2_3 | testGateway3 | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    And I logout

  Scenario: Creating a Device with 'clientId' containing invalid symbols
  Login as kapua-sys, go to devices, create a device with invalid symbols in name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.clientId: Device"
    And I try to create devices with invalid symbols in name
    Then An exception was thrown
    And I logout

  Scenario: Creating a Device with long 'clientId'
  Login as kapua-sys, go to devices, create device with 255 characters long name.
  Kapua should not return any errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with name "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    And I logout

  Scenario: Creating a Device with too long 'clientId'
  Login as kapua-sys, go to devices, create device with too long (256 characters) name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.clientId: Value over than allowed max length. Max length is: 255."
    And I create a device with name "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    And I logout

  Scenario: Update a Device 'clientId'
  Login as kapua-sys, go to devices, create a device.
  Try to edit device's name.
  Nothing should change as ClientId cannot be changed.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  |
    When I search for a device with the client ID "dev-123"
    Then I try to edit device to clientId "device1"
    Then There is no device with the client ID "device1"
    And I logout

  #
  # status

  Scenario: Creating a Device with 'status' set to 'DISABLED'
  Login as kapua-sys, go to devices, create a device with its status set to DISABLED.
  Kapua should not return any errors

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber | status   |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  | DISABLED |
    Then No exception was thrown
    Then I logout

  Scenario: Creating a Device with 'status' set as 'DISABLED' and trying to connect to the broker.
  Login as kapua-sys, create a device with its status set to DISABLED.
  Then, trying to connect to the broker with a client who's client id equals to the created
  device. Should result in an authentication failure.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber | status   | scopeId |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  | DISABLED | 1       |
    Then No exception was thrown
    Then I logout
    Given I expect the exception "MqttSecurityException" with the text "Not authorized to connect"
    When Client with name "dev-123" with client id "dev-123" user "kapua-broker" password "kapua-password" is connected
    Then An exception was thrown

  Scenario: Creating a Device with 'status' set to 'ENABLED'
  Login as kapua-sys, go to devices, create a device with its status set to ENABLED.
  Kapua should not return errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber | status  |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  | ENABLED |
    Then No exception was thrown
    Then I logout

  Scenario: Creating a Device with 'status' set to 'ENABLED' and trying to connect to the broker.
  Login as kapua-sys, create a device with its status set to ENABLED.
  Then, trying to connect to the broker with a client who's client id equals to the created
  device. Should not result in an authentication failure.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber | status  | scopeId |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  | ENABLED | 1       |
    Then No exception was thrown
    Then I logout
    When Client with name "dev-12" with client id "dev-12" user "kapua-broker" password "kapua-password" is connected
    Then No exception was thrown

  Scenario: Update a Device 'status' to 'ENABLED'
  Login as kapua-sys, go to devices, create a device.
  Try to edit device's status to ENABLED.
  Kapua should not return any errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
    And I search for a device with the client ID "dev-123"
    And I change device status to "ENABLED"
    Then No exception was thrown
    And I logout

  Scenario: Update a Device 'status' to 'DISABLED'
  Login as kapua-sys, go to devices, create a device.
  Try to edit device's status to ENABLED.
  Kapua should not return any errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber | status  |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | ENABLED |
    And I search for a device with the client ID "dev-123"
    And I change device status to "DISABLED"
    Then No exception was thrown
    And I logout

  #
  # displayName

  Scenario: Creating a Device with unique 'displayName'
  Login as kapua-sys, go to devices, create a device with a unique display name.
  Kapua should not return any errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | dev-123  | displayName1 | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    And I logout

  Scenario: Creating a Device with non-unique '.displayName'
  Login as kapua-sys, go to devices, create two devices with different clientIDs and same Display Names.
  Kapua should not any return errors. Duplicates are allowed.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName    | modelId         | serialNumber |
      | dev-111  | displayName111 | ReliaGate 10-20 | 12541234ABC  |
    And I create a device with parameters
      | clientId | displayName    | modelId         | serialNumber |
      | dev-222  | displayName222 | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    And I logout

  Scenario: Creating a Device with short 'displayName'
  Login as kapua-sys, go to devices, create a device with a short display name.
  Kapua should not return any errors. Duplicates are allowed.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName | modelId         | serialNumber |
      | dev-123  | d           | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    And I logout

  Scenario: Creating a Device with long 'displayName'
  Login as kapua-sys, go to devices, create a device with a long (valid) display name.
  Kapua should not return errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName                                                                                                                                                                                                                                                     | modelId         | serialNumber |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    And I logout

  Scenario: Creating a Device with too long 'displayName'
  Login as kapua-sys, go to devices, create a device with too long display name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.displayName: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | displayName                                                                                                                                                                                                                                                      | modelId         | serialNumber |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL | ReliaGate 10-20 | 12541234ABC  |
    Then An exception was thrown
    Then I logout

  Scenario: Creating a Device with permitted symbols in its 'displayName'
  Login as kapua-sys, go to devices, create a device with special symbols in its display name.
  Kapua should not return any errors. All symbols are permitted.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    Then I logout

  Scenario: Deleting a Device
  Login as kapua-sys, go to devices, create device with "enabled" status.
  Try to delete the device. Kapua should not return any errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber | status  |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | ENABLED |
    And I delete the device with the clientId "dev-123"
    When I search for a device with the client ID "dev-123"
    Then There is no such device
    Then No exception was thrown
    And I logout

  #
  # Device Querying

  Scenario: Search Devices by 'clientId'
  Login as kapua-sys, go to devices, create a device.
  Try to find it by Client ID.
  More than one devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | dev111   | displayName1 | ReliaGate 10-30 | 12541234ABD  | DISABLED |
      | test123  | displm22     | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test333  | displame1    | ReliaGate 10-30 | 12541234ABD  | DISABLED |
    And I filter devices by
      | clientId |
      | dev      |
    Then I find 2 devices
    And I filter devices by
      | clientId |
      | dev      |
    Then I find 2 devices
    And I create a device with name "device-123"
    And I filter devices by
      | clientId |
      | dev      |
    Then I find 3 devices
    And I filter devices by
      | clientId |
      | test     |
    Then I find 2 devices
    And I filter devices by
      | clientId |
      | asd      |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'displayName'
  Login as kapua-sys, go to devices, create a device.
  Try to find it by Display Name.
  One device should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
    And I filter devices by
      | displayName |
      | dis         |
    Then I find 1 device
    And I filter devices by
      | displayName |
      | test        |
    Then I find 1 device
    And I filter devices by
      | displayName |
      | DEVICE      |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'serialNumber'
  Login as kapua-sys, go to devices, create several devices.
  Try to find some of them by Serial Number.
  One device should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABC  | DISABLED |
    And I filter devices by
      | serialNumber |
      | 125          |
    Then I find 1 device
    And I filter devices by
      | displayName |
      | 123         |
    Then I find 1 device
    And I filter devices by
      | serialNumber |
      | asd          |
    Then I find 0 device
    And I logout

  Scenario: Search Devices by 'status'
  Login as kapua-sys, go to devices, create several devices.
  Try to find it by its Status.
  One device should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | dev-456  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABC  | ENABLED  |
    And I filter devices by
      | status  |
      | ENABLED |
    Then I find 1 device
    And I filter devices by
      | status   |
      | DISABLED |
    Then I find 2 device
    And I logout

  Scenario: Search Devices by 'clientId' and 'displayName'
  Login as kapua-sys, go to devices, create several devices.
  Try to find specific devices by their Client ID and Display Name.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | clientId | displayName |
      | dev      | display     |
    Then I find 1 device
    And I filter devices by
      | clientId | displayName |
      | test     | test        |
    Then I find 4 devices
    And I filter devices by
      | clientId | displayName |
      | test     | display     |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'clientId' and 'serialNumber'
  Login as kapua-sys, go to devices, create several devices.
  Try to find devices by their Client IDs and Serial Number.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | clientId | serialNumber |
      | e        | 125          |
    Then I find 1 device
    And I filter devices by
      | clientId | serialNumber |
      | test     | 1234         |
    Then I find 4 devices
    And I filter devices by
      | clientId | serialNumber |
      | test     | 1254123      |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'clientId' and 'status'
  Login as kapua-sys, go to devices, create several devices.
  Try to find devices by their Client IDs and Status.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | clientId | status   |
      | dev      | DISABLED |
    Then I find 1 device
    And I filter devices by
      | clientId | status  |
      | test     | ENABLED |
    Then I find 3 devices
    And I filter devices by
      | clientId | status  |
      | test126  | ENABLED |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'displayName' and 'serialNumber'
  Login as kapua-sys, go to devices, create several devices.
  Try to find them by their Display Name and Serial Number.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | displayName | serialNumber |
      | dis         | 12           |
    Then I find 1 device
    And I filter devices by
      | displayName | serialNumber |
      | test        | 123          |
    Then I find 4 devices
    And I filter devices by
      | displayName | serialNumber |
      | display     | 1234123      |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'displayName' and 'status'
  Login as kapua-sys, go to devices, create several devices.
  Try to find it by Display Name and Status.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | displayName | status   |
      | dis         | DISABLED |
    Then I find 1 device
    And I filter devices by
      | displayName | status  |
      | test        | ENABLED |
    Then I find 3 devices
    And I filter devices by
      | displayName | status  |
      | displayName | ENABLED |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'serialNumber' and 'status"
  Login as kapua-sys, go to devices, create several devices
  Try to find it by Serial Number and Status.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | serialNumber | status   |
      | 125          | DISABLED |
    Then I find 1 device
    And I filter devices by
      | serialNumber | status  |
      | 123          | ENABLED |
    Then I find 3 devices
    And I filter devices by
      | serialNumber | status   |
      | ABF          | DISABLED |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'clientId', 'displayName' and 'serialNumber'
  Login as kapua-sys, go to devices, create several devices.
  Try to find it by Client ID, Display Name and Serial Number.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | clientId | displayName | serialNumber |
      | dev      | dis         | ABC          |
    Then I find 1 device
    And I filter devices by
      | clientId | displayName | serialNumber |
      | test     | test        | 1234         |
    Then I find 4 devices
    And I filter devices by
      | clientId | displayName | serialNumber |
      | dev-123  | Serial      | 123          |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'clientId', 'displayName' and 'status'
  Login as kapua-sys, go to devices, create several devices
  Try to find devices by their Client ID, Display Name and Status.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | clientId | displayName | status   |
      | dev      | dis         | DISABLED |
    Then I find 1 device
    And I filter devices by
      | clientId | displayName | status  |
      | test     | test        | ENABLED |
    Then I find 3 devices
    And I filter devices by
      | clientId | displayName | status  |
      | dev-123  | test        | ENABLED |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'serialNumber', 'displayName' and 'status'
  Login as kapua-sys, go to devices, create several devices
  Try to find devices by their Serial Number, Display Name and Status.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | serialNumber | displayName | status   |
      | 125          | dis         | DISABLED |
    Then I find 1 device
    And I filter devices by
      | serialNumber | displayName | status  |
      | 123          | test        | ENABLED |
    Then I find 3 devices
    And I filter devices by
      | serialNumber | displayName | status   |
      | ABE          | test        | DISABLED |
    Then I find 0 devices
    And I logout

  Scenario: Search Devices by 'clientId', 'displayName', 'serialNumber' and 'status'
  Login as kapua-sys, go to devices, create several devices
  Try to find devices by their Serial Number, Display Name and Status.
  Devices should be found.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    When I create devices with parameters
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
      | test123  | test12323333 | SerialNum123    | 12341234ABG  | ENABLED  |
      | test124  | test12323335 | SerialNum126    | 12341234ABF  | ENABLED  |
      | test125  | test12323336 | SerialNum125    | 12341234ABE  | ENABLED  |
      | test126  | test12323337 | SerialNum124    | 12341234ABD  | DISABLED |
    And I filter devices by
      | serialNumber | displayName | status   | clientId |
      | 125          | dis         | DISABLED | dev      |
    Then I find 1 device
    And I filter devices by
      | serialNumber | displayName | status  | clientId |
      | 123          | test        | ENABLED | test     |
    Then I find 3 devices
    And I filter devices by
      | serialNumber | displayName | status  | clientId |
      | 126          | test        | ENABLED | dev      |
    Then I find 0 devices
    And I logout

  @teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
