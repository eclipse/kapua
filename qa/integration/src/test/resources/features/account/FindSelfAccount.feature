###############################################################################
# Copyright (c) 2019 Eurotech and/or its affiliates and others
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
@accountFindSelf
@integration

Feature: Self account find feature
  Finding self accounts require a different logic to be applied to the permission

  Scenario: Find self account by id
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    Given Account
      | name         | scopeId |
      | test-account | 1       |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    And A generic user
      | name      | displayName | email             | phoneNumber     | status  | userType |
      | test-user | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name      | password          | enabled |
      | test-user | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain    | action |
      | account   | read   |
    Given I login as user with name "test-user" and password "ToManySecrets123#"
    And I look for my account by id
    Then I am able to read my account info

  Scenario: Find self account by id and scope id
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    Given Account
      | name         | scopeId |
      | test-account | 1       |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    And A generic user
      | name      | displayName | email             | phoneNumber     | status  | userType |
      | test-user | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name      | password          | enabled |
      | test-user | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain    | action |
      | account   | read   |
    Given I login as user with name "test-user" and password "ToManySecrets123#"
    And I look for my account by id and scope id
    Then I am able to read my account info

  Scenario: Find self account by name
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    Given Account
      | name         | scopeId |
      | test-account | 1       |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    And A generic user
      | name      | displayName | email             | phoneNumber     | status  | userType |
      | test-user | Test User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name      | password          | enabled |
      | test-user | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain    | action |
      | account   | read   |
    Given I login as user with name "test-user" and password "ToManySecrets123#"
    And I look for my account by name
    Then I am able to read my account info
