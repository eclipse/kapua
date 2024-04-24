###############################################################################
# Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
@user
@userCredentials
@env_none

Feature: Feature file for testing Password user credential
  This feature file provides test scenarios for user password credential.

  @setup
  Scenario: Init Security Context for all scenarios
    Given Init Security Context

  Scenario: Find mfa of the user
  Creating a new user "kapua-a" in kapua-sys account and enabled mfa.
  After that login as "kapua-a" user and find the mfa of myself.
  No exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain     | action |
      | credential | read   |
      | credential | write  |
    And I logout
    And I login as user with name "kapua-a" and password "ToManySecrets123#"
    And I enable mfa
    Then mfa repository can get mfa for user "kapua-a"
