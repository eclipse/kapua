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
#  Admin
#
  Scenario: A1 User with admin rights publishes arbitrary message to arbitrary topic
    and is successful.
    Given Mqtt Device is started
    When broker with clientId "client-1" and user "kapua-sys" and password "kapua-password" is listening on topic "#"
      And string "Hello world" is published to topic "/foo/bar" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello world" on topic "/foo/bar"
      And clients are disconnected
      And Mqtt Device is stoped
#
# Broker / connect
#
  Scenario: B1 Broker publish to CTRL_ACC_REPLY
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

  Scenario: B2 Broker create sub-topic on CTRL_ACC_REPLY
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

  Scenario: B3 Broker subscribe on personal CTRL_ACC_REPLY
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

  Scenario: B4 Broker subscribe on CTRL_ACC_REPLY of another account
    Normal user with broker connect profile subscribes to $EDC.{0}.*.*.REPLY of other account
    Subscribe is not allowed on other account.
    Given Mqtt Device is started
      And broker account and user are created
      And other broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/domino/client-1/CONF-V1/REPLY"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B5 Broker publish to CTRL_ACC is not allowed
    Normal user with broker connect profile publishes to topic $EDC.{0}.>
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B6 Broker create sub-topic on CTRL_ACC is not allowed
    Normal user with broker connect profile publishes to topic $EDC.{0}.foo
    This means that foo topic is not created as broker has no admin rights on this topic.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B7 Broker subscribe on CTRL_ACC is not allowed
    Normal user with broker connect profile subscribes to $EDC.{0}.>
    Subscribe is not allowed.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B8 Broker subscribe - publish - admin on CTRL_ACC_CLI
    Normal user with broker connect profile subscribes to $EDC.{0}.{1}.> and at the same time
    publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/client-1/foo"
      And string "Hello broker" is published to topic "$EDC/acme/client-1/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/client-1/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B9 Broker publish to ACL_DATA_ACC is not allowed
    Normal user with broker connect profile publishes to topic {0}.>
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "acme"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B10 Broker create sub-topic on ACL_DATA_ACC is not allowed
    Normal user with broker connect profile publishes to topic {0}.foo
    This means that foo topic is not created as broker has no admin rights on this topic.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "acme/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B11 Broker subscribe on ACL_DATA_ACC is not allowed
    Normal user with broker connect profile subscribes to {0}.>
    Subscribe is not allowed.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "acme"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B12 Broker subscribe - publish - admin on ACL_DATA_ACC_CLI
    Normal user with broker connect profile subscribes to {0}.{1}.> and at the same time
    publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "acme/client-1/foo"
      And string "Hello broker" is published to topic "acme/client-1/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "acme/client-1/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: B13 Broker publish to ACL_CTRL_ACC_NOTIFY is allowed
    Normal user with broker connect profile publishes to topic $EDC.{0}.*.*.NOTIFY.{1}.>
    Publish is allowed, but not subscribe and admin.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/foo/bar/NOTIFY/client-1" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/foo/bar/NOTIFY/client-1"
      And clients are disconnected
      And Mqtt Device is stoped

#  Scenario: B14 Broker create sub-topic on ACL_CTRL_ACC_NOTIFY is not allowed
#    Normal user with broker connect profile publishes to topic $EDC.{0}.*.*.NOTIFY.{1}.foo
#    This means that foo topic is not created as broker has no admin rights on this topic.
#    Given Mqtt Device is started
#      And broker account and user are created
#    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
#      And string "Hello broker" is published to topic "$EDC/acme/foo/bar/NOTIFY/client-1/foo" with client "client-1"
#      And 1 second passed for message to arrive
#    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme/foo/bar/NOTIFY/client-1/foo"
#      And clients are disconnected
#      And Mqtt Device is stoped

  Scenario: B15 Broker subscribe on ACL_CTRL_ACC_NOTIFY is not allowed
    Normal user with broker connect profile subscribes to $EDC.{0}.*.*.NOTIFY.{1}.>
    Subscribe is not allowed.
    Given Mqtt Device is started
      And broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/foo/bar/NOTIFY/client-1"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped
#
# Device / manage
#
  Scenario: D1 Device publish to CTRL_ACC_REPLY
    Normal user with device manage profile publishes to topic $EDC.{0}.*.*.REPLY.>
    and this is allowed as it is part of publishing data.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D2 Device create sub-topic on CTRL_ACC_REPLY
    Normal user with device manage profile publishes to topic $EDC.{0}.*.*.REPLY.foo
    This means that foo topic is created and this is allowed as device has admin rights
    on REPLY.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D3 Device subscribe on personal CTRL_ACC_REPLY
    Normal user with device manage profile subscribes to $EDC.{0}.*.*.REPLY
    Subscribe is not allowed, but it is on client's own topic. Is that OK?
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D4 Device subscribe on CTRL_ACC_REPLY of another account
    Normal user with device manage profile subscribes to $EDC.{0}.*.*.REPLY of other account
    Subscribe is not allowed on other account.
    Given Mqtt Device is started
      And device account and user are created
      And other broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/domino/client-1/CONF-V1/REPLY"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D5 Device subscribe - publish - admin on CTRL_ACC
    Normal user with device manage profile subscribes to $EDC.{0}.> and at the same time
    publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/foo"
      And string "Hello broker" is published to topic "$EDC/acme/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D6 Device subscribe - publish - admin on CTRL_ACC_CLI
    Normal user with device manage profile subscribes to $EDC.{0}.{1}.> and at the same time
    publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/client-1/foo"
      And string "Hello broker" is published to topic "$EDC/acme/client-1/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/client-1/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D7 Device publish to ACL_DATA_ACC is not allowed
    Normal user with device manage profile publishes to topic {0}.>
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "acme"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D8 Device create sub-topic on ACL_DATA_ACC is not allowed
    Normal user with device manage profile publishes to topic {0}.foo
    This means that foo topic is not created as device has no admin rights on this topic.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "acme/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D9 Device subscribe on ACL_DATA_ACC is not allowed
    Normal user with device manage profile subscribes to {0}.>
    Subscribe is not allowed.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "acme"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D10 Device subscribe - publish - admin on ACL_DATA_ACC_CLI
    Normal user with device manage profile subscribes to {0}.{1}.> and at the same time
    publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "acme/client-1/foo"
      And string "Hello broker" is published to topic "acme/client-1/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "acme/client-1/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: D11 Device publish to ACL_CTRL_ACC_NOTIFY is allowed
    Normal user with device manage profile publishes to topic $EDC.{0}.*.*.NOTIFY.{1}.>
    Publish is allowed, but not subscribe and admin.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/foo/bar/NOTIFY/client-1" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/foo/bar/NOTIFY/client-1"
      And clients are disconnected
      And Mqtt Device is stoped

#  Scenario: D12 Device create sub-topic on ACL_CTRL_ACC_NOTIFY is not allowed
#    Normal user with device manage profile publishes to topic $EDC.{0}.*.*.NOTIFY.{1}.foo
#    This means that foo topic is not created as broker has no admin rights on this topic.
#    Given Mqtt Device is started
#      And device account and user are created
#    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
#      And string "Hello broker" is published to topic "$EDC/acme/foo/bar/NOTIFY/client-1/foo" with client "client-1"
#      And 1 second passed for message to arrive
#    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme/foo/bar/NOTIFY/client-1/foo"
#      And clients are disconnected
#      And Mqtt Device is stoped

  Scenario: D13 Device subscribe on ACL_CTRL_ACC_NOTIFY is not allowed
    Normal user with device manage profile subscribes to $EDC.{0}.*.*.NOTIFY.{1}.>
    Subscribe is not allowed.
    Given Mqtt Device is started
      And device account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/foo/bar/NOTIFY/client-1"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped
