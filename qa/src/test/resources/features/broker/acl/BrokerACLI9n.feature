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

  Scenario: User with admin rights publishes arbitrary message to arbitrary topic
  and is successful.
    Given I start Mqtt Device
    When I connect to broker with clientId "client-1" and user "kapua-sys" and password "kapua-password" and listening on topic "#"
      And I publish string "Hello world" to topic "/foo/bar"
      And I wait 5 seconds for message to arrive
    Then I receive string "Hello world" on topic "/foo/bar"
      And I disconnect client
      And I stop Mqtt Device

  Scenario: Broker publish to REPLY
    Normal user with broker connect profile publishes to topic $EDC.{0}.*.*.REPLY.>
    and this is allowed as it is part of broker connect procedure.
    Given I start Mqtt Device
      And I Create broker account and user
    When I login as user with name "luise" and password "kapua-password"
      And I connect to broker with clientId "client-1" and user "luise" and password "kapua-password" and listening on topic ""
      And I publish string "Hello broker" to topic "$EDC/acme/client-1/CONF-V1/REPLY"
      And I wait 5 seconds for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY"
    And I disconnect client
    And I stop Mqtt Device

  Scenario: Broker create sub-topic on REPLY
    Normal user with broker connect profile publishes to topic $EDC.{0}.*.*.REPLY.foo
    This means that foo topic is created and this is allowed as broker has admin rights
    on REPLY.
    Given I start Mqtt Device
      And I Create broker account and user
    When I login as user with name "luise" and password "kapua-password"
      And I connect to broker with clientId "client-1" and user "luise" and password "kapua-password" and listening on topic ""
      And I publish string "Hello broker" to topic "$EDC/acme/client-1/CONF-V1/REPLY/foo"
      And I wait 5 seconds for message to arrive
    Then Broker receives string "Hello broker" on topic "$EDC/acme/client-1/CONF-V1/REPLY/foo"
    And I disconnect client
    And I stop Mqtt Device
