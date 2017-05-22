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
#
###############################################################################

Feature: Device Registry Integration 
    Device Registy integration test scenarios. These scenarios test higher level device service functionality
    with all services live.

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
