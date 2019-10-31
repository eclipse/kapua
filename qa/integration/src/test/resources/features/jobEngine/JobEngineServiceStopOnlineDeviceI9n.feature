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
@jobEngineServiceStop
@integration

Feature: JobEngineService stop job tests with online device
  Job Engine Service test scenarios for stopping job. This feature file contains scenarios for stopping job with one target and one step,
  one target and multiple steps, multiple targets and one step and multiple targets and multiple steps.

  Scenario: Set environment variables
    Given System property "broker.ip" with value "localhost"
    And System property "commons.db.connection.host" with value "localhost"

  Scenario: Start event broker for all scenarios
    Given Start Event Broker

  Scenario: Start broker for all scenarios
    Given Start Broker

    # *****************************************************
    # * Stopping a job with one Target and multiple Steps *
    # *****************************************************

  Scenario: Stop job with multiple Bundle Start steps and one target
  Create a new job and set a connected KuraMock device as the job target.
  Add two Bundle Start steps to the created job. Start the job.
  Before job is finished, stop the job. When job is stopped, the executed target's
  step index should be different than 1 and the status PROCESS_AWAITING. Start the job again.
  If job is finished step index should be 1, and the status PROCESS_OK.

    Given I start the Kura Mock
    And Devices "is" connected
    And I wait 1 second
    And Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add target to job
    And I search for the job targets in database
    And I count 1
    And Search for step definition with the name
      | Bundle Start |
    And A regular step creator with the name "TestStep1" and properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34,95 |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entities from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait for 10 milliseconds to settle down
    And I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    And I count 1
    And I search for the last job target in the database
    And I confirm the step index is different than 1 and status is "PROCESS_AWAITING"
    And I stop the job
    And I search for the last job target in the database
    And I confirm the step index is different than 1 and status is "PROCESS_AWAITING"
    And I start a job
    And I wait 15 seconds
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Stop job with multiple Bundle Start and Bundle Stop steps and one target
  Create a new job and set a connected KuraMock device as the job target.
  Add two Bundle Start and one Bundle Stop steps to the created job. Start the job.
  Before job is finished, stop the job. When job is stopped, the executed target's
  step index should be different than 2 and the status PROCESS_AWAITING. Start the job again.
  If job is finished step index should be 2, and job target status PROCESS_OK.

    Given I start the Kura Mock
    And Devices "is" connected
    And I wait 1 second
    And Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add target to job
    And I search for the job targets in database
    And I count 1
    And Search for step definition with the name
      | Bundle Start |
    And A regular step creator with the name "TestStep2" and properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34,95 |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entities from the existing creator
    And Search for step definition with the name
      | Bundle Stop |
    And A regular step creator with the name "TestStep1" and properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entities from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 3
    And I start a job
    And I wait for 10 milliseconds to settle down
    And I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    And I count 1
    And I search for the last job target in the database
    And I confirm the step index is different than 2 and status is "PROCESS_AWAITING"
    And I stop the job
    And I search for the last job target in the database
    And I confirm the step index is different than 2 and status is "PROCESS_AWAITING"
    And I start a job
    And I wait 15 seconds
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 2 and status is "PROCESS_OK"
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Stop job with multiple Command Execution and Bundle Start steps and one target
  Create a new job and set a connected KuraMock device as the job target.
  Add two Command Execution and Bundle Start steps to the created job. Start the job.
  Before job is finished, stop the job. When job is stopped, the executed target's
  step index should be different than 1 and the status PROCESS_AWAITING. Start the job again.
  If job is finished step index should be 1, and job target status PROCESS_OK.

    Given I start the Kura Mock
    And Devices "is" connected
    And I wait 1 second
    And Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add target to job
    And I search for the job targets in database
    And I count 1
    And Search for step definition with the name
      | Command Execution |
      | Bundle Start      |
    And A regular step creator with the name "TestStep1" and properties
      | name         | type                                                                   | value                                                                                                                                         |
      | bundleId     | java.lang.String                                                       | 34                                                                                                                                            |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                         |
    And I create a new step entities from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait for 10 milliseconds to settle down
    And I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    And I count 1
    And I search for the last job target in the database
    And I confirm the step index is different than 1 and status is "PROCESS_AWAITING"
    And I stop the job
    And I search for the last job target in the database
    And I confirm the step index is different than 1 and status is "PROCESS_AWAITING"
    And I start a job
    And I wait 15 seconds
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Stop job with multiple Configuration Put and Bundle Start steps and one target
  Create a new job and set a connected KuraMock device as the job target.
  Add two Configuration Put and Bundle Start steps to the created job. Start the job.
  Before job is finished, stop the job. When job is stopped, the executed target's
  step index should be different than 1 and the status PROCESS_AWAITING. Start the job again.
  If job is finished step index should be 1, and job target status PROCESS_OK.

    Given I start the Kura Mock
    And Devices "is" connected
    And I wait 1 second
    And Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add target to job
    And I search for the job targets in database
    And I count 1
    And Search for step definition with the name
      | Configuration Put |
      | Bundle Start      |
    And A regular step creator with the name "TestStep1" and properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | bundleId      | java.lang.String                                                              | 34                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    And I create a new step entities from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I start a job
    And I wait for 10 milliseconds to settle down
    And I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    And I count 1
    And I search for the last job target in the database
    And I confirm the step index is different than 1 and status is "PROCESS_AWAITING"
    And I stop the job
    And I search for the last job target in the database
    And I confirm the step index is different than 1 and status is "PROCESS_AWAITING"
    And I start a job
    And I wait 15 seconds
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

    # *****************************************************
    # * Stopping a job with multiple Targets and one Step *
    # *****************************************************

  Scenario: Stop job with multiple targets and Bundle Start step
  Create a new job and set a connected KuraMock devices as the job targets.
  Add Bundle Start step to the created job. Start the job. Before
  job is finished, stop the job. When job is stopped, the executed target's
  step index should be different than 0 and the status PROCESS_AWAITING. Start the job again.
  If job is finished step index should be 0, and job target status PROCESS_OK.

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for the job targets in database
    And I count 2
    And Search for step definition with the name
      | Bundle Start |
    And A regular step creator with the name "TestStep1" and properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
    And I create a new step entities from the existing creator
    And I search the database for created job steps and I find 1
    And I start a job
    And I wait for 10 milliseconds to settle down
    And I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    And I count 1
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_AWAITING"
    And I stop the job
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_AWAITING"
    And I start a job
    And I wait 15 seconds
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Stop job with multiple targets and Bundle Stop step
  Create a new job and set a connected KuraMock devices as the job targets.
  Add Bundle Stop step to the created job. Start the job.
  Before job is finished, stop the job. When job is stopped, the executed target's
  step index should be different than 0 and the status PROCESS_AWAITING. Start the job again.
  If job is finished step index should be 0, and job target status PROCESS_OK.

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for the job targets in database
    And I count 2
    And Search for step definition with the name
      | Bundle Stop |
    And A regular step creator with the name "TestStep1" and properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    And I create a new step entities from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 1
    And I start a job
    And I wait for 10 milliseconds to settle down
    And I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    And I count 1
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_AWAITING"
    And I stop the job
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_AWAITING"
    And I start a job
    And I wait 15 seconds
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Stop job with multiple targets and Command Execution step
  Create a new job and set a connected KuraMock devices as the job targets.
  Add Command Execution step to the created job. Start the job.
  Before job is finished, stop the job. When job is stopped, the executed target's
  step index should be different than 0 and the status PROCESS_AWAITING. Start the job again.
  If job is finished step index should be 0, and job target status PROCESS_OK.

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 seconds
    And Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for the job targets in database
    And I count 2
    And Search for step definition with the name
      | Command Execution |
    And A regular step creator with the name "TestStep1" and properties
      | name         | type                                                                   | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                         | 10000                                                                                                                                         |
    And I create a new step entities from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 1
    And I start a job
    And I wait for 10 milliseconds to settle down
    And I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    And I count 1
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_AWAITING"
    And I stop the job
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_AWAITING"
    And I start a job
    And I wait 15 seconds
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Bundles are requested
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Stop job with multiple targets and Configuration Put step
  Create a new job and set a connected KuraMock devices as the job targets.
  Add Configuration Put step to the created job. Start the job.
  Before job is finished, stop the job. When job is stopped, the executed target's
  step index should be different than 0 and the status PROCESS_AWAITING. Start the job again.
  If job is finished step index should be 0, and job target status PROCESS_OK.

    Given I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I create a job with the name "TestJob"
    And I add targets to job
    And I search for the job targets in database
    And I count 2
    And Search for step definition with the name
      | Configuration Put |
    And A regular step creator with the name "TestStep1" and properties
      | name          | type                                                                          | value                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
      | configuration | org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration | <?xml version="1.0" encoding="UTF-8"?><configurations xmlns:ns0="http://www.osgi.org/xmlns/metatype/v1.2.0"><configuration><id>org.eclipse.kura.clock.ClockService></id><properties><property name="clock.ntp.host" array="false" encrypted="false" type="String"><value>0.pool.ntp.org</value></property><property name="clock.provider" array="false" encrypted="false" type="String"><value>java-ntp</value></property><property name="clock.ntp.port" array="false" encrypted="false" type="Integer"><value>123</value></property><property name="clock.ntp.max-retry" array="false" encrypted="false" type="Integer"><value>0</value></property><property name="clock.ntp.refresh-interval" array="false" encrypted="false" type="Integer"><value>3600</value></property><property name="clock.set.hwclock" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="enabled" array="false" encrypted="false" type="Boolean"><value>true</value></property><property name="clock.ntp.timeout" array="false" encrypted="false" type="Integer"><value>10000</value></property><property name="clock.ntp.retry.interval" array="false" encrypted="false" type="Integer"><value>10</value></property></properties></configuration></configurations> |
      | timeout       | java.lang.Long                                                                | 10000                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
    And I create a new step entities from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 1
    And I start a job
    And I wait for 10 milliseconds to settle down
    And I query for the job with the name "TestJob"
    And I query for the execution items for the current job
    And I count 1
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_AWAITING"
    And I stop the job
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_AWAITING"
    And I start a job
    And I wait 15 seconds
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When Bundles are requested
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker