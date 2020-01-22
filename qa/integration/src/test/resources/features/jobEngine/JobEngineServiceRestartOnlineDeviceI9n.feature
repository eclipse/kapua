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

  Scenario: Restarting job with valid Command Execution step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Command Execution step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

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
    And I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><command>pwd</command><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 or more device events
    Then KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Command Execution step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Command Execution step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

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
    And I create a new job target item
    And Search for step definition with the name "Command Execution"
    And A regular step creator with the name "TestStep" and the following properties
      | name         | type                                                                    | value                                                                                                                                         |
      | commandInput | org.eclipse.kapua.service.device.management.command.DeviceCommandInput  | <?xml version="1.0" encoding="UTF-8"?><commandInput><commandInvalidTag>pwd</commandInvalidTag><timeout>30000</timeout><runAsynch>false</runAsynch></commandInput> |
      | timeout      | java.lang.Long                                                          | 10000                                                                                                                                         |
    And I create a new step entity from the existing creator
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    And I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 1 device event
    Then KuraMock is disconnected
    And I logout

  Scenario: Restarting job with valid Bundle Start step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new Bundle Start step to the created job. Restart the job two times.
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
    And I create a new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 or more device events
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Bundle Start step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Bundle Start step to the created job. Restart the job two times.
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
    Then I find 2 device event
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting job with valid Bundle Stop step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Bundle Stop step to the created job. Restart the job two times.
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
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 or more device events
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Bundle Stop step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Bundle Stop step to the created job. Restart the job two times.
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
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with valid Package Uninstall step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new valid Package Uninstall step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested and 1 package is received
    And I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                  |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.beacon</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                  |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    When I query for the execution items for the current job and I count 2 or more
    And I confirm the executed job is finished
    And I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 or more device events
    Then Packages are requested and 0 packages are received
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Uninstall step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Package Uninstall step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    And I wait 1 second
    And Devices is connected
    Then Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Packages are requested and 1 package is received
    And I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.beacon</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    And I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And Packages are requested and 1 package is received
    And KuraMock is disconnected
    And I logout

     #*******************************************************
     #* Restarting a job with one Target and multiple Steps *
     #*******************************************************

  Scenario: Restarting job with valid Command Execution and Package Install steps two times
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Command Execution and valid Package Install steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Command pwd is executed
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
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
    Then I restart a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    When I query for the execution items for the current job and I count 2 or more
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 or more device events
    Then KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Command Execution and Package Install step two times
  Create a new job and set a connected KuraMock device as the job target.
  Add new invalid Command Execution and invalid Package Install steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED

    Given I start the Kura Mock
    When Device is connected
    And I wait 1 second
    Then Device status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Command pwd is executed
    And Packages are requested and 1 package is received
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
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
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 3 device events
    Then KuraMock is disconnected
    And I logout

  Scenario: Restarting job with valid Bundle Start And Bundle Stop steps two times
  Create a new job and set a connected KuraMock device as the job target.
  Add new Bundle Start and Bundle Stop steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 4 or more device events
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Bundle Start and Bundle Stop steps two times
  Create a new job and set a connected KuraMock device as the job target.
  Add new valid Bundle Start and Bundle Stop steps to the created job. Restart the job two times.
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
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I create a new job target item
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search for events from device "rpione3" in account "kapua-sys"
    Then I find 2 device events
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

     #*******************************************************
     #* Restarting a job with multiple Targets and one Step *
     #*******************************************************

  Scenario: Restarting job with valid Command Execution step and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Command Execution step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices are connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command pwd is executed
    When I search events from devices in account "kapua-sys" and 2 events are found
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
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 or more events are found
    Then KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Command Execution step and multiple devices two times
  Create a new job and set a connected KuraMock device as the job target.
  Add a new invalid Command Execution step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices are connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command pwd is executed
    When I search events from devices in account "kapua-sys" and 2 events are found
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
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 2 events are found
    Then KuraMock is disconnected
    And I logout

  Scenario: Restarting job with valid Bundle Start step and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new Bundle Start step to the created job. Restart the job two times.
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
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search events from devices in account "kapua-sys" and 2 events are found
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
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 or more events are found
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Bundle Start step and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Bundle Start step to the created job. Restart the job two times.
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
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    When I search events from devices in account "kapua-sys" and 2 events are found
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
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 2 events are found
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting job with valid Bundle Stop step and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Bundle Stop step to the created job. Restart the job two times.
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
    When I search events from devices in account "kapua-sys" and 2 events are found
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
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 or more events are found
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Bundle Stop step and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Bundle Stop step to the created job. Restart the job two times.
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
    When I search events from devices in account "kapua-sys" and 2 events are found
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
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 2 events are found
    Then Bundles are requested
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with valid Package Uninstall step and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Package Uninstall step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    Then Packages are requested and 1 package is received
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                  |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><name>org.eclipse.kura.example.beacon</name><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                  |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    When I query for the execution items for the current job and I count 2 or more
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 2 or more events are found
    And Packages are requested and 0 packages are received
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting a job with invalid Package Uninstall step and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Package Uninstall step to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    And I wait 1 second
    And Devices are connected
    Then Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    Then Packages are requested and 1 package is received
    And I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Package Uninstall"
    And A regular step creator with the name "TestStep" and the following properties
      | name                    | type                                                                                               | value                                                                                                                                                                                                                |
      | packageUninstallRequest | org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest | <?xml version="1.0" encoding="UTF-8"?><uninstallRequest><packageName>org.eclipse.kura.example.beacon</packageName><version>1.0.300</version><reboot>true</reboot><rebootDelay>10000</rebootDelay></uninstallRequest> |
      | timeout                 | java.lang.Long                                                                                     | 10000                                                                                                                                                                                                                |
    When I create a new step entity from the existing creator
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    And I search events from devices in account "kapua-sys" and 2 events are found
    And Packages are requested and 1 package is received
    And KuraMock is disconnected
    And I logout

    # *************************************************************
    # * Restarting a job with multiple Targets and multiple Steps *
    # *************************************************************

  Scenario: Restarting job with valid Command Execution and Package Install steps and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Command Execution and valid Package Install steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's
  step index should be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    When Devices are connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command pwd is executed
    And Packages are requested and 1 package is received
    When I search events from devices in account "kapua-sys" and 3 events are found
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
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
    Then I restart a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    When I query for the execution items for the current job and I count 1 or more
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    When I query for the execution items for the current job and I count 2 or more
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 3 or more events are found
    And Packages are requested and 2 packages are received
    Then KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Command Execution and Package Install steps and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new invalid Command Execution and invalid Package Install steps to the created job.
  Restart the job two times. After the executed job is finished, the executed target's step
  index should be 0 and the status PROCESS_FAILED

    Given I add 2 devices to Kura Mock
    When Devices are connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Command pwd is executed
    And Packages are requested and 1 package is received
    When I search events from devices in account "kapua-sys" and 3 events are found
    And The type of the last event is "DEPLOY"
    Given I create a job with the name "TestJob"
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
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    And I search for the last job target in the database
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    When I search events from devices in account "kapua-sys" and 3 events is found
    And Packages are requested and 1 package is received
    Then KuraMock is disconnected
    And I logout

  Scenario: Restarting job with valid Bundle Start and Bundle Stop steps and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a new valid Bundle Start and Bundle Stop steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 1 and the status PROCESS_OK

    Given I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 34    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | 77    |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 1 and status "PROCESS_OK"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 4 or more events are found
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and ACTIVE
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and RESOLVED
    And KuraMock is disconnected
    And I logout

  Scenario: Restarting job with invalid Bundle Start and Bundle Stop steps and multiple devices two times
  Create a new job and set a connected KuraMock devices as the job targets.
  Add a two new invalid Bundle Start steps to the created job. Restart the job two times.
  After the executed job is finished, the executed target's step index should
  be 0 and the status PROCESS_FAILED

    When I add 2 devices to Kura Mock
    And Devices are connected
    And I wait 1 second
    Then Devices status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock devices
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    When I search events from devices in account "kapua-sys" and 2 events are found
    And The type of the last event is "BUNDLE"
    Given I create a job with the name "TestJob"
    And I add targets to job
    And Search for step definition with the name "Bundle Start"
    And A regular step creator with the name "TestStep" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #34   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    And Search for step definition with the name "Bundle Stop"
    And A regular step creator with the name "TestStep2" and the following properties
      | name     | type             | value |
      | bundleId | java.lang.String | #77   |
      | timeout  | java.lang.Long   | 10000 |
    When I create a new step entity from the existing creator
    Then No exception was thrown
    And I search the database for created job steps and I find 2
    And I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 1
    And I confirm the executed job is finished
    Then I restart a job
    And I confirm job target has step index 0 and status "PROCESS_FAILED"
    Given I query for the job with the name "TestJob" and I find it
    And I wait 1 second
    When I query for the execution items for the current job and I count 2
    And I confirm the executed job is finished
    When I search events from devices in account "kapua-sys" and 2 events are found
    And Bundles are requested
    Then A bundle named slf4j.api with id 34 and version 1.7.21 is present and RESOLVED
    And A bundle named org.eclipse.kura.linux.bluetooth with id 77 and version 1.0.300 is present and ACTIVE
    And KuraMock is disconnected
    And I logout

   Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios

    Given Stop Event Broker
