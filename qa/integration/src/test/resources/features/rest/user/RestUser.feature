###############################################################################
# Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@rest
Feature: REST API tests for User
  REST API test of Kapua User API.

  Scenario: Start event broker for all scenarios

    Given Start Event Broker

  Scenario: Start Jetty server for all scenarios

    Given Start Jetty Server on host "127.0.0.1" at port "8081"

  Scenario: Simple Jetty with rest-api war
    Jetty with api.war is already started, now just login with sys user and
    call GET on users to retrieve list of all users.

    Given Server with host "127.0.0.1" on port "8081"
    When REST POST call at "/v1/authentication/user" with JSON "{"password": "kapua-password", "username": "kapua-sys"}"
    Then REST response containing AccessToken
    When REST GET call at "/v1/_/users?offset=0&limit=50"
    Then REST response contains list of Users

#  Commented out as it is not possible to set configuration of user account
#
#  Scenario: Create account and user in that account
#
#    Given Server with host "127.0.0.1" on port "8080"
#    And REST POST call at "/v1/authentication/user" with JSON "{"password": "kapua-password", "username": "kapua-sys"}"
#    And REST response containing AccessToken
#    And REST POST call at "/v1/AQ/accounts" with JSON "{"expirationDate": "2030-02-21T12:05:00.000Z", "organizationName": "Org A", "organizationPersonName": "Person Name", "organizationEmail": "person@org-a.com", "organizationPhoneNumber": "555-123-456", "organizationAddressLine1": "Address A", "organizationAddressLine2": "NA",  "organizationCity": "Ljubljana", "organizationZipPostCode": "1000", "organizationStateProvinceCounty": "Province", "organizationCountry": "Slovenia", "name": "Person Name","entityAttributes": {}, "scopeId": "1"}"
#    Then REST response containing Account
#    And REST POST call at "/v1/$lastAccountCompactId$/users" with JSON "{"displayName": "User A", "email": "user.a@comp-a.com", "phoneNumber": "555-123-456", "userType": "INTERNAL", "externalId": "", "expirationDate": "2030-02-21T12:05:00.000Z", "userStatus": "ENABLED", "name": "usera", "entityAttributes": {}, "scopeId": "$lastAccountId$"}"
#

  Scenario: Bug when user can retrieve user in another account if it has other account's user id
    Prepare two different accounts with each having single user.
    Login into account A.
    Fetch user in that account and keep his ID.
    Login into account B and search for user from previous step with its ID.

    Given Server with host "127.0.0.1" on port "8081"
# ------ Service steps ------
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
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
    And Move step data "LastAccount" to "AccountA"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
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
    And Move step data "LastUser" to "UserA"
    And Credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Permissions
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    And Account
      | name      | scopeId |
      | account-b | 1       |
    And Move step data "LastAccount" to "AccountB"
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User B
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User B | kapua_b@kapua.com | +386 31 323 555 | ENABLED | INTERNAL |
    And Credentials
      | name    | password          | enabled |
      | kapua-b | ToManySecrets123# | true    |
    And Permissions
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    And I logout
# ------ Service steps ------
    Then REST POST call at "/v1/authentication/user" with JSON "{"password": "ToManySecrets123#", "username": "kapua-a"}"
    And REST response containing AccessToken
    And Move Account compact id from step data "AccountA" to "accountACompactId"
    And Move User compact id from step data "UserA" to "userACompactId"
    And REST GET call at "/v1/$accountACompactId$/users/$userACompactId$"
    Then REST response containing User
    Then REST POST call at "/v1/authentication/logout" with JSON ""
    And Clear step data with key "tokenId"
    And REST POST call at "/v1/authentication/user" with JSON "{"password": "ToManySecrets123#", "username": "kapua-b"}"
    And REST response containing AccessToken
    And Move Account compact id from step data "AccountB" to "accountBCompactId"
    And REST GET call at "/v1/$accountBCompactId$/users/$lastUserCompactId$"
    Then REST response code is 404

  Scenario: Stop Jetty server for all scenarios

    Given Stop Jetty Server

  Scenario: Stop event broker for all scenarios

    Given Stop Event Broker
