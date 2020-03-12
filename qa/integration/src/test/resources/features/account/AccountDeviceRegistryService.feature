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
@device
@integration

Feature: Account Device Registry Service Integration Tests

  Scenario: Creating Devices Under Account That Allows Infinite Child Devices
  Login as kapua-sys, create an account
  Configure DeviceRegistryService of that account, set infiniteChildDevices to true and maxNumberChildDevices to 0
  Create a few Devices
  No exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email adress "acc1@org.com"
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 0     |
    Given I select account "acc1"
    When I create a device with name "Device1"
    And I create a device with name "Device2"
    And I create a device with name "Device3"
    Then No exception was thrown
    And I logout

  Scenario: Creating Devices Under Account That Has Limited Child Devices
  Login as kapua-sys, create an account
  Configure DeviceRegistryService of that account, set infiniteChildDevices to false and maxNumberChildDevices to 3
  Create a few Devices
  Only 3 Devices should be created, creating more will throw an Exception

    Given I login as user with name "kapua-sys" and password "kapua-password"
    Then I create an account with name "acc1", organization name "acc1" and email adress "acc1@org.com"
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | false |
      | integer | maxNumberChildEntities | 3     |
    Given I select account "acc1"
    When I create a device with name "Device1"
    And I create a device with name "Device2"
    And I create a device with name "Device3"
    Then No exception was thrown
    Given I expect the exception "KapuaMaxNumberOfItemsReachedException" with the text "*"
    When I create a device with name "Device4"
    Then An exception was thrown
    And I logout