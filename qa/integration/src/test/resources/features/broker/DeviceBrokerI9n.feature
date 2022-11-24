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
@broker
@deviceBroker
@integration

Feature: Device Broker Integration
  Device Service integration scenarios with running broker service.
  Each Scenario starts with BIRTH of device and then the communication over MQTT
  between device and Kapua.

  Scenario: Set environment variables

    Given System property "broker.ip" with value "localhost"
    And System property "commons.db.connection.host" with value "localhost"

  Scenario: Start datastore for all scenarios

    Given Start Datastore

  Scenario: Start event broker for all scenarios

    Given Start Event Broker

  Scenario: Start broker for all scenarios

    Given Start Broker

  Scenario: Send BIRTH message and then DC message
    Effectively this is connect and disconnect of Kura device.
    Basic birth - death scenario.

    When I start the Kura Mock
    And Device birth message is sent
    And I wait 5 seconds
    And I login as user with name "kapua-sys" and password "kapua-password"
    And Packages are requested
    Then Packages are received
    And Bundles are requested
    Then Bundles are received
    And Configuration is requested
    Then Configuration is received
    And Command (ls) is executed
    Then Exit code 0 is received
    And I logout
    And Device death message is sent

  Scenario: Creating a device with disabled status and trying to connect to the broker
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

  Scenario: Creating a device with enabled status and trying to connect to the broker
  Login as kapua-sys, create a device with its status set to ENABLED.
  Then, trying to connect to the broker with a client who's client id equals to the created
  device. Should not result in an authentication failure.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I create a device with parameters
      | clientId | displayName       | modelId         | serialNumber | status   | scopeId |
      | dev-123  | dply-Name_123@#$% | ReliaGate 10-20 | 12541234ABC  | ENABLED | 1       |
    Then No exception was thrown
    Then I logout
    When Client with name "dev-12" with client id "dev-12" user "kapua-broker" password "kapua-password" is connected
    Then No exception was thrown


  Scenario: Stop broker after all scenarios

    Given Stop Broker

  Scenario: Stop event broker for all scenarios

    Given Stop Event Broker

  Scenario: Stop datastore after all scenarios

    Given Stop Datastore
