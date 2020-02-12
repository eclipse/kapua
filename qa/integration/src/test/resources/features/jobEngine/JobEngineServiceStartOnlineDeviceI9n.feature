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
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 1 device event
    And The type of the last event is "BIRTH"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                         |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And KuraMock is disconnected
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
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 1 device event
    And The type of the last event is "BIRTH"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                                                                 |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInputInvalidTag><commandInvalidTag>pwd</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInputInvalidTag> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                                                                 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search for events from device "rpione3" in account "kapua-sys"
    Then I find 1 device event
    And KuraMock is disconnected
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
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    When KuraMock is disconnected
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
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    When I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
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
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
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
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Configuration Put step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Configuration Put step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When Configuration is requested
    Then A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "CONFIGURATION"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    Then I create a new step entity from the existing creator
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    When I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "CONFIGURATION"
    And Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Configuration Put step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Configuration Put step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When Configuration is requested
    Then A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "CONFIGURATION"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
    Then I create a new step entity from the existing creator
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Package Install step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Package Install step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK.

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "DEPLOY"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 or more device events
    And Packages are requested and 2 packages are received
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Install step
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Package Install step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED.

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    When Packages are requested and 1 package is received
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
    When Device assets are requested
    Then Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device event
    And The type of the last event is "ASSET"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><valueType>java.lang.Integer</valueType><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    When Device assets are requested
    Then Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    And KuraMock is disconnected
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
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                    |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    When Device assets are requested
    Then Asset with name "asset1" and channel with name "channel1" and value 123 are received
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Package Uninstall step
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Package Uninstall step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device event
    And The type of the last event is "DEPLOY"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                  |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.beacon</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                  |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1 or more
    And I wait 1 second
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 or more device events
    And Packages are requested and 0 packages are received
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Uninstall step
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Package Uninstall step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device event
    And The type of the last event is "DEPLOY"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.beacon</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    Then I find 2 device events
    And The type of the last event is "DEPLOY"
    And Packages are requested and 1 package is received
    And KuraMock is disconnected
    And I logout

     #*****************************************************
     #* Starting a job with one Target and multiple Steps *
     #*****************************************************

  Scenario: Starting job with valid Command Execution and Package Install steps
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Command Execution and Package Install steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Command "pwd" is executed
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    And I find 3 device events
    Then The type of the last event is "DEPLOY"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And I search for step definition with the name
      | Command Execution          |
      | Package Download / Install |
    And I create a regular step creator with the name "TestStep1" and properties
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | commandInput           | org.eclipse.kapua.service.device.management.command.DeviceCommandInput                           | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput>                                                                                                                    |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 10000                                                                                                                                                                                                                                                            |
    And I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    When I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    And I find 3 or more device events
    And Packages are requested and 2 packages are received
    When KuraMock is disconnected
    And I logout

  Scenario: Starting job with invalid Command Execution and Package Install steps
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Command Execution and Package Install steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Command "pwd" is executed
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    And I find 3 device events
    Then The type of the last event is "DEPLOY"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And I search for step definition with the name
      | Command Execution          |
      | Package Download / Install |
    And I create a regular step creator with the name "TestStep1" and properties
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | commandInput           | org.eclipse.kapua.service.device.management.command.DeviceCommandInput                           | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandTag>pwd</commandTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput>                                                                                                                 |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 10000                                                                                                                                                                                                                                                               |
    And I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    When I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    And I find 3 device events
    And Packages are requested and 1 package is received
    When KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Bundle Start and Bundle Stop steps
  Create a new job and set a connected KuraMock device as the job target.
  Add two new Bundle Start and Bundle Stop steps to the created job. Start the job.
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
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 4 device events
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Bundle Start and Bundle Stop steps
  Create a new job and set a connected KuraMock device as the job target.
  Add two new Bundle Start and Bundle Stop steps to the created job. Start the job.
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
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Starting job with valid Configuration Put and Command Execution steps
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Configuration Put and Command Execution steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Configuration is requested
    Then A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And Command pwd is executed
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And I search for step definitions with the name
      | Configuration Put |
      | Command Execution |
    And I create a regular step creator with the name "TestStep1" and properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations> |
      | commandInput  | org.eclipse.kapua.service.device.management.command.DeviceCommandInput        | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    When I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then I start a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 5 device events
    And Configuration is requested
    Then A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    When KuraMock is disconnected
    And I logout

  Scenario: Starting job with invalid Configuration Put and Command Execution steps
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Configuration Put and Command Execution steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And Command pwd is executed
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And I search for step definitions with the name
      | Configuration Put |
      | Command Execution |
    And I create a regular step creator with the name "TestStep1" and properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurationsIvanlidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurationsInvalidTag> |
      | commandInput  | org.eclipse.kapua.service.device.management.command.DeviceCommandInput        | <?xml version="1.0" encoding="UTF-8"?><commandInputInvalidTag><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInputInvalidTag>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
    When I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    When KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Package Uninstall and Asset Write steps
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Package Uninstall and Asset Write steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When Device assets are requested
    Then Asset with name "asset1" and channel with name "channel1" and value 123 are received
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "DEPLOY"
    And I create a job with the name "TestJob"
    And I create a new job target item
    And I search for step definitions with the name
      | Package Uninstall |
      | Asset Write       |
    And I create a regular step creator with the name "TestStep1" and properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.beacon</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest>                                    |
      | assets                  | org.eclipse.kapua.service.device.management.asset.DeviceAssets                                     | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><valueType>java.lang.Integer</valueType><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                                     |
    When I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 or more device events
    When Device assets are requested
    Then Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    And Packages are requested and 0 packages are received
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Uninstall and Asset Write steps
  Create a new job and set a connected KuraMock device as the job target.
  Add new invalid Package Uninstall and Asset Write steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When Device assets are requested
    Then Asset with name "asset1" and channel with name "channel1" and value 123 are received
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "DEPLOY"
    Then Number of received packages is 1
    And I create a job with the name "TestJob"
    And I create a new job target item
    And I search for step definitions with the name
      | Package Uninstall |
      | Asset Write       |
    And I create a regular step creator with the name "TestStep1" and properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                                       |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.beacon</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest>                        |
      | assets                  | org.eclipse.kapua.service.device.management.asset.DeviceAssets                                     | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                                       |
    When I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    When Device assets are requested
    Then Asset with name "asset1" and channel with name "channel1" and value 123 are received
    And Packages are requested and 1 package is received
    And KuraMock is disconnected
    And I logout

     #*****************************************************
     #* Starting a job with multiple Targets and one Step *
     #*****************************************************

  Scenario: Starting job with valid Command Execution step and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new valid Command Execution step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command "pwd" is executed
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "COMMAND"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                         |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    Then I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 3 events are found
    When KuraMock is disconnected
    And I logout

  Scenario: Starting job with invalid Command Execution step and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new invalid Command Execution step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command "pwd" is executed
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "COMMAND"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                   | value                                                                                                                                               |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandTag>pwd</commandTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 2 events are found
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    When KuraMock is disconnected
    And I logout

  Scenario: Starting job with valid Bundle Start step and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new Bundle Start step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Device are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    And A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 3 events are found
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Starting job with invalid Bundle Start step and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new Bundle Start step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Device are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 2 events are found
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Bundle Stop step and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new valid Bundle Stop step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 3 events are found
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    Then KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Bundle Stop step and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new invalid Bundle Stop step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | *77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 2 events are found
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Configuration Put step and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add a new valid Configuration Put step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And I search events from devices in account "kapua-sys" and 2 events are found
    And I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope and I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    When I create a new step entity from the existing creator
    Then I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 3 events are found
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
    And I wait 1 second
    And Devices are connected
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And I search events from devices in account "kapua-sys" and 2 events are found
    Given I create a job with the name "TestJob"
    And I add targets to job
    When I count the targets in the current scope and I count 2
    And Search for step definition with the name "Configuration Put"
    And A regular step creator with the name "TestStep" and the following properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurationsInvalidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configurationInvalidTag><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configurationInvalidTag></configurationsInvalidTag> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 2 events are found
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
    When Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "ASSET"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                     |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><valueType>java.lang.Integer</valueType><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                     |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 3 events are found
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    When KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Asset Write step and multiple targets
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Asset Write step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "ASSET"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Asset Write"
    And A regular step creator with the name "TestStep" and the following properties
      | name    | type                                                           | value                                                                                                                                                                                                                                       |
      | assets  | org.eclipse.kapua.service.device.management.asset.DeviceAssets | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout | java.lang.Long                                                 | 10000                                                                                                                                                                                                                                       |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 2 events are found
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Package Uninstall step and multiple targets
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Package Uninstall step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Packages are requested and 1 package is received
    When I search events from devices in account "kapua-sys" and 2 events are found
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                  |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.beacon</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                  |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 or more events are found
    And Packages are requested and 0 packages are received
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Uninstall step and multiple targets
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new invalid Package Uninstall step to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Packages are requested and 1 package is received
    When I search events from devices in account "kapua-sys" and 2 events are found
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.beacon</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 2 events are found
    When Packages are requested and 1 package is received
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Package Install step and multiple devices
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Package Install step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK.

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Packages are requested and 1 package is received
    And I search events from devices in account "kapua-sys" and 2 events are found
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                            |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And  I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 3 or more events are found
    When Packages are requested and 2 packages are received
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Install step and multiple devices
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Package Install step to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED.

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Packages are requested and 1 package is received
    And I search events from devices in account "kapua-sys" and 2 events are found
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Download / Install"
    And A regular step creator with the name "TestStep" and the following properties
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 30000                                                                                                                                                                                                                                                               |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 2 events are found
    When Packages are requested and 1 package is received
    And KuraMock is disconnected
    And I logout

     #************************************************************
     #* Starting a job with multiple Targets and multiple Steps  *
     #************************************************************

  Scenario: Starting job with valid Command Execution, valid Package Install steps and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new valid Command Execution and Package Install steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command "pwd" is executed
    And Packages are requested and 1 package is received
    When I search events from devices in account "kapua-sys" and 3 events are found
    And The type of the last event is "DEPLOY"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for step definition with the name
      | Command Execution          |
      | Package Download / Install |
    And I create a regular step creator with the name "TestStep1" and properties
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                            |
      | commandInput           | org.eclipse.kapua.service.device.management.command.DeviceCommandInput                           | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput>                                                                                                                    |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 10000                                                                                                                                                                                                                                                            |
    And I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    And I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 or more events are found
    And Packages are requested and 2 packages are received
    When KuraMock is disconnected
    And I logout

  Scenario: Starting job with invalid Command Execution, invalid Package Install steps and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new invalid Command Execution and Package Install steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command "pwd" is executed
    And Packages are requested and 1 package is received
    When I search events from devices in account "kapua-sys" and 3 events are found
    And The type of the last event is "DEPLOY"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for step definition with the name
      | Command Execution          |
      | Package Download / Install |
    And I create a regular step creator with the name "TestStep1" and properties
      | name                   | type                                                                                             | value                                                                                                                                                                                                                                                               |
      | commandInput           | org.eclipse.kapua.service.device.management.command.DeviceCommandInput                           | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandTag>pwd</commandTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput>                                                                                                                 |
      | packageDownloadRequest | org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest | <?xml version="1.0" encoding="UTF-8"?><downloadRequest><uri>###http://download.eclipse.org/kura/releases/3.2.0/org.eclipse.kura.example.publisher_1.0.300.dp</uri><name>Example Publisher</name><version>1.0.300</version><install>true</install></downloadRequest> |
      | timeout                | java.lang.Long                                                                                   | 10000                                                                                                                                                                                                                                                               |
    And I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 events are found
    And Packages are requested and 1 package is received
    When KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Bundle Start and Bundle Stop steps and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add two new valid Bundle Start and Bundle Stop steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 4 events are found
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Bundle Start and Bundle Stop steps and multiple devices
  Create a new job and set connected KuraMock devices as the job targets.
  Add two new invalid Bundle Start and Bundle Stop steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep1" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 2 events are found
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Starting job with valid Configuration Put and Command Execution steps and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new valid Configuration Put and Command Execution steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And Command pwd is executed
    When I search events from devices in account "kapua-sys" and 3 events are found
    And The type of the last event is "COMMAND"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for step definitions with the name
      | Configuration Put |
      | Command Execution |
    And I create a regular step creator with the name "TestStep1" and properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations> |
      | commandInput  | org.eclipse.kapua.service.device.management.command.DeviceCommandInput        | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    When I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then I start a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 5 events are found
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    When KuraMock is disconnected
    And I logout

  Scenario: Starting job with invalid Configuration Put and Command Execution steps and multiple devices
  Create a new job. Set connected Kura Mock devices as a job targets.
  Add a new invalid Configuration Put and Command Execution steps to the created job. Start the job.
  After the executed job is finished, the step index of executed targets should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    And Command pwd is executed
    When I search events from devices in account "kapua-sys" and 3 events are found
    And The type of the last event is "COMMAND"
    And I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Configuration Put"
    And I search for step definitions with the name
      | Configuration Put |
      | Command Execution |
    And I create a regular step creator with the name "TestStep1" and properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurationsIvanlidTag xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurationsInvalidTag> |
      | commandInput  | org.eclipse.kapua.service.device.management.command.DeviceCommandInput        | <?xml version="1.0" encoding="UTF-8"?><commandInputInvalidTag><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInputInvalidTag>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
    When I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 events are found
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 5
    When KuraMock is disconnected
    And I logout

  Scenario: Starting a job with valid Package Uninstall and Asset Write steps and multiple devices
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Package Uninstall and Asset Write steps to the created job.
  Start the job. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Packages are requested and 1 package is received
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search events from devices in account "kapua-sys" and 3 events are found
    And The type of the last event is "ASSET"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for step definitions with the name
      | Package Uninstall |
      | Asset Write       |
    And I create a regular step creator with the name "TestStep1" and properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                                     |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.beacon</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest>                                    |
      | assets                  | org.eclipse.kapua.service.device.management.asset.DeviceAssets                                     | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><valueType>java.lang.Integer</valueType><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                                     |
    When I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 or more events are found
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 1233 are received
    And Packages are requested and 0 packages are received
    When KuraMock is disconnected
    And I logout

  Scenario: Starting a job with invalid Package Uninstall and Asset Write steps and multiple devices
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Package Uninstall and Asset Write steps to the created job. Start the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Device are connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Packages are requested and 1 package is received
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    When I search events from devices in account "kapua-sys" and 3 events are found
    And The type of the last event is "ASSET"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for step definitions with the name
      | Package Uninstall |
      | Asset Write       |
    And I create a regular step creator with the name "TestStep1" and properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                                       |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.beacon</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest>                        |
      | assets                  | org.eclipse.kapua.service.device.management.asset.DeviceAssets                                     | <?xml version="1.0" encoding="UTF-8"?><deviceAssets><deviceAsset><name>asset1</name><channels><channel><assetValue>java.lang.Integer</assetValue><value>1233</value><name>channel1</name></channel></channels></deviceAsset></deviceAssets> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                                       |
    When I create a new step entities from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I start a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 events are found
    And Device assets are requested
    And Asset with name "asset1" and channel with name "channel1" and value 123 are received
    And Packages are requested and 1 package is received
    When KuraMock is disconnected
    And I logout

  Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker
