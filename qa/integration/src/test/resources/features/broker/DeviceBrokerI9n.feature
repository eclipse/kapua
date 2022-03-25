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
    And Command "ls" is executed
    Then Exit code 0 is received
    And I logout
    And Device death message is sent

  Scenario: Test the stealing link handling with multiple clients connecting at the same time

    When I prepare 10 clients with "client-stealing-link" as client id in a pool called "stealing" with username "kapua-broker" password "kapua-password" and brokerUrl "tcp://localhost:1883" and if connected disconnect after 5 seconds
    And I connect the pool called "stealing"
    Then Only 1 client of the pool called "stealing" is still connected within 10 seconds

#TODO
#disable for now. Wait for further investigation.
#  Scenario: Test the stealing link handling with 2 clients with same client id but different account (they should be able to connect both)
#    Given I login as user with name "kapua-sys" and password "kapua-password"
#    Given broker account "acme-1" with organization "acme-org-1" and email "acme-1@acme.com" and user "acme-1" are created
#    Given broker account "acme-2" with organization "acme-org-2" and email "acme-2@acme.com" and user "acme-2" are created
#    And Connect client with clientId "clientId" and user "acme-1" and password "KeepCalm123." and keep into device group "stealing-link-group"
#    And I wait 3 seconds
#    And Connect client with clientId "clientId" and user "acme-2" and password "KeepCalm123." and keep into device group "stealing-link-group"
#    And I wait 3 seconds
#    Then Clients from group "stealing-link-group" are connected

@teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
