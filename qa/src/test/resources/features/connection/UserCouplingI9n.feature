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
@connection
Feature: User Coupling

  @StartBroker
  Scenario: Start broker for all scenarios

  Scenario: Test LOOSE user coupling on single connection

    Given Account
      | name        | scopeId |
      | test-acc-1  | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "LOOSE"
    And Such a set of privileged users for account "test-acc-1"
      | name        | password   | displayName  | status  |
      | test-user-1 | kp-pass    | Test User 1  | ENABLED |
      | test-user-2 | kp-pass    | Test User 2  | ENABLED |
    And Devices such as
      | clientId | displayName | modelId         | serialNumber |
      | device-1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
    And The following device connections
      | scope       | clientId   | user        | userCouplingMode |
      | test-acc-1  | device-1   | test-user-1 | LOOSE            |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kp-pass@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kp-pass@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-2"
    Then I stop the simulator
    And I wait for 2 seconds

  Scenario: New connection with reserved ID

    Given Account
      | name        | scopeId |
      | test-acc-1  | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "LOOSE"
    And Such a set of privileged users for account "test-acc-1"
      | name        | password   | displayName  | status  |
      | test-user-1 | kp-pass    | Test User 1  | ENABLED |
      | test-user-2 | kp-pass    | Test User 2  | ENABLED |
    And Devices such as
      | clientId | displayName | modelId         | serialNumber |
      | device-1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
    And The following device connections
      | scope       | clientId   | user        | reservedUser |
      | test-acc-1  | device-1   | test-user-1 | test-user-1  |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kp-pass@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kp-pass@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 0 connections

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kp-pass@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-2"
    Then I stop the simulator
    And I wait for 2 seconds

  Scenario: Test STRICT user coupling on single connection

    Given Account
      | name        | scopeId |
      | test-acc-1  | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "LOOSE"
    And Such a set of privileged users for account "test-acc-1"
      | name        | displayName  | status  |
      | test-user-1 | Test User 1  | ENABLED |
      | test-user-2 | Test User 2  | ENABLED |
    And Devices such as
      | clientId | displayName | modelId         | serialNumber |
      | device-1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
    And The following device connections
      | scope       | clientId   | user        | userCouplingMode |
      | test-acc-1  | device-1   | test-user-1 | STRICT           |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

  Scenario: Test STRICT user coupling with user change allowed on single connection

    Given Account
      | name        | scopeId |
      | test-acc-1  | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "LOOSE"
    And Such a set of privileged users for account "test-acc-1"
      | name        | displayName  | status  |
      | test-user-1 | Test User 1  | ENABLED |
      | test-user-2 | Test User 2  | ENABLED |
    And Devices such as
      | clientId | displayName | modelId         | serialNumber |
      | device-1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
    And The following device connections
      | scope       | clientId   | user        | userCouplingMode |
      | test-acc-1  | device-1   | test-user-1 | STRICT           |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-1" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-2"
    Then I stop the simulator
    And I wait for 2 seconds

  Scenario: Test LOOSE user coupling with 3 connections

    Given Account
      | name        | scopeId |
      | test-acc-1  | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "LOOSE"
    And Such a set of privileged users for account "test-acc-1"
      | name        | displayName  | status  |
      | test-user-1 | Test User 1  | ENABLED |
      | test-user-2 | Test User 2  | ENABLED |
      | test-user-3 | Test User 3  | ENABLED |
    And Devices such as
      | clientId | displayName | modelId         | serialNumber |
      | device-1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
      | device-2 | testGateway | ReliaGate 10-20 | 12341234ABD  |
      | device-3 | testGateway | ReliaGate 10-20 | 12341234ABE  |
    And The following device connections
      | scope       | clientId   | user        | userCouplingMode |
      | test-acc-1  | device-1   | test-user-1 | LOOSE            |
      | test-acc-1  | device-2   | test-user-2 | LOOSE            |
      | test-acc-1  | device-3   | test-user-3 | LOOSE            |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

  Scenario: Test STRICT user coupling with 3 connections and a reserved user

    Given Account
      | name       | scopeId |
      | test-acc-1 | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "LOOSE"
    And Such a set of privileged users for account "test-acc-1"
      | name        | displayName | status  |
      | test-user-1 | Test User 1 | ENABLED |
      | test-user-2 | Test User 2 | ENABLED |
      | test-user-3 | Test User 3 | ENABLED |
    And Devices such as
      | clientId | displayName | modelId         | serialNumber |
      | device-1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
      | device-2 | testGateway | ReliaGate 10-20 | 12341234ABD  |
      | device-3 | testGateway | ReliaGate 10-20 | 12341234ABE  |
    And The following device connections
      | scope      | clientId | user        | userCouplingMode | reservedUser |
      | test-acc-1 | device-1 | test-user-1 | STRICT           | test-user-1  |
      | test-acc-1 | device-2 | test-user-2 | LOOSE            |              |
      | test-acc-1 | device-3 | test-user-3 | LOOSE            |              |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

  Scenario: Extra long continuous test with multiple subscenarios with LOOSE default connection mode

    Given Account
      | name        | scopeId |
      | test-acc-1  | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "LOOSE"
    And Such a set of privileged users for account "test-acc-1"
      | name        | displayName  | status  |
      | test-user-1 | Test User 1  | ENABLED |
      | test-user-2 | Test User 2  | ENABLED |
      | test-user-3 | Test User 3  | ENABLED |
    And Devices such as
      | clientId | displayName | modelId         | serialNumber |
      | device-1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
      | device-2 | testGateway | ReliaGate 10-20 | 12341234ABD  |
      | device-3 | testGateway | ReliaGate 10-20 | 12341234ABE  |
    And The following device connections
      | scope       | clientId   | user        |
      | test-acc-1  | device-1   | test-user-1 |
      | test-acc-1  | device-2   | test-user-2 |
      | test-acc-1  | device-3   | test-user-3 |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Then I set the user coupling mode for the connection from device "device-1" in account "test-acc-1" to "STRICT"
    And I set the reserved user for the connection from device "device-1" in account "test-acc-1" to "test-user-1"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    And I set the reserved user for the connection from device "device-1" in account "test-acc-1" to "null"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator

    And I set the reserved user for the connection from device "device-1" in account "test-acc-1" to "test-user-1"
    Then I set the user coupling mode for the connection from device "device-2" in account "test-acc-1" to "STRICT"
    # Try to set a duplicate reserved user
    When I set the reserved user for the connection from device "device-2" in account "test-acc-1" to "test-user-1"
    Then An exception was thrown
    # Reserved users must be unique!
    When I set the reserved user for the connection from device "device-2" in account "test-acc-1" to "test-user-2"
    Then No exception was thrown

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    When I set the reserved user for the connection from device "device-2" in account "test-acc-1" to "null"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Then I set the user change flag for the connection from device "device-2" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-2" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-2" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Then I set the user coupling mode for the connection from device "device-3" in account "test-acc-1" to "STRICT"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-3" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-3" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

  Scenario: Extra long continuous test with multiple subscenarios with STRICT default connection mode

    Given Account
      | name       | scopeId |
      | test-acc-1 | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "STRICT"
    And Such a set of privileged users for account "test-acc-1"
      | name        | displayName | status  |
      | test-user-1 | Test User 1 | ENABLED |
      | test-user-2 | Test User 2 | ENABLED |
      | test-user-3 | Test User 3 | ENABLED |
    And Devices such as
      | clientId | displayName | modelId         | serialNumber |
      | device-1 | testGateway | ReliaGate 10-20 | 12341234ABC  |
      | device-2 | testGateway | ReliaGate 10-20 | 12341234ABD  |
      | device-3 | testGateway | ReliaGate 10-20 | 12341234ABE  |
    And The following device connections
      | scope      | clientId | user        |
      | test-acc-1 | device-1 | test-user-1 |
      | test-acc-1 | device-2 | test-user-2 |
      | test-acc-1 | device-3 | test-user-3 |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user coupling mode for the connection from device "device-1" in account "test-acc-1" to "STRICT"
    And I set the reserved user for the connection from device "device-1" in account "test-acc-1" to "test-user-1"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    And I set the reserved user for the connection from device "device-1" in account "test-acc-1" to "null"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator

    And I set the reserved user for the connection from device "device-1" in account "test-acc-1" to "test-user-1"
    Then I set the user coupling mode for the connection from device "device-2" in account "test-acc-1" to "STRICT"
