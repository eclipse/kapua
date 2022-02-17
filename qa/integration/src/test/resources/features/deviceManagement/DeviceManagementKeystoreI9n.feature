###############################################################################
# Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@deviceManagement
@deviceManagementKeystore
@integration

Feature: Device Management Keystore Service Tests

  #
  # Setup
  #

  Scenario: Set environment variables
    Given System property "broker.ip" with value "localhost"
    And System property "commons.db.connection.host" with value "localhost"

  Scenario: Start event broker for all scenarios
    Given Start Event Broker

  Scenario: Start broker for all scenarios
    Given Start Broker

  #
  # Tests
  #

  Scenario: Request Keystores to a Device

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Keystores are requested
    And Keystores are received
    And Keystores are 2
    And Keystores has Keystore named "HttpsKeystore" is present
    And Keystores has Keystore named "HttpsKeystore" has type "jks"
    And Keystores has Keystore named "HttpsKeystore" has size 1
    And Keystores has Keystore named "SSLKeystore" is present
    And Keystores has Keystore named "SSLKeystore" has type "jks"
    And Keystores has Keystore named "SSLKeystore" has size 1
    Then KuraMock is disconnected
    And I logout

  Scenario: Request All Keystore Items to a Device

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And All Keystore Items are requested
    And Keystore Items are received
    And Keystore Items are 2
    And Keystore Items has Item with alias "localhost" is present
    And Keystore Items has Item with alias "localhost" has type "PRIVATE_KEY"
    And Keystore Items has Item with alias "ssl-eclipse" is present
    And Keystore Items has Item with alias "ssl-eclipse" has type "TRUSTED_CERTIFICATE"
    Then KuraMock is disconnected
    And I logout

  Scenario: Request All Keystore Items to a Device filtered by alias

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Keystore Items with alias "localhost" are requested
    And Keystore Items are received
    And Keystore Items are 1
    And Keystore Items has Item with alias "localhost" is present
    And Keystore Items has Item with alias "localhost" has type "PRIVATE_KEY"
    And Keystore Items has Item with alias "ssl-eclipse" is not present
    Then KuraMock is disconnected
    And I logout

  Scenario: Request All Keystore Items to a Device filtered by keystore

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Keystore Items with keystore id "SSLKeystore" are requested
    And Keystore Items are received
    And Keystore Items are 1
    And Keystore Items has Item with alias "ssl-eclipse" is present
    And Keystore Items has Item with alias "ssl-eclipse" has type "TRUSTED_CERTIFICATE"
    And Keystore Items has Item with alias "localhost" is not present
    Then KuraMock is disconnected
    And I logout

  Scenario: Request a Keystore Item to a Device

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And Keystore Item with keystore id "HttpsKeystore" and alias "localhost" is requested
    And Keystore Item is received
    And Keystore Item matches expected
    Then KuraMock is disconnected
    And I logout

  Scenario: Install a Keystore Device Certificate on a Device

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And All Keystore Items are requested
    And Keystore Items are received
    And Keystore Items are 2
    And Keystore Items has Item with alias "localhost" is present
    And Keystore Items has Item with alias "localhost" has type "PRIVATE_KEY"
    And Keystore Items has Item with alias "ssl-eclipse" is present
    And Keystore Items has Item with alias "ssl-eclipse" has type "TRUSTED_CERTIFICATE"
    When I install a Keystore Certificate with alias "qaCertificate"
    And All Keystore Items are requested
    And Keystore Items are received
    Then Keystore Items are 3
    And Keystore Items has Item with alias "qaCertificate" is present
    And Keystore Items has Item with alias "qaCertificate" that matches the installed certificate
    Then KuraMock is disconnected
    And I logout

  Scenario: Install a Keystore Device Keypair on a Device

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And All Keystore Items are requested
    And Keystore Items are received
    And Keystore Items are 2
    And Keystore Items has Item with alias "localhost" is present
    And Keystore Items has Item with alias "localhost" has type "PRIVATE_KEY"
    And Keystore Items has Item with alias "ssl-eclipse" is present
    And Keystore Items has Item with alias "ssl-eclipse" has type "TRUSTED_CERTIFICATE"
    When I install a Keystore Keypair with alias "qaKeypair"
    And All Keystore Items are requested
    And Keystore Items are received
    Then Keystore Items are 3
    And Keystore Items has Item with alias "qaKeypair" is present
    And Keystore Items has Item with alias "qaKeypair" that matches the installed keypair
    Then KuraMock is disconnected
    And I logout

  Scenario: Request a Keystore Certificate Signing Request to a Device
  The request is sent twice because the mock device is set to send two different format of response
  This different format is due to https://github.com/eclipse/kura/issues/3387
  Kapua code is capable to handle both formats.

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    When I send a Certificate Signing Request for Keystore Item with keystore "HttpsKeystore" and alias "localhostFixed"
    Then The Certificate Signing Request is received
    And The Certificate Signing Request matches expected
    When I send a Certificate Signing Request for Keystore Item with keystore "HttpsKeystore" and alias "localhostKuraBugged"
    Then The Certificate Signing Request is received
    And The Certificate Signing Request matches expected
    Then KuraMock is disconnected
    And I logout

  Scenario: Install and delete a Keystore Items on a Device

    Given I start the Kura Mock
    And Device is connected
    And I wait 1 second
    And Device status is "CONNECTED"
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And I get the KuraMock device
    And All Keystore Items are requested
    And Keystore Items are received
    And Keystore Items are 2
    And Keystore Items has Item with alias "localhost" is present
    And Keystore Items has Item with alias "localhost" has type "PRIVATE_KEY"
    And Keystore Items has Item with alias "ssl-eclipse" is present
    And Keystore Items has Item with alias "ssl-eclipse" has type "TRUSTED_CERTIFICATE"
    When I install a Keystore Certificate with alias "qaCertificate"
    And All Keystore Items are requested
    And Keystore Items are received
    Then Keystore Items are 3
    And Keystore Items has Item with alias "qaCertificate" is present
    And Keystore Items has Item with alias "qaCertificate" that matches the installed certificate
    And Keystore Items has Item with alias "qaKeypair" is not present
    When I install a Keystore Keypair with alias "qaKeypair"
    And All Keystore Items are requested
    And Keystore Items are received
    Then Keystore Items are 4
    And Keystore Items has Item with alias "qaKeypair" is present
    And Keystore Items has Item with alias "qaKeypair" that matches the installed keypair
    And Keystore Items has Item with alias "qaCertificate" is present
    When I delete a Keystore Item from keystore "SSLKeystore" with alias "qaCertificate"
    Then All Keystore Items are requested
    And Keystore Items are received
    Then Keystore Items are 3
    And Keystore Items has Item with alias "qaCertificate" is not present
    And Keystore Items has Item with alias "qaKeypair" is present
    When I delete a Keystore Item from keystore "SSLKeystore" with alias "qaKeypair"
    Then All Keystore Items are requested
    And Keystore Items are received
    Then Keystore Items are 2
    And Keystore Items has Item with alias "qaCertificate" is not present
    And Keystore Items has Item with alias "qaKeypair" is not present
    Then KuraMock is disconnected
    And I logout

  #
  # Teardown
  #

  Scenario: Stop broker after all scenarios
    Given Stop Broker

  Scenario: Stop event broker for all scenarios
    Given Stop Event Broker
