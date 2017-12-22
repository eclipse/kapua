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
@user
Feature: Tenant service with Service Events
  Basic workflow of Account and User creation and deletion, where Service Events are
  being trigered on create, update and delete action on Account and User service.

  @StartEventBroker
  Scenario: Start event broker for all scenarios

  Scenario: To be defined
    Given this step says to skip
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
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Permissions
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-g | Kapua User G | kapua_g@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    When I try to delete account "account-a"
    And I don't find user "kapua-g"
    And I logout

  @StopEventBroker
  Scenario: Stop event broker for all scenarios
