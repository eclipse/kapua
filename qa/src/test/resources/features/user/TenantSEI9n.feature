###############################################################################
# Copyright (c) 2018 Eurotech and/or its affiliates and others
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
  being triggered on create, update and delete action on Account and User service.

  Background: Creation of account and user with credentials

    Given I login as user with name "kapua-sys" and password "kapua-password"
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

  @StartEventBroker
  Scenario: Start event broker for all scenarios

  Scenario: Account is deleted, user has to be deleted too

    Remove account and eventually user has to be deleted to by user service
    listening on account delete event.
    When I try to delete account "account-a"
    And I wait 1 second
    Then I don't find user "kapua-a"
    And I logout

  Scenario: User is deleted, credentials have to be deleted too

    Remove user and his credentials has to be deleted by Security service
    listening on user service events.
    When I try to delete user "kapua-a"
    And I wait 1 second
    Then I don't find user credentials

  Scenario: User is deleted, access permissions for that user have to be deleted too

    To be implemented.

  @StopEventBroker
  Scenario: Stop event broker for all scenarios
