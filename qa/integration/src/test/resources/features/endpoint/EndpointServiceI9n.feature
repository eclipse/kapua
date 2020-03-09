###############################################################################
# Copyright (c) 2020 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@integration
@endpoint

Feature: Endpoint Info Service Integration Tests
  Integration test scenarios for Endpoint Info service

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
    And I logout

  Scenario: Creating Endpoint With Too Long "Schema"
  Login as kapua-sys
  Create an endpoint with too long Schema
  Exception should be raised

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Given I expect the exception "KapuaIllegalArgumentException" with the text "Value over than allowed max length. Max length is 64."
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