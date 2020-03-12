###############################################################################
# Copyright (c) 2020 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@account
@credential
@integration

Feature: Account Credential Service Integration Tests

  Scenario: Uncorrect Login While Lockout Policy Is Enabled
  Login as kapua-sys, create an account, create a user under that account (user1)
  Configure CredentialService of that account, set lockoutPolicy.enabled to true and lockoutPolicy.maxFailures to 2, leave other fields at default values
  Login twice as user1 with wrong password
  Try to login again with correct credentials
  User should be locked

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email adress "acc1@org.com"
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
    Then I create an account with name "acc1", organization name "acc1" and email adress "acc1@org.com"
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