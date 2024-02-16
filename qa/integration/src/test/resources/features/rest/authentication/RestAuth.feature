###############################################################################
# Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
@env_docker_base
@rest

Feature: REST API tests for User
  REST API test of Kapua User API.

  @setup
  Scenario: Start event broker for all scenarios
    Given Init Jaxb Context
    And Init Security Context
    And start rest-API container and dependencies

  Scenario: Simple login works and I can call another API endpoint without errors
    Given Server with host "127.0.0.1" on port "8081"
    Given An authenticated user
    Then REST response containing AccessToken
    When REST "GET" call at "/v1/_/users?offset=0&limit=50" with JSON ""
    Then REST response code is 200
    Then REST response contains list of Users

  Scenario: Refresh token and try to call another api endpoint with the previous "refreshed", now invalidated, token

    Given Server with host "127.0.0.1" on port "8081"
    Given An authenticated user
    When I refresh last access token
    Then REST response code is 200
    When REST "GET" call at "/v1/_/users?offset=0&limit=50" with JSON ""
    Then REST response code is 401
    And REST response containing text "The provided access token has been invalidated in the past"

  Scenario: Refresh token is working properly and returns a token that I can use to login

    Given Server with host "127.0.0.1" on port "8081"
    Given An authenticated user
    When I refresh last access token
    Then REST response code is 200
    # now I extract the jwt created by the refresh and I substitute the old token with this one
    And I extract "tokenId" from the response in the key "tokenId"
    When REST "GET" call at "/v1/_/users?offset=0&limit=50" with JSON ""
    Then REST response code is 200
    Then REST response contains list of Users

  Scenario: Auth. with access token fails when I wait after the token TTL

    Given Server with host "127.0.0.1" on port "8081"
    Given An authenticated user
    When REST "GET" call at "/v1/_/users?offset=0&limit=50" with JSON ""
    Then REST response code is 200
    Then REST response contains list of Users
    Then I wait 3 seconds
    When REST "POST" call at "/v1/authentication/user" with JSON "{\"password\": \"kapua-password\", \"username\": \"kapua-sys\"}"
    When REST "GET" call at "/v1/_/users?offset=0&limit=50" with JSON ""
    Then REST response code is 401

  Scenario: Refresh token using wrong parameters - 1
  Case for "The provided refresh token doesn't match the one for this jwt"

    Given Server with host "127.0.0.1" on port "8081"
    Given An authenticated user
    When I refresh access token using refresh token "foo" and jwt ""
    Then REST response code is 401
    And REST response containing text "The provided refresh token doesn't match the one for this jwt"

  Scenario: Refresh token using wrong parameters - 2

    Given Server with host "127.0.0.1" on port "8081"
    Given An authenticated user
    When I refresh access token using refresh token "" and jwt "foo"
    Then REST response code is 401

  Scenario: Refresh token is not working properly for token refresh when I await the token refresh TTL
  Case for "The provided refresh token is expired"

    Given Server with host "127.0.0.1" on port "8081"
    Given An authenticated user
    Then I wait 2 seconds
    When I refresh last access token
    Then REST response code is 401
    And REST response containing text "The provided refresh token is expired"

  Scenario: Refresh a token that is invalidated in the past
  Case for "The provided access token has been invalidated"

    Given Server with host "127.0.0.1" on port "8081"
    Given An authenticated user
    When I refresh last access token
    #now in step data I have the previous token that is invalidated on db
    When I refresh last access token
    Then REST response code is 401
    And REST response containing text "The provided access token has been invalidated"

  @teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
