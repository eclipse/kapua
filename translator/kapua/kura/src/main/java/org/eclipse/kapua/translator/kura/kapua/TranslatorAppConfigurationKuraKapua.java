/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.Password;
import org.eclipse.kapua.commons.configuration.metatype.TadImpl;
import org.eclipse.kapua.commons.configuration.metatype.TiconImpl;
import org.eclipse.kapua.commons.configuration.metatype.TocdImpl;
import org.eclipse.kapua.commons.configuration.metatype.ToptionImpl;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.service.device.call.kura.app.ConfigurationMetrics;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceComponentConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraPassword;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceComponentConfigurationImpl;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationImpl;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponseChannel;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponseMessage;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponsePayload;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

/**
 * Messages translator implementation from {@link KuraResponseMessage} to {@link ConfigurationResponseMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorAppConfigurationKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<ConfigurationResponseChannel, ConfigurationResponsePayload, ConfigurationResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<ConfigurationMetrics, DeviceConfigurationAppProperties> METRICS_DICTIONARY;

    static {
        METRICS_DICTIONARY = new HashMap<>();

        METRICS_DICTIONARY.put(ConfigurationMetrics.APP_ID, DeviceConfigurationAppProperties.APP_NAME);
        METRICS_DICTIONARY.put(ConfigurationMetrics.APP_VERSION, DeviceConfigurationAppProperties.APP_VERSION);
    }

    /**
     * Constructor
     */
    public TranslatorAppConfigurationKuraKapua() {
        super(ConfigurationResponseMessage.class);
    }

    protected ConfigurationResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws KapuaException {

        if (!CONTROL_MESSAGE_CLASSIFIER.equals(kuraChannel.getMessageClassification())) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    kuraChannel.getMessageClassification());
        }

        ConfigurationResponseChannel configurationResponseChannel = new ConfigurationResponseChannel();

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!ConfigurationMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME,
                    null,
                    appIdTokens[0]);
        }

        if (!ConfigurationMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION,
                    null,
                    appIdTokens[1]);
        }

        configurationResponseChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        configurationResponseChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);

        //
        // Return Kapua Channel
        return configurationResponseChannel;
    }

    protected ConfigurationResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws KapuaException {
        ConfigurationResponsePayload configurationResponsePayload = new ConfigurationResponsePayload();

        configurationResponsePayload.setExceptionMessage((String) kuraPayload.getMetrics().get(ResponseMetrics.RESP_METRIC_EXCEPTION_MESSAGE.getValue()));
        configurationResponsePayload.setExceptionStack((String) kuraPayload.getMetrics().get(ResponseMetrics.RESP_METRIC_EXCEPTION_STACK.getValue()));

        DeviceManagementSetting config = DeviceManagementSetting.getInstance();
        String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

        if (kuraPayload.getBody() != null) {
            String body = null;
            try {
                body = new String(kuraPayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD,
                        e,
                        configurationResponsePayload.getBody());
            }

            KuraDeviceConfiguration kuraDeviceConfiguration = null;
            try {
                kuraDeviceConfiguration = XmlUtil.unmarshal(body, KuraDeviceConfiguration.class);
            } catch (Exception e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD,
                        e,
                        body);
            }

            translateBody(configurationResponsePayload,
                    charEncoding,
                    kuraDeviceConfiguration);
        }

        // Return Kapua Payload
        return configurationResponsePayload;
    }

    private void translateBody(ConfigurationResponsePayload configurationResponsePayload, String charEncoding, KuraDeviceConfiguration kuraDeviceConfiguration)
            throws TranslatorException {
        try {
            DeviceConfigurationImpl deviceConfiguration = new DeviceConfigurationImpl();
            for (KuraDeviceComponentConfiguration kuraDeviceCompConf : kuraDeviceConfiguration.getConfigurations()) {

                String componentId = kuraDeviceCompConf.getComponentId();
                DeviceComponentConfigurationImpl deviceComponentConfiguration = new DeviceComponentConfigurationImpl(componentId);
                deviceComponentConfiguration.setProperties(translate(kuraDeviceCompConf.getProperties()));

                // Translate also definitions when they are available
                if (kuraDeviceCompConf.getDefinition() != null) {
                    deviceComponentConfiguration.setDefinition(translate(kuraDeviceCompConf.getDefinition()));
                }

                // Add to kapua configuration
                deviceConfiguration.getComponentConfigurations().add(deviceComponentConfiguration);
            }

            StringWriter sw = new StringWriter();

            XmlUtil.marshal(deviceConfiguration, sw);
            byte[] requestBody = sw.toString().getBytes(charEncoding);

            configurationResponsePayload.setBody(requestBody);
        } catch (Exception e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_BODY,
                    e,
                    kuraDeviceConfiguration);
        }
    }

    private KapuaTocd translate(KapuaTocd kuraDefinition) {
        TocdImpl definition = new TocdImpl();

        definition.setId(kuraDefinition.getId());
        definition.setName(kuraDefinition.getName());
        definition.setDescription(kuraDefinition.getDescription());

        for (KapuaTad kuraAd : kuraDefinition.getAD()) {
            TadImpl ad = new TadImpl();
            ad.setCardinality(kuraAd.getCardinality());
            ad.setDefault(ad.getDefault());
            ad.setDescription(kuraAd.getDescription());
            ad.setId(kuraAd.getId());
            ad.setMax(kuraAd.getMax());
            ad.setMin(kuraAd.getMin());
            ad.setName(kuraAd.getName());
            ad.setType(kuraAd.getType());
            ad.setRequired(kuraAd.isRequired());

            for (KapuaToption kuraToption : kuraAd.getOption()) {
                ToptionImpl kapuaToption = new ToptionImpl();

                kapuaToption.setLabel(kuraToption.getLabel());
                kapuaToption.setValue(kuraToption.getValue());

                ad.addOption(kapuaToption);
            }

            for (Entry<QName, String> entry : kuraAd.getOtherAttributes().entrySet()) {
                ad.putOtherAttribute(entry.getKey(),
                        entry.getValue());
            }

            definition.addAD(ad);
        }

        for (KapuaTicon kuraIcon : kuraDefinition.getIcon()) {
            KapuaTicon icon = new TiconImpl();
            icon.setResource(kuraIcon.getResource());
            icon.setSize(kuraIcon.getSize());

            definition.addIcon(icon);
        }

        for (Object kuraAny : kuraDefinition.getAny()) {
            definition.addAny(kuraAny);
        }

        for (Entry<QName, String> entry : kuraDefinition.getOtherAttributes().entrySet()) {
            definition.putOtherAttribute(entry.getKey(),
                    entry.getValue());
        }

        return definition;
    }

    private Map<String, Object> translate(Map<String, Object> kuraProperties) {
        Map<String, Object> properties = new HashMap<>();
        for (Entry<String, Object> entry : kuraProperties.entrySet()) {

            Object value = entry.getValue();

            //
            // Special management of Password type field
            if (value instanceof KuraPassword) {
                value = new Password(((KuraPassword) value).getPassword());
            } else if (value instanceof KuraPassword[]) {
                KuraPassword[] kuraPasswords = (KuraPassword[]) value;
                Password[] passwords = new Password[kuraPasswords.length];

                int i = 0;
                for (KuraPassword p : kuraPasswords) {
                    passwords[i++] = new Password(p.getPassword());
                }

                value = passwords;
            }

            //
            // Set property
            properties.put(entry.getKey(),
                    value);
        }
        return properties;
    }
}
