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
@broker
Feature: Device Broker Integration
  Device Service integration scenarios with running broker service.
  Each Scenario starts with BIRTH of device and then the communication over MQTT
  between device and Kapua.

  @StartBroker
  Scenario: Start broker for all scenarios

  @StartDatastore
  Scenario: Start datastore for all scenarios

  Scenario: Send BIRTH message and then DC message
    Effectively this is connect and disconnect of Kura device.
    Basic birth - death scenario.

    When I start the Kura Mock
    And Device birth message is sent
    And I wait 5 seconds for system to receive and process that message
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

  @StopBroker
  Scenario: Stop broker after all scenarios

  @StopDatastore
  Scenario: Stop datastore after all scenarios

