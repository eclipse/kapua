###############################################################################
# Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@integration
@deviceRegistry

Feature: Device Registry Integration
  Device Registy integration test scenarios. These scenarios test higher level device service functionality
  with all services live.

  Scenario: Set environment variables

    Given System property "commons.settings.hotswap" with value "true"
    And System property "broker.ip" with value "localhost"
    And System property "kapua.config.url" with value "null"

  Scenario: Birth message handling from a new device
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
    When I search for events from device "device_1" in account "AccountA"
    Then I find 1 device event
    And The type of the last event is "BIRTH"
    And I logout

  Scenario: Birth message handling from an existing device
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
    When I search for events from device "device_1" in account "AccountA"
    Then I find 1 device event
    And The type of the last event is "BIRTH"
    And I logout

  Scenario: Handling of 2 birth messages
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
    When I search for events from device "device_1" in account "AccountA"
    Then I find 2 device events
    And The type of the last event is "BIRTH"
    And I logout

  Scenario: Handling of a disconnect message from a non existing device
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
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When A disconnect message from device "device_1"
    Then An exception was thrown
    And I logout

  Scenario: Birth and death message handling
  Reception of a BIRTH-DISCONNECT pair. The first message (BIRTH) should cause a device to be
  created in the database, allowing the successive DISCONNECT message to be successsfully
  processsed. After the messages the database should contain two events (both BIRTH
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
    When I search for events from device "device_1" in account "AccountA"
    Then I find 2 device events
    And The type of the last event is "DEATH"
    And I logout

  Scenario: Birth and missing event handling
  Reception of a BIRTH-MISSING pair. The first message (BIRTH) should cause a device to be
  created in the database, allowing the successive MISSING message to be successsfully
  processsed. After the messages the database should contain two events (both BIRTH
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
    When I search for events from device "device_1" in account "AccountA"
    Then I find 2 device events
    And The type of the last event is "MISSING"
    And I logout

  Scenario: Birth and applications event handling
  Reception of a BIRTH-APPLICATION pair. The first message (BIRTH) should cause a device to be
  created in the database, allowing the successive APPLICATION message to be successsfully
  processsed. After the messages the database should contain two events (both BIRTH
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
    When I search for events from device "device_1" in account "AccountA"
    Then I find 2 device events
    And The type of the last event is "APPLICATION"
    And I logout

  Scenario: Creating new device and tagging it with specific Tag
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

  Scenario: Creating new device, tagging it with specific Tag and then deleting this Tag
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

  Scenario: Creating A Device With Unique Name
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

  Scenario: Creating A Device With Non-unique Name
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
    Given I expect the exception "KapuaDuplicateNameException" with the text "*"
    When I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | Device1  | testGateway1 | ReliaGate 10-21 | 99541234ABC  |
    Then An exception was thrown
    And I logout

  Scenario: Creating A Device With Short Name
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

  Scenario: Creating A Device With No Name
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
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I create a device with name ""
    Then An exception was thrown
    And I logout

  Scenario: Creating A Device With Name Containing Permitted Symbols
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

  Scenario: Creating A Device With Name Containing Invalid Symbols
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
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I try to create devices with invalid symbols in name
    Then An exception was thrown
    And I logout

  Scenario: Creating A Device With Long Name
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

  Scenario: Creating A Device With Too Long Name
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
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create a device with name "aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL"
    Then An exception was thrown
    And I logout

  Scenario: Creating A Device With Non-unique Display Name
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

  Scenario: Creating a Device With Unique Display Name
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
      | clientId | displayName  | modelId         | serialNumber |
      | dev-123  | displayName1 | ReliaGate 10-20 | 12541234ABC  |
    And I create a device with parameters
      | clientId | displayName  | modelId         | serialNumber |
      | dev-222  | displayName1 | ReliaGate 10-20 | 12541234ABC  |
    Then No exception was thrown
    And I logout

  Scenario: Creating A Device With Short Display Name
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

  Scenario: Creating A Device With Long Display Name
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

  Scenario: Creating A Device With Too Long Display Name
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
    Given I expect the exception "KapuaException" with the text "*"
    And I create a device with parameters
      | clientId | displayName                                                                                                                                                                                                                                                      | modelId         | serialNumber |
      | dev-123  | aduyTkz0YUAlEonkehWsSOMQslBFu1GlXG5D3iadhSu5nHDrofVdWRX4mI5tzdt8r0EaAXJpHf06C4DRdiloQ7yuxZWG5web2szcuu43Hf4Bz3QPYxs1wXl5m40ZytbV3AZBI70SD99mDUImj3X66gW1G5nz5QXelhyNXEEuuPyBMmJPcgJ6w7Y1ZZC1AwDr4ShH3c2lgCyzKQcMREpCHFGWF4wK4dsF1hWa63Q4gAthqiDHIhhqBwxQuzce8ziL | ReliaGate 10-20 | 12541234ABC  |
    Then An exception was thrown
    Then I logout

  Scenario: Creating a Device With Permitted Symbols in its Display Name
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

  Scenario: Creating A Device With Disabled Status
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

  Scenario: Creating A Device With Enabled Status
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

  Scenario: Changing Client ID
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

  Scenario: Changing Device Status To Enabled
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

  Scenario: Changing Device Status To Disabled
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

  Scenario: Deleting Device With Enabled Status
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

  Scenario: Deleting Device With Disabled Status
  Login as kapua-sys, go to devices, create a device with "disabled" status.
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
      | clientId | displayName  | modelId         | serialNumber | status   |
      | dev-123  | displayNam22 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
    And I delete the device with the clientId "dev-123"
    When I search for a device with the client ID "dev-123"
    Then There is no such device
    Then No exception was thrown
    And I logout

  Scenario: Search By Client ID And Get One Match
  Login as kapua-sys, go to devices, create a device.
  Try to find it by Client ID.
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
      | test123  | displayNam21 | ReliaGate 10-20 | 12541234ABC  | DISABLED |
    And I filter devices by
      | clientId |
      | d        |
    Then I find 1 device
    And I logout