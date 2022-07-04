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
#     Eurotech - initial API and implementation
###############################################################################
@account
@credential
@integration

Feature: Account Credential Service Integration Tests

Scenario: Init Security Context for all scenarios

  Given Init Security Context

  Scenario: Uncorrect Login While Lockout Policy Is Enabled
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true and lockoutPolicy.maxFailures to 2, leave other fields at default values
  Login twice as user1 with wrong password
  Try to login again with correct credentials
  User should be locked

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | true   |
      | integer | lockoutPolicy.maxFailures  | 2      |
      | integer | lockoutPolicy.resetAfter   | 3600   |
      | integer | lockoutPolicy.lockDuration | 108000 |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Then An exception was thrown
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongAgain"
    Then An exception was thrown
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "ToManySecrets123#"
    Then An exception was thrown

  Scenario: Uncorrect Login While Lockout Policy Is Disabled
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to false and lockoutPolicy.maxFailures to 2, leave other fields at default values
  Login twice as user1 with wrong password
  Try to login again with correct credentials
  There should be no problems as  lockoutPolicy is disabled

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | false  |
      | integer | lockoutPolicy.maxFailures  | 2      |
      | integer | lockoutPolicy.resetAfter   | 3600   |
      | integer | lockoutPolicy.lockDuration | 108000 |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Then An exception was thrown
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongAgain"
    Then An exception was thrown
    When I login as user with name "user1" and password "ToManySecrets123#"
    Then No exception was thrown
    Then I logout

  Scenario: Setting Max Failures To 0
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true and lockoutPolicy.maxFailures to 0, leave other fields at default values
  Login as user1 with wrong password
  Try to login again with correct credentials
  Exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | true   |
      | integer | lockoutPolicy.maxFailures  | 0      |
      | integer | lockoutPolicy.resetAfter   | 3600   |
      | integer | lockoutPolicy.lockDuration | 108000 |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Then An exception was thrown
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "ToManySecrets123#"
    Then An exception was thrown
    Then I logout

  Scenario: Setting Max Failures To 1
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true and lockoutPolicy.maxFailures to 1, leave other fields at default values
  Login as user1 with wrong password
  Try to login again with correct credentials
  Exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | true   |
      | integer | lockoutPolicy.maxFailures  | 1      |
      | integer | lockoutPolicy.resetAfter   | 3600   |
      | integer | lockoutPolicy.lockDuration | 108000 |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Then An exception was thrown
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "ToManySecrets123#"
    Then An exception was thrown
    Then I logout

  Scenario: Setting Max Failures To Negative Value
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true and lockoutPolicy.maxFailures to -1, leave other fields at default values
  Exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument lockoutPolicy.maxFailures: Value -1 is out of range."
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | true   |
      | integer | lockoutPolicy.maxFailures  | -1     |
      | integer | lockoutPolicy.resetAfter   | 3600   |
      | integer | lockoutPolicy.lockDuration | 108000 |
    Then An exception was thrown
    Then I logout

  Scenario: Setting Max Failures To 100
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true and lockoutPolicy.maxFailures to 100, leave other fields at default values
  Login as user1 with wrong password 100 times
  Try to login again with correct credentials
  Exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | true   |
      | integer | lockoutPolicy.maxFailures  | 100    |
      | integer | lockoutPolicy.resetAfter   | 3600   |
      | integer | lockoutPolicy.lockDuration | 108000 |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I try to login as user with name "user1" with wrong password 100 times
    Then An exception was thrown
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "ToManySecrets123#"
    Then An exception was thrown
    Then I logout

  Scenario: Waiting For Reset After Login Counter To Pass
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true, lockoutPolicy.maxFailures to 2, lockoutPolicy.resetAfter to 5 seconds
  Login as user1 with wrong password
  Wait 5 seconds
  Login again as user1 with wrong password
  Try to login with correct password
  User should not be locked and no exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | true   |
      | integer | lockoutPolicy.maxFailures  | 2      |
      | integer | lockoutPolicy.resetAfter   | 5      |
      | integer | lockoutPolicy.lockDuration | 108000 |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Then An exception was thrown
    And I wait 5 seconds
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Then An exception was thrown
    And I login as user with name "user1" and password "ToManySecrets123#"
    Then No exception was thrown
    Then I logout

  Scenario: Setting Reset After Login Counter To 0 Seconds
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true, lockoutPolicy.maxFailures to 2, lockoutPolicy.resetAfter to 0 seconds
  Login as user1 with wrong password a few times
  Login as user1 with correct password
  User should not be locked

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | true   |
      | integer | lockoutPolicy.maxFailures  | 2      |
      | integer | lockoutPolicy.resetAfter   | 0      |
      | integer | lockoutPolicy.lockDuration | 108000 |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Then An exception was thrown
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Then An exception was thrown
    And I login as user with name "user1" and password "ToManySecrets123#"
    Then No exception was thrown
    Then I logout

  Scenario: Setting Reset After Login Counter To Negative Value
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true, lockoutPolicy.maxFailures to 2, lockoutPolicy.resetAfter to 0 seconds
  Login as user1 with wrong password a few times
  Login as user1 with correct password
  User should not be locked

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument lockoutPolicy.resetAfter: Value -1 is out of range"
    And I configure credential service
      | type    | name                       | value  |
      | boolean | lockoutPolicy.enabled      | true   |
      | integer | lockoutPolicy.maxFailures  | 2      |
      | integer | lockoutPolicy.resetAfter   | -1     |
      | integer | lockoutPolicy.lockDuration | 108000 |
    Then An exception was thrown
    And I logout

  Scenario: Waiting For Lock Duration Time To Pass
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true, lockoutPolicy.maxFailures to 2, lockoutPolicy.durationTime to 5 seconds
  Login as user1 with wrong password 2 times
  Login again with correct password, exception should be thrown
  Wait 5 seconds
  Login again with correct password, no exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value |
      | boolean | lockoutPolicy.enabled      | true  |
      | integer | lockoutPolicy.maxFailures  | 2     |
      | integer | lockoutPolicy.resetAfter   | 120   |
      | integer | lockoutPolicy.lockDuration | 5     |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "ToManySecrets123#"
    Then An exception was thrown
    Then I wait 5 seconds
    When I login as user with name "user1" and password "ToManySecrets123#"
    Then No exception was thrown
    And I logout

  Scenario: Setting Lockout Duration To 0 Seconds
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true, lockoutPolicy.maxFailures to 2, lockoutPolicy.durationTime to 0 seconds
  Login as user1 with wrong password 2 times
  Login again with correct password, no exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    And I configure credential service
      | type    | name                       | value |
      | boolean | lockoutPolicy.enabled      | true  |
      | integer | lockoutPolicy.maxFailures  | 2     |
      | integer | lockoutPolicy.resetAfter   | 120   |
      | integer | lockoutPolicy.lockDuration | 0     |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    When A generic user
      | name  | displayName | email             | phoneNumber     | status  | userType |
      | user1 | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name  | password          | enabled |
      | user1 | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "user1" and password "wrongpassword"
    When I login as user with name "user1" and password "ToManySecrets123#"
    Then No exception was thrown
    And I logout

  Scenario: Setting Lockout Duration To Negative Value
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true, lockoutPolicy.maxFailures to 2, lockoutPolicy.durationTime to -1 seconds
  Exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument lockoutPolicy.lockDuration: Value -1 is out of range"
    When I configure credential service
      | type    | name                       | value |
      | boolean | lockoutPolicy.enabled      | true  |
      | integer | lockoutPolicy.maxFailures  | 2     |
      | integer | lockoutPolicy.resetAfter   | 120   |
      | integer | lockoutPolicy.lockDuration | -1    |
    Then An exception was thrown
    And I logout

  Scenario: Reset Security Context for all scenarios

    Given Reset Security Context
