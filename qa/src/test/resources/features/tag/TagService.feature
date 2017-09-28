###############################################################################
# Copyright (c) 2017 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech
###############################################################################
@tag
Feature: Tag Service
  Tag Service is responsible for CRUD operations on Tags. This service is currently
  used to attach tags to Devices, but could be used to tag eny kapua entity, like
  User for example.

  Background:
    Given I login as user with name "kapua-sys" and password "kapua-password"
      And Tag Service configuration
        | type    | name                       | value |
        | boolean | infiniteChildEntities      | true  |
        | integer | maxNumberChildEntities     | 5     |

  @StartBroker
  Scenario: Start broker for all scenarios

  Scenario: Creating tag
    Create a tag entry, with specified name. Name is only tag specific attribute.
    Once created search for it and is should been created.

    Given Tag with name "tagName"
    When Tag with name "tagName" is searched
    Then Tag with name "tagName" is found
      And I logout
      
  Scenario: Deleting tag
    Create a tag entry, with specified name. Name is only tag specific attribute.
    Once created search and find it, then delete it.

    Given Tag with name "tagName2"
    When Tag with name "tagName2" is searched
    Then Tag with name "tagName2" is found and deleted
      And I logout

  @StopBroker
  Scenario: Stop broker after all scenarios
