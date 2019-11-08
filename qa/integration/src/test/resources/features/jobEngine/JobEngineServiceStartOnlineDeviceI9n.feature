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
@jobEngineStartOnlineDevice
@integration

Feature: JobEngineService start job tests with online device

  Scenario: Set environment variables
    Given System property "broker.ip" with value "localhost"
    And System property "commons.db.connection.host" with value "localhost"

  Scenario: Start event broker for all scenarios
    Given Start Event Broker

  Scenario: Start broker for all scenarios
    Given Start Broker

    # ***********************************************
    # * Starting a job with one Target and one Step *
    # ***********************************************

  Scenario: Starting a job with valid Command Execution step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Command Execution step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK.

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 1 device event
    And The type of the last event is "BIRTH"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                         |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "COMMAND"
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Command Execution step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Command Execution step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED.

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 1 device event
    And The type of the last event is "BIRTH"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                                                                 |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInputInvalidTag><commandInvalidTag>pwd</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInputInvalidTag> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                                                                 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 1 device event
    And The type of the last event is "BIRTH"
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Bundle Start step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Bundle Start step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Bundle Start step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Bundle Start step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Bundle Stop step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Bundle Stop step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Bundle Stop step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Bundle Stop step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Configuration Put step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Configuration Put step to the created job. Start the job.
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
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    Then I create a new step entity from the existing creator
    And I start a job
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

  Scenario: Starting a job with invalid Configuration Put step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Configuration Put step to the created job. Start the job.
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
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
    Then I create a new step entity from the existing creator
    And I start a job
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

  Scenario: Starting a job with valid Package Install step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Package Install step to the created job. Start the job.
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
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
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

  Scenario: Starting a job with invalid Package Install step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Package Install step to the created job. Start the job.
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
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
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

  Scenario: Starting a job with valid Asset Write step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Asset Write step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device is connected
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
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Asset Write step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Asset Write step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device is connected
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
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Package Uninstall step
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Package Uninstall step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.publisher</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 90 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Uninstall step
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Package Uninstall step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                   |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.publisher</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    And KuraMock is disconnected
    And I logout

    # *****************************************************
    # * Starting a job with one Target and multiple Steps *
    # *****************************************************

  Scenario: Starting job with valid Command Execution and Bundle Start steps
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Command Execution and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Command "pwd" is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    And The type of the last event is "BUNDLE"
    Then I find 3 device events
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                                 |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                                 |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    And The type of the last event is "BUNDLE"
    Then I find 5 device events
    When Command pwd is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting job with invalid Command Execution and Bundle Start steps
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Command Execution and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Command "pwd" is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    And The type of the last event is "BUNDLE"
    Then I find 3 device events
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                                 |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandTag>pwd</commandTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                                 |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device event
    And The type of the last event is "BUNDLE"
    When Command pwd is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with two valid Bundle Start steps
  Create a new job and set a connected KuraMock device as the job target.
  Add two new Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 4 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with two invalid Bundle Start steps
  Create a new job and set a connected KuraMock device as the job target.
  Add two new Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Bundle Stop and Bundle Start steps
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Bundle Stop and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 4 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Bundle Stop and Bundle Start steps
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Bundle Stop and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting job with valid Configuration Put and Bundle Start steps
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Configuration Put and Bundle Start steps to the created job. Start the job.
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
    Then I start a job
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

  Scenario: Starting job with invalid Configuration Put and Bundle Start steps
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Configuration Put and Bundle Start steps to the created job. Start the job.
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
    Then I start a job
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

  Scenario: Starting a job with valid Package Install and Bundle Start steps
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Package Install and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Packages are requested
    Then Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep2" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 60 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And Packages are requested
    Then Number of received packages is 2
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Package Install and Bundle Start steps
  Create a new job and set a connected KuraMock device as the job target.
  Add new invalid Package Install and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 seconds
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Packages are requested
    Then Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep2" and the following properties
      | name                     | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest   | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                  | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Packages are requested
    Then Number of received packages is 1
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Asset Write and Bundle Start steps
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Asset Write and Bundle Start steps to the created job.
  Start the job. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device event
    And The type of the last event is "ASSET"
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
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 5 device events
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Asset Write and Bundle Start steps
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Asset Write and Bundle Start steps to the created job.
  Start the job. After the executed job is finished, the executed target's
  step index should be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device event
    And The type of the last event is "ASSET"
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
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Package Uninstall and Bundle Start step
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Package Uninstall and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Packages are requested
    Then Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.publisher</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 90 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Uninstall and Bundle Start step
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Package Uninstall and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Packages are requested
    Then Number of received packages is 1
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                   |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.publisher</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

    # *****************************************************
    # * Starting a job with multiple Targets and one Step *
    # *****************************************************

  Scenario: Starting job with valid Command Execution step and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new valid Command Execution step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command "pwd" is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "COMMAND"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                                 |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                                 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Command pwd is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 4 device events
    And The type of the last event is "COMMAND"
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting job with invalid Command Execution step and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new invalid Command Execution step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command "pwd" is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "COMMAND"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                                 |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandTag>pwd</commandTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                                 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Command pwd is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting job with valid Bundle Start step and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new Bundle Start step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Device "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then Bundles are received
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting job with invalid Bundle Start step and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new Bundle Start step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Device "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Bundle Stop step and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new valid Bundle Stop step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then Device status is "CONNECTED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 4 device events
    And The type of the last event is "BUNDLE"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Bundle Stop step and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new invalid Bundle Stop step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then Device status is "CONNECTED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | *77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Configuration Put step and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new valid Configuration Put step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
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
    Then I start a job
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

  Scenario: Starting a job with invalid Configuration Put step and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new invalid Configuration Put step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And I wait 1 seconds
    And Devices "are" connected
    Then Device status is "CONNECTED"
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
    Then I start a job
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

  Scenario: Starting a job with valid Asset Write step and multiple targets
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Asset Write step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    When I search for events from device "device1" in account "kapua-sys"
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
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    When I search for events from device "device1" in account "kapua-sys"
    Then I find 3 device events
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Asset Write step and multiple targets
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Asset Write step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device event
    When I search for events from device "device1" in account "kapua-sys"
    Then I find 2 device event
    And The type of the last event is "ASSET"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    When I search for events from device "device1" in account "kapua-sys"
    Then I find 2 device events
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

    # ***********************************************************
    # * Starting a job with multiple Targets and multiple Steps *
    # ***********************************************************
  Scenario: Starting a job with valid Package Uninstall step and multiple targets
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Package Uninstall step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.publisher</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 90 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Uninstall step and multiple targets
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Package Uninstall step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Packages are requested
    And Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                   |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.publisher</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    And KuraMock is disconnected
    And I logout

    # ************************************************************
    # * Starting a job with multiple Targets and multiple Steps  *
    # ************************************************************

  Scenario: Starting job with valid Command Execution, valid Bundle Start steps and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new valid Command Execution and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command "pwd" is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    And The type of the last event is "BUNDLE"
    Then I find 3 device events
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                         |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    And The type of the last event is "BUNDLE"
    Then I find 5 device events
    When Command pwd is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting job with invalid Command Execution, invalid Bundle Start steps and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new invalid Command Execution and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command "pwd" is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    And The type of the last event is "BUNDLE"
    Then I find 3 device events
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                               |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandTag>pwd</commandTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | *34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    When Command pwd is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with two valid Bundle Start steps and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add two new valid Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 4 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with two invalid Bundle Start steps and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add two new invalid Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Bundle Stop and Bundle Start steps and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new valid Bundle Stop and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 4 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Bundle Stop and Bundle Start steps and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new invalid Bundle Stop and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting job with valid Configuration Put and Bundle Start steps and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new valid Configuration Put and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
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
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I start a job
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
    And I logout

  Scenario: Starting job with invalid Configuration Put and Bundle Start steps and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new invalid Configuration Put and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
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
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then I start a job
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
    And I logout

  Scenario: Starting a job with valid Asset Write and Bundle Start steps and multiple devices
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Asset Write and Bundle Start steps to the created job.
  Start the job. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    When I search for events from device "device1" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "ASSET"
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
    And I start a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 5 device events
    When I search for events from device "device1" in account "kapua-sys"
    Then I find 5 device events
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with invalid Asset Write and Bundle Start steps and multiple devices
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Asset Write and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Device "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    When I search for events from device "device1" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "ASSET"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                       |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                       |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    When I search for events from device "device1" in account "kapua-sys"
    Then I find 3 device events
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Starting a job with valid Package Uninstall and Bundle Start step and multiple targets
  Create a new job. Set a connected Kura Mock devices as a job targets.
  Add a new valid Package Uninstall and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Packages are requested
    Then Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.publisher</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 90 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Packages are requested
    Then Number of received packages is 2
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Uninstall and Bundle Start step and multiple targets
  Create a new job. Set a connected Kura Mock devices as a job targets.
  Add a new invalid Package Uninstall and Bundle Start steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Packages are requested
    Then Number of received packages is 1
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                   |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.publisher</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                   |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1 or more
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When Packages are requested
    Then Number of received packages is 1
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker
