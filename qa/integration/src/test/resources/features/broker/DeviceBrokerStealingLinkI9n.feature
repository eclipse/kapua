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
@stealingLink
@env_docker

Feature: Device Broker Cluster tests
    Test functionality for distributed Stealing link scenarios. This is case for
    cluster of brokers where CONNECT messages get forwarded form one broker to
    others in cluster and by this disconnecting client form other brokers.
    Tests also include connecting client with same id.

@setup
  Scenario: Start full docker environment
    Given Init Jaxb Context
    And Init Security Context
    And Start full docker environment

    Scenario: Positive scenario without stealing link
        Connect first client and send BIRTH message. Then connect two more
        clients with different client ids. After that all clients should be connected.
        This is pure positive scenario.

    Given Client with name "client-1" with client id "client-1" user "kapua-broker" password "kapua-password" is connected
        And topic "$EDC/kapua-sys/client-1/MQTT/BIRTH" content "/mqtt/rpione3_MQTT_BIRTH.mqtt" is published by client named "client-1"
        And I wait 2 seconds
        And Client with name "client-2" with client id "client-2" user "kapua-broker" password "kapua-password" is connected
        And Client with name "client-3" with client id "client-3" user "kapua-broker" password "kapua-password" is connected
    Then Client named "client-1" is connected
        And Client named "client-2" is connected
        And Client named "client-3" is connected
    Then Disconnect client with name "client-1"
        And Disconnect client with name "client-2"
        And Disconnect client with name "client-3"

    Scenario: Stealing link scenario
        Connect first client and send BIRTH message. Then connect two more
        clients with different client ids.After that all clients should be connected.
        then connect another client under admin account for simulating other broker and
        send CONNECT messages for all three clients with their client ids. This disconnects
        all clients locally. This emulates that those clients were connected on another broker.

    Given Client with name "client-1" with client id "client-1" user "kapua-broker" password "kapua-password" is connected
        And Client with name "client-sys" with client id "client-sys" user "kapua-sys" password "kapua-password" is connected
        And topic "$EDC/kapua-sys/client-1/MQTT/BIRTH" content "/mqtt/rpione3_MQTT_BIRTH.mqtt" is published by client named "client-1"
        And I wait 2 seconds
        And Client with name "client-2" with client id "client-2" user "kapua-broker" password "kapua-password" is connected
        And Client with name "client-3" with client id "client-3" user "kapua-broker" password "kapua-password" is connected
    Then Client named "client-1" is connected
        And Client named "client-2" is connected
        And Client named "client-3" is connected
    Given topic "$EDC/kapua-sys/client-2/MQTT/CONNECT" content "/mqtt/rpione3_MQTT_BIRTH.mqtt" is published by client named "client-sys"
        And I wait 2 seconds
    Then Client named "client-1" is connected
        And Client named "client-2" is not connected
        And Client named "client-3" is connected
    Given topic "$EDC/kapua-sys/client-1/MQTT/CONNECT" content "/mqtt/rpione3_MQTT_BIRTH.mqtt" is published by client named "client-sys"
        And I wait 2 seconds
    Then Client named "client-1" is not connected
        And Client named "client-2" is not connected
        And Client named "client-3" is connected
    Given topic "$EDC/kapua-sys/client-3/MQTT/CONNECT" content "/mqtt/rpione3_MQTT_BIRTH.mqtt" is published by client named "client-sys"
        And I wait 2 seconds
    Then Client named "client-1" is not connected
        And Client named "client-2" is not connected
        And Client named "client-3" is not connected
    Then Disconnect client with name "client-1"
        And Disconnect client with name "client-2"
        And Disconnect client with name "client-3"

    Scenario: Negative scenario when client connects twice with same client id
        Connect first client and send BIRTH message and CONNECT message. Then
        connect another client with same client id and send CONNECT message.
        This disconnects first client.

    Given Client with name "client-1-1" with client id "client-1" user "kapua-broker" password "kapua-password" is connected
        And topic "$EDC/kapua-sys/client-1/MQTT/BIRTH" content "/mqtt/rpione3_MQTT_BIRTH.mqtt" is published by client named "client-1-1"
        And topic "$EDC/kapua-sys/client-1/MQTT/CONNECT" content "/mqtt/rpione3_MQTT_BIRTH.mqtt" is published by client named "client-1-1"
        And I wait 2 seconds
        And Client with name "client-1-2" with client id "client-1" user "kapua-broker" password "kapua-password" is connected
        And I wait 2 seconds
    Then Client named "client-1-1" is not connected
        And Client named "client-1-2" is connected
    Then Disconnect client with name "client-1-1"
        And Disconnect client with name "client-1-2"

@teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
