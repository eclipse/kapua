###############################################################################
# Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
@jobs
@jobService
@env_none

Feature: Job Step Service CRUD tests
  The JobStepService is responsible for maintaining jobs.

  @setup
  @KapuaProperties("locator.class.impl=org.eclipse.kapua.qa.common.MockedLocator")
  Scenario: Initialize test environment
    Given Init Jaxb Context
    And Init Security Context
    Given A regular definition creator with the name "TestDefinition"
    And I create a new step definition entity from the existing creator

  Scenario: Create a step
    Given I create a regular step creator with the name "Step1"


  @teardown
  Scenario: Reset Security Context for all scenarios
    Given Reset Security Context
