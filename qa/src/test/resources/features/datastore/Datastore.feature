###############################################################################
# Copyright (c) 2017 Eurotech and/or its affiliates and others
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

Feature: Datastore tests

  Scenario: Query before schema search
    Before schema is created methods that search with find for messages, channel info,
    metric info and client info, should return null values.
    Query methods on messages, channel info, metric info and client info should return empty
    results and count on the same services should return 0.
    Delete based on query or parametrized delete on non existent data should not fail. If data
    doesn't exist it is not deleted.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given Account for "kapua-sys"
      When I search for data message with id "fake-id"
        Then I don't find message
      When I search for channel info with id "fake-id"
        Then I don't find channel info
      When I search for metric info with id "fake-id"
        Then I don't find metric info
      When I search for client info with id "fake-id"
        Then I don't find client info
    Given I create message query for current account with limit 1000
       When I query for data message
         Then I get empty message list result
       When I count for data message
         Then I get message count 0
    Given I create channel info query for current account with limit 10
       When I query for channel info
         Then I get empty channel info list result
       When I count for channel info
         Then I get channel info count 0
    Given I create metric info query for current account with limit 10
       When I query for metric info
         Then I get empty metric info list result
       When I count for metric info
         Then I get metric info count 0
    Given I create client info query for current account with limit 10
       When I query for client info
         Then I get empty client info list result
       When I count for client info
         Then I get client info count 0
    And I logout

  Scenario: Check the database cache coherency
    This test checks the coherence of the registry cache for the metrics info (so if, once the 
    cache is erased, after a new metric insert the firstMessageId and firstMessageOn contain 
    the previous value)

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    And I prepare a random message and save it as "RandomDataMessage1"
    Then I store the message "RandomDataMessage1" and remember its ID as "RandomDataMessage1Id"
    When I search for a data message with ID "RandomDataMessage1Id" and remember it as "DataStoreMessage"
    Then The datastore message "DataStoreMessage" matches the prepared message "RandomDataMessage1"
    Then I clear all the database caches
    And I store the message "RandomDataMessage1" and remember its ID as "RandomDataMessage2Id"
    When I refresh all database indices
    And I search for a data message with ID "RandomDataMessage1Id" and remember it as "DataStoreMessageNew"
    Then The datastore messages "DataStoreMessage" and "DataStoreMessageNew" match

  Scenario: Check the message store
    Store few messages with few metrics, position and body (partially randomly generated) and check 
    if the stored message (retrieved by id) has all the fields correctly set.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    When I prepare 15 random messages and remember the list as "RandomMessagesList"
    And I store the messages from list "RandomMessagesList" and remember the IDs as "StoredMessageIDs"
    When I search for messages with IDs from the list "StoredMessageIDs" and store them in the list "StoredMessagesList"
    Then The datastore messages in list "StoredMessagesList" matches the prepared messages in list "RandomMessagesList"

  Scenario: Delete items by the datastore ID
    Delete a previously stored message and verify that it is not in the store any more. Also delete and check the
    message related channel, metric and client info entries.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    And I prepare a random message and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all database indices
    When I search for a data message with ID "RandomDataMessageId" and remember it as "DataStoreMessage"
    Then The datastore message "DataStoreMessage" matches the prepared message "RandomDataMessage"
    When I delete the datastore message with ID "RandomDataMessageId"
    And I refresh all database indices
    When I search for a data message with ID "RandomDataMessageId" and remember it as "ShouldBeNull"
    Then Message "ShouldBeNull" is null
    When I query for the current account channels and store them as "AccountChannelList"
    Then There is exactly 1 channel in the list "AccountChannelList"
    When I delete all channels from the list "AccountChannelList"
    And I refresh all database indices
    When I count the current account channels and store the count as "AccountChannelCount"
    Then The value of "AccountChannelCount" is exactly 0
    When I query for the current account metrics and store them as "AccountMetriclList"
    Then There is exactly 5 metrics in the list "AccountMetriclList"
    When I delete all metrics from the list "AccountMetriclList"
    And I refresh all database indices
    When I count the current account metrics and store the count as "AccountMetricCount"
    Then The value of "AccountMetricCount" is exactly 0
    When I query for the current account clients and store them as "AccountClientlList"
    Then There is exactly 1 client in the list "AccountClientlList"
    When I delete all clients from the list "AccountClientlList"
    And I refresh all database indices
    When I count the current account clients and store the count as "AccountClientCount"
    Then The value of "AccountClientCount" is exactly 0
