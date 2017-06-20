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
Feature: Broker ACL tests
  These tests are validating correct access control rights of broker security.
  User with one or more profile connects to the broker and tries to issue actions such as
  publish and subscribe to topics, manage topic. Based on his profile these actions are
  successful or not.
  --
  Profile:
    broker connect  B
    device manage   DEV_M
    data view       DATA_V
    data manage     DATA_M
  --
  ACL tests:
                          B      DEV_M    DATA_V    DATA_M
  CTRL_ACC_REPLY        X/P/A    X/P/A    X/P/A     X/P/A
  CTRL_ACC              X/X/X    S/P/A    X/X/X     X/X/X
  CTRL_ACC_CLI          S/P/A    S/P/A    S/P/A     S/P/A
  ACL_DATA_ACC          X/X/X    X/X/X    S/X/A     S/P/A
  ACL_DATA_ACC_CLI      S/P/A    S/P/A    X/P/X     S/P/A
  ACL_CTRL_ACC_NOTIFY   X/P/X    X/P/X    X/P/X     X/P/X
  --
  Topics:
  CTRL_ACC_REPLY = $EDC.{0}.*.*.REPLY.>
  CTRL_ACC_CLI_MQTT_LIFE_CYCLE = $EDC.{0}.{1}.MQTT.>
  CTRL_ACC = $EDC.{0}.>
  CTRL_ACC_CLI = $EDC.{0}.{1}.>
  ACL_DATA_ACC = {0}.>
  ACL_DATA_ACC_CLI = {0}.{1}.>
  ACL_CTRL_ACC_NOTIFY = $EDC.{0}.*.*.NOTIFY.{1}.>

  Scenario: User with admin rights publishes arbitrary message to arbitrary topic
    and is successful.
    Given Mqtt Device is started
    When broker with clientId "client-1" and user "kapua-sys" and password "kapua-password" is listening on topic "#"
      And string "Hello world" is published to topic "/foo/bar" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello world" on topic "/foo/bar"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: Broker publish to CTRL_ACC_REPLY
    Normal user with broker connect profile publishes to topic $EDC.{0}.*.*.REPLY.>
    and this is allowed as it is part of broker connect procedure.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: Broker create sub-topic on CTRL_ACC_REPLY
    Normal user with broker connect profile publishes to topic $EDC.{0}.*.*.REPLY.foo
    This means that foo topic is created and this is allowed as broker has admin rights
    on REPLY.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY/foo"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: Broker subscribe on personal CTRL_ACC_REPLY
    Normal user with broker connect profile subscribes to $EDC.{0}.*.*.REPLY
    Subscribe is not allowed, but it is on client's own topic. Is that OK?
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: Broker subscribe on CTRL_ACC_REPLY of another account
    Normal user with broker connect profile subscribes to $EDC.{0}.*.*.REPLY of other account
    Subscribe is not allowed on other account.
    Given Mqtt Device is started
      And broker account and user are created
      And other broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/domino/client-1/CONF-V1/REPLY"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped