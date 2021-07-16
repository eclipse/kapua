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
@deviceBrokerIpSysEvn
@env_docker

Feature: Device Broker connection ip with System environment variable
  Device Service integration scenarios with running broker service.

@setup
  Scenario: Start full docker environment
    Given Init Jaxb Context
    And Init Security Context
    And System property "broker.ip" with value "localhost"
    And Start full docker environment

  Scenario: Send BIRTH message and then DC message while broker ip is set by System
  environment variable. Effectively this is connect and disconnect of Kura device.
  Basic birth - death scenario. Scenario includes check that broker server ip
  is correctly set with System environment variable.

    When I start the Kura Mock
    And Device birth message is sent
    And I wait 5 seconds
    And I login as user with name "kapua-sys" and password "kapua-password"
    Then Device is connected with "message-broker" server ip
    And I logout
    And Device death message is sent

@teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
