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
      And I wait 5 seconds for broker to start
      And I login as user with name "kapua-sys" and password "kapua-password"
    When I connect to broker with clientId "client-1" and user "kapua-sys" and password "kapua-password" and listening on topic "#"
      And I publish string "Hello world" to topic "/foo/bar"
      And I wait 5 seconds for message to arrive
    Then I receive string "Hello world" on topic "/foo/bar"
    Then I disconnect client
      And I stop Mqtt Device
      And I logout
