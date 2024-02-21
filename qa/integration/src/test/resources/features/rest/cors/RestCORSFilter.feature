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
@rest_cors

  #NB: this test depends on tests tagged as @endpoint

Feature: REST API tests for User
  REST API tests of Kapua CORS filter logic.

  @setup
  Scenario: Initialize Jaxb and security context, then start rest-api container and dependencies
    Given Init Jaxb Context
    And Init Security Context
    #NB: be aware that sub-sequent tests depends on the value of cors endpoint refresh interval that you set here
    And start rest-API container and dependencies with auth token TTL "10000000"ms and refresh token TTL "20000"ms and cors endpoint refresh interval 1s

  Scenario: The auth from a "same-origin" call should not present, in the response headers fields, the values that represent a "success CORS filter auth. attempt"

    Given Server with host "127.0.0.1" on port "8081"
    Given I try to authenticate with a same-site call
    Then REST response code is 200
    And REST response containing AccessToken
    And I expect no "Access-Control-Allow-Origin" header in the response
    And I expect no "Access-Control-Allow-Credentials" header in the response

  Scenario: The success of an authentication attempt from a 'Cross-origin' request depends on the presence of the CORS endpoint used in the 'origin' header in the scope

    Given Server with host "127.0.0.1" on port "8081"
    Given I try to authenticate with a cross-site call with origin "https://api-sbx.everyware.io" using json "{\"password\": \"kapua-password\", \"username\": \"kapua-sys\"}"
    #auth is "formally" right (response code 200) but...
    Then REST response code is 200
    #...CORS headers are not present
    Then I expect no "Access-Control-Allow-Origin" header in the response
    Then I expect no "Access-Control-Allow-Credentials" header in the response
    #now I insert the CORS endpoint, using directly services and not rest-API because here I want to test ONLY CORS logic and not other rest APIs. Also, this 'endpoint service' has been tested previously on @endpoint tests
    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create a CORS filter with schema "https", domain "api-sbx.everyware.io" and port 443
    Then I wait 2 seconds
    Given I try to authenticate with a cross-site call with origin "https://api-sbx.everyware.io" using json "{\"password\": \"kapua-password\", \"username\": \"kapua-sys\"}"
    Then REST response code is 200
    Then I expect "Access-Control-Allow-Origin" header in the response with value "https://api-sbx.everyware.io"
    Then I expect "Access-Control-Allow-Credentials" header in the response with value "true"
    When I delete all CORS filters
    Then I wait 2 seconds
    Given I try to authenticate with a cross-site call with origin "https://api-sbx.everyware.io" using json "{\"password\": \"kapua-password\", \"username\": \"kapua-sys\"}"
    Then REST response code is 200
    Then I expect no "Access-Control-Allow-Credentials" header in the response
    Then I expect no "Access-Control-Allow-Origin" header in the response


  Scenario: Trying to define CORS endpoints in child account and seeing if they mask the ones defined in the father

    Given Server with host "127.0.0.1" on port "8081"
    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create a CORS filter with schema "https", domain "api-sbx.everyware.io" and port 443
    Then I wait 2 seconds
    Then I create an account with name "acc1", organization name "acc1" and email address "acc1@org.com"
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
    And Add permissions to the last created user
      | domain | action |
      | endpoint_info   | read   |
      | endpoint_info   | write  |
      | user            | read   |
    Then I logout
    When REST POST call at "/v1/authentication/user" with JSON "{\"password\": \"ToManySecrets123#\", \"username\": \"user1\"}"
    Then REST response code is 200
    Then REST response containing AccessToken
    Given I try to authenticate with a cross-site call with origin "https://api-sbx.everyware.io" using json "{\"password\": \"ToManySecrets123#\", \"username\": \"user1\"}"
    Then REST response code is 200
    Then I expect "Access-Control-Allow-Origin" header in the response with value "https://api-sbx.everyware.io"
    Then I expect "Access-Control-Allow-Credentials" header in the response with value "true"
    #now I mask the parent account CORS endpoints creating a new one in this child account
    Given I login as user with name "user1" and password "ToManySecrets123#"
    When I create a CORS filter with schema "https", domain "api-sbx.asdsadas.io" and port 443
    Then I wait 2 seconds
    Given I try to authenticate with a cross-site call with origin "https://api-sbx.everyware.io" using json "{\"password\": \"ToManySecrets123#\", \"username\": \"user1\"}"
    Then REST response code is 200
    And REST response containing AccessToken
    #Authentication has no defined scopeID in session so parent CORS is used in CORS filter matching...
    And I expect "Access-Control-Allow-Origin" header in the response with value "https://api-sbx.everyware.io"
    And I expect "Access-Control-Allow-Credentials" header in the response with value "true"
    #...but for this call there is a scopeID in session (after auth) so there is masking of CORS endpoint
    When REST "GET" call at "/v1/_/users?offset=0&limit=50" with JSON "" in cross-site mode with origin "https://api-sbx.everyware.io"
    Then REST response code is 200
    And I expect no "Access-Control-Allow-Origin" header in the response
    And I expect no "Access-Control-Allow-Credentials" header in the response





