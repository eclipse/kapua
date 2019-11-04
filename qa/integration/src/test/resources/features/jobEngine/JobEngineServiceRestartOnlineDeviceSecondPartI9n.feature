###############################################################################
# Copyright (c) 2019 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@jobEngineRestartOnlineDeviceSecondPart
@integration

Feature: JobEngineService restart job tests with online device - second part

  Scenario: Set environment variables
    Given System property "broker.ip" with value "localhost"
    And System property "commons.db.connection.host" with value "localhost"

  Scenario: Start event broker for all scenarios
    Given Start Event Broker

  Scenario: Start broker for all scenarios
    Given Start Broker

    # *************************************************
    # * Restarting a job with one Target and one Step *
    # *************************************************

  Scenario: Restarting A Job with valid Configuration Put Step And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Configuration Put step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    Then I create a new step entity from the existing creator
    And I restart a job
    And I wait 30 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting A Job with invalid Configuration Put Step And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Configuration Put step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                               |
    Then I create a new step entity from the existing creator
    And I restart a job
    And I wait 15 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting A Job with valid Configuration Put Step And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Configuration Put step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    Then I create a new step entity from the existing creator
    And I restart a job
    And I wait 30 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then I restart a job
    And I wait 30 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting A Job with invalid Configuration Put Step And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Configuration Put step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                               |
    Then I create a new step entity from the existing creator
    And I restart a job
    And I wait 15 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then I restart a job
    And I wait 15 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting a job with valid Package Install Step And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Package Install step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK.

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Install Step And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Package Install Command step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED.

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with valid Package Install Step And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Package Install Command step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK.

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    And I restart a job
    And I wait 60 seconds
    Then I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    When I restart a job
    And I wait 60 seconds
    Then I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Install Step And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Package Install Command step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED.

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    And I restart a job
    And I wait 15 seconds
    Then I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    When I restart a job
    And I wait 15 seconds
    Then I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting Job With Valid Asset Write And Step Index=0
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Asset Write step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device event
    And The type of the last event is "ASSET"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><valueType>java.lang.Integer</valueType><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 4 device events
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid Asset Write And Step Index=0
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Asset Write step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device event
    And The type of the last event is "ASSET"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device event
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting A Job with valid Package Uninstall Step And Step Index=0
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Package Uninstall step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.publisher</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    And I restart a job
    And I wait 60 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Packages are requested
    And Number of received packages is 2
    Then I restart a job
    And I wait 60 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Packages are requested
    And Number of received packages is 2
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting A Job with invalid Package Uninstall Step And Step Index=0
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Package Uninstall step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                   |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.publisher</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    And I restart a job
    And I wait 15 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Packages are requested
    And Number of received packages is 1
    Then I restart a job
    And I wait 15 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Packages are requested
    And Number of received packages is 1
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

    # *******************************************************
    # * Restarting a job with one Target and multiple Steps *
    # *******************************************************

  Scenario: Restarting Job With Valid Configuration Put And Bundle Start Steps And Step Index=0 For The First Time
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Configuration Put and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep1" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid Configuration Put And Bundle Start Steps And Step Index=0 For The First Time
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Configuration Put and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep1" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid Configuration Put And Bundle Start Steps And Step Index=0 For The Second Time
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Configuration Put and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep1" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid Configuration Put And Bundle Start Steps And Step Index=0 For The Second Time
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Configuration Put and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep1" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting a job with valid Package Install and Bundle Start Steps And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Package Install and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK.

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    Then Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 45 seconds
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Install and Bundle Start Steps And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Package Install and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED.

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    Then Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with valid Package Install and Bundle Start Steps And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Package Install and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK.

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    Then Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then I restart a job
    And I wait 45 seconds
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Install and Bundle Start Steps And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Package Install and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED.

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    Then Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 second
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 second
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting Job With Valid Asset Write and Bundle Start And Step Index=0
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Asset Write and valid Bundle Start steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "ASSET"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><valueType>java.lang.Integer</valueType><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 7 device events
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid Asset Write and Bundle Start And Step Index=0
  Create a new job and set a connected KuraMock device as the job target.
  Add new invalid Asset Write and invalid Bundle Start steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "ASSET"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid Package Uninstall And Bundle Start Steps And Step Index=0
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Package Uninstall and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.publisher</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Packages are requested
    And Number of received packages is 2
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Packages are requested
    And Number of received packages is 2
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid Package Uninstall And Bundle Start Steps And Step Index=0
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Package Uninstall and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                   |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.publisher</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

    # *******************************************************
    # * Restarting a job with multiple Targets and one Step *
    # *******************************************************

  Scenario: Restarting A Job With Valid Configuration Put Step, Multiple Devices and Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job target.
  Add a new valid Configuration Put step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Configuration Put Step, Multiple Devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job target.
  Add a new invalid Configuration Put step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And I wait 1 second
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting A Job With Valid Configuration Put Step, Multiple Devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job target.
  Add a new valid Configuration Put step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Configuration Put, Multiple Devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job target.
  Add a new invalid Configuration Put step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And I wait 1 second
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with valid Package Install Step, Multiple Devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets. Add a new valid Package Install
  step to the created job. Restart the job. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_OK.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And I get the KuraMock devices
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I wait 45 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Install Step, Multiple Devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets. Add a new invalid Package Install
  step to the created job. Restart the job. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And I get the KuraMock devices
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with valid Package Install Step, Multiple Devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target. Add a new valid Package Install
  step to the created job. Restart the job two times. After the executed job is finished, the executed
  target's step index should be 0 and the status PROCESS_OK.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And I get the KuraMock devices
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    And I restart a job
    And I wait 45 seconds
    Then I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    When I restart a job
    And I wait 45 seconds
    Then I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Install Step, Multiple Devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets. Add a new invalid Package Install
  step to the created job. Restart the job two times. After the executed job is finished, the executed target's
  step index should be 0 and the status PROCESS_FAILED.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And I get the KuraMock devices
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    And I restart a job
    And I wait 15 seconds
    Then I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    When I restart a job
    And I wait 15 seconds
    Then I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting Job With Valid Asset Write, multiple devices And Step Index=0
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Asset Write step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "ASSET"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><valueType>java.lang.Integer</valueType><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 4 device events
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid Asset Write, multiple devices And Step Index=0
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Asset Write step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "ASSET"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting A Job With Valid Package Uninstall Step, Multiple Devices And Step Index=0
  Create a new job and set a connected KuraMock devices as the job target.
  Add a new valid Package Uninstall step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.publisher</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 90 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Packages are requested
    And Number of received packages is 2
    Then I restart a job
    And I wait 90 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Packages are requested
    And Number of received packages is 2
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Uninstall, Multiple Devices And Step Index=0
  Create a new job and set a connected KuraMock devices as the job target.
  Add a new invalid Package Uninstall step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And I wait 1 seconds
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                   |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.publisher</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Packages are requested
    And Number of received packages is 1
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Packages are requested
    And Number of received packages is 1
    And KuraMock is disconnected
    And I logout

    # *************************************************************
    # * Restarting a job with multiple Targets and multiple Steps *
    # *************************************************************

  Scenario: Restarting job with valid Configuration Put and Bundle Start Steps, Multiple Devices And Step Index=0 For The First Time
  Create a new job. Set a connected Kura Mock devices as a job target.
  Add a new valid Configuration Put and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep1" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 45 seconds
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Configuration Put and Bundle Start Steps, Multiple Devices And Step Index=0 For The First Time
  Create a new job. Set connected Kura Mock devices as a job target.
  Add a new invalid Configuration Put and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep1" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When KuraMock is disconnected
    And I logout

  Scenario: Restarting job with valid Configuration Put and Bundle Start Steps, Multiple Devices And Step Index=0 For The Second Time
  Create a new job. Set a connected Kura Mock devices as a job target.
  Add a new valid Configuration Put and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep1" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Configuration Put and Bundle Start Steps, Multiple Devices And Step Index=0 For The Second Time
  Create a new job. Set connected Kura Mock devices as a job target.
  Add a new invalid Configuration Put and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep1" and the following properties
      | name          | type                                                                           | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration  | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag>|
      | timeout       | java.lang.Long                                                                 | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with valid Package Install and Bundle Start Steps, Multiple Devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets. Add a new valid Package Install
  and Bundle Start steps to the created job. Restart the job. After the executed job is finished,
  the executed target's step index should be 1 and the status PROCESS_OK.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And I get the KuraMock devices
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    Then Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Install and Bundle Start Steps, Multiple Devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets. Add a new invalid Package Install
  and Bundle Start steps to the created job. Restart the job. After the executed job is finished,
  the executed target's step index should be 0 and the status PROCESS_FAILED.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And I get the KuraMock devices
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    Then Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with valid Package Install and Bundle Start Steps, Multiple Devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets. Add a new valid Package Install
  and Bundle Start steps to the created job. Restart the job two times. After the executed job is finished,
  the executed target's step index should be 1 and the status PROCESS_OK.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And I get the KuraMock devices
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    Then Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 45 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then I restart a job
    And I wait 45 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Install and Bundle Start Steps, Multiple Devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets. Add a new invalid Package Install
  and Bundle Start steps to the created job. Restart the job two times. After the executed job is finished,
  the executed target's step index should be 0 and the status PROCESS_FAILED.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And I get the KuraMock devices
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    Then Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I wait 1 seconds
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting Job With Valid Asset Write and Bundle Start, multiple devices And Step Index=0
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Asset Write and valid Bundle Start steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "ASSET"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><valueType>java.lang.Integer</valueType><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 9 device events
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid Asset Write and Bundle Start, multiple devices And Step Index=0
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Asset Write and invalid Bundle Start steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "ASSET"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 5 device events
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting job with valid Package Uninstall and Bundle Start Steps, Multiple Devices And Step Index=0
  Create a new job. Set a connected Kura Mock devices as a job target.
  Add a new valid Package Uninstall and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.publisher</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 90 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Packages are requested
    And Number of received packages is 2
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then I restart a job
    And I wait 90 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then Packages are requested
    And Number of received packages is 2
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Package Uninstall and Bundle Start Steps, Multiple Devices And Step Index=0
  Create a new job. Set connected Kura Mock devices as a job target.
  Add a new invalid Package Uninstall and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    Then I add 2 devices to Kura Mock
    And Devices "are" connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope
    Then I count 2
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                   |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.publisher</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then Packages are requested
    And Number of received packages is 1
    Then Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When KuraMock is disconnected
    And I logout

  Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker