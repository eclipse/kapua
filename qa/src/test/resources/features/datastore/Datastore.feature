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

