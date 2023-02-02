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
  # connectionId

  Scenario: Create a Device with no 'connectionId'
  Login as kapua-sys, go to devices, create a device with no connectionId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId |
      | Device1  |
    Then No exception was thrown
    When I search for a device with the client ID "Device1"
    Then I find the device
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with invalid 'connectionId'
  Login as kapua-sys, go to devices, create a device with an invalid connectionId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument deviceCreator.connectionId."
    And I create a device with parameters
      | clientId | connectionId |
      | Device1  | 1            |
    Then An exception was thrown
    And I logout

  Scenario: Create a Device with valid 'connectionId'
  Login as kapua-sys, go to devices, create a device with a valid connectionId.
  Kapua should not return any errors.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I have the following connection
      | clientId | clientIp    | serverIp   | protocol | allowUserChange |
      | Device1  | 127.0.0.101 | 127.0.0.10 | tcp      | true            |
    And No exception was thrown
    And The connection object matches the creator
    Then I create a device with parameters and connection
      | clientId |
      | Device1  |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Update a Device with no 'connectionId'
  Login as kapua-sys, go to devices, create a device with connectionId, update a device with no connectionId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I have the following connection
      | clientId | clientIp    | serverIp   | protocol | allowUserChange |
      | Device1  | 127.0.0.101 | 127.0.0.10 | tcp      | true            |
    And No exception was thrown
    And The connection object matches the creator
    And I create a device with parameters and connection
      | clientId |
      | Device1  |
    And No exception was thrown
    And The device matches the creator parameters
    Then I search for a device with the client ID "Device1"
    And I change device connectionId to null
    And No exception was thrown
    And The device was correctly updated
    Then I logout

  Scenario: Update a Device with invalid 'connectionId'
  Login as kapua-sys, go to devices, create a device with connectionId, update a device with invalid connectionId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters and connection
      | clientId |
      | Device1  |
    And No exception was thrown
    And The device matches the creator parameters
    Then I search for a device with the client ID "Device1"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument device.connectionId."
    And I change device connectionId to invalid
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with valid 'connectionId'
  Login as kapua-sys, go to devices, create a device with connectionId, update a device with valid connectionId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters and connection
      | clientId |
      | Device1  |
    And No exception was thrown
    And The device matches the creator parameters
    And I have the following connection
      | clientId | clientIp    | serverIp   | protocol | allowUserChange |
      | Device1  | 127.0.0.101 | 127.0.0.10 | tcp      | true            |
    And No exception was thrown
    And The connection object matches the creator
    Then I search for a device with the client ID "Device1"
    And I change device connectionId to valid
    Then No exception was thrown
    Then I logout

  #
  # lastEventId

  Scenario: Create a Device with no 'lastEventId'
  Login as kapua-sys, go to devices, create a device with no lastEventId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId |
      | Device1  |
    Then No exception was thrown
    When I search for a device with the client ID "Device1"
    Then I find the device
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with invalid 'lastEventId'
  Login as kapua-sys, go to devices, create a device with an invalid lastEventId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument deviceCreator.lastEventId."
    And I create a device with parameters
      | clientId | lastEventId |
      | Device1  | 1           |
    Then An exception was thrown
    And I logout

  Scenario: Update a Device with no 'lastEventId'
  Login as kapua-sys, go to devices, create a device with connectionId, update a device with no lastEventId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId |
      | Device1  |
    And No exception was thrown
    And The device matches the creator parameters
    And A "CREATE" event from device "Device1"
    Then I search for a device with the client ID "Device1"
    And I change device lastEventId to valid
    Then I search for a device with the client ID "Device1"
    And I change device lastEventId to null
    And No exception was thrown
    And The device was correctly updated
    Then I logout

  Scenario: Update a Device with invalid 'lastEventId'
  Login as kapua-sys, go to devices, create a device with connectionId, update a device with invalid lastEventId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId |
      | Device1  |
    And No exception was thrown
    And The device matches the creator parameters
    And A "CREATE" event from device "Device1"
    Then I search for a device with the client ID "Device1"
    And I change device lastEventId to valid
    Then I search for a device with the client ID "Device1"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument device.lastEventId."
    And I change device lastEventId to invalid
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with valid 'lastEventId'
  Login as kapua-sys, go to devices, create a device with connectionId, update a device with valid lastEventId.
  Kapua should return error.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I create a device with parameters
      | clientId |
      | Device1  |
    And No exception was thrown
    And The device matches the creator parameters
    And A "CREATE" event from device "Device1"
    Then I search for a device with the client ID "Device1"
    And I change device lastEventId to valid
    And A "WRITE" event from device "Device1"
    Then I search for a device with the client ID "Device1"
    And I change device lastEventId to valid
    Then No exception was thrown
    Then I logout

  #
  # tagIds

  Scenario: Create new Device and tagging it with specific Tag
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
    And No exception was thrown
    And A birth message from device "device_1"
    And I search for the device "device_1" in account "AccountA"
    And I tag device with "KuraDevice" tag
    When I search for device with tag "KuraDevice"
    Then I find device "device_1"
    And I untag device with "KuraDevice" tag
    And I logout

  Scenario: Create new Device, tagging it with specific Tag and then deleting this Tag
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
    And No exception was thrown
    And The device matches the creator parameters
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

  Scenario: Create a Device with unique 'clientId'
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
    And The device matches the creator parameters
    When I search for a device with the client ID "Device1"
    Then I find the device
    And I logout

  Scenario: Create a Device with non-unique 'clientId'
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
    And The device matches the creator parameters
    Given I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name Device1 already exists."
    When I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | Device1  | testGateway1 | ReliaGate 10-21 | 99541234ABC  |
    Then An exception was thrown
    And I logout

  Scenario: Create a Device with short 'clientId'
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
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with no 'clientId'
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

  Scenario: Create a Device with 'clientId' containing permitted symbols
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
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with 'clientId' containing invalid symbols
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

  Scenario: Create a Device with long 'clientId'
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
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'clientId'
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
    And No exception was thrown
    And The device matches the creator parameters
    When I search for a device with the client ID "dev-123"
    Then I try to edit device to clientId "device1"
    Then There is no device with the client ID "device1"
    And I logout

  #
  # status

  Scenario: Create a Device with 'status' set to 'DISABLED'
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
    And The device matches the creator parameters
    Then I logout

  Scenario: Create a Device with 'status' set as 'DISABLED' and trying to connect to the broker.
  Login as kapua-sys, create a device with its status set to DISABLED.
  Then, trying to connect to the broker with a client who's client id equals to the created
  device. Should result in an authentication failure.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber | status   | scopeId |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  | DISABLED | 1       |
    Then No exception was thrown
    And The device matches the creator parameters
    Then I logout
    Given I expect the exception "MqttSecurityException" with the text "Not authorized to connect"
    When Client with name "dev-123" with client id "dev-123" user "kapua-broker" password "kapua-password" is connected
    Then An exception was thrown

  Scenario: Create a Device with 'status' set to 'ENABLED'
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
    And The device matches the creator parameters
    Then I logout

  Scenario: Create a Device with 'status' set to 'ENABLED' and trying to connect to the broker.
  Login as kapua-sys, create a device with its status set to ENABLED.
  Then, trying to connect to the broker with a client who's client id equals to the created
  device. Should not result in an authentication failure.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber | status  | scopeId |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  | ENABLED | 1       |
    Then No exception was thrown
    And The device matches the creator parameters
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
    And No exception was thrown
    And The device matches the creator parameters
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
    And No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device status to "DISABLED"
    Then No exception was thrown
    And I logout

  #
  # displayName

  Scenario: Create a Device with unique 'displayName'
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
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with non-unique 'displayName'
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
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'displayName'
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
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'displayName'
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
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'displayName'
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

  Scenario: Create a Device with permitted symbols in its 'displayName'
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
    And The device matches the creator parameters
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
    Then No exception was thrown
    And The device matches the creator parameters
    And I delete the device with the clientId "dev-123"
    When I search for a device with the client ID "dev-123"
    Then There is no such device
    Then No exception was thrown
    And I logout

  #
  # serialNumber

  Scenario: Create a Device with no 'serialNumber'
  Login as kapua-sys, go to Devices, create a Device without a serialNumber.
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
      | clientId | serialNumber |
      | dev-123  |              |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'serialNumber'
  Login as kapua-sys, go to Devices, create a Device with a short serialNumber.
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
      | clientId | serialNumber |
      | dev-123  | d            |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'serialNumber'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) serialNumber.
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
      | clientId | serialNumber                                                                                                                                                                                                                                                    |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'serialNumber'
  Login as kapua-sys, go to devices, create a Device with too long serialNumber.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.serialNumber: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | serialNumber                                                                                                                                                                                                                                                     |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'serialNumber'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a serialNumber.
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
      | clientId | serialNumber |
      | dev-123  | serialNumber |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device serialNumber to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'serialNumber'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short serialNumber.
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
      | clientId | serialNumber |
      | dev-123  | serialNumber |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device serialNumber to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'serialNumber'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) serialNumber.
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
      | clientId | serialNumber |
      | dev-123  | serialNumber |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device serialNumber to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'serialNumber'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long serialNumber.
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
      | clientId | serialNumber |
      | dev-123  | serialNumber |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.serialNumber: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device serialNumber to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # modelId

  Scenario: Create a Device with no 'modelId'
  Login as kapua-sys, go to Devices, create a Device without a modelId.
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
      | clientId | modelId |
      | dev-123  |         |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'modelId'
  Login as kapua-sys, go to Devices, create a Device with a short modelId.
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
      | clientId | modelId |
      | dev-123  | d       |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'modelId'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) modelId.
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
      | clientId | modelId                                                                                                                                                                                                                                                         |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'modelId'
  Login as kapua-sys, go to devices, create a Device with too long modelId.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.modelId: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | modelId                                                                                                                                                                                                                                                          |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'modelId'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a modelId.
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
      | clientId | modelId |
      | dev-123  | modelId |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device modelId to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'modelId'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short modelId.
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
      | clientId | modelId |
      | dev-123  | modelId |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device modelId to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'modelId'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) modelId.
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
      | clientId | modelId |
      | dev-123  | modelId |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device modelId to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'modelId'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long modelId.
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
      | clientId | modelId |
      | dev-123  | modelId |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.modelId: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device modelId to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # modelName

  Scenario: Create a Device with no 'modelName'
  Login as kapua-sys, go to Devices, create a Device without a modelName.
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
      | clientId | modelName |
      | dev-123  |           |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'modelName'
  Login as kapua-sys, go to Devices, create a Device with a short modelName.
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
      | clientId | modelName |
      | dev-123  | d         |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'modelName'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) modelName.
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
      | clientId | modelName                                                                                                                                                                                                                                                       |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'modelName'
  Login as kapua-sys, go to devices, create a Device with too long modelName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.modelName: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | modelName                                                                                                                                                                                                                                                        |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'modelName'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a modelName.
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
      | clientId | modelName |
      | dev-123  | modelName |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device modelName to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'modelName'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short modelName.
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
      | clientId | modelName |
      | dev-123  | modelName |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device modelName to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'modelName'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) modelName.
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
      | clientId | modelName |
      | dev-123  | modelName |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device modelName to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'modelName'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long modelName.
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
      | clientId | modelName |
      | dev-123  | modelName |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.modelName: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device modelName to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # imei

  Scenario: Create a Device with no 'imei'
  Login as kapua-sys, go to Devices, create a Device without a imei.
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
      | clientId | imei |
      | dev-123  |      |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'imei'
  Login as kapua-sys, go to Devices, create a Device with a short imei.
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
      | clientId | imei |
      | dev-123  | d    |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'imei'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) imei.
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
      | clientId | imei                     |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQs |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'imei'
  Login as kapua-sys, go to devices, create a Device with too long imei.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.imei: Value over than allowed max length. Max length is: 24."
    And I create a device with parameters
      | clientId | imei                      |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQs |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'imei'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a imei.
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
      | clientId | imei |
      | dev-123  | imei |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device imei to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'imei'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short imei.
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
      | clientId | imei |
      | dev-123  | imei |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device imei to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'imei'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) imei.
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
      | clientId | imei |
      | dev-123  | imei |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device imei to "duyTkz0YUAlEonkehWsSOMQs"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'imei'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long imei.
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
      | clientId | imei |
      | dev-123  | imei |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.imei: Value over than allowed max length. Max length is: 24."
    And I search for a device with the client ID "dev-123"
    And I change device imei to "aduyTkz0YUAlEonkehWsSOMQs"
    Then An exception was thrown
    Then I logout

  #
  # imsi

  Scenario: Create a Device with no 'imsi'
  Login as kapua-sys, go to Devices, create a Device without a imsi.
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
      | clientId | imsi |
      | dev-123  |      |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'imsi'
  Login as kapua-sys, go to Devices, create a Device with a short imsi.
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
      | clientId | imsi |
      | dev-123  | d    |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'imsi'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) imsi.
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
      | clientId | imsi            |
      | dev-123  | duyTkz0YUAlEonk |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'imsi'
  Login as kapua-sys, go to devices, create a Device with too long imsi.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.imsi: Value over than allowed max length. Max length is: 15."
    And I create a device with parameters
      | clientId | imsi             |
      | dev-123  | aduyTkz0YUAlEonk |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'imsi'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a imsi.
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
      | clientId | imsi |
      | dev-123  | imsi |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device imsi to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'imsi'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short imsi.
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
      | clientId | imsi |
      | dev-123  | imsi |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device imsi to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'imsi'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) imsi.
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
      | clientId | imsi |
      | dev-123  | imsi |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device imsi to "duyTkz0YUAlEonk"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'imsi'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long imsi.
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
      | clientId | imsi |
      | dev-123  | imsi |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.imsi: Value over than allowed max length. Max length is: 15."
    And I search for a device with the client ID "dev-123"
    And I change device imsi to "aduyTkz0YUAlEonk"
    Then An exception was thrown
    Then I logout

  #
  # iccid

  Scenario: Create a Device with no 'iccid'
  Login as kapua-sys, go to Devices, create a Device without a iccid.
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
      | clientId | iccid |
      | dev-123  |       |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'iccid'
  Login as kapua-sys, go to Devices, create a Device with a short iccid.
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
      | clientId | iccid |
      | dev-123  | d     |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'iccid'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) iccid.
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
      | clientId | iccid                  |
      | dev-123  | duyTkz0YUAlEonkehWsSOM |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'iccid'
  Login as kapua-sys, go to devices, create a Device with too long iccid.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.iccid: Value over than allowed max length. Max length is: 22."
    And I create a device with parameters
      | clientId | iccid                   |
      | dev-123  | aduyTkz0YUAlEonkehWsSOM |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'iccid'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a iccid.
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
      | clientId | iccid |
      | dev-123  | iccid |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device iccid to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'iccid'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short iccid.
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
      | clientId | iccid |
      | dev-123  | iccid |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device iccid to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'iccid'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) iccid.
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
      | clientId | iccid |
      | dev-123  | iccid |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device iccid to "duyTkz0YUAlEonkehWsSOM"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'iccid'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long iccid.
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
      | clientId | iccid |
      | dev-123  | iccid |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.iccid: Value over than allowed max length. Max length is: 22."
    And I search for a device with the client ID "dev-123"
    And I change device iccid to "aduyTkz0YUAlEonkehWsSOM"
    Then An exception was thrown
    Then I logout

  #
  # biosVersion

  Scenario: Create a Device with no 'biosVersion'
  Login as kapua-sys, go to Devices, create a Device without a biosVersion.
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
      | clientId | biosVersion |
      | dev-123  |             |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'biosVersion'
  Login as kapua-sys, go to Devices, create a Device with a short biosVersion.
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
      | clientId | biosVersion |
      | dev-123  | d           |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'biosVersion'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) biosVersion.
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
      | clientId | biosVersion                                                                                                                                                                                                                                                     |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'biosVersion'
  Login as kapua-sys, go to devices, create a Device with too long biosVersion.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.biosVersion: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | biosVersion                                                                                                                                                                                                                                                      |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'biosVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a biosVersion.
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
      | clientId | biosVersion |
      | dev-123  | biosVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device biosVersion to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'biosVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short biosVersion.
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
      | clientId | biosVersion |
      | dev-123  | biosVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device biosVersion to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'biosVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) biosVersion.
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
      | clientId | biosVersion |
      | dev-123  | biosVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device biosVersion to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'biosVersion'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long biosVersion.
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
      | clientId | biosVersion |
      | dev-123  | biosVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.biosVersion: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device biosVersion to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # firmwareVersion

  Scenario: Create a Device with no 'firmwareVersion'
  Login as kapua-sys, go to Devices, create a Device without a firmwareVersion.
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
      | clientId | firmwareVersion |
      | dev-123  |                 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'firmwareVersion'
  Login as kapua-sys, go to Devices, create a Device with a short firmwareVersion.
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
      | clientId | firmwareVersion |
      | dev-123  | d               |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'firmwareVersion'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) firmwareVersion.
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
      | clientId | firmwareVersion                                                                                                                                                                                                                                                 |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'firmwareVersion'
  Login as kapua-sys, go to devices, create a Device with too long firmwareVersion.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.firmwareVersion: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | firmwareVersion                                                                                                                                                                                                                                                  |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'firmwareVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a firmwareVersion.
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
      | clientId | firmwareVersion |
      | dev-123  | firmwareVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device firmwareVersion to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'firmwareVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short firmwareVersion.
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
      | clientId | firmwareVersion |
      | dev-123  | firmwareVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device firmwareVersion to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'firmwareVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) firmwareVersion.
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
      | clientId | firmwareVersion |
      | dev-123  | firmwareVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device firmwareVersion to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'firmwareVersion'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long firmwareVersion.
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
      | clientId | firmwareVersion |
      | dev-123  | firmwareVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.firmwareVersion: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device firmwareVersion to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # osVersion

  Scenario: Create a Device with no 'osVersion'
  Login as kapua-sys, go to Devices, create a Device without a osVersion.
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
      | clientId | osVersion |
      | dev-123  |           |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'osVersion'
  Login as kapua-sys, go to Devices, create a Device with a short osVersion.
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
      | clientId | osVersion |
      | dev-123  | d         |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'osVersion'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) osVersion.
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
      | clientId | osVersion                                                                                                                                                                                                                                                       |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'osVersion'
  Login as kapua-sys, go to devices, create a Device with too long osVersion.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.osVersion: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | osVersion                                                                                                                                                                                                                                                        |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'osVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a osVersion.
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
      | clientId | osVersion |
      | dev-123  | osVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device osVersion to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'osVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short osVersion.
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
      | clientId | osVersion |
      | dev-123  | osVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device osVersion to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'osVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) osVersion.
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
      | clientId | osVersion |
      | dev-123  | osVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device osVersion to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'osVersion'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long osVersion.
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
      | clientId | osVersion |
      | dev-123  | osVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.osVersion: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device osVersion to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # jvmVersion

  Scenario: Create a Device with no 'jvmVersion'
  Login as kapua-sys, go to Devices, create a Device without a jvmVersion.
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
      | clientId | jvmVersion |
      | dev-123  |            |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'jvmVersion'
  Login as kapua-sys, go to Devices, create a Device with a short jvmVersion.
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
      | clientId | jvmVersion |
      | dev-123  | d          |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'jvmVersion'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) jvmVersion.
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
      | clientId | jvmVersion                                                                                                                                                                                                                                                      |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'jvmVersion'
  Login as kapua-sys, go to devices, create a Device with too long jvmVersion.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.jvmVersion: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | jvmVersion                                                                                                                                                                                                                                                       |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'jvmVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a jvmVersion.
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
      | clientId | jvmVersion |
      | dev-123  | jvmVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device jvmVersion to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'jvmVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short jvmVersion.
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
      | clientId | jvmVersion |
      | dev-123  | jvmVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device jvmVersion to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'jvmVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) jvmVersion.
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
      | clientId | jvmVersion |
      | dev-123  | jvmVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device jvmVersion to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'jvmVersion'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long jvmVersion.
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
      | clientId | jvmVersion |
      | dev-123  | jvmVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.jvmVersion: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device jvmVersion to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # osgiFrameworkVersion

  Scenario: Create a Device with no 'osgiFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device without a osgiFrameworkVersion.
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
      | clientId | osgiFrameworkVersion |
      | dev-123  |                      |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'osgiFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device with a short osgiFrameworkVersion.
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
      | clientId | osgiFrameworkVersion |
      | dev-123  | d                    |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'osgiFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) osgiFrameworkVersion.
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
      | clientId | osgiFrameworkVersion                                                                                                                                                                                                                                            |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'osgiFrameworkVersion'
  Login as kapua-sys, go to devices, create a Device with too long osgiFrameworkVersion.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.osgiFrameworkVersion: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | osgiFrameworkVersion                                                                                                                                                                                                                                             |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'osgiFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a osgiFrameworkVersion.
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
      | clientId | osgiFrameworkVersion |
      | dev-123  | osgiFrameworkVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device osgiFrameworkVersion to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'osgiFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short osgiFrameworkVersion.
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
      | clientId | osgiFrameworkVersion |
      | dev-123  | osgiFrameworkVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device osgiFrameworkVersion to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'osgiFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) osgiFrameworkVersion.
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
      | clientId | osgiFrameworkVersion |
      | dev-123  | osgiFrameworkVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device osgiFrameworkVersion to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'osgiFrameworkVersion'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long osgiFrameworkVersion.
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
      | clientId | osgiFrameworkVersion |
      | dev-123  | osgiFrameworkVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.osgiFrameworkVersion: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device osgiFrameworkVersion to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # applicationFrameworkVersion

  Scenario: Create a Device with no 'applicationFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device without a applicationFrameworkVersion.
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
      | clientId | applicationFrameworkVersion |
      | dev-123  |                             |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'applicationFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device with a short applicationFrameworkVersion.
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
      | clientId | applicationFrameworkVersion |
      | dev-123  | d                           |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'applicationFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) applicationFrameworkVersion.
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
      | clientId | applicationFrameworkVersion                                                                                                                                                                                                                                     |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'applicationFrameworkVersion'
  Login as kapua-sys, go to devices, create a Device with too long applicationFrameworkVersion.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.applicationFrameworkVersion: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | applicationFrameworkVersion                                                                                                                                                                                                                                      |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'applicationFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a applicationFrameworkVersion.
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
      | clientId | applicationFrameworkVersion |
      | dev-123  | applicationFrameworkVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device applicationFrameworkVersion to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'applicationFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short applicationFrameworkVersion.
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
      | clientId | applicationFrameworkVersion |
      | dev-123  | applicationFrameworkVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device applicationFrameworkVersion to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'applicationFrameworkVersion'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) applicationFrameworkVersion.
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
      | clientId | applicationFrameworkVersion |
      | dev-123  | applicationFrameworkVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device applicationFrameworkVersion to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'applicationFrameworkVersion'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long applicationFrameworkVersion.
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
      | clientId | applicationFrameworkVersion |
      | dev-123  | applicationFrameworkVersion |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.applicationFrameworkVersion: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device applicationFrameworkVersion to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # connectionInterface

  Scenario: Create a Device with no 'connectionInterface'
  Login as kapua-sys, go to Devices, create a Device without a connectionInterface.
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
      | clientId | connectionInterface |
      | dev-123  |                     |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'connectionInterface'
  Login as kapua-sys, go to Devices, create a Device with a short connectionInterface.
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
      | clientId | connectionInterface |
      | dev-123  | d                   |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with 255 long 'connectionInterface'
  Login as kapua-sys, go to Devices, create a Device with a 255 long (valid) connectionInterface.
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
      | clientId | connectionInterface                                                                                                                                                                                                                                             |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with 4096 long 'connectionInterface'
  Login as kapua-sys, go to Devices, create a Device with a 4096 long (valid) connectionInterface.
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
      | clientId | connectionInterface                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
      | dev-123  | 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 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'connectionInterface'
  Login as kapua-sys, go to devices, create a Device with too long connectionInterface.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.connectionInterface: Value over than allowed max length. Max length is: 4096."
    And I create a device with parameters
      | clientId | connectionInterface                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
      | dev-123  | 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 |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'connectionInterface'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a connectionInterface.
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
      | clientId | connectionInterface |
      | dev-123  | connectionInterface |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device connectionInterface to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'connectionInterface'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short connectionInterface.
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
      | clientId | connectionInterface |
      | dev-123  | connectionInterface |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device connectionInterface to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with 255 long 'connectionInterface'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a 255 long (valid) connectionInterface.
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
      | clientId | connectionInterface |
      | dev-123  | connectionInterface |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device connectionInterface to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with 4096 long 'connectionInterface'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a 4096 long (valid) connectionInterface.
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
      | clientId | connectionInterface |
      | dev-123  | connectionInterface |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device connectionInterface to "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"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'connectionInterface'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long connectionInterface.
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
      | clientId | connectionInterface |
      | dev-123  | connectionInterface |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.connectionInterface: Value over than allowed max length. Max length is: 4096."
    And I search for a device with the client ID "dev-123"
    And I change device connectionInterface to "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"
    Then An exception was thrown
    Then I logout

  #
  # connectionIp

  Scenario: Create a Device with no 'connectionIp'
  Login as kapua-sys, go to Devices, create a Device without a connectionIp.
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
      | clientId | connectionIp |
      | dev-123  |              |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'connectionIp'
  Login as kapua-sys, go to Devices, create a Device with a short connectionIp.
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
      | clientId | connectionIp |
      | dev-123  | d            |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with 64 long 'connectionIp'
  Login as kapua-sys, go to Devices, create a Device with a 64 long (valid) connectionIp.
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
      | clientId | connectionIp                                                     |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with 4096 long 'connectionIp'
  Login as kapua-sys, go to Devices, create a Device with a 4096 long (valid) connectionIp.
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
      | clientId | connectionIp                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
      | dev-123  | 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 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'connectionIp'
  Login as kapua-sys, go to devices, create a Device with too long connectionIp.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.connectionIp: Value over than allowed max length. Max length is: 4096."
    And I create a device with parameters
      | clientId | connectionIp                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
      | dev-123  | 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 |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'connectionIp'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a connectionIp.
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
      | clientId | connectionIp |
      | dev-123  | connectionIp |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device connectionIp to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'connectionIp'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short connectionIp.
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
      | clientId | connectionIp |
      | dev-123  | connectionIp |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device connectionIp to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with 64 long 'connectionIp'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a 64 long (valid) connectionIp.
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
      | clientId | connectionIp |
      | dev-123  | connectionIp |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device connectionIp to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with 4096 long 'connectionIp'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a 4096 long (valid) connectionIp.
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
      | clientId | connectionIp |
      | dev-123  | connectionIp |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device connectionIp to "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"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'connectionIp'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long connectionIp.
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
      | clientId | connectionIp |
      | dev-123  | connectionIp |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.connectionIp: Value over than allowed max length. Max length is: 4096."
    And I search for a device with the client ID "dev-123"
    And I change device connectionIp to "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"
    Then An exception was thrown
    Then I logout

  #
  # applicationIdentifiers

  Scenario: Create a Device with no 'applicationIdentifiers'
  Login as kapua-sys, go to Devices, create a Device without a applicationIdentifiers.
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
      | clientId | applicationIdentifiers |
      | dev-123  |                        |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'applicationIdentifiers'
  Login as kapua-sys, go to Devices, create a Device with a short applicationIdentifiers.
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
      | clientId | applicationIdentifiers |
      | dev-123  | d                      |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'applicationIdentifiers'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) applicationIdentifiers.
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
      | clientId | applicationIdentifiers                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyT |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'applicationIdentifiers'
  Login as kapua-sys, go to devices, create a Device with too long applicationIdentifiers.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.applicationIdentifiers: Value over than allowed max length. Max length is: 1024."
    And I create a device with parameters
      | clientId | applicationIdentifiers                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyT |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'applicationIdentifiers'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a applicationIdentifiers.
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
      | clientId | applicationIdentifiers |
      | dev-123  | applicationIdentifiers |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device applicationIdentifiers to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'applicationIdentifiers'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short applicationIdentifiers.
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
      | clientId | applicationIdentifiers |
      | dev-123  | applicationIdentifiers |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device applicationIdentifiers to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'applicationIdentifiers'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) applicationIdentifiers.
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
      | clientId | applicationIdentifiers |
      | dev-123  | applicationIdentifiers |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device applicationIdentifiers to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyT"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'applicationIdentifiers'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long applicationIdentifiers.
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
      | clientId | applicationIdentifiers |
      | dev-123  | applicationIdentifiers |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.applicationIdentifiers: Value over than allowed max length. Max length is: 1024."
    And I search for a device with the client ID "dev-123"
    And I change device applicationIdentifiers to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziLduyT"
    Then An exception was thrown
    Then I logout

  #
  # acceptEncoding

  Scenario: Create a Device with no 'acceptEncoding'
  Login as kapua-sys, go to Devices, create a Device without a acceptEncoding.
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
      | clientId | acceptEncoding |
      | dev-123  |                |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'acceptEncoding'
  Login as kapua-sys, go to Devices, create a Device with a short acceptEncoding.
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
      | clientId | acceptEncoding |
      | dev-123  | d              |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'acceptEncoding'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) acceptEncoding.
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
      | clientId | acceptEncoding                                                                                                                                                                                                                                                  |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'acceptEncoding'
  Login as kapua-sys, go to devices, create a Device with too long acceptEncoding.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.acceptEncoding: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | acceptEncoding                                                                                                                                                                                                                                                   |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'acceptEncoding'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a acceptEncoding.
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
      | clientId | acceptEncoding |
      | dev-123  | acceptEncoding |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device acceptEncoding to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'acceptEncoding'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short acceptEncoding.
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
      | clientId | acceptEncoding |
      | dev-123  | acceptEncoding |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device acceptEncoding to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'acceptEncoding'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) acceptEncoding.
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
      | clientId | acceptEncoding |
      | dev-123  | acceptEncoding |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device acceptEncoding to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'acceptEncoding'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long acceptEncoding.
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
      | clientId | acceptEncoding |
      | dev-123  | acceptEncoding |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.acceptEncoding: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device acceptEncoding to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # customAttribute1

  Scenario: Create a Device with no 'customAttribute1'
  Login as kapua-sys, go to Devices, create a Device without a customAttribute1.
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
      | clientId | customAttribute1 |
      | dev-123  |                  |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'customAttribute1'
  Login as kapua-sys, go to Devices, create a Device with a short customAttribute1.
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
      | clientId | customAttribute1 |
      | dev-123  | d                |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'customAttribute1'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) customAttribute1.
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
      | clientId | customAttribute1                                                                                                                                                                                                                                                |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'customAttribute1'
  Login as kapua-sys, go to devices, create a Device with too long customAttribute1.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.customAttribute1: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | customAttribute1                                                                                                                                                                                                                                                 |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'customAttribute1'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a customAttribute1.
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
      | clientId | customAttribute1 |
      | dev-123  | customAttribute1 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute1 to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'customAttribute1'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short customAttribute1.
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
      | clientId | customAttribute1 |
      | dev-123  | customAttribute1 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute1 to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'customAttribute1'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) customAttribute1.
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
      | clientId | customAttribute1 |
      | dev-123  | customAttribute1 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute1 to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'customAttribute1'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long customAttribute1.
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
      | clientId | customAttribute1 |
      | dev-123  | customAttribute1 |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.customAttribute1: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute1 to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # customAttribute2

  Scenario: Create a Device with no 'customAttribute2'
  Login as kapua-sys, go to Devices, create a Device without a customAttribute2.
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
      | clientId | customAttribute2 |
      | dev-123  |                  |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'customAttribute2'
  Login as kapua-sys, go to Devices, create a Device with a short customAttribute2.
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
      | clientId | customAttribute2 |
      | dev-123  | d                |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'customAttribute2'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) customAttribute2.
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
      | clientId | customAttribute2                                                                                                                                                                                                                                                |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'customAttribute2'
  Login as kapua-sys, go to devices, create a Device with too long customAttribute2.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.customAttribute2: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | customAttribute2                                                                                                                                                                                                                                                 |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'customAttribute2'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a customAttribute2.
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
      | clientId | customAttribute2 |
      | dev-123  | customAttribute2 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute2 to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'customAttribute2'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short customAttribute2.
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
      | clientId | customAttribute2 |
      | dev-123  | customAttribute2 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute2 to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'customAttribute2'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) customAttribute2.
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
      | clientId | customAttribute2 |
      | dev-123  | customAttribute2 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute2 to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'customAttribute2'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long customAttribute2.
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
      | clientId | customAttribute2 |
      | dev-123  | customAttribute2 |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.customAttribute2: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute2 to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # customAttribute3

  Scenario: Create a Device with no 'customAttribute3'
  Login as kapua-sys, go to Devices, create a Device without a customAttribute3.
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
      | clientId | customAttribute3 |
      | dev-123  |                  |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'customAttribute3'
  Login as kapua-sys, go to Devices, create a Device with a short customAttribute3.
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
      | clientId | customAttribute3 |
      | dev-123  | d                |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'customAttribute3'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) customAttribute3.
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
      | clientId | customAttribute3                                                                                                                                                                                                                                                |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'customAttribute3'
  Login as kapua-sys, go to devices, create a Device with too long customAttribute3.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.customAttribute3: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | customAttribute3                                                                                                                                                                                                                                                 |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'customAttribute3'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a customAttribute3.
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
      | clientId | customAttribute3 |
      | dev-123  | customAttribute3 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute3 to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'customAttribute3'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short customAttribute3.
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
      | clientId | customAttribute3 |
      | dev-123  | customAttribute3 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute3 to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'customAttribute3'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) customAttribute3.
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
      | clientId | customAttribute3 |
      | dev-123  | customAttribute3 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute3 to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'customAttribute3'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long customAttribute3.
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
      | clientId | customAttribute3 |
      | dev-123  | customAttribute3 |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.customAttribute3: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute3 to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # customAttribute4

  Scenario: Create a Device with no 'customAttribute4'
  Login as kapua-sys, go to Devices, create a Device without a customAttribute4.
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
      | clientId | customAttribute4 |
      | dev-123  |                  |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'customAttribute4'
  Login as kapua-sys, go to Devices, create a Device with a short customAttribute4.
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
      | clientId | customAttribute4 |
      | dev-123  | d                |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'customAttribute4'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) customAttribute4.
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
      | clientId | customAttribute4                                                                                                                                                                                                                                                |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'customAttribute4'
  Login as kapua-sys, go to devices, create a Device with too long customAttribute4.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.customAttribute4: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | customAttribute4                                                                                                                                                                                                                                                 |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'customAttribute4'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a customAttribute4.
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
      | clientId | customAttribute4 |
      | dev-123  | customAttribute4 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute4 to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'customAttribute4'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short customAttribute4.
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
      | clientId | customAttribute4 |
      | dev-123  | customAttribute4 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute4 to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'customAttribute4'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) customAttribute4.
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
      | clientId | customAttribute4 |
      | dev-123  | customAttribute4 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute4 to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'customAttribute4'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long customAttribute4.
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
      | clientId | customAttribute4 |
      | dev-123  | customAttribute4 |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.customAttribute4: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute4 to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # customAttribute5

  Scenario: Create a Device with no 'customAttribute5'
  Login as kapua-sys, go to Devices, create a Device without a customAttribute5.
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
      | clientId | customAttribute5 |
      | dev-123  |                  |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'customAttribute5'
  Login as kapua-sys, go to Devices, create a Device with a short customAttribute5.
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
      | clientId | customAttribute5 |
      | dev-123  | d                |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'customAttribute5'
  Login as kapua-sys, go to Devices, create a Device with a long (valid) customAttribute5.
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
      | clientId | customAttribute5                                                                                                                                                                                                                                                |
      | dev-123  | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'customAttribute5'
  Login as kapua-sys, go to devices, create a Device with too long customAttribute5.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.customAttribute5: Value over than allowed max length. Max length is: 255."
    And I create a device with parameters
      | clientId | customAttribute5                                                                                                                                                                                                                                                 |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then An exception was thrown
    Then I logout

  Scenario: Update a Device with no 'customAttribute5'
  Login as kapua-sys, go to Devices, create a Device and then update the Device without a customAttribute5.
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
      | clientId | customAttribute5 |
      | dev-123  | customAttribute5 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute5 to ""
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'customAttribute5'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a short customAttribute5.
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
      | clientId | customAttribute5 |
      | dev-123  | customAttribute5 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute5 to "d"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'customAttribute5'
  Login as kapua-sys, go to Devices, create a Device and then update the Device with a long (valid) customAttribute5.
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
      | clientId | customAttribute5 |
      | dev-123  | customAttribute5 |
    Then No exception was thrown
    And The device matches the creator parameters
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute5 to "duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then No exception was thrown
    Then The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'customAttribute5'
  Login as kapua-sys, go to devices, create a Device and then update the Device with too long customAttribute5.
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
      | clientId | customAttribute5 |
      | dev-123  | customAttribute5 |
    Then No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.customAttribute5: Value over than allowed max length. Max length is: 255."
    And I search for a device with the client ID "dev-123"
    And I change device customAttribute5 to "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    Then I logout

  #
  # extendedProperties.groupName
  Scenario: Create a Device with no 'extendedProperties.groupName'
  Login as kapua-sys, go to Devices, create a Device without a extendedProperties.groupName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName | name                 | value                 |
      |           | extendedPropertyName | extendedPropertyValue |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument deviceCreator.extendedProperties[].groupName"
    Then I create a device from the existing creator
    Then An exception was thrown
    And I logout

  Scenario: Create a Device with short 'extendedProperties.groupName'
  Login as kapua-sys, go to Devices, create a Device with short extendedProperties.groupName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName | name                 | value                 |
      | d         | extendedPropertyName | extendedPropertyValue |
    Then I create a device from the existing creator
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'extendedProperties.groupName'
  Login as kapua-sys, go to Devices, create a Device with long extendedProperties.groupName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                                                        | name                 | value                 |
      | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r | extendedPropertyName | extendedPropertyValue |
    Then I create a device from the existing creator
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'extendedProperties.groupName'
  Login as kapua-sys, go to Devices, create a Device with too long extendedProperties.groupName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                                                         | name                 | value                 |
      | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r | extendedPropertyName | extendedPropertyValue |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.extendedProperties[].groupName: Value over than allowed max length. Max length is: 64."
    Then I create a device from the existing creator
    Then An exception was thrown
    And I logout

  Scenario: Update a Device with no 'extendedProperties.groupName'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property without a extendedProperties.groupName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument device.extendedProperties[].groupName"
    And I change device extended property to
      | groupName | name                 | value                 |
      |           | extendedPropertyName | extendedPropertyValue |
    And An exception was thrown
    And I logout

  Scenario: Update a Device with short 'extendedProperties.groupName'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with short extendedProperties.groupName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    And I change device extended property to
      | groupName | name                 | value                 |
      | d         | extendedPropertyName | extendedPropertyValue |
    And No exception was thrown
    And The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'extendedProperties.groupName'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with long extendedProperties.groupName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    And I change device extended property to
      | groupName                                                        | name                 | value                 |
      | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r | extendedPropertyName | extendedPropertyValue |
    And No exception was thrown
    And The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'extendedProperties.groupName'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with too long extendedProperties.groupName.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.extendedProperties[].groupName: Value over than allowed max length. Max length is: 64."
    And I change device extended property to
      | groupName                                                         | name                 | value                 |
      | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r | extendedPropertyName | extendedPropertyValue |
    Then An exception was thrown
    And I logout

  #
  # extendedProperties.name

  Scenario: Create a Device with no 'extendedProperties.name'
  Login as kapua-sys, go to Devices, create a Device without a extendedProperties.name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name | value                 |
      | extendedPropertyGroupName |      | extendedPropertyValue |
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument deviceCreator.extendedProperties[].name"
    Then I create a device from the existing creator
    Then An exception was thrown
    And I logout

  Scenario: Create a Device with short 'extendedProperties.name'
  Login as kapua-sys, go to Devices, create a Device with short extendedProperties.name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name | value                 |
      | extendedPropertyGroupName | d    | extendedPropertyValue |
    Then I create a device from the existing creator
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with long 'extendedProperties.name'
  Login as kapua-sys, go to Devices, create a Device with long extendedProperties.name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                                                             | value                 |
      | extendedPropertyGroupName | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r | extendedPropertyValue |
    Then I create a device from the existing creator
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'extendedProperties.name'
  Login as kapua-sys, go to Devices, create a Device with too long extendedProperties.name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                                                              | value                 |
      | extendedPropertyGroupName | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r | extendedPropertyValue |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.extendedProperties[].name: Value over than allowed max length. Max length is: 64."
    Then I create a device from the existing creator
    Then An exception was thrown
    And I logout

  Scenario: Update a Device with no 'extendedProperties.name'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property without a extendedProperties.name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "An illegal null value was provided for the argument device.extendedProperties[].name"
    And I change device extended property to
      | groupName                 | name | value                 |
      | extendedPropertyGroupName |      | extendedPropertyValue |
    And An exception was thrown
    And I logout

  Scenario: Update a Device with short 'extendedProperties.name'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with short extendedProperties.name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    And I change device extended property to
      | groupName                 | name | value                 |
      | extendedPropertyGroupName | d    | extendedPropertyValue |
    And No exception was thrown
    And The device was correctly updated
    And I logout

  Scenario: Update a Device with long 'extendedProperties.name'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with long extendedProperties.name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    And I change device extended property to
      | groupName                 | name                                                             | value                 |
      | extendedPropertyGroupName | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r | extendedPropertyValue |
    And No exception was thrown
    And The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'extendedProperties.name'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with too long extendedProperties.name.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.extendedProperties[].name: Value over than allowed max length. Max length is: 64."
    And I change device extended property to
      | groupName                 | name                                                              | value                 |
      | extendedPropertyGroupName | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r | extendedPropertyValue |
    Then An exception was thrown
    And I logout

  #
  # extendedProperties.value

  Scenario: Create a Device with no 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device without a extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value |
      | extendedPropertyGroupName | extendedPropertyName |       |
    Then I create a device from the existing creator
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with short 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with short extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name | value                 |
      | extendedPropertyGroupName | d    | extendedPropertyValue |
    Then I create a device from the existing creator
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with 255 long 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with 255 long extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                                                                                                                                                                                                                                                           |
      | extendedPropertyGroupName | extendedPropertyName | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    Then I create a device from the existing creator
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with 524288 long 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with 524288 long extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property with very long value to the device creator
    Then I create a device from the existing creator
    Then No exception was thrown
    And The device matches the creator parameters
    And I logout

  Scenario: Create a Device with too long 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with too long extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property with too long value to the device creator
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument deviceCreator.extendedProperties[].value: Value over than allowed max length. Max length is: 524288."
    Then I create a device from the existing creator
    Then An exception was thrown
    And I logout

  Scenario: Update a Device with no 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property without a extendedProperties.value.
  Kapua should not throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And I change device extended property to
      | groupName                 | name                 | value |
      | extendedPropertyGroupName | extendedPropertyName |       |
    And No exception was thrown
    And The device was correctly updated
    And I logout

  Scenario: Update a Device with short 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with short extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    And I change device extended property to
      | groupName                 | name                 | value |
      | extendedPropertyGroupName | extendedPropertyName | d     |
    And No exception was thrown
    And The device was correctly updated
    And I logout

  Scenario: Update a Device with 255 long 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with 255 long extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    And I change device extended property to
      | groupName                 | name                 | value                                                                                                                                                                                                                                                           |
      | extendedPropertyGroupName | extendedPropertyName | duyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL |
    And No exception was thrown
    And The device was correctly updated
    And I logout

  Scenario: Update a Device with 524288 long 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with 524288 long extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    And I change device extended property with very long value to the device
    And No exception was thrown
    And The device was correctly updated
    And I logout

  Scenario: Update a Device with too long 'extendedProperties.value'
  Login as kapua-sys, go to Devices, create a Device with extended properties and update with extended property with too long extendedProperties.value.
  Kapua should throw an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name     | scopeId |
      | AccountA | 1       |
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And A regular device creator
    And I add an extended property to the device creator
      | groupName                 | name                 | value                 |
      | extendedPropertyGroupName | extendedPropertyName | extendedPropertyValue |
    And I create a device from the existing creator
    And No exception was thrown
    And The device matches the creator parameters
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument device.extendedProperties[].value: Value over than allowed max length. Max length is: 524288."
    And I change device extended property with too long value to the device
    And An exception was thrown
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
