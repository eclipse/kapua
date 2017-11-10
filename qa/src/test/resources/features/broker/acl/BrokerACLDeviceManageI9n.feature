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
@brokerAcl
Feature: Broker ACL tests
  These tests are validating correct access control rights of broker security.
  User with one or more profile connects to the broker and tries to issue actions such as
  publish and subscribe to topics, manage topic. Based on his profile these actions are
  successful or not.
  This part of tests is meant for Device manage profile
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
  Topics ({0} account name, {1} client id):
  CTRL_ACC_REPLY = $EDC.{0}.*.*.REPLY.>
  CTRL_ACC_CLI_MQTT_LIFE_CYCLE = $EDC.{0}.{1}.MQTT.>
  CTRL_ACC = $EDC.{0}.>
  CTRL_ACC_CLI = $EDC.{0}.{1}.>
  ACL_DATA_ACC = {0}.>
  ACL_DATA_ACC_CLI = {0}.{1}.>
  ACL_CTRL_ACC_NOTIFY = $EDC.{0}.*.*.NOTIFY.{1}.>

  @StartBroker
  Scenario: Start broker for all scenarios

  @StartDatastore
  Scenario: Start datastore for all scenarios

#
# Data manage
#
  Scenario: DM1 Data manage publish to CTRL_ACC_REPLY is allowed
  Normal user with data manage profile publishes to topic $EDC.{0}.*.*.REPLY.>
  and this is allowed.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
    And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY" with client "client-1"
    And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM2 Data manage create sub-topic on CTRL_ACC_REPLY
  Normal user with data manage profile publishes to topic $EDC.{0}.*.*.REPLY.foo
  This means that foo topic is created and this is allowed as data manage has admin rights
  on REPLY.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
    And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY/foo" with client "client-1"
    And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY/foo"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM3 Data manage subscribe on personal CTRL_ACC_REPLY
  Normal user with data manage profile subscribes to $EDC.{0}.*.*.REPLY
  Subscribe is not allowed, but it is on client's own topic. Is that OK?
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/client-1/CONF-V1/REPLY"
    And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY" with client "client-1"
    And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM4 Data manage subscribe on CTRL_ACC_REPLY of another account
  Normal user with data manage profile subscribes to $EDC.{0}.*.*.REPLY of other account
  Subscribe is not allowed on other account.
    Given Mqtt Device is started
    And data manage account and user are created
    And other broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/domino/client-1/CONF-V1/REPLY"
    Then exception is thrown
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM5 Data manage publish to CTRL_ACC is not allowed
  Normal user with data manage profile publishes to topic $EDC.{0}.>
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
    And string "Hello broker" is published to topic "$EDC/acme" with client "client-1"
    And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM6 Data manage create sub-topic on CTRL_ACC is not allowed
  Normal user with data manage profile publishes to topic $EDC.{0}.foo
  This means that foo topic is not created as data manage has no admin rights on this topic.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
    And string "Hello broker" is published to topic "$EDC/acme/foo" with client "client-1"
    And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme/foo"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM7 Data manage subscribe on CTRL_ACC is not allowed
  Normal user with data manage profile subscribes to $EDC.{0}.>
  Subscribe is not allowed.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme"
    Then exception is thrown
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM8 Data manage subscribe - publish - admin on CTRL_ACC_CLI
  Normal user with data manage profile subscribes to $EDC.{0}.{1}.> and at the same time
  publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/client-1/foo"
    And string "Hello broker" is published to topic "$EDC/acme/client-1/foo" with client "client-1"
    And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/client-1/foo"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM9 Data manage subscribe - publish - admin on ACL_DATA_ACC
  Normal user with data manage profile subscribes to {0}.> and at the same time
  publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "acme/foo"
    And string "Hello broker" is published to topic "acme/foo" with client "client-1"
    And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "acme/foo"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM10 Data manage subscribe - publish - admin on ACL_DATA_ACC_CLI
  Normal user with data manage profile subscribes to {0}.{1}.> and at the same time
  publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "acme/client-1/foo"
    And string "Hello broker" is published to topic "acme/client-1/foo" with client "client-1"
    And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "acme/client-1/foo"
    And clients are disconnected
    And Mqtt Device is stoped

  Scenario: DM11 Data manage publish to ACL_CTRL_ACC_NOTIFY is allowed
  Normal user with data manage profile publishes to topic $EDC.{0}.*.*.NOTIFY.{1}.>
  Publish is allowed, but not subscribe and admin.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
    And string "Hello broker" is published to topic "$EDC/acme/foo/bar/NOTIFY/client-1" with client "client-1"
    And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/foo/bar/NOTIFY/client-1"
    And clients are disconnected
    And Mqtt Device is stoped

#  Scenario: DM12 Data manage create sub-topic on ACL_CTRL_ACC_NOTIFY is not allowed
#    Normal user with data manage profile publishes to topic $EDC.{0}.*.*.NOTIFY.{1}.foo
#    This means that foo topic is not created as data manage has no admin rights on this topic.
#    Given Mqtt Device is started
#      And data manage account and user are created
#    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
#      And string "Hello broker" is published to topic "$EDC/acme/foo/bar/NOTIFY/client-1/foo" with client "client-1"
#      And 1 second passed for message to arrive
#    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme/foo/bar/NOTIFY/client-1/foo"
#      And clients are disconnected
#      And Mqtt Device is stoped

  Scenario: DM13 Data manage subscribe on ACL_CTRL_ACC_NOTIFY is not allowed
  Normal user with data manage profile subscribes to $EDC.{0}.*.*.NOTIFY.{1}.>
  Subscribe is not allowed.
    Given Mqtt Device is started
    And data manage account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/foo/bar/NOTIFY/client-1"
    Then exception is thrown
    And clients are disconnected
    And Mqtt Device is stoped

  @StopBroker
  Scenario: Stop broker after all scenarios

  @StopDatastore
  Scenario: Stop datastore after all scenarios
