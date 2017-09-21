###############################################################################
# Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
Feature: Domain Service CRUD tests

  Scenario: Count domains in a blank database
  The default domain table must contain 8 preset entries.

    When I count the domain entries in the database
    Then I get 8 as result

  Scenario: Regular domain
  Create a regular domain entry. The newly created entry must match the
  creator parameters.

    Given I create the domain
      | name        | serviceName    | actions    |
      | test_name_1 | test_service_1 | read,write |
    Then A domain was created
    And The domain matches the creator

  Scenario: Domain with null name
  It must not be possible to create a domain entry with a null name. In
  such case the domain service must throw an exception.

    Given I create the domain
      | serviceName    | actions    |
      | test_service_1 | read,write |
    Then An exception was thrown

  Scenario: Domain with null service name
  It must not be possible to create a domain entry with a null service name. In
  such case the domain service must throw an exception.

    Given I create the domain
      | name        | actions    |
      | test_name_1 | read,write |
    Then An exception was thrown

  Scenario: Domain with null actions
  It must not be possible to create a domain entry with a null set of supported actions. In
  such case the domain service must throw an exception.

    Given I create the domain
      | name        | serviceName    |
      | test_name_1 | test_service_1 |
    Then An exception was thrown

  Scenario: Domains with duplicate names
  Domain names must be unique in the database. If an already existing name is used for a
  new domain an exception must be thrown.

    Given I create the domain
      | name        | serviceName    | actions    |
      | test_name_1 | test_service_1 | read,write |
    Then A domain was created
    And The domain matches the creator
    When I create the domain
      | name        | serviceName    | actions    |
      | test_name_1 | test_service_1 | read,write |
    Then An exception was thrown

  Scenario: Find the last created domain entry
  It must be possible to find a dmain entry based on its unique ID.

    Given I create the domain
      | name        | serviceName    | actions      |
      | test_name_2 | test_service_2 | read,execute |
    When I search for the last created domain
    Then The domain matches the creator

  Scenario: Find the first domain entry for the specified service
  It must be possible to find a domain entry based on the Service name property.

    Given I create the domains
      | name        | serviceName    | actions      |
      | test_name_1 | test_service_1 | read,write   |
      | test_name_2 | test_service_2 | read,execute |
      | test_name_3 | test_service_3 | write,delete |
    When I search for the domains for the service "test_service_2"
    Then The domain matches the parameters
      | name        | serviceName    | actions      |
      | test_name_2 | test_service_2 | read,execute |

  Scenario: Compare domain entries
  The domain object is comparable.

    Then I can compare domain objects

  Scenario: Delete the last created domain entry
  It must be possible to delete an entry from the domain table. The domain is deleted
  based on its ID.

    Given I create the domain
      | name        | serviceName    | actions      |
      | test_name_2 | test_service_2 | read,execute |
    When I search for the last created domain
    Then The domain matches the creator
    When I delete the last created domain
    And I search for the last created domain
    Then There is no domain

  Scenario: Delete an inexistent domain
  If the requested ID is not found in the database, the delete function must throw
  an exception.

    When I try to delete domain with a random ID
    Then An exception was thrown

  Scenario: Count domains in the database
  It must be possible to count all the domain entries in the domain table.

    When I count the domain entries in the database
    Then This is the initial count
    Given I create the domains
      | name        | serviceName    | actions      |
      | test_name_1 | test_service_1 | read,write   |
      | test_name_2 | test_service_2 | read,execute |
      | test_name_3 | test_service_3 | write,delete |
    When I count the domain entries in the database
    Then 3 more domains were created

  Scenario: Domain entry query
  It must be possible to query domain entries based on the name property.

    Given I create the domains
      | name        | serviceName    | actions      |
      | test_name_1 | test_service_1 | read,write   |
      | test_name_2 | test_service_2 | read,execute |
      | test_name_3 | test_service_3 | write,delete |
    When I query for domains with the name "test_name_2"
    Then I get 1 as result

  Scenario: Domain entry query - service name
  It must be possible to query domain entries based on the service name.
  The whole list of matching entries must be returned.

    Given I create the domains
      | name        | serviceName    | actions      |
      | test_name_1 | test_service_1 | read,write   |
      | test_name_2 | test_service_2 | read,execute |
      | test_name_3 | test_service_3 | write,delete |
      | test_name_4 | test_service_3 | read,write   |
      | test_name_5 | test_service_3 | read,execute |
      | test_name_6 | test_service_2 | write,delete |
    When I query for domains with the service name "test_service_2"
    Then I get 2 as result
    When I query for domains with the service name "test_service_3"
    Then I get 3 as result
    When I query for domains with the service name "test_service_x"
    Then I get 0 as result
