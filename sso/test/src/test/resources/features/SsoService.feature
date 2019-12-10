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
@unit
@sso
Feature: Sso Service
  Scenario: Set environment variables
    Given System property "KEYCLOAK_URL" with value "localhost:9090"
    And System property "KAPUA_URL" with value "localhost:8080"

  Scenario: Start Keycloak
    Given Create network
    And Start Keycloak container with name "keycloak"
    And I wait 10 seconds

#  Scenario: Stop Keycloak
#    Given Stop container with name "keycloak"

