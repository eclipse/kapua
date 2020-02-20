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
package org.eclipse.kapua.translator.kapua.kura;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.device.call.kura.app.ConfigurationMetrics;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfigurationUtils;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationImpl;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestChannel;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestMessage;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestPayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link ConfigurationRequestMessage} to {@link KuraRequestMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppConfigurationKapuaKura extends AbstractTranslatorKapuaKura<ConfigurationRequestChannel, ConfigurationRequestPayload, ConfigurationRequestMessage> {

    @Override
    protected KuraRequestChannel translateChannel(ConfigurationRequestChannel kapuaChannel) throws InvalidChannelException {
        try {
            KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
            kuraRequestChannel.setMessageClassification(getControlMessageClassifier());
            kuraRequestChannel.setAppId(ConfigurationMetrics.APP_ID + "-" + ConfigurationMetrics.APP_VERSION);
            kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.translate(kapuaChannel.getMethod()));

            // Build resources
            List<String> resources = new ArrayList<>();
            if (kapuaChannel.getConfigurationId() == null) {
                resources.add("configurations");
                String componentId = kapuaChannel.getComponentId();
                if (componentId != null) {
                    resources.add(componentId);
                }
            } else if (kapuaChannel.getConfigurationId() != null) {
                resources.add("snapshots");

                String configurationId = kapuaChannel.getConfigurationId();
                if (configurationId != null) {
                    resources.add(configurationId);
                }
            }
            kuraRequestChannel.setResources(resources.toArray(new String[0]));

            // Return Kura Channel
            return kuraRequestChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kapuaChannel);
        }
    }

    @Override
    protected KuraRequestPayload translatePayload(ConfigurationRequestPayload kapuaPayload) throws InvalidPayloadException {
        try {
            KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

            if (kapuaPayload.hasBody()) {
                DeviceConfiguration kapuaDeviceConfiguration;
                try {
                    kapuaDeviceConfiguration = XmlUtil.unmarshal(new String(kapuaPayload.getBody()), DeviceConfigurationImpl.class);
                } catch (Exception e) {
                    throw new InvalidPayloadException(e, kapuaPayload);
                }

                KuraDeviceConfiguration kuraDeviceConfiguration = KuraDeviceConfigurationUtils.toKuraConfiguration(kapuaDeviceConfiguration);

                byte[] body;
                try {
                    body = XmlUtil.marshal(kuraDeviceConfiguration).getBytes();
                } catch (Exception e) {
                    throw new InvalidPayloadException(e, kapuaPayload);
                }

                kuraRequestPayload.setBody(body);
            }

            // Return Kura Payload
            return kuraRequestPayload;
        } catch (InvalidPayloadException ipe) {
            throw ipe;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kapuaPayload);
        }
    }

    @Override
    public Class<ConfigurationRequestMessage> getClassFrom() {
        return ConfigurationRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }
}
