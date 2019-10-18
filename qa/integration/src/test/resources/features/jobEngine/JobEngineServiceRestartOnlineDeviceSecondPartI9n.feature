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
    And I wait 15 seconds
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
    And I wait 15 seconds
    When I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    Then I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then I restart a job
    And I wait 15 seconds
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

     # *******************************************************
    # * Restarting a job with one Target and multiple Steps *
    # *******************************************************

  Scenario: Restarting Job With Valid Configuration Put And Bundle Start Steps And Step Index=0 For The First Time
  Create a new job. Set a connected Kura Mock device as a job target.
  Add a new valid Configuration Put and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the step index of executed targets should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
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
    And I wait 15 seconds
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

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
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

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
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
    Then I restart a job
    And I wait 30 seconds
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

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
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
    And I wait 15 seconds
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
    And I wait 1 seconds
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
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then Configuration is requested
    And A Configuration named org.eclipse.kura.clock.ClockService has property clock.ntp.retry.interval with value 10
    Then I restart a job
    And I wait 15 seconds
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
    And I wait 1 seconds
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
    Then I restart a job
    And I wait 30 seconds
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

  Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker