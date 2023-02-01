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
  This feature file contains Unit tests for User Credentials.

  @setup
  Scenario: Initialize test environment
    Given Init Jaxb Context
    And Init Security Context

  Scenario: Change the PASSWORD credential, providing a correct current password, with a new password meeting the standard requirements.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create default test-user
    And I create a new PASSWORD credential for the default user with password "Welcome12345!"
    And I login as user with name "test-user" and password "Welcome12345!"
    And No exception was thrown
    When I change the user credential password with old password "Welcome12345!" and new password "Welcome12345!_"
    Then No exception was thrown


  Scenario: Change the PASSWORD credential, providing a correct current password, with an illegal new password.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create default test-user
    And I create a new PASSWORD credential for the default user with password "Welcome12345!"
    And I login as user with name "test-user" and password "Welcome12345!"
    And No exception was thrown
    Given I expect the exception "PasswordLengthException" with the text "Password length must be between 12 and 255 characters long (inclusive)."
    When I change the user credential password with old password "Welcome12345!" and new password "We1!"
    Then An exception was thrown
    Given I expect the exception "PasswordLengthException" with the text "Password length must be between 12 and 255 characters long (inclusive)."
    When I change the user credential password with old password "Welcome12345!" and new password "Welcome12345!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    Then An exception was thrown
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument passwordChangeRequest.newPassword: Welcome12345."
    When I change the user credential password with old password "Welcome12345!" and new password "Welcome12345"
    Then An exception was thrown
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument passwordChangeRequest.newPassword: welcome12345!."
    When I change the user credential password with old password "Welcome12345!" and new password "welcome12345!"
    Then An exception was thrown
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument passwordChangeRequest.newPassword: welcomeeeeee!."
    When I change the user credential password with old password "Welcome12345!" and new password "welcomeeeeee!"
    Then An exception was thrown

  Scenario: Change the PASSWORD credential, providing an invalid current password, with a new password.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create default test-user
    And I create a new PASSWORD credential for the default user with password "Welcome12345!"
    And I login as user with name "test-user" and password "Welcome12345!"
    And No exception was thrown
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument passwordChangeRequest.oldPassword: WrongPassword!"
    When I change the user credential password with old password "WrongPassword!" and new password "Welcome12345!"
    Then An exception was thrown
