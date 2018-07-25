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
#
###############################################################################
@datastore
Feature: Datastore tests

  @StartEventBroker
  Scenario: Start event broker for all scenarios

  @StartBroker
  Scenario: Start broker for all scenarios

  @StartDatastore
  Scenario: Start datastore for all scenarios

  Scenario: Simple positive scenario for creating default - weekly index
  Create elasticsearch index with default setting for index creation which is weekly
  index creation. Index gets created when user publishes data. Index is based on
  capture date, which is set to specific value, this value determines index name.

    Given Server with host "127.0.0.1" on port "9200"
    When All indices are deleted
    And I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all database indices
    When REST call at "/_cat/indices/"
    Then REST response containing "green open 1-2018-01"
    And All indices are deleted

  Scenario: Simple positive scenario for creating daily index
  Create elasticsearch index with setting for daily index creation. Index gets created when
  user publishes data. Index is based on capture date, which is set to specific value, this
  value determines index name.

    Given Server with host "127.0.0.1" on port "9200"
    And I login as user with name "kapua-sys" and password "kapua-password"
    Given Dataservice config enabled "true", dataTTL 30, rxByteLimit 0, dataIndexBy "DEVICE_TIMESTAMP", indexWindow "DAY"
    When All indices are deleted
    And Account for "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all database indices
    When REST call at "/_cat/indices/"
    And REST response containing "green open 1-2018-01-02"
    And All indices are deleted

  Scenario: Simple positive scenario for creating hourly index
  Create elasticsearch index with setting for hour index creation. Index gets created when
  user publishes data. Index is based on capture date, which is set to specific value, this
  value determines index name.

    Given Server with host "127.0.0.1" on port "9200"
    And I login as user with name "kapua-sys" and password "kapua-password"
    Given Dataservice config enabled "true", dataTTL 30, rxByteLimit 0, dataIndexBy "DEVICE_TIMESTAMP", indexWindow "HOUR"
    When All indices are deleted
    And Account for "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all database indices
    When REST call at "/_cat/indices/"
    And REST response containing "green open 1-2018-01-02-10"
    And All indices are deleted

  @StopBroker
  Scenario: Stop broker after all scenarios

  @StopDatastore
  Scenario: Stop datastore after all scenarios

  @StopEventBroker
  Scenario: Stop event broker for all scenarios