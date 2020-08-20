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
    protected ConfigurationResponseChannel translateChannel(KuraResponseChannel kuraResponseChannel) throws InvalidChannelException {
        try {
            TranslatorKuraKapuaUtils.validateKuraResponseChannel(kuraResponseChannel, ConfigurationMetrics.APP_ID, ConfigurationMetrics.APP_VERSION);

            ConfigurationResponseChannel configurationResponseChannel = new ConfigurationResponseChannel();
            configurationResponseChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
            configurationResponseChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);

            // Return Kapua Channel
            return configurationResponseChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraResponseChannel);
        }
    }

    @Override
    protected ConfigurationResponsePayload translatePayload(KuraResponsePayload kuraResponsePayload) throws InvalidPayloadException {
        try {
            ConfigurationResponsePayload configurationResponsePayload = TranslatorKuraKapuaUtils.buildBaseResponsePayload(kuraResponsePayload, new ConfigurationResponsePayload());

            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            if (kuraResponsePayload.hasBody()) {
                String body;
                try {
                    body = new String(kuraResponsePayload.getBody(), charEncoding);
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
            throw new InvalidPayloadException(e, kuraResponsePayload);
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
