###############################################################################
# Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@endpoint
@env_docker

Feature: Endpoint Info Service Integration Tests
  Integration test scenarios for Endpoint Info service

@setup
Scenario: Init Security Context for all scenarios
  Given Init Jaxb Context
  And Init Security Context

  Scenario: Creating Valid Endpoint
  Login as kapua-sys
  Create an endpoint with valid Schema, Domain Name and Port
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Creating Endpoint Without "Schema"
  Login as kapua-sys
  Create a endpoint with valid Domain Name and Port and without Schema
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create endpoint with domain name "abc.com" and port 2222 without schema
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With "Schema" Only
  Login as kapua-sys
  Create a endpoint only with valid Schema
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create endpoint with schema "Schema1" without domain name and port
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint Without Long "Schema"
  Login as kapua-sys
  Create an endpoint with long Schema
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "YUrdWClCWvg98SN11A1TuFEgjobJLtPxOwaNkdHAj1z7vs0GncKhNuSsJr9aAopl", domain "abc.com" and port 2221
    Then No exception was thrown
    Then I delete endpoint with schema "YUrdWClCWvg98SN11A1TuFEgjobJLtPxOwaNkdHAj1z7vs0GncKhNuSsJr9aAopl", domain "abc.com" and port 2221
    And I logout

  Scenario: Creating Endpoint With Too Long "Schema"
  Login as kapua-sys
  Create an endpoint with too long Schema
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "Value over than allowed max length. Max length is: 64."
    And I create endpoint with schema "aYUrdWClCWvg98SN11A1TuFEgjobJLtPxOwaNkdHAj1z7vs0GncKhNuSsJr9aAopl", domain "abc.com" and port 2221
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With Short "Schema"
  Login as kapua-sys
  Create an endpoint with too short Schema
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "a", domain "abc.com" and port 2221
    Then No exception was thrown
    Then I delete endpoint with schema "a", domain "abc.com" and port 2221
    And I logout

  Scenario: Creating Endpoint With Invalid "Schema" containing symbols
  Login as kapua-sys
  Create an endpoint with Schema that contains invalid symbols
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I try to create endpoint with invalid symbols in schema
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint with invalid "Schema" which contains only numbers
  Login as kapua-sys.
  Create an endpoint with invalid Schema containing only numbers.
  An exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create endpoint with schema "12345", domain "abc.com" and port 2222
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint Non-Unique "Schema"
  Login as kapua-sys
  Create a endpoint with valid Schema, Domain and Port, then create another one with same Schema, Domain name and Port
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then No exception was thrown
    Given I expect the exception "KapuaEntityUniquenessException" with the text "*"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Creating Endpoint With Schema Containing "http://"
  Login as kapua-sys
  Create an endpoint with Schema containing "http://"
  Exception should be raised
    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create endpoint with schema "http://abc.com", domain "abc.com" and port 2222
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With Schema Containing "https://"
  Login as kapua-sys
  Create a endpoint with Schema containing "https://"
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create endpoint with schema "https://abc.com", domain "abc.com" and port 2222
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint Without "Domain Name"
  Login as kapua-sys
  Create a endpoint with valid Schema and Port and without Domain Name
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create endpoint with schema "Schema1" and port 2222 without domain name
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With "Domain Name" Only
  Login as kapua-sys
  Create an endpoint only with valid Domain Name
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create endpoint with domain name "abc.com" without schema and port
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With Long "Domain Name"
  Login as kapua-sys
  Create an endpoint with long Domain Name
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "IApYdQLo4WGsvQ9gSPOIIFFOmkQeIYiGlCEU9zK7e3a9qmMyVSADuM5cJ68H1uEZj4JAML0FbYBXwO32o9AAoqSTsRs3n8tqp4E6YLJUhOnLsGMxrPZ1Rmha4DbNOqweNuvziFkmwTaAc9rIJlUr0VpJEi567JYNvVHSVtK1SdMKAfuLiTQTLOKoGZZIVdp7jwXyhgAEFJxUl3swT1uNz8MOiJHT4ToKCKZ5rwWcN6kYi55XnkMESrjdRKTcSDZmzt8wut7USdPNouIYIMGIzsw1bKDwA2fNx8b9ISsEcDAbX1EWER8GeWkqUAnKBNvNacjYHNEHceeacx72Wchd8uD8Y17w55vdAi98mmA8x7F8fFNZmC4b446Jy8HEgtE42TTOglcgeb0nGCGeUicWqxSeH0DaHf1QFvd7f05NZNli5PYPmeN5WqlVQT6b5bLEala1UD94t5PqWC01o59hHCSAJyD7Zj01gWbyuDy5LmkuARZybDdxige6CzDVWvKBYxLthaLqqE3E7cWQA6wsDPqtyEsfoCycYXuPYGx81M7f1JVvh0I5oj2Eg0VHvYcvGnfUUBXUY3abYzCyvEBf2kZ6pQUkwgJHBaA2p7UBYTqpqt5zfc5w3qn1n6uD1QzyQukvqBvuaVPlXqVobmyz7VTgGBT45cMGevJhs5Td4UrtjifSmzEDbuzBZbQub3cbutLLmJqH2pctG4gtBPz2GNNRCC2Lsx2PsYynkUr5c9aBeENFXmR41BDRLlyEzpKRtzJ6QLEVrZKAvNahm8wvlmzI6JkLB9YZpEm7diq54bmm4EaolSBvs2X9e5EEGPosoIjyQfNsbasognvYY4fGCiNuTchmxnHnPlMyfRcGPas8SWYH7fvQFDWLquEXqX3kHGAn4KsShiyAvbj9OeJ4ao2QmJSHOtP0212nx4iooHGPpMOM8difFRt0i4AYdb68q51cs8OtHaW41IZSlJai6FttoiMl3ln7uAIHp6FrPXNBmwoMxAvUgdn60mXPs1XK" and port 2221
    Then No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "IApYdQLo4WGsvQ9gSPOIIFFOmkQeIYiGlCEU9zK7e3a9qmMyVSADuM5cJ68H1uEZj4JAML0FbYBXwO32o9AAoqSTsRs3n8tqp4E6YLJUhOnLsGMxrPZ1Rmha4DbNOqweNuvziFkmwTaAc9rIJlUr0VpJEi567JYNvVHSVtK1SdMKAfuLiTQTLOKoGZZIVdp7jwXyhgAEFJxUl3swT1uNz8MOiJHT4ToKCKZ5rwWcN6kYi55XnkMESrjdRKTcSDZmzt8wut7USdPNouIYIMGIzsw1bKDwA2fNx8b9ISsEcDAbX1EWER8GeWkqUAnKBNvNacjYHNEHceeacx72Wchd8uD8Y17w55vdAi98mmA8x7F8fFNZmC4b446Jy8HEgtE42TTOglcgeb0nGCGeUicWqxSeH0DaHf1QFvd7f05NZNli5PYPmeN5WqlVQT6b5bLEala1UD94t5PqWC01o59hHCSAJyD7Zj01gWbyuDy5LmkuARZybDdxige6CzDVWvKBYxLthaLqqE3E7cWQA6wsDPqtyEsfoCycYXuPYGx81M7f1JVvh0I5oj2Eg0VHvYcvGnfUUBXUY3abYzCyvEBf2kZ6pQUkwgJHBaA2p7UBYTqpqt5zfc5w3qn1n6uD1QzyQukvqBvuaVPlXqVobmyz7VTgGBT45cMGevJhs5Td4UrtjifSmzEDbuzBZbQub3cbutLLmJqH2pctG4gtBPz2GNNRCC2Lsx2PsYynkUr5c9aBeENFXmR41BDRLlyEzpKRtzJ6QLEVrZKAvNahm8wvlmzI6JkLB9YZpEm7diq54bmm4EaolSBvs2X9e5EEGPosoIjyQfNsbasognvYY4fGCiNuTchmxnHnPlMyfRcGPas8SWYH7fvQFDWLquEXqX3kHGAn4KsShiyAvbj9OeJ4ao2QmJSHOtP0212nx4iooHGPpMOM8difFRt0i4AYdb68q51cs8OtHaW41IZSlJai6FttoiMl3ln7uAIHp6FrPXNBmwoMxAvUgdn60mXPs1XK" and port 2221
    And I logout

  Scenario: Creating Endpoint With Too Long "Domain Name"
  Login as kapua-sys
  Create an endpoint with too long Domain Name
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create endpoint with schema "Schema1", domain "aIApYdQLo4WGsvQ9gSPOIIFFOmkQeIYiGlCEU9zK7e3a9qmMyVSADuM5cJ68H1uEZj4JAML0FbYBXwO32o9AAoqSTsRs3n8tqp4E6YLJUhOnLsGMxrPZ1Rmha4DbNOqweNuvziFkmwTaAc9rIJlUr0VpJEi567JYNvVHSVtK1SdMKAfuLiTQTLOKoGZZIVdp7jwXyhgAEFJxUl3swT1uNz8MOiJHT4ToKCKZ5rwWcN6kYi55XnkMESrjdRKTcSDZmzt8wut7USdPNouIYIMGIzsw1bKDwA2fNx8b9ISsEcDAbX1EWER8GeWkqUAnKBNvNacjYHNEHceeacx72Wchd8uD8Y17w55vdAi98mmA8x7F8fFNZmC4b446Jy8HEgtE42TTOglcgeb0nGCGeUicWqxSeH0DaHf1QFvd7f05NZNli5PYPmeN5WqlVQT6b5bLEala1UD94t5PqWC01o59hHCSAJyD7Zj01gWbyuDy5LmkuARZybDdxige6CzDVWvKBYxLthaLqqE3E7cWQA6wsDPqtyEsfoCycYXuPYGx81M7f1JVvh0I5oj2Eg0VHvYcvGnfUUBXUY3abYzCyvEBf2kZ6pQUkwgJHBaA2p7UBYTqpqt5zfc5w3qn1n6uD1QzyQukvqBvuaVPlXqVobmyz7VTgGBT45cMGevJhs5Td4UrtjifSmzEDbuzBZbQub3cbutLLmJqH2pctG4gtBPz2GNNRCC2Lsx2PsYynkUr5c9aBeENFXmR41BDRLlyEzpKRtzJ6QLEVrZKAvNahm8wvlmzI6JkLB9YZpEm7diq54bmm4EaolSBvs2X9e5EEGPosoIjyQfNsbasognvYY4fGCiNuTchmxnHnPlMyfRcGPas8SWYH7fvQFDWLquEXqX3kHGAn4KsShiyAvbj9OeJ4ao2QmJSHOtP0212nx4iooHGPpMOM8difFRt0i4AYdb68q51cs8OtHaW41IZSlJai6FttoiMl3ln7uAIHp6FrPXNBmwoMxAvUgdn60mXPs1XK" and port 2221
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With Short "Domain Name"
  Login as kapua-sys
  Create an endpoint with too short Domain Name
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "a" and port 2221
    Then No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "a" and port 2221
    And I logout

  Scenario: Creating Endpoint With Invalid "Domain Name"
  Login as kapua-sys
  Create an endpoint with Domain Name that contains invalid symbols
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create endpoint with schema "Schema1", domain "a@bc.com" and port 2221
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint Non-Unique "Domain Name"
  Login as kapua-sys
  Create an endpoint with valid Domain Name, Schema and Port, then create another one with same Domain Name, but different Schema and Port
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2221
    And I create endpoint with schema "Schema2", domain "abc.com" and port 2222
    Then No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2221
    And I delete endpoint with schema "Schema2", domain "abc.com" and port 2222
    And I logout

  Scenario: Creating Endpoint Without "Port"
  Login as kapua-sys
  Create an endpoint with valid Schema and Domain Name and without Port
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    And I create endpoint with schema "Schema1" and domain "abc.com" without port
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With "Port" Only
  Login as kapua-sys
  Create an endpoint only with valid Port
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create endpoint with port 2222 without schema and domain name
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With Too Big "Port"
  Login as kapua-sys
  Create an endpoint with too big Port
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "Value over than allowed max value. Max value is 65535"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 65536
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With Max Length "Port"
  Login as kapua-sys
  Create an endpoint with max length Port
  Exception should not be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 65535
    Then No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 65535
    And I logout

  Scenario: Creating Endpoint With Small Number "Port"
  Login as kapua-sys
  Create an endpoint with small Port
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "a", domain "abc.com" and port 1
    Then No exception was thrown
    Then I delete endpoint with schema "a", domain "abc.com" and port 1
    And I logout

  Scenario: Creating Endpoint Non-Unique "Port"
  Login as kapua-sys
  Create an endpoint with valid Port, Schema and Port, then create another one with same Port, but different Schema and Domain Name
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abcd.com" and port 2222
    Then No exception was thrown
    Then I delete all endpoints with schema "Schema1"
    And I delete all endpoints with schema "Schema2"
    And I logout

  Scenario: Creating Endpoint With NULL parameters
  Login as kapua-sys
  Create an endpoint with null parameters
  An exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    And I create endpoint with NULL parameters
    Then An exception was thrown
    And I logout

  Scenario: Creating Endpoint With Enabled Secure Field
  Login as kapua-sys
  Create an endpoint with valid Port, Schema and Port and enabled Secure field.
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com", port 2222 and "ENABLED" secure field
    Then No exception was thrown
    And I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Creating Endpoint With Disabled Secure Field
  Login as kapua-sys
  Create an endpoint with valid Port, Schema and Port and disabled Secure field.
  No exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com", port 2222 and "DISABLED" secure field
    Then No exception was thrown
    And I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Schema To Unique Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that change endpoint's Schema - it should remain unique.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I edit last created endpoint Schema to "Schema2"
    And No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Schema To Non-unique Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that change endpoint's Schema - it should not remain unique.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abd.com" and port 2221
    Then I edit last created endpoint Schema to "Schema1"
    And No exception was thrown
    Then I delete all endpoints with schema "Schema1"
    And I logout

  Scenario: Editing Endpoint Schema So It Contains Invalid Symbols
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Schema to a invalid value.
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I edit last created endpoint Schema to "Schema@"
    Then An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Schema So It Contains Only Numbers
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Schema so it contains only numbers.
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I edit last created endpoint Schema to "12345"
    Then An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Schema So It Has Max Length
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Schema so it has maximal length.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    When I edit last created endpoint Schema to "BpCIO90RoOspstaZOvIsHaflsrnY6lYS9jQHBsL7SzvlaqYUkyz9vtvJcnzMZlwo"
    Then No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Schema So It Has Min Length
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Schema so it has minimal length.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    When I edit last created endpoint Schema to "a"
    Then No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Schema So It Contains "http://"
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Schema so it contains "http://".
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I edit last created endpoint Schema to "http://abc.com"
    Then An exception was thrown
    And I delete endpoint with schema "schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Schema So It Contains "https://"
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Schema so it contains "https://".
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I edit last created endpoint Schema to "https://abc.com"
    Then An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Schema And Leaving It Empty
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that delete endpoint's Schema and keep it empty.
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I edit last created endpoint Schema to ""
    Then An exception was thrown
    And I delete endpoint with schema "schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Domain Name To Unique Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Domain Name - it should stay valid and unique.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I edit last created endpoint Domain Name to "abd.com"
    And No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Domain Name To Non-unique Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Domain Name - it should be invalid but unique.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abd.com" and port 2221
    Then I edit last created endpoint Domain Name to "abc.com"
    And No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I delete endpoint with schema "Schema2", domain "abc.com" and port 2221
    And I logout

  Scenario: Editing Endpoint Domain Name So It Contains Invalid Symbols
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Domain Name to a invalid value.
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I edit last created endpoint Domain Name to "domain@abc"
    Then An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Domain Name So It Contains Only Numbers
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Domain Name so it contains only numbers.
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I edit last created endpoint Domain Name to "12345"
    Then An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Domain Name So It Has Max Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Domain Name so it has maximum length.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    When I edit last created endpoint Domain Name to "IApYdQLo4WGsvQ9gSPOIIFFOmkQeIYiGlCEU9zK7e3a9qmMyVSADuM5cJ68H1uEZj4JAML0FbYBXwO32o9AAoqSTsRs3n8tqp4E6YLJUhOnLsGMxrPZ1Rmha4DbNOqweNuvziFkmwTaAc9rIJlUr0VpJEi567JYNvVHSVtK1SdMKAfuLiTQTLOKoGZZIVdp7jwXyhgAEFJxUl3swT1uNz8MOiJHT4ToKCKZ5rwWcN6kYi55XnkMESrjdRKTcSDZmzt8wut7USdPNouIYIMGIzsw1bKDwA2fNx8b9ISsEcDAbX1EWER8GeWkqUAnKBNvNacjYHNEHceeacx72Wchd8uD8Y17w55vdAi98mmA8x7F8fFNZmC4b446Jy8HEgtE42TTOglcgeb0nGCGeUicWqxSeH0DaHf1QFvd7f05NZNli5PYPmeN5WqlVQT6b5bLEala1UD94t5PqWC01o59hHCSAJyD7Zj01gWbyuDy5LmkuARZybDdxige6CzDVWvKBYxLthaLqqE3E7cWQA6wsDPqtyEsfoCycYXuPYGx81M7f1JVvh0I5oj2Eg0VHvYcvGnfUUBXUY3abYzCyvEBf2kZ6pQUkwgJHBaA2p7UBYTqpqt5zfc5w3qn1n6uD1QzyQukvqBvuaVPlXqVobmyz7VTgGBT45cMGevJhs5Td4UrtjifSmzEDbuzBZbQub3cbutLLmJqH2pctG4gtBPz2GNNRCC2Lsx2PsYynkUr5c9aBeENFXmR41BDRLlyEzpKRtzJ6QLEVrZKAvNahm8wvlmzI6JkLB9YZpEm7diq54bmm4EaolSBvs2X9e5EEGPosoIjyQfNsbasognvYY4fGCiNuTchmxnHnPlMyfRcGPas8SWYH7fvQFDWLquEXqX3kHGAn4KsShiyAvbj9OeJ4ao2QmJSHOtP0212nx4iooHGPpMOM8difFRt0i4AYdb68q51cs8OtHaW41IZSlJai6FttoiMl3ln7uAIHp6FrPXNBmwoMxAvUgdn60mXPs1XK"
    Then No exception was thrown
    And I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Domain Name So It Has Min Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Domain Name so it has minimum length.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    When I edit last created endpoint Domain Name to "a"
    Then No exception was thrown
    And I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Deleting Endpoint Domain Name And Leaving it Empty
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that delete endpoint's Domain Name and leave it empty.
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I edit last created endpoint Domain Name to ""
    Then An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Port To Unique Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Port number so it stays valid and unique.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I edit port number to 1111 in endpoint with schema "Schema1", domain "abc.com" and port 2222
    And No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 1111
    And I logout

  Scenario: Editing Endpoint Port To Non-unique Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Port number to a non-unique value.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abd.com" and port 2221
    Then I edit last created endpoint Port to 2222
    And No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I delete endpoint with schema "Schema2", domain "abd.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Secure Field To Unique Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Secure field to a unique value.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com", port 2222 and "ENABLED" secure field
    Then I edit last created endpoint Secure field to "DISABLED"
    And No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint Secure Field To Non-unique Value
  Login as kapua-sys.
  Create endpoint with all valid parameters. After that edit endpoint's Secure field to a non-unique value.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com", port 2222 and "ENABLED" secure field
    And I create endpoint with schema "Schema2", domain "abd.com", port 2221 and "DISABLED" secure field
    Then I edit last created endpoint Secure field to "ENABLED"
    And No exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I delete endpoint with schema "Schema2", domain "abd.com" and port 2221
    And I logout

  Scenario: Editing Endpoint To Non-unique Endpoint
  Login as kapua-sys.
  Create 2 endpoints. After that edit one of the endpoints so it matches other endpoints parameters.
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abc.com" and port 2222
    Given I expect the exception "KapuaEntityUniquenessException" with the text "*"
    Then I edit last created endpoint Schema to "Schema1"
    And An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I delete endpoint with schema "Schema2", domain "abc.com" and port 2222
    And I logout

  Scenario: Editing Endpoint To NULL Values
  Login as kapua-sys.
  Create endpoint. Edit endpoint parameters to NULL value
  Exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I edit last created endpoint Schema to "NULL"
    Then An exception was thrown
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I edit last created endpoint Domain Name to "NULL"
    Then An exception was thrown
    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I edit last created endpoint Port to 0
    Then An exception was thrown
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I logout

  Scenario: Creating and Deleting Endpoint Two Times
  Login as kapua-sys. Create an endpoint and after that delete it. No exceptions should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    When I try to find endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I found endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "NullPointerException" with the text "*"
    When I try to find endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then An exception was thrown
    And I logout

  Scenario: Creating and Deleting Endpoint Two Times
  Login as kapua-sys.
  Create endpoint, delete it, create another one with same parameters and delete it again.
  No exception should be raised.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    When I try to find endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I found endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "NullPointerException" with the text "*"
    When I try to find endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then An exception was thrown
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    Given I expect the exception "NullPointerException" with the text "*"
    When I try to find endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then An exception was thrown
    And I logout

  Scenario: Deleting Endpoint Which Does Not Exist
  Login as kapua-sys
  I try to delete endpoint that does not exist
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "NullPointerException" with the text "*"
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then An exception was thrown
    And I logout

  Scenario: List Endpoints From "kapua-sys" Account
  Login as kapua-sys.
  Create several endpoints.
  List those endpoints.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abd.com" and port 2223
    And I create endpoint with schema "Schema3", domain "abd.com" and port 2223
    When I search for all endpoints in current scopeId
    Then I find 3 endpoints
    And I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I delete endpoint with schema "Schema2", domain "abd.com" and port 2223
    And I delete endpoint with schema "Schema3", domain "abd.com" and port 2223
    Then I logout

  Scenario: List Endpoints From Sub-account
  Login as kapua-sys.
  Create several endpoints. Create sub-account without creating new endpoints.
  List endpoints. Endpoints from kapua-sys account should be listed.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abd.com" and port 2223
    And I create endpoint with schema "Schema3", domain "abd.com" and port 2223
    Then I create a generic account with name "Account-1" in current scopeId
    Given I search for all endpoints in current scopeId
    Then I find 3 endpoints
    When I try to find endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I found endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I select account "kapua-sys"
    And I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I delete endpoint with schema "Schema2", domain "abd.com" and port 2223
    And I delete endpoint with schema "Schema3", domain "abd.com" and port 2223
    Then I logout

  Scenario: List Endpoints Created From Sub-Account
  Login as kapua-sys.
  Create several endpoints. Create sub-account and several endpoints and create an user in this account.
  List endpoints. Endpoints from sub-account should be listed.
  Delete endpoints under sub-account. Create a query again. Endpoints from kapua-sys account should be listed.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abd.com" and port 2223
    Then I create a generic account with name "Account-1" in current scopeId
    And I create endpoint with schema "Schema3", domain "ddd.com" and port 1111
    And I create endpoint with schema "Schema4", domain "ddd.com" and port 1111
    And I create endpoint with schema "Schema5", domain "ddd.com" and port 1111
    Then I search for all endpoints in current scopeId
    Then I find 3 endpoints
    When I try to find endpoint with schema "Schema3", domain "ddd.com" and port 1111
    Then I found endpoint with schema "Schema3", domain "ddd.com" and port 1111
    Then I select account "Account-1"
    Then I delete endpoint with schema "Schema3", domain "ddd.com" and port 1111
    Then I delete endpoint with schema "Schema4", domain "ddd.com" and port 1111
    Then I delete endpoint with schema "Schema5", domain "ddd.com" and port 1111
    When I search for all endpoints in current scopeId
    Then I find 2 endpoints
    When I try to find endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I found endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I logout
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    Then I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    Then I delete endpoint with schema "Schema2", domain "abd.com" and port 2223
    And I logout

  Scenario: List Endpoints From Sub-Account Of Another Sub-Account
  Login as kapua-sys. Create sub-account (Account-1) and under this sub-account another one (Account-11).
  Create endpoints under all those accounts.
  List all endpoints from Account-11. Endpoints from Account-11 should be listed.
  Delete all endpoints from Account-11 and list all endpoints again. Endpoints from Account-1 should be listed.
  Delete all endpoints from Account-1 and list all endpoints again from Account-11. Endpoints from kapua-sys account should be listed.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I create endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema2", domain "abc.com" and port 2222
    Then I create a generic account with name "Account-1" in current scopeId
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And I create endpoint with schema "Schema3", domain "abc.com" and port 2222
    Then I create a generic account with name "Account-11" in current scopeId
    And I create endpoint with schema "Schema4", domain "abc.com" and port 2222
    And I create endpoint with schema "Schema5", domain "abc.com" and port 2222
    Then I select account "Account-11"
    When I search for all endpoints in current scopeId
    Then I find 2 endpoints
    Then I delete endpoint with schema "Schema4", domain "abc.com" and port 2222
    Then I delete endpoint with schema "Schema5", domain "abc.com" and port 2222
    When I search for all endpoints in current scopeId
    Then I find 1 endpoints
    Then I select account "Account-1"
    And I delete endpoint with schema "Schema3", domain "abc.com" and port 2222
    Then I select account "Account-11"
    And I search for all endpoints in current scopeId
    Then I find 2 endpoints
    And I select account "kapua-sys"
    And I delete endpoint with schema "Schema1", domain "abc.com" and port 2222
    And I delete endpoint with schema "Schema2", domain "abc.com" and port 2222
    And I logout

  Scenario: List CORS filters from kapua-sys account
  Login as kapua-sys, create a CORS filter, then get all CORS filter.
  There should be 1 CORS filter and no exceptions throw.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create a CORS filter with schema "http", domain "localhost" and port 8080
    Then I have 1 CORS filter
    And I delete all CORS filters
    And I logout

  Scenario: List CORS filters from kapua-sys account
  Login as kapua-sys, create 5 CORS filter, then get all CORS filter.
  There should be 5 CORS filters and no exceptions throw.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create the following CORS filters
      | http | localhost | 8080 |
      | http | localhost | 8081 |
      | http | localhost | 8082 |
      | http | localhost | 8090 |
      | http | localhost | 42   |
    Then I have 5 CORS filter
    And I delete all CORS filters
    And I logout

  Scenario: List CORS filters from a newer created user of kapua-sys account
  Login as kapua-sys, create a new user user1 and do not give him the permission to creates
  new CORS filters. Then login as user1 and try to create a CORS filter, an exception should be
  thrown and no filter should be created.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
    And I create user with name "user1"
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "domain"
    And I create the access info entity
    And I create role "test_role" in account "kapua-sys"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Given I login as user with name "user1" and password "User@10031995"
    And I expect the exception "SubjectUnauthorizedException" with the text "*"
    When I create a CORS filter with schema "http", domain "localhost" and port 8080
    Then An exception was thrown
    And I have 0 CORS filter
    And I logout

  Scenario: List CORS filters from a newer created user of kapua-sys account
  Login as kapua-sys, create a new user user1 and do not give him the permission to creates
  new CORS filters (give him instead the domain write permission). Then login as user1 and try to
  create a CORS filter, no exception should be thrown and 1 filter should be created.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
    And I create user with name "user1"
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "domain"
    And I create the access info entity
    And I create role "test_role" in account "kapua-sys"
    And I create the following role permissions
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write       |
      | 1       | delete     |
    And I add access role "test_role" to user "user1"
    And I logout
    Given I login as user with name "user1" and password "User@10031995"
    And I expect the exception "SubjectUnauthorizedException" with the text "*"
    When I create a CORS filter with schema "http", domain "localhost" and port 8080
    Then An exception was thrown
    And I have 0 CORS filter
    And I logout

  Scenario: List CORS filters from a newer created user of kapua-sys account
  Login as kapua-sys, create a new user user1 and give him the permission to creates
  new CORS filters. Then login as user1 and try to create 5 CORS filters, no exception should be
  thrown and 5 new filters should be created.
    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I create a generic account with name "kapua-sub"
    And I select account "kapua-sub"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
    And I create user with name "user1" in account "kapua-sub"
    And I add credentials
      | name  | password      | enabled |
      | user1 | User@10031995 | true    |
    And I select the domain "endpoint_info"
    And I create the access info entity in account "kapua-sub"
    And I create role "test_role" in account "kapua-sub"
    And I create the following role permissions in account "kapua-sub"
      | scopeId | actionName |
      | 1       | read       |
      | 1       | write      |
      | 1       | delete     |
    And I add access role "test_role" to user "user1" in account "kapua-sub"
    And I logout
    Given I login as user with name "user1" and password "User@10031995"
    When I create the following CORS filters
      | http | localhost | 8080 |
      | http | localhost | 8081 |
      | http | localhost | 8082 |
      | http | localhost | 8090 |
      | http | localhost | 42   |
    Then I have 5 CORS filter
    And I delete all CORS filters
    And I logout

@teardown
Scenario: Reset Security Context for all scenarios
    Given Reset Security Context
