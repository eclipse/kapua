###############################################################################
# Copyright (c) 2020 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech
###############################################################################
@unit
@translator

Feature: Translator Service
  The Translator Service is responsible for messaging operations between Kura and Kapua.

  #KapuaTranslatorApi

  Scenario: Translating "CommandRequestMessage" to "KuraRequestMessage"
  Trying to make translation from CommandRequestMessage to KuraRequestMessage.

    When I try to translate from "org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestMessage" to "org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage"
    Then Translator "TranslatorAppCommandKapuaKura" is found
    And No exception was thrown

  Scenario: Translating CommandRequestMessage to null
  Trying to make translation from CommandRequestMessage to null message.
  NullPointerException should be thrown.

    Given I expect the exception "NullPointerException" with the text "*"
    When I try to translate from "org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestMessage" to ""
    Then An exception was thrown

  Scenario: Translating null to KuraRequestMessage
  Trying to make translation from null to KuraRequestMessage message.
  NullPointerException should be thrown.

    Given I expect the exception "NullPointerException" with the text "*"
    When I try to translate from "" to "org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage"
    Then An exception was thrown

  Scenario: Translating empty message to empty message
  Trying to do translation without messages. NPE exception should be thrown.

    Given I expect the exception "NullPointerException" with the text "*"
    And I try to translate from "" to ""
    Then An exception was thrown

  Scenario: Translating from "AssetRequestMessage" to "AssetResponseMessage"
  Trying to make translation from AssetRequestMessage to AssetResponseMessage.
  A translator not found exception should be thrown.

    Given I expect the exception "TranslatorNotFoundException" with the text "*"
    And I try to translate from "org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestMessage" to "org.eclipse.kapua.service.device.management.asset.message.internal.AssetResponseMessage"
    Then An exception was thrown

  #TranslatorDataMqttKura

  Scenario: Translation of mqtt message with invalid payload and valid topic into kura data message
  Creating mqtt message with invalid payload and valid topic. Trying to translate it into kura data message.
  Checking if kura data message with valid channel and byte[] payload is received.

    Given I create mqtt message with invalid payload "invalidPayload" and valid topic "kapua-sys/rpione3/DEPLOY-V2/GET/packages"
    And I try to translate mqtt message to kura data message
    Then I got kura data message with "byte[]" payload body
    And I got kura data message channel with "kapua-sys" and "rpione3" data
    And No exception was thrown

  Scenario: Translation of mqtt message with valid payload and valid topic into kura data message
  Creating mqtt message with valid payload and valid topic. Trying to translate it into kura data message.
  Checking if mqtt message with valid channel and proper payload metrics is received.

    Given I create mqtt message with valid payload "response.code" and valid topic "kapua-sys/rpione3/DEPLOY-V2/GET/packages"
    And I try to translate mqtt message to kura data message
    Then I got kura data message with proper payload metrics response code 200
    And I got kura data message channel with "kapua-sys" and "rpione3" data
    And No exception was thrown

  Scenario: Translation of mqtt message with empty payload into kura data message
  Creating mqtt message with empty payload and valid topic. Trying to translate it into kura data message.
  Checking if mqtt message with valid channel and empty payload is received.

    Given I create mqtt message with empty payload "" and valid topic "kapua-sys/rpione3/DEPLOY-V2/GET/packages"
    And I try to translate mqtt message to kura data message
    Then I got kura data message with empty payload
    And I got kura data message channel with "kapua-sys" and "rpione3" data
    And No exception was thrown

  #TranslatorResponseMqttKura

  Scenario: Translation of mqtt message with invalid payload and invalid topic into kura response message
  Creating mqtt message with invalid payload and invalid topic. Trying to translate it into kura response message.

    Given I expect the exception "InvalidChannelException" with the text "Invalid channel: DEPLOY-V2/GET/packages"
    And I create mqtt message with invalid payload "invalidPayload" and invalid topic "DEPLOY-V2/GET/packages"
    When I try to translate mqtt response
    Then An exception was thrown

  Scenario: Translation of mqtt message with invalid payload and valid topic into kura response message
  Creating mqtt message with invalid payload and valid topic. Trying to translate it into kura response message.
  Check if kura response with byte[] body and correct channel is received.

    Given I create mqtt message with invalid payload "invalidPayload" and valid topic "$EDC/kapua-sys/rpione3/DEPLOY-V2/GET/packages"
    When I try to translate mqtt response
    Then I got kura response message with "byte[]" payload body
    And I got kura response message channel with "GET", "packages", "DEPLOY-V2", "$EDC", "kapua-sys" and "rpione3" data
    And No exception was thrown

  Scenario: Translation of mqtt message with valid payload and invalid topic into kura response message
  Creating mqtt message with valid payload and invalid topic. Trying to translate it into kura response message.

    Given I expect the exception "InvalidChannelException" with the text "Invalid channel: DEPLOY-V2/GET/packages"
    And I create mqtt message with valid payload "response.code" and invalid topic "DEPLOY-V2/GET/packages"
    When I try to translate mqtt response
    Then An exception was thrown

  Scenario: Translation of mqtt message with valid payload and valid topic into kura response message
  Creating mqtt message with valid payload and valid topic. Trying to translate it into kura response message.
  Check if kura response message with proper payload, metrics and correct channel is received.

    Given I create mqtt message with valid payload "response.code" and valid topic "$EDC/kapua-sys/rpione3/DEPLOY-V2/GET/packages"
    When I try to translate mqtt response
    Then I got kura response message with proper payload metrics
    And I got kura response message channel with "GET", "packages", "DEPLOY-V2", "$EDC", "kapua-sys" and "rpione3" data
    And No exception was thrown

  #TranslatorDataKuraMqtt

  Scenario: Translation of kura data message with valid channel and without body and metrics into mqtt message
  Creating kura data message with valid channel and without body and metrics. Trying to translate it into mqtt message.
  Checking if mqtt message with valid topic and empty body is received.

    Given I create kura data message with channel with scope "kapua-sys", client id "rpione3" and payload without body and metrics
    When I try to translate kura data message to mqtt message
    Then I get mqtt message with channel with scope "kapua-sys", client id "rpione3" and empty body
    And No exception was thrown

  Scenario: Translation of kura data message with valid channel, metrics and without body into mqtt message
  Creating kura data message with valid channel, metrics but without body. Trying to translate it into mqtt message.
  Check if mqtt message with valid topic and encoded body is received.

    Given I create kura data message with channel with scope "kapua-sys", client id "rpione3", valid payload and metrics but without body
    When I try to translate kura data message to mqtt message
    Then I get mqtt message with channel with scope "kapua-sys", client id "rpione3" and non empty body
    And No exception was thrown

  Scenario: Translation of kura data message with valid channel, body and metrics into mqtt message
  Creating kura data message with valid channel, body and metrics. Trying to translate it into mqtt message.
  Check if mqtt message with valid topic and encoded body is received.

    Given I create kura data message with channel with scope "kapua-sys", client id "rpione3" and payload with body and metrics
    And I try to translate kura data message to mqtt message
    Then I get mqtt message with channel with scope "kapua-sys", client id "rpione3" and non empty body
    And No exception was thrown