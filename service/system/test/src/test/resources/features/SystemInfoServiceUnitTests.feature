###############################################################################
# Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech
###############################################################################
@env_none

Feature: System Info
  This feature file contains unit tests for System Info.

  @setup
  @KapuaProperties("locator.class.impl=org.eclipse.kapua.qa.common.MockedLocator")
  Scenario: Initialize test environment
    Given Init Jaxb Context
    And Init Security Context


  Scenario: Retrieve the system info, then check if everything match with provided properties.
    When I retrieve the system info
    Then The version of the system is "kapuaVersion42"
    And The build number of the system is "kapuaBuildNumber42"
    And The build revision of the system is "42a24b"
    And The build timestamp of the system is "1677748276 UTC"
    And The build branch of the system is "test-branch"