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
#
###############################################################################
@datastore
@datastoreNewIndexCustomPrefix
@integration

Feature: Datastore tests

  Scenario: Set environment variables

    Given System property "commons.settings.hotswap" with value "true"
    And System property "datastore.index.prefix" with value "custom-prefix"
    And System property "kapua.config.url" with value "null"
    And System property "broker.ip" with value "192.168.33.10"
    And System property "datastore.elasticsearch.provider" with value "org.eclipse.kapua.service.elasticsearch.client.rest.RestElasticsearchClientProvider"

  Scenario: Start datastore for all scenarios

    Given Start Datastore

  Scenario: Start event broker for all scenarios

    Given Start Event Broker

  Scenario: Start broker for all scenarios

    Given Start Broker


  Scenario: Create index with specific prefix
  Create elasticsearch index with specific prefix set by system property.
  Index gets created when user publishes data.

    Given Server with host "127.0.0.1" on port "9200"
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

  Scenario: Stop broker after all scenarios

    Given Stop Broker

  Scenario: Stop event broker for all scenarios

    Given Stop Event Broker

  Scenario: Stop datastore after all scenarios

    Given Stop Datastore
