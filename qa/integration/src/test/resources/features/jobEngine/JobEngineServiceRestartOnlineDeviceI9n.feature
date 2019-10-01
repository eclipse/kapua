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
@jobEngineServiceRestart
@jobEngineRestartOnlineDevice
@integration

Feature: JobEngineService restart job tests with online device

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

  Scenario: Restarting Job With Valid "Command Execution" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Command Execution step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
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
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then I restart a job
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
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Command Execution" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Command Execution step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
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
      | name         | type                                                                    | value                                                                                                                                                                        |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInputInvalidTag><commandInvalidTag>invalidCommand</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInputInvalidTag> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                                                        |
    And I create a new step entity from the existing creator
    Then I restart a job
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
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Command Execution" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Command Execution step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
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
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Command Execution" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Command Execution step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
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
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandInvalidTag>pwd</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
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
    Then I find 1 device event
    And The type of the last event is "BIRTH"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Start" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Bundle Start step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
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
    And I restart a job
    And I wait 15 seconds
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

  Scenario: Restarting Job With Invalid "Bundle Start" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Bundle Start step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
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
    And I restart a job
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
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Start" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Bundle Start step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
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
    And I restart a job
    And I wait 15 seconds
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
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 5 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Start" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Bundle Start step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device event
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
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Stop" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Bundle Stop step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
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
    And I restart a job
    And I wait 15 seconds
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
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Stop" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Bundle Stop step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
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
    And I restart a job
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

  Scenario: Restarting Job With Valid "Bundle Stop" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Bundle Stop step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And I get the KuraMock device
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
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
    And I restart a job
    And I wait 15 seconds
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
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 5 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Stop" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Bundle Stop step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And I get the KuraMock device
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
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
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

    # *******************************************************
    # * Restarting a job with one Target and multiple Steps *
    # *******************************************************

  Scenario: Restarting Job With Valid "Command Execution" and "Bundle Start" steps And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Command Execution and valid Bundle Start steps to the created job.
  Restart the job one time. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And Command pwd is executed
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34   |
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
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 5 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Command Execution" and "Bundle Start" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add new invalid Command Execution, and invalid Bundle Start steps to the created job.
  Restart the job one time. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And Command invalidCommand is executed
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                                                        |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInputInvalidTag><commandInvalidTag>invalidCommand</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInputInvalidTag> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                                                        |
    And I create a new step entity from the existing creator
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
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Command Execution" and "Bundle Start" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Command Execution and valid Bundle Start steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Command pwd is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
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
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 7 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Command Execution" and "Bundle Start" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add new invalid Command Execution and invalid Bundle Start steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Command invalidCommand is executed
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandInvalidTag>pwd</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
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
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Two Valid "Bundle Start" steps And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add two new valid Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I restart a job
    And I wait 15 seconds
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

  Scenario: Restarting Job With two Invalid "Bundle Start" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add two new invalid Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34  |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
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

  Scenario: Restarting Job With two Valid "Bundle Start" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add two new Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I wait 15 seconds
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
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 7 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With two Invalid "Bundle Start" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add two new invalid Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
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

  Scenario: Restarting Job With Valid "Bundle Stop" and "Bundle Start" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Bundle Stop and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I wait 15 seconds
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
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Stop" and "Bundle Start" And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock device as the job target.
  Add new invalid Bundle Stop and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
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
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Stop" and "Bundle Start" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Bundle Stop step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I wait 15 seconds
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
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 7 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Stop" and "Bundle Start" And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add new invalid Bundle Stop and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I start the Kura Mock
    And Device "is" connected
    And I wait 1 second
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock device
    Given I create a job with the name "TestJob"
    And A new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
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
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
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
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

    # *******************************************************
    # * Restarting a job with multiple Targets and one Step *
    # *******************************************************

  Scenario: Restarting Job With Valid "Command Execution", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Command Execution step to the created job. Restart the job one time.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command pwd is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "COMMAND"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Command Execution", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Command Execution step to the created job. Restart the job one time.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command invalidCommand is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "COMMAND"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                                                        |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInputInvalidTag><commandInvalidTag>invalidCommand</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInputInvalidTag> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                                                        |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Command Execution", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Command Execution step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command pwd is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "COMMAND"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 4 device events
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Command Execution", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Command Execution step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command invalidCommand is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "COMMAND"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandInvalidTag>pwd</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
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
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Start", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Bundle Start step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
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
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I wait 15 seconds
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

  Scenario: Restarting Job With Invalid "Bundle Start", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Bundle Start step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
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
    And I restart a job
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

  Scenario: Restarting Job With Valid "Bundle Start", multiple devicess And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new Bundle Start step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
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
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I wait 15 seconds
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
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 5 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Start", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Bundle Start step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
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
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
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
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Stop", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Bundle Stop step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
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
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Stop", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Bundle Stop step to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
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
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
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
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Stop", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Bundle Stop step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
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
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 5 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Stop", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Bundle Stop step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
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
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
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
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 seconds
    And Device status is "DISCONNECTED"
    And I logout

    # *************************************************************
    # * Restarting a job with multiple Targets and multiple Steps *
    # *************************************************************

  Scenario: Restarting Job With Valid "Command Execution" and "Bundle Start" steps, multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Command Execution and valid Bundle Start steps to the created job.
  Restart the job one time. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Command pwd is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
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
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And Command pwd is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 7 device events
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Command Execution" and "Bundle Start", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Command Execution, and invalid Bundle Start steps to the created job.
  Restart the job one time. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And Command invalidCommand is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "COMMAND"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                                                        |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInputInvalidTag><commandInvalidTag>invalidCommand</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInputInvalidTag> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                                                        |
    And I create a new step entity from the existing creator
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
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And Command pwd is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 5 device events
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Command Execution" and "Bundle Start", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Command Execution and valid Bundle Start steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Command pwd is executed
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
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
    And I confirm the step index is 1 and status is "PROCESS_OK"
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And Command pwd is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 9 device events
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Command Execution" and "Bundle Start", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Command Execution and invalid Bundle Start steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And Command invalidCommand is executed
    And Bundles are requested
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandInvalidTag>pwd</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
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
    And Command invalidCommand is executed
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 5 device events
    And The type of the last event is "COMMAND"
    Then KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Two Valid "Bundle Start" steps, multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a two new valid Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And I search the database for created job steps and I find 2
    Then No exception was thrown
    And I restart a job
    And I wait 15 seconds
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

  Scenario: Restarting Job With two Invalid "Bundle Start", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a two new invalid Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34  |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With two Valid "Bundle Start", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a two new Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
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
    Then I restart a job
    And I wait 30 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 7 device events
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With two Invalid "Bundle Start", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a two new invalid Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 1
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
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
    And The type of the last event is "BUNDLE"
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Stop" and "Bundle Start", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Bundle Stop and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
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
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Stop" and "Bundle Start", multiple devices And Step Index=0 For The First Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Bundle Stop and Bundle Start steps to the created job. Restart the job.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
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
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Valid "Bundle Stop" and "Bundle Start", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Bundle Stop step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 95    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I wait 15 seconds
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
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 1 and status is "PROCESS_OK"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 7 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and ACTIVE
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Restarting Job With Invalid "Bundle Stop" and "Bundle Start", multiple devices And Step Index=0 For The Second Time
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Bundle Stop and Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    When I add 2 devices to Kura Mock
    And Devices "are" connected
    And I wait 1 second
    And Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then Device status is "CONNECTED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    And I get the KuraMock devices
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #95   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
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
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    Then I restart a job
    And I wait 15 seconds
    Given I query for the job with the name "TestJob"
    When I query for the execution items for the current job
    Then I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm the step index is 0 and status is "PROCESS_FAILED"
    When I search for events from device "device0" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "BUNDLE"
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And A bundle named com.google.guava with id 95 and version 19.0.0 is present and RESOLVED
    And KuraMock is disconnected
    And I wait 1 second
    And Device status is "DISCONNECTED"
    And I logout

  Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker
