/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.device.call.kura.app.ConfigurationMetrics;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfigurationUtils;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponseChannel;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponseMessage;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponsePayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

import java.io.StringWriter;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link KuraResponseMessage} to {@link ConfigurationResponseMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppConfigurationKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<ConfigurationResponseChannel, ConfigurationResponsePayload, ConfigurationResponseMessage> {

    public TranslatorAppConfigurationKuraKapua() {
        super(ConfigurationResponseMessage.class);
    }

    @Override
    protected ConfigurationResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws InvalidChannelException {
        try {
            if (!getControlMessageClassifier().equals(kuraChannel.getMessageClassification())) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER, null, kuraChannel.getMessageClassification());
            }

            String[] appIdTokens = kuraChannel.getAppId().split("-");

            if (appIdTokens.length < 2) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME, null, (Object) appIdTokens);
            }

            if (!ConfigurationMetrics.APP_ID.getName().equals(appIdTokens[0])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME, null, appIdTokens[0]);
            }

            if (!ConfigurationMetrics.APP_VERSION.getName().equals(appIdTokens[1])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION, null, appIdTokens[1]);
            }

            ConfigurationResponseChannel configurationResponseChannel = new ConfigurationResponseChannel();
            configurationResponseChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
            configurationResponseChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);

            // Return Kapua Channel
            return configurationResponseChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraChannel);
        }
    }

    @Override
    protected ConfigurationResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws InvalidPayloadException {
        try {
            ConfigurationResponsePayload configurationResponsePayload = new ConfigurationResponsePayload();

            configurationResponsePayload.setExceptionMessage((String) kuraPayload.getMetrics().get(KuraResponseMetrics.EXCEPTION_MESSAGE.getName()));
            configurationResponsePayload.setExceptionStack((String) kuraPayload.getMetrics().get(KuraResponseMetrics.EXCEPTION_STACK.getName()));

            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            if (kuraPayload.hasBody()) {
                String body;
                try {
                    body = new String(kuraPayload.getBody(), charEncoding);
                } catch (Exception e) {
                    throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, (Object) configurationResponsePayload.getBody());
                }

                KuraDeviceConfiguration kuraDeviceConfiguration = null;
                try {
                    kuraDeviceConfiguration = XmlUtil.unmarshal(body, KuraDeviceConfiguration.class);
                } catch (Exception e) {
                    throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, body);
                }

                translateBody(configurationResponsePayload, charEncoding, kuraDeviceConfiguration);
            }

            // Return Kapua Payload
            return configurationResponsePayload;
        } catch (InvalidPayloadException ipe) {
            throw ipe;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraPayload);
        }
    }

    private void translateBody(ConfigurationResponsePayload configurationResponsePayload, String charEncoding, KuraDeviceConfiguration kuraDeviceConfiguration)
            throws TranslatorException {
        try {

            DeviceConfiguration deviceConfiguration = KuraDeviceConfigurationUtils.toDeviceConfiguration(kuraDeviceConfiguration);
            StringWriter sw = new StringWriter();

            XmlUtil.marshal(deviceConfiguration, sw);
            byte[] requestBody = sw.toString().getBytes(charEncoding);

            configurationResponsePayload.setBody(requestBody);
        } catch (Exception e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_BODY, e, kuraDeviceConfiguration);
        }
    }

}
