###############################################################################
# Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
@env_docker

Feature: Device Broker Integration
  Device Service integration scenarios with running broker service.
  Each Scenario starts with BIRTH of device and then the communication over MQTT
  between device and Kapua.

@setup
  Scenario: Start full docker environment
    Given Init Jaxb Context
    And Init Security Context
    And Start full docker environment

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

@teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
