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
@rest
Feature: REST API tests for User
  REST API test of Kapua User API.

  Scenario: Start event broker for all scenarios

    Given Start Event Broker

  Scenario: Start Jetty server for all scenarios

    Given Start Jetty Server on host "127.0.0.1" at port "8080"


  Scenario: Simple Jetty with rest-api war
    Jetty with api.war is already started, now just login with sys user and
    call GET on users to retrieve list of all users.

    Given Server with host "127.0.0.1" on port "8080"
    When REST POST call at "/v1/authentication/user" with JSON "{"password": "kapua-password", "username": "kapua-sys"}"
    Then REST response containing AccessToken
    When REST GET call at "/v1/_/users?offset=0&limit=50"
    Then REST response containing Users

  Scenario: Stop Jetty server for all scenarios

    Given Stop Jetty Server

  Scenario: Stop event broker for all scenarios

    Given Stop Event Broker