# Try to set a duplicate reserved user
    When I set the reserved user for the connection from device "device-2" in account "test-acc-1" to "test-user-1"
    Then An exception was thrown
# Reserved users must be unique!
    When I set the reserved user for the connection from device "device-2" in account "test-acc-1" to "test-user-2"
    Then No exception was thrown

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    When I set the reserved user for the connection from device "device-2" in account "test-acc-1" to "null"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Then I set the user change flag for the connection from device "device-2" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-2" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-2" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Then I set the user change flag for the connection from device "device-3" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-3" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    Then I stop the simulator
    And I wait for 2 seconds

  Scenario: Extra long continuous test with multiple subscenarios with STRICT default connection mode and no previously defined devices

    Given Account
      | name       | scopeId |
      | test-acc-1 | 1       |
    And A full set of device privileges for account "test-acc-1"
    And The default connection coupling mode for account "test-acc-1" is set to "STRICT"
    And Such a set of privileged users for account "test-acc-1"
      | name        | displayName | status  |
      | test-user-1 | Test User 1 | ENABLED |
      | test-user-2 | Test User 2 | ENABLED |
      | test-user-3 | Test User 3 | ENABLED |

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-2"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-2"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    When I set the reserved user for the connection from device "device-2" in account "test-acc-1" to "test-user-2"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-2"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-3" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-3"
    Then I stop the simulator
    And I wait for 2 seconds

    And The default connection coupling mode for account "test-acc-1" is set to "LOOSE"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-3"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-2"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-2
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-2" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-3"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-3
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-3" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-3"
    Then I stop the simulator
    And I wait for 2 seconds

    And The default connection coupling mode for account "test-acc-1" is set to "STRICT"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-3"
    Then I stop the simulator
    And I wait for 2 seconds

    Then I set the user change flag for the connection from device "device-1" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-1:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-1"
    Then I stop the simulator
    And I wait for 2 seconds

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-2:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "DISCONNECTED"

    Then I set the user change flag for the connection from device "device-1" in account "test-acc-1" to "true"

    Given The account name is test-acc-1 and the client ID is device-1
    And The broker URI is tcp://test-user-3:kapua-password@localhost:1883
    When I start the simulator
    And I wait for 2 seconds
    When I search for a connection from the device "device-1" in account "test-acc-1"
    Then I find 1 connection
    And The connection status is "CONNECTED"
    And The connection user is "test-user-3"
    Then I stop the simulator
    And I wait for 2 seconds

  @StopBroker
  Scenario: Stop broker after all scenarios
