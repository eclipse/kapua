###############################################################################
# Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
@integration
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

  Scenario: Start datastore for all scenarios

    Given Start Datastore

  Scenario: Start event broker for all scenarios

    Given Start Event Broker

  Scenario: Start broker for all scenarios

    Given Start Broker

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

  Scenario: Stop broker after all scenarios

    Given Stop Broker

  Scenario: Stop event broker for all scenarios

    Given Stop Event Broker

  Scenario: Stop datastore after all scenarios

    Given Stop Datastore