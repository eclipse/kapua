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
###############################################################################
@default
Feature: User Account Service
    The User Account Service is responsible for CRUD operations for user accounts in the Kapua
    database.

Scenario: Handle account creation
    Create a test account and check whether it was created correctly.

    When I create account "test_acc"
    Then The account matches the creator settings

Scenario: Find all child accounts
    Create a number of subaccounts. Check that the account has the correct number of subaccounts.

    When I create 15 childs for account with Id 1
    Then The account with Id 1 has 15 subaccounts

Scenario: Handle duplicate account names
    Accounts with duplicate names must not be created. When a duplicate account name is
    given, the account service should throw an exception.

    Given An existing account with the name "test_acc_1"
    And I expect the exception "KapuaException" with the text "Error during Persistence Operation"
    When I create a duplicate account "test_acc_1"
    Then An exception is caught

Scenario: Handle null account name
    If an account name is null, the account service should throw an exception.

    Given I expect the exception "KapuaIllegalNullArgumentException" with the text "*"
    When I create an account with a null name
    Then An exception is caught

Scenario: Find account by Id
    The account service must be able to find the requested account entity by its Id.

     Given An existing account with the name "test_acc_42"
     When I search for the account with the remembered account Id
     Then The account matches the creator settings

Scenario: Find account by Ids
    The account service must be able to find the requested account entity by its
    parent and account Ids.

    Given An existing account with the name "test_acc_42"
    When I search for the account with the remembered parent and account Ids
    Then The account matches the creator settings

Scenario: Find account by random Id
    Search for an account that does not exist.

    When I search for a random account Id
    Then The account does not exist

Scenario: Find account by name
    The account service must be able to find the requested account entity by its name.

    Given An existing account with the name "test_acc_42"
    When I search for the account with name "test_acc_42"
    Then The account matches the creator settings

Scenario: Find by name nonexisting account
    Search for an account name that does not exist.

    When I search for the account with name "test_acc_unlikely_to_exist"
    Then The account does not exist

Scenario: Modify an existing account
    Test that the account service correctly modifies an existing account. The modified
    account details must be correctly stored in the account database.

    Given An existing account with the name "test_acc_42"
    When I modify the account "test_acc_42"
    Then Account "test_acc_42" is correctly modified

Scenario: Modify nonexisting account
    Try to update an account that does not exist anymore. An exception should be thrown.

    Given An existing account with the name "test_acc_42"
    Then I delete account "test_acc_42"
    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type account with id/name"
    And I modify the current account
    Then An exception is caught

Scenario: Delete an existing account
    Delete a previously created account. The account should be deleted from the database.

    Given An existing account with the name "test_acc_123"
    When I delete account "test_acc_123"
    And I search for the account with name "test_acc_123"
    Then The account does not exist

Scenario: Delete the Kapua system account
    It must not be possible to delete the system account.

    Given I expect the exception "KapuaIllegalAccessException" with the text "The current subject is not authorized for write"
    When I try to delete the system account
    Then An exception is caught
    And The System account exists

Scenario: Delete nonexisting account
    Try to delete an account with a a random Id. The operation should fail and an exception
    should be thrown.

    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type account with id/name"
    When I delete a random account
    Then An exception is caught

Scenario: Change the account parent Id
    The account service must not allow the account parent Id to be changed.

    Given An existing account with the name "test_acc_123"
    And I expect the exception "KapuaAccountException" with the text "An illegal value was provided for the argument"
    When I try to change the account "test_acc_123" scope Id to 2
    Then An exception is caught

Scenario: Change the account parent path
    It must not be possible to change the account parent path. Any try should result in an exception.

    Given An existing account with the name "test_acc_11"
    And I expect the exception "KapuaAccountException" with the text "An illegal value was provided for the argument"
    When I change the parent path for account "test_acc_11"
    Then An exception is caught

Scenario: Check account properties
    It must be possible to set arbitrary account properties.

    Given An existing account with the name "test_acc_11"
    When I set the following parameters
        | name | value  |
        | key1 | value1 |
        | key2 | value2 |
        | key3 | value3 |
    Then The account has the following parameters
        | name | value  |
        | key1 | value1 |
        | key2 | value2 |
        | key3 | value3 |

Scenario: Every account must have the default configuration items
    Create a new account and check whether it has the default configuration items set.

    Given An existing account with the name "test_acc_11"
    Then The default configuration for the account is set

Scenario: A newly created account must have some metadata
    Create a new account. Check whether the account has some associated metadata.

    Given An existing account with the name "test_acc_11"
    Then The account has metadata

Scenario: It is possible to change the configuration items
    Values of the supported configurationm items must be modifiable.

    Given An existing account with the name "test_acc_11"
    When I configure "integer" item "maxNumberChildEntities" to "5"
    Then The config item "maxNumberChildEntities" is set to "5"

Scenario: Unknown configuiration items are silently ignored
    Unknown items must be ignored. No exception or error must be raised.

    Given An existing account with the name "test_acc_11"
    When I add the unknown config item "UnknownItem" with value 10
    Then The config item "UnknownItem" is missing

Scenario: Setting configuration without mandatory items must raise an error
    Mandatory configuration items must always be set. Trying to set configuration items without
    specifying the mandatory items must raise an error.

    Given An existing account with the name "test_acc_11"
    And I expect the exception "KapuaConfigurationException" with the text "Required configuration attribute missing"
    When I configure "integer" item "ArbitraryUnknownItem" to "5"
    Then An exception is caught

Scenario: Test account query
    Perform a database query and find all the accounts that have the 'scopeId'
    property set to '1' (children of the system account). This is the default query and
    no additional parameter is needed to create it.

    Given I create 9 accounts with organization name "test_org"
    When I query for all accounts that have the system account as parent
    Then The returned value is 9

Scenario: Account service metadata
    The Account service must have some associated configuration metadata. Check that the
    metadata exists. No content checks will be performed.

    Then Account service metadata is available

Scenario: Account name must not be mutable
    It must be impossible to change an existing account name. When tried, an exception must
    be thrown and the original account must be unchanged.

    Given An existing account with the name "test_acc"
    And I expect the exception "KapuaAccountException" with the text "An illegal value was provided for the argument"
    When I change the account "test_acc" name to "test_acc_new"
    Then An exception is caught
    And Account "test_acc" exists
