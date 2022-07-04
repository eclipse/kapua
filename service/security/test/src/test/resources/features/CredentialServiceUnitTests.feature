###############################################################################
# Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
@unit
Feature: Credentials
  This feature file contains Unit tests for Credentials (CRUD tests).

  Scenario: Creating a new PASSWORD Credential meeting the standard length requirement
  Create a new Credential of type PASSWORD that meets the default string length requirements
    Given I create a new PASSWORD credential for the default user with password "Welcome12345!"
    Then No exception was thrown

  Scenario: Creating a new PASSWORD Credential twice gives DuplicatedPasswordCredentialException
    And I create a new PASSWORD credential for the default user with password "Welcome12345!"
    Then No exception was thrown
    And I expect the exception "DuplicatedPasswordCredentialException" with the text "The user already has a Credential of type PASSWORD."
    When I create a new PASSWORD credential for the default user with password "Welcome12345!"
    Then An exception was thrown

  Scenario: Creating a new PASSWORD Credential not meeting the standard length requirement
  Create a new Credential of type PASSWORD that does NOT meet the default string length requirements
    And I expect the exception "PasswordLengthException" with the text "Password length must be between 12 and 255 characters long (inclusive)."
    When I create a new PASSWORD credential for the default user with password "badPass"
    Then An exception was thrown

  Scenario: Creating a new PASSWORD Credential not meeting the standard length requirement
  Create a new Credential of type PASSWORD that does NOT meet the default string length requirements
    And I expect the exception "PasswordLengthException" with the text "Password length must be between 12 and 255 characters long (inclusive)."
    When I create a new PASSWORD credential for the default user with password "ThisIsLongPasswordToTestTheMaximumPasswordLengthValidationOf255Chars!ThisIsLongPasswordToTestTheMaximumPasswordLengthValidationOf255Chars!ThisIsLongPasswordToTestTheMaximumPasswordLengthValidationOf255Chars!ThisIsLongPasswordToTestTheMaximumPasswordLengthValidationOf255Chars!"
    Then An exception was thrown

  Scenario: Set a correct minimum for password length
  Set a custom minimum value for password length that must be equal or higher to the default system limit
    Given I configure the credential service
      | type    | name                       | value |
      | boolean | lockoutPolicy.enabled      | true  |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 3600  |
      | integer | lockoutPolicy.lockDuration | 10800 |
      | integer | password.minLength         | 15    |
    Then No exception was thrown

  Scenario: Set an incorrect minimum for password length
  Set a custom minimum value for password length that is not equal or higher to the default system limit must result in
  an error
    And I expect the exception "KapuaIllegalArgumentException"
    And I configure the credential service
      | type    | name                       | value |
      | boolean | lockoutPolicy.enabled      | true  |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 3600  |
      | integer | lockoutPolicy.lockDuration | 10800 |
      | integer | password.minLength         | 10    |
    Then An exception was thrown
    When I read credential service configuration
    Then I check that service configuration "password.minLength" has no value

  Scenario: Creating a new PASSWORD Credential meeting a custom length requirement
  Create a new Credential of type PASSWORD that meets a custom string length requirements
    Given I configure the credential service
      | type    | name                       | value |
      | boolean | lockoutPolicy.enabled      | true  |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 3600  |
      | integer | lockoutPolicy.lockDuration | 10800 |
      | integer | password.minLength         | 15    |
    And I create a new PASSWORD credential for the default user with password "Welcome12345678!"
    Then No exception was thrown

  Scenario: Creating a new PASSWORD Credential not meeting a custom length requirement
  Create a new Credential of type PASSWORD that does NOT meet a custom string length requirements
    Given I configure the credential service
      | type    | name                       | value |
      | boolean | lockoutPolicy.enabled      | true  |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 3600  |
      | integer | lockoutPolicy.lockDuration | 10800 |
      | integer | password.minLength         | 15    |
    And I expect the exception "PasswordLengthException" with the text "Password length must be between 15 and 255 characters long (inclusive)"
    When I create a new PASSWORD credential for the default user with password "Welcome12345!"
    Then An exception was thrown
