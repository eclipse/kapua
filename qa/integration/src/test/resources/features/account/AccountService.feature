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
@account
@integration

Feature: Account Service Tests

  Scenario: Creating A Valid Account
  Login as kapua-sys, create an account with all valid fields
  No exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create an account with name "account-1", organization name "organization" and email adress "org@abc.com"
    Then No exception was thrown
    And I logout

  Scenario: Creating An Account With Unique Name
  Login as kapua-sys, create two accounts with different names
  No exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create an account with name "account-1", organization name "organization1" and email adress "org1@abc.com"
    And I select account "kapua-sys"
    When I create an account with name "account-2", organization name "organization2" and email adress "org2@abc.com"
    Then No exception was thrown
    And I logout


  Scenario: Creating An Account With Non-unique Name
  Login as kapua-sys, create an account and create another one with same name
  Exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create an account with name "account-1", organization name "organization" and email adress "org@abc.com"
    And I select account "kapua-sys"
    Given I expect the exception "KapuaDuplicateNameException" with the text "*"
    When I create an account with name "account-1", organization name "organization2" and email adress "org2@abc.com"
    Then An exception was thrown
    And I logout

  Scenario: Creating An Account With Numbers And Valid Symbols In Name
  Login as kapua-sys, create an account with numbers and '-' in name
  No exception should be thrown, dash and numbers are allowed

    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I create an account with name "1231-2323", organization name "organization" and email adress "org@abc.com"
    Then No exception was thrown
    And I logout