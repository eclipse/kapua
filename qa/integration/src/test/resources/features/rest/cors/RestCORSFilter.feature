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
  REST API test of Kapua User API.

  @setup
  Scenario: Start event broker for all scenarios
    Given Init Jaxb Context
    And Init Security Context
    #NB: be aware that sub-sequent tests depends on the value of TTL that you set here
#    And start rest-API container and dependencies with auth token TTL "10000"ms and refresh token TTL "20000"ms

  Scenario: The auth from a "same-origin" call

    Given Server with host "127.0.0.1" on port "8081"
    Given I try to authenticate with a same-site call
    Then I expect no "Access-Control-Allow-Origin" header in the response
    Then I expect no "Access-Control-Allow-Credentials" header in the response

  Scenario: The success of an authentication attempt from a 'Cross-origin' request depends on the presence of the CORS endpoint used in the 'origin' header in the scope

    Given Server with host "127.0.0.1" on port "8081"
    Given I try to authenticate with a cross-site call with origin "https://api-sbx.everyware.io"
    Then I expect no "Access-Control-Allow-Origin" header in the response
    Then I expect no "Access-Control-Allow-Credentials" header in the response
    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create a CORS filter with schema "https", domain "api-sbx.everyware.io" and port 443
    Then I wait 3 seconds
    Given I try to authenticate with a cross-site call with origin "https://api-sbx.everyware.io"
    Then I expect "Access-Control-Allow-Origin" header in the response with value "https://api-sbx.everyware.io"
    Then I expect "Access-Control-Allow-Credentials" header in the response with value "true"
    When I delete all CORS filters
    Then I wait 3 seconds
    Given I try to authenticate with a cross-site call with origin "https://api-sbx.everyware.io"
    Then I expect no "Access-Control-Allow-Origin" header in the response
