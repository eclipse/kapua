###############################################################################
# Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
@tenantSE
@env_none

Feature: Tenant service with Service Events
  Basic workflow of Account and User creation and deletion, where Service Events are
  being trigered on create, update and delete action on Account and User service.

@setup
Scenario: Initialize test environment
    Given Init Jaxb Context
    And Init Security Context

  Scenario: To be defined
#    Given this step says to skip
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-g | Kapua User G | kapua_g@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    When I delete account "account-a"
#    And I don't find user "kapua-g"
    And I logout

@teardown
  Scenario: Stop event broker for all scenarios
    Given Reset Security Context
