###############################################################################
# Copyright (c) 2017 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
Feature: User and Credential expiration abd lockout features
  User Service has expiration value after which user is disabled.
  There is also expiration and status on user's credentials which are also tested.
  Additionally login failures and lockout and lockout resets are tested.

  Scenario: If user credential is in state enabled, user can login
    User is set up with credentials that have state enabled. If credentials state is
    enabled, user can login into system. All other expiration settings are set for
    successful login. Only state is tested.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  5    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then No exception was thrown
    And I logout

  Scenario: If user credential is in state disabled, user can not login
    User is set up with credentials that have state disabled. If credentials state is
    disabled, user can not login into system. All other expiration settings are set for
    successful login. Only state is tested.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  5    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | false   |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then An exception was thrown
    And I logout

  Scenario: If user credential expiration date is before today, user can not login
    Expiration date on credentials is set one day in the past and is in state enabled.
    This prevents user from logging in.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  5    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled | expirationDate |
      | kapua-a | ToManySecrets123# | true    | yesterday      |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then An exception was thrown
    And I logout

  Scenario: If user credential expiration date is today, user can not login it is day inclusive
    Expiration date on credentials is set to today and is in state enabled.
    This prevents user from logging in, because expiration is today and is day inclusive.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  5    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled | expirationDate |
      | kapua-a | ToManySecrets123# | true    | today          |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then An exception was thrown
    And I logout

  Scenario: If user credential expiration date is tomorrow, user can login
    Expiration date on credentials is set to tomorrow and is in state enabled.
    This allows user to login, because expiration is not yet reached.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  5    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled | expirationDate |
      | kapua-a | ToManySecrets123# | true    | tomorrow       |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then No exception was thrown
    And I logout

  Scenario: If user expiration date is before today, user can not login
    Expiration date on user is set to yesterday and is in state enabled.
    Expiration on Credentials is not set and is in state enabled.
    This doesn't allow user to login, because expiration was reached.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  5    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType | expirationDate |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL | yesterday      |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then An exception was thrown
    And I logout

  Scenario: If user expiration date is today, user can not login because expiration date was reached
    Expiration date on user is set today and is in state enabled.
    Expiration on Credentials is not set and is in state enabled.
    This doesn't allow user to login, because expiration is reached.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  5    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType | expirationDate |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL | today          |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then An exception was thrown
    And I logout

  Scenario: If user expiration date is tomorrow, user can login
    Expiration date on user is set tomorrow and is in state enabled.
    Expiration on Credentials is not set and is in state enabled.
    This allows user to login, because expiration is not yet reached.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities |  5    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType | expirationDate |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL | tomorrow       |
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then No exception was thrown
    And I logout
