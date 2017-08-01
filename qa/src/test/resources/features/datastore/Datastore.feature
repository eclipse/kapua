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

  @StartBroker
  Scenario: Start broker for all scenarios

  @StartDatastore
  Scenario: Start datastore for all scenarios

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
    Then All indices are deleted
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
    And I refresh all database indices
    When I search for a data message with ID "RandomDataMessage1Id" and remember it as "DataStoreMessage"
    Then The datastore message "DataStoreMessage" matches the prepared message "RandomDataMessage1"
    Then I clear all the database caches
    And I store the message "RandomDataMessage1" and remember its ID as "RandomDataMessage2Id"
    When I refresh all database indices
    And I search for a data message with ID "RandomDataMessage1Id" and remember it as "DataStoreMessageNew"
    Then The datastore messages "DataStoreMessage" and "DataStoreMessageNew" match
    And All indices are deleted

  Scenario: Check the message store
    Store few messages with few metrics, position and body (partially randomly generated) and check
    if the stored message (retrieved by id) has all the fields correctly set.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    When I prepare 15 random messages and remember the list as "RandomMessagesList"
    And I store the messages from list "RandomMessagesList" and remember the IDs as "StoredMessageIDs"
    And I refresh all database indices
    When I search for messages with IDs from the list "StoredMessageIDs" and store them in the list "StoredMessagesList"
    Then The datastore messages in list "StoredMessagesList" matches the prepared messages in list "RandomMessagesList"
    And All indices are deleted

  Scenario: Delete items by the datastore ID
    Delete a previously stored message and verify that it is not in the store any more. Also delete and check the
    message related channel, metric and client info entries.

    Given All indices are deleted
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
    And All indices are deleted

  Scenario: Delete items based on query results

    Given All indices are deleted
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    Given I prepare a number of messages with the following details and remember the list as "TestMessages1"
      |topic                  |
      |delete/by/query/test/1 |
    And The device "test-device-2"
    Given I prepare a number of messages with the following details and remember the list as "TestMessages2"
      |topic                  |
      |delete/by/query/tests/2 |
    And The device "test-device-3"
    Given I prepare a number of messages with the following details and remember the list as "TestMessages3"
      |topic                  |
      |delete/by/query/test/3 |
    Then I store the messages from list "TestMessages1" and remember the IDs as "StoredMessageIDs1"
    Then I store the messages from list "TestMessages2" and remember the IDs as "StoredMessageIDs2"
    Then I store the messages from list "TestMessages3" and remember the IDs as "StoredMessageIDs3"
    And I refresh all database indices
    When I search for messages with IDs from the list "StoredMessageIDs1" and store them in the list "StoredMessagesList"
    Then The datastore messages in list "StoredMessagesList" matches the prepared messages in list "TestMessages1"
    When I pick the ID number 0 from the list "StoredMessageIDs1" and remember it as "SelectedMessageId"
    When I delete the datastore message with ID "SelectedMessageId"
    And I refresh all database indices
    When I search for a data message with ID "SelectedMessageId" and remember it as "ShouldBeNull"
    Then Message "ShouldBeNull" is null
    When I query for the current account channels and store them as "AccountChannelList"
    Then There are exactly 3 channels in the list "AccountChannelList"
    When I pick the ID of the channel number 0 in the list "AccountChannelList" and remember it as "FirstAccountId"
    And I delete the channel with the ID "FirstAccountId"
    And I refresh all database indices
    When I query for the current account channels and store them as "AccountChannelList"
    Then There are exactly 2 channels in the list "AccountChannelList"
    When I query for the current account metrics and store them as "AccountMetriclList"
    Then There are exactly 15 metrics in the list "AccountMetriclList"
    When I query for the metrics in topic "delete/by/query/test/1" and store them as "TopicMetricList"
    Then There are exactly 5 metrics in the list "TopicMetricList"
    When I delete all metrics from the list "TopicMetricList"
    And I refresh all database indices
    When I query for the metrics in topic "delete/by/query/test/1" and store them as "TopicMetricList2"
    Then There are exactly 0 metrics in the list "TopicMetricList2"
    When I query for the current account clients and store them as "AccountClientlList"
    Then There are exactly 3 clients in the list "AccountClientlList"
    And I delete client number 1 from the list "AccountClientlList"
    And I refresh all database indices
    When I query for the current account clients and store them as "AccountClientlList2"
    Then There are exactly 2 clients in the list "AccountClientlList2"
    And All indices are deleted

  Scenario: Check the mapping for message semantic topics

    Given All indices are deleted
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    Given I prepare a number of messages with the following details and remember the list as "TestMessages"
      |topic                             |
      |same/metric/name/different/type/1 |
      |same/metric/name/different/type/2 |
      |same/metric/name/different/type/3 |
    Then I store the messages from list "TestMessages" and remember the IDs as "StoredMessageIDs"
    And I refresh all database indices
    When I count the current account messages and store the count as "AccountMessageCount"
    Then The value of "AccountMessageCount" is exactly 3
    When I count the current account metrics and store the count as "AccountMetricCount"
    Then The value of "AccountMetricCount" is exactly 15
    When I search for messages with IDs from the list "StoredMessageIDs" and store them in the list "StoredMessagesList"
    Then The datastore messages in list "StoredMessagesList" matches the prepared messages in list "TestMessages"
    And All indices are deleted

  Scenario: Ordered query
    Test the correctness of the query filtering order (3 fields: date descending, date ascending, string descending)

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    Given I prepare 100 randomly ordered messages and remember the list as "TestMessageList"
      |topic           |
      |bus/route/one   |
      |bus/route/one   |
      |bus/route/two/a |
      |bus/route/two/b |
      |tram/route/one  |
      |car/one         |
    And I store the messages from list "TestMessageList" and remember the IDs as "StoredMessageIDs"
    Then I refresh all database indices
    When I perform an ordered query for messages and store the results as "QueriedMessageList"
    Then The items in the list "QueriedMessageList" are stored in the default order
    And All indices are deleted

  Scenario: Test the message store with timestamp indexing
    Test the correctness of the storage process with a basic message (no metrics, payload and position)
    indexing message date by device timestamp (as default).

    And All indices are deleted
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    When I prepare a random message with null payload and save it as "TestMessageWithNullPayload"
    And I set the database to device timestamp indexing
    And I store the message "TestMessageWithNullPayload" and remember its ID as "TestMessageId"
    Then I refresh all database indices
    When I perform a default query for the account messages and store the results as "AccountMessages"
    And I pick message number 0 from the list "AccountMessages" and remember it as "QueriedMessage"
    Then The datastore message "QueriedMessage" matches the prepared message "TestMessageWithNullPayload"
    And All indices are deleted

  Scenario: Test the message store with server timestamp indexing
    Test the correctness of the storage process with a basic message (no metrics, payload and position)
    indexing message date by server timestamp (as default).

    Given All indices are deleted
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    When I prepare a random message with null payload and save it as "TestMessageWithNullPayload"
    And I set the database to server timestamp indexing
    And I store the message "TestMessageWithNullPayload" with the server time and remember its ID as "TestMessageId"
    Then I refresh all database indices
    When I perform a default query for the account messages and store the results as "AccountMessages"
    And I pick message number 0 from the list "AccountMessages" and remember it as "QueriedMessage"
    Then The datastore message "QueriedMessage" matches the prepared message "TestMessageWithNullPayload"
    And All indices are deleted

  Scenario: ChannelInfo client ID and topic data based on the account id
    Check the correctness of the client ids and topics stored in the channel info data by retrieving the
    channel info by account id

    Given All indices are deleted
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    And I set the database to device timestamp indexing
    When I prepare a number of messages with the following details and remember the list as "TestMessages"
      |clientId      |topic           |
      |test-client-1 |bus/route/one   |
      |test-client-2 |bus/route/one   |
      |test-client-3 |bus/route/two/a |
      |test-client-2 |bus/route/two/b |
      |test-client-3 |tram/route/one  |
      |test-client-3 |car/one         |
    Then I store the messages from list "TestMessages" and remember the IDs as "StoredMessageIDs"
    And I refresh all database indices
    When I query for the current account channels and store them as "AccountChannelList"
    Then There are exactly 6 channels in the list "AccountChannelList"
    And The channel info items "AccountChannelList" match the prepared messages in "TestMessages"
    And All indices are deleted

  Scenario: ChannelInfo client ID and topic data based on the client id
  Check the correctness of the client ids and topics stored in the channel info data by retrieving the
  channel info by client id

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And I set the database to device timestamp indexing
    And The device "test-device-1"
    When I prepare a number of messages with the following details and remember the list as "TestMessages1"
      |clientId      |topic           |
      |test-client-1 |bus/route/one   |
      |test-client-1 |bus/route/one/a |
      |test-client-1 |bus/route/two/a |
      |test-client-1 |bus/route/two/b |
    Then I store the messages from list "TestMessages1" and remember the IDs as "StoredMessageIDs1"
    When I prepare a number of messages with the following details and remember the list as "TestMessages2"
      |clientId      |topic             |
      |test-client-2 |bus/route/three   |
      |test-client-2 |bus/route/four    |
      |test-client-2 |bus/route/four/a  |
    Then I store the messages from list "TestMessages2" and remember the IDs as "StoredMessageIDs2"
    And I refresh all database indices
    When I query for the channel info of the client "test-client-1" and store the result as "ClientInfoList"
    Then There are exactly 4 channels in the list "ClientInfoList"
    And The channel info items "ClientInfoList" match the prepared messages in "TestMessages1"
    And All indices are deleted

  Scenario: ChannelInfo last published date
    Check the correctness of the channel info last publish date stored by retrieving the
    channel info by client id.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Account for "kapua-sys"
    And The device "test-device-1"
    And I set the database to device timestamp indexing
    When I prepare a number of messages with the following details and remember the list as "TestMessages"
      |clientId      |topic                            |captured            |
      |test-client-1 |ci_client_by_pd_by_account/1/2/3 |03/07/2017T09:00:00 |
      |test-client-2 |ci_client_by_pd_by_account/1/2/3 |03/07/2017T09:00:00 |
      |test-client-2 |ci_client_by_pd_by_account/1/2/3 |03/07/2017T09:00:10 |
      |test-client-2 |ci_client_by_pd_by_account/1/2/3 |03/07/2017T09:00:20 |
    Then I store the messages from list "TestMessages" and remember the IDs as "StoredMessageIDs"
    And I refresh all database indices
    When I query for the current account channels in the date range "03/07/2017T09:00:00" to "03/07/2017T09:00:20" and store them as "ChannelList"
    Then There are exactly 2 channels in the list "ChannelList"
    And Client "test-client-1" first published on a channel in the list "ChannelList" on "03/07/2017T09:00:00"
    And Client "test-client-1" last published on a channel in the list "ChannelList" on "03/07/2017T09:00:00"
    And Client "test-client-2" first published on a channel in the list "ChannelList" on "03/07/2017T09:00:00"
    And Client "test-client-2" last published on a channel in the list "ChannelList" on "03/07/2017T09:00:20"
    And All indices are deleted

  @StopBroker
  Scenario: Stop broker after all scenarios

  @StopDatastore
  Scenario: Stop datastore after all scenarios