#
# Data view
#
  Scenario: DV1 Data view publish to CTRL_ACC_REPLY
    Normal user with data view profile publishes to topic $EDC.{0}.*.*.REPLY.>
    and this is allowed as it is part of broker connect procedure.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV2 Data view create sub-topic on CTRL_ACC_REPLY
    Normal user with data view profile publishes to topic $EDC.{0}.*.*.REPLY.foo
    This means that foo topic is created and this is allowed as data view has admin rights
    on REPLY.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV3 Data view subscribe on personal CTRL_ACC_REPLY
    Normal user with data view profile subscribes to $EDC.{0}.*.*.REPLY
    Subscribe is not allowed, but it is on client's own topic. Is that OK?
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And string "Hello broker" is published to topic "$EDC/acme/client-1/CONF-V1/REPLY" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV4 Data view subscribe on CTRL_ACC_REPLY of another account
    Normal user with data view profile subscribes to $EDC.{0}.*.*.REPLY of other account
    Subscribe is not allowed on other account.
    Given Mqtt Device is started
      And data view account and user are created
      And other broker account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/domino/client-1/CONF-V1/REPLY"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV5 Data view publish to CTRL_ACC is not allowed
    Normal user with data view profile publishes to topic $EDC.{0}.>
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV6 Data view create sub-topic on CTRL_ACC is not allowed
    Normal user with data view profile publishes to topic $EDC.{0}.foo
    This means that foo topic is not created as data view has no admin rights on this topic.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV7 Data view subscribe on CTRL_ACC is not allowed
    Normal user with data view profile subscribes to $EDC.{0}.>
    Subscribe is not allowed.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV8 Data view subscribe - publish - admin on CTRL_ACC_CLI
    Normal user with data view profile subscribes to $EDC.{0}.{1}.> and at the same time
    publishes to subtopic foo. All this operations are allowed.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/client-1/foo"
      And string "Hello broker" is published to topic "$EDC/acme/client-1/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "$EDC/acme/client-1/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV9 Data view publish to ACL_DATA_ACC is not allowed
    Normal user with data view profile publishes to topic {0}.>
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "acme"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV10 Data view create sub-topic on ACL_DATA_ACC is allowed
    Normal user with data view profile publishes to topic {0}.foo
    This means that foo topic is created as broker has admin rights on this topic, but
    message is not received as publish is not allowed. How is this scenario possible?
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker doesn't receive string "Hello broker" on topic "acme/foo"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV11 Data view subscribe on ACL_DATA_ACC is allowed
    Normal user with data view profile subscribes to {0}.> Admin user publishes to this topic and message
    is received by listening client.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "acme"
      And broker with clientId "admin-1" and user "kapua-sys" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme" with client "admin-1"
      And 1 second passed for message to arrive
    Then client "client-1" receives string "Hello broker" on topic "acme"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV12 Data view publish to ACL_CTRL_ACC_CLI is allowed
    Normal user with data view profile publishes to topic {0}.{1}.>
    Publish is allowed, but not subscribe and admin.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme/client-1" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "acme/client-1"
      And clients are disconnected
      And Mqtt Device is stoped

  Scenario: DV13 Data view create sub-topic on ACL_CTRL_ACC_CLI is not allowed
    Normal user with data view profile publishes to topic {0}.{1}.foo
    This means that foo topic is not created as data view has no admin rights on this topic.
    Because user also has broker connect privilege it out-rules this ACL and can admin this topic.
    Is this correct behaviour?
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "acme/client-1/foo" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "acme/client-1/foo"
      And clients are disconnected
      And Mqtt Device is stoped

#  Scenario: DV14 Data view subscribe on ACL_CTRL_ACC_CLI is not allowed
#    Normal user with data view profile subscribes to {0}.{1}.>
#    Because user also has broker connect privilege it out-rules this ACL and can subscribe to this topic.
#    This out-roule is not applied? Why?
#    Given Mqtt Device is started
#      And data view account and user are created
#    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "acme/client-1"
#    Then exception is not thrown
#      And clients are disconnected
#      And Mqtt Device is stoped

  Scenario: DV15 Data view publish to ACL_CTRL_ACC_NOTIFY is allowed
    Normal user with data view profile publishes to topic $EDC.{0}.*.*.NOTIFY.{1}.>
    Publish is allowed, but not subscribe and admin.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
      And string "Hello broker" is published to topic "$EDC/acme/foo/bar/NOTIFY/client-1" with client "client-1"
      And 1 second passed for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/foo/bar/NOTIFY/client-1"
      And clients are disconnected
      And Mqtt Device is stoped

#  Scenario: DV16 Data view create sub-topic on ACL_CTRL_ACC_NOTIFY is not allowed
#    Normal user with data view profile publishes to topic $EDC.{0}.*.*.NOTIFY.{1}.foo
#    This means that foo topic is not created as data view has no admin rights on this topic.
#    Given Mqtt Device is started
#      And data view account and user are created
#    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic ""
#      And string "Hello broker" is published to topic "$EDC/acme/foo/bar/NOTIFY/client-1/foo" with client "client-1"
#      And 1 second passed for message to arrive
#    Then Broker doesn't receive string "Hello broker" on topic "$EDC/acme/foo/bar/NOTIFY/client-1/foo"
#      And clients are disconnected
#      And Mqtt Device is stoped

  Scenario: DV17 Data view subscribe on ACL_CTRL_ACC_NOTIFY is not allowed
    Normal user with data view profile subscribes to $EDC.{0}.*.*.NOTIFY.{1}.>
    Subscribe is not allowed.
    Given Mqtt Device is started
      And data view account and user are created
    When broker with clientId "client-1" and user "luise" and password "kapua-password" is listening on topic "$EDC/acme/foo/bar/NOTIFY/client-1"
    Then exception is thrown
      And clients are disconnected
      And Mqtt Device is stoped

  @StopBroker
  Scenario: Stop broker after all scenarios

  @StopDatastore
  Scenario: Stop datastore after all scenarios
