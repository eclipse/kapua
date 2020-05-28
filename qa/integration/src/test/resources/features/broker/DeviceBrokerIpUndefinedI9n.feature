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
@deviceBrokerIpUndefined
@env_docker

Feature: Device Broker connection ip not set
  Device Service integration scenarios with running broker service.

@setup
  Scenario: Start full docker environment
    Given Init Jaxb Context
    And Init Security Context
    And System property "broker.ip" with value "null"
    And System property "kapua.config.url" with value "null"
    And Start full docker environment

  Scenario: Send BIRTH message and then DC message while broker ip is NOT set
  Effectively this is connect and disconnect of Kura device.
  Basic birth - death scenario. Scenario should fail as broker ip is not set
  as it should be.

    When I start the Kura Mock
    And Device birth message is sent
    And I wait 5 seconds
    And Device death message is sent
    And I wait 5 seconds

@teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
