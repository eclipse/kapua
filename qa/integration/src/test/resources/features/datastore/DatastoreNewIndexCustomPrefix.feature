###############################################################################
# Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial API and implementation
#
###############################################################################
@datastore
@datastoreNewIndexCustomPrefix
@env_docker

Feature: Datastore tests

@setup
  Scenario: Start full docker environment
    Given Init Jaxb Context
    And Init Security Context
    And Start full docker environment

  Scenario: Create index with specific prefix
  Create elasticsearch index with specific prefix set by system property.
  Index gets created when user publishes data.

    Given Server with host "127.0.0.1" on port "9200"
    And I wait for 3 seconds
    When I delete all indices
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When REST GET call at "/_cat/indices/"
    Then REST response containing text "green open"
#    And REST response containing text "custom-prefix-1-2018-01"
    And I delete all indices

@teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
