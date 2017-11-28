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
@datastore
Feature: Datastore TTL tests

    @StartBroker
    Scenario: Start broker for all scenarios

    @StartDatastore
    Scenario: Start datastore for all scenarios

    Scenario: Basic negative TTL test on datastore
        For specific account which user belongs to Set TTL to 7 days. Then insert one
        message that is 10 days old and one message that is current.
        Followed by count and query on that messages. Result should be single message,
        because out-lived message is not included in results.

        Given I login as user with name "kapua-sys" and password "kapua-password"
          And Account for "kapua-sys"
          And The device "test-device-1"
          And I configure datastore service
            | type    | name            | value               |
            | boolean | enabled         | true                |
            | integer | dataTTL         | 7                   |
            | integer | rxByteLimit     | 0                   |
            | string  | dataIndexBy     | DEVICE_TIMESTAMP    |
        When I prepare a random message with delta for sent -864000000, captured -864000000 and received -864000000, save it as "msgOld"
          And I store the message "msgOld" and remember its ID as "msgOldId"
          And I prepare a random message with delta for sent 0, captured 0 and received 0, save it as "msgNow"
          And I store the message "msgNow" and remember its ID as "msgNowId"
          And I refresh all database indices
          And I count the current account messages and store the count as "count"
          And I perform an ordered query for messages and store the results as "query"
        Then The value of "count" is exactly 1
          And There are exactly 1 message in the list "query"
          And All indices are deleted

    Scenario: Basic positive TTL test on datastore
        For specific account which user belongs to Set TTL to 7 days. Then insert one
        message that is 1 day old and one message that is current.
        Followed by count and query on that messages. Result should be both messages,
        because none of the messages is outside TTL period.

        Given I login as user with name "kapua-sys" and password "kapua-password"
          And Account for "kapua-sys"
          And The device "test-device-1"
          And I configure datastore service
            | type    | name            | value               |
            | boolean | enabled         | true                |
            | integer | dataTTL         | 7                   |
            | integer | rxByteLimit     | 0                   |
            | string  | dataIndexBy     | DEVICE_TIMESTAMP    |
        When I prepare a random message with delta for sent -86400000, captured -86400000 and received -86400000, save it as "msgOld"
          And I store the message "msgOld" and remember its ID as "msgOldId"
          And I prepare a random message with delta for sent 0, captured 0 and received 0, save it as "msgNow"
          And I store the message "msgNow" and remember its ID as "msgNowId"
          And I refresh all database indices
          And I count the current account messages and store the count as "count"
          And I perform an ordered query for messages and store the results as "query"
        Then The value of "count" is exactly 2
          And There are exactly 2 message in the list "query"
          And All indices are deleted

    Scenario: Combined outside TTL and timestamp query
        Generate message that lies outside TTL range, but inside query range 12 days till now.
        TTL is 7 days but message is 10 days old.
        Result should be empty as message is outside TTL range.

        Given I login as user with name "kapua-sys" and password "kapua-password"
          And Account for "kapua-sys"
          And The device "test-device-1"
          And I configure datastore service
              | type    | name            | value               |
              | boolean | enabled         | true                |
              | integer | dataTTL         | 7                   |
              | integer | rxByteLimit     | 0                   |
              | string  | dataIndexBy     | DEVICE_TIMESTAMP    |
        When I prepare a random message with delta for sent -864000000, captured -864000000 and received -864000000, save it as "msgOld"
          And I store the message "msgOld" and remember its ID as "msgOldId"
          And I refresh all database indices
          And I count the current account messages with delta for date from -1036800000 to 1000 and store the count as "count"
          And I perform an ordered query for messages with delta for date from -1036800000 to 1000 and store the results as "query"
        Then The value of "count" is exactly 0
          And There is exactly 0 messages in the list "query"
          And All indices are deleted

    Scenario: Combined inside TTL and timestamp query
        Generate message that lies inside TTL range and inside query range.
        Message is 1 day old and query range is from 2 days till now.
        TTL is 7 days and message is 1 day old.
        Result should be this single message.

        Given I login as user with name "kapua-sys" and password "kapua-password"
          And Account for "kapua-sys"
          And The device "test-device-1"
          And I configure datastore service
              | type    | name            | value               |
              | boolean | enabled         | true                |
              | integer | dataTTL         | 7                   |
              | integer | rxByteLimit     | 0                   |
              | string  | dataIndexBy     | DEVICE_TIMESTAMP    |
        When I prepare a random message with delta for sent -86400000, captured -86400000 and received -86400000, save it as "msgOld"
          And I store the message "msgOld" and remember its ID as "msgOldId"
          And I refresh all database indices
          And I count the current account messages with delta for date from -172800000 to 1000 and store the count as "count"
          And I perform an ordered query for messages with delta for date from -172800000 to 1000 and store the results as "query"
        Then The value of "count" is exactly 1
          And There is exactly 1 messages in the list "query"
          And All indices are deleted

    @StopBroker
    Scenario: Stop broker after all scenarios

    @StopDatastore
    Scenario: Stop datastore after all scenarios