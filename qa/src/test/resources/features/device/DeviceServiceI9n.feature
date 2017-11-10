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
@device
Feature: Device Registry Integration
    Device Registy integration test scenarios. These scenarios test higher level device service functionality
    with all services live.

  @StartBroker
  Scenario: Start broker for all scenarios

  @StartDatastore
  Scenario: Start datastore for all scenarios

Scenario: Birth message handling from a new device
    A birth message is received. The referenced device does not yet exist and is created on-the-fly. After the
    message is processed a new device must be created and a BIRTH event inserted in the database.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    And A birth message from device "device_1"
    When I search for the device "device_1" in account "AccountA"
    Then I find 1 device
    When I search for events from device "device_1" in account "AccountA"
    Then I find 1 event
    And The type of the last event is "BIRTH"
    And I logout

Scenario: Birth message handling from an existing device
    A BIRTH message is received from an already existing device. A new BIRTH event must be
    inserted into the database.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    And A device such as
      | clientId | displayName | modelId         | serialNumber |
      | device_1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
    And A birth message from device "device_1"
    When I search for events from device "device_1" in account "AccountA"
    Then I find 1 event
    And The type of the last event is "BIRTH"
    And I logout

Scenario: Handling of 2 birth messages
    Two BIRTH messages are received from a device. No exception should be thrown and there should be
    2 BIRTH events in the database.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    And A birth message from device "device_1"
    And A birth message from device "device_1"
    When I search for events from device "device_1" in account "AccountA"
    Then I find 2 events
    And The type of the last event is "BIRTH"
    And I logout

Scenario: Handling of a disconnect message from a non existing device
    Reception of a DISCONNECT message with a nonexistent client ID should result in an exception.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
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
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    Given A birth message from device "device_1"
    And A disconnect message from device "device_1"
    When I search for events from device "device_1" in account "AccountA"
    Then I find 2 events
    And The type of the last event is "DEATH"
    And I logout

Scenario: Birth and missing event handling
    Reception of a BIRTH-MISSING pair. The first message (BIRTH) should cause a device to be
    created in the database, allowing the successive MISSING message to be successsfully
    processsed. After the messages the database should contain two events (both BIRTH
    and MISSING).

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    Given A birth message from device "device_1"
    And A missing message from device "device_1"
    When I search for events from device "device_1" in account "AccountA"
    Then I find 2 events
    And The type of the last event is "MISSING"
    And I logout

Scenario: Birth and applications event handling
    Reception of a BIRTH-APPLICATION pair. The first message (BIRTH) should cause a device to be
    created in the database, allowing the successive APPLICATION message to be successsfully
    processsed. After the messages the database should contain two events (both BIRTH
    and APPLICATION).

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    Given A birth message from device "device_1"
    And An application message from device "device_1"
    When I search for events from device "device_1" in account "AccountA"
    Then I find 2 events
    And The type of the last event is "APPLICATION"
    And I logout

Scenario: Creating new device and tagging it with specific Tag
  Procedure of registering a device is executed and device BIRTH message is sent.
  After that device is tagged with Tag KuraDevice and searched by this same tag.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Given Account
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    And I configure the tag service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 10     |
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
      | name      | scopeId |
      | AccountA  | 1       |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
    Given User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | UserA   | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I configure the device service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  10   |
    And I configure the tag service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 10     |
    And A device such as
      | clientId | displayName | modelId         | serialNumber |
      | device_2 | testGateway3 | ReliaGate 10-20 | 12541234ABC  |
    And A birth message from device "device_1"
    And I search for the device "device_1" in account "AccountA"
    And I tag device with "KuraDevice2" tag
    When I search for device with tag "KuraDevice2"
    Then I find device "device_1"
    And I untag device with "KuraDevice2" tag
    And I verify that tag "KuraDevice2" is deleted
    And I logout

  @StopBroker
  Scenario: Stop broker after all scenarios

  @StopDatastore
  Scenario: Stop datastore after all scenarios
