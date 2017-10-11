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
Feature: Device Broker Cluster tests
    Test functionality for distributed Stealing link scenarios. This is case for
    cluster of brokers where CONNECT messages get forwarded form one broker to
    others in cluster and by this disconnecting client form other brokers.
    Tests also include connecting client with same id.

    @StartBroker
    Scenario: Start broker for all scenarios

    @StartDatastore
    Scenario: Start datastore for all scenarios

    Scenario: Positive scenario without stealing link
        Connect first client and send BIRTH message. Then connect two more
        clients with different client ids. After that all clients should be connected.
        This is pure positive scenario.

    Given Client with name "client-1" with client id "client-1" user "kapua-broker" password "kapua-password" is connected
        And topic "$EDC/kapua-sys/client-1/MQTT/BIRTH" content "test" is published by client named "client-1"
        And I wait 2 seconds for system to receive and process that message
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
        And topic "$EDC/kapua-sys/client-1/MQTT/BIRTH" content "test" is published by client named "client-1"
        And I wait 2 seconds for system to receive and process that message
        And Client with name "client-2" with client id "client-2" user "kapua-broker" password "kapua-password" is connected
        And Client with name "client-3" with client id "client-3" user "kapua-broker" password "kapua-password" is connected
    Then Client named "client-1" is connected
        And Client named "client-2" is connected
        And Client named "client-3" is connected
    Given topic "$EDC/kapua-sys/client-2/MQTT/CONNECT" content "" is published by client named "client-sys"
        And I wait 2 seconds for system to receive and process that message
    Then Client named "client-1" is connected
        And Client named "client-2" is not connected
        And Client named "client-3" is connected
    Given topic "$EDC/kapua-sys/client-1/MQTT/CONNECT" content "" is published by client named "client-sys"
        And I wait 2 seconds for system to receive and process that message
    Then Client named "client-1" is not connected
        And Client named "client-2" is not connected
        And Client named "client-3" is connected
    Given topic "$EDC/kapua-sys/client-3/MQTT/CONNECT" content "" is published by client named "client-sys"
        And I wait 2 seconds for system to receive and process that message
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
        And topic "$EDC/kapua-sys/client-1/MQTT/BIRTH" content "test" is published by client named "client-1-1"
        And topic "$EDC/kapua-sys/client-1/MQTT/CONNECT" content "" is published by client named "client-1-1"
        And I wait 2 seconds for system to receive and process that message
        And Client with name "client-1-2" with client id "client-1" user "kapua-broker" password "kapua-password" is connected
#        And topic "$EDC/kapua-sys/client-1/MQTT/CONNECT" content "" is published by client named "client-1-2"
        And I wait 2 seconds for system to receive and process that message
    Then Client named "client-1-1" is not connected
        And Client named "client-1-2" is connected
    Then Disconnect client with name "client-1-1"
        And Disconnect client with name "client-1-2"

    @StopBroker
    Scenario: Stop broker after all scenarios

    @StopDatastore
    Scenario: Stop datastore after all scenarios