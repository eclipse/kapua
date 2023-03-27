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
@security
@env_none

Feature: User Credential
  This feature file contains Unit tests for User Profile.

  @setup
  Scenario: Initialize test environment
    Given Init Jaxb Context
    And Init Security Context

  Scenario: Change User Profile correctly
  Create a user, login with it, change the user profile and then check if the operation is performed correctly.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I change the profile to the following
      | displayName | phoneNumber | email       |
      | Foo         | 424242      | foo@bar.com |
    When I search for user with name "kapua-sys"
    Then I find user
      | kapua-sys | Foo | foo@bar.com | 424242 | ENABLED |
    And I logout

  Scenario: Change User Profile correctly to all blank values
  Create a user, login with it, change the user profile with all fields blank, and then check if the operation is performed correctly.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I change the profile to the following
      | displayName | phoneNumber | email |
      |             |             |       |
    When I search for user with name "kapua-sys"
    Then I find user
      | kapua-sys |  |  |  | ENABLED |
    And I logout

  Scenario: Change User Profile incorrectly
  Create a user, login with it, change the user profile incorrectly, and then check if the exception is occurred.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument userProfile.email: foo.com."
    When I change the profile to the following
      | displayName | phoneNumber | email   |
      | Foo         | 424242      | foo.com |
    Then An exception was thrown
    And I logout

  Scenario: Read User Profile correctly
  Create a user, login with it, and then read its user profile.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I read the following user profile
      | displayName    | phoneNumber     | email                 |
      | Kapua Sysadmin | +1 555 123 4567 | kapua-sys@eclipse.org |
    And I logout
