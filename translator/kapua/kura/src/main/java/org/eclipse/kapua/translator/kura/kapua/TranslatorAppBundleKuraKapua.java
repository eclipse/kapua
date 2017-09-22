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
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.app.BundleMetrics;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.kura.model.bundle.KuraBundle;
import org.eclipse.kapua.service.device.call.kura.model.bundle.KuraBundles;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleFactory;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.bundle.internal.DeviceBundleAppProperties;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleResponseChannel;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleResponseMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleResponsePayload;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

/**
 * Messages translator implementation from {@link KuraResponseMessage} to {@link BundleResponseMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorAppBundleKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<BundleResponseChannel, BundleResponsePayload, BundleResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<BundleMetrics, DeviceBundleAppProperties> METRICS_DICTIONARY;

    static {
        METRICS_DICTIONARY = new HashMap<>();

        METRICS_DICTIONARY.put(BundleMetrics.APP_ID, DeviceBundleAppProperties.APP_NAME);
        METRICS_DICTIONARY.put(BundleMetrics.APP_VERSION, DeviceBundleAppProperties.APP_VERSION);
    }

    public TranslatorAppBundleKuraKapua() {
        super(BundleResponseMessage.class);
    }

    protected BundleResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws KapuaException {

        if (!CONTROL_MESSAGE_CLASSIFIER.equals(kuraChannel.getMessageClassification())) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    kuraChannel.getMessageClassification());
        }

        BundleResponseChannel bundleResponseChannel = new BundleResponseChannel();

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!BundleMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME,
                    null,
                    appIdTokens[0]);
        }

        if (!BundleMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION,
                    null,
                    appIdTokens[1]);
        }

        bundleResponseChannel.setAppName(DeviceBundleAppProperties.APP_NAME);
        bundleResponseChannel.setVersion(DeviceBundleAppProperties.APP_VERSION);

        //
        // Return Kapua Channel
        return bundleResponseChannel;
    }

    protected BundleResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws KapuaException {
        BundleResponsePayload bundleResponsePayload = new BundleResponsePayload();

        bundleResponsePayload.setExceptionMessage((String) kuraPayload.getMetrics().get(ResponseMetrics.RESP_METRIC_EXCEPTION_MESSAGE.getValue()));
        bundleResponsePayload.setExceptionStack((String) kuraPayload.getMetrics().get(ResponseMetrics.RESP_METRIC_EXCEPTION_STACK.getValue()));

        if (kuraPayload.getBody() != null) {
            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            String body = null;
            try {
                body = new String(kuraPayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD,
                        e,
                        kuraPayload.getBody());
            }

            KuraBundles kuraBundles = null;
            try {
                kuraBundles = XmlUtil.unmarshal(body, KuraBundles.class);
            } catch (Exception e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD,
                        e,
                        body);
            }

            translate(bundleResponsePayload, charEncoding, kuraBundles);
        }

        //
        // Return Kapua Payload
        return bundleResponsePayload;
    }

    private void translate(BundleResponsePayload bundleResponsePayload, String charEncoding, KuraBundles kuraBundles)
            throws KapuaException {
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceBundleFactory deviceBundleFactory = locator.getFactory(DeviceBundleFactory.class);

            KuraBundle[] kuraBundleArrays = kuraBundles.getBundles();
            DeviceBundles deviceBundles = deviceBundleFactory.newBundleListResult();

            List<DeviceBundle> deviceBundlesList = deviceBundles.getBundles();
            for (KuraBundle kuraBundle : kuraBundleArrays) {
                DeviceBundle deviceBundle = deviceBundleFactory.newDeviceBundle();
                deviceBundle.setId(kuraBundle.getId());
                deviceBundle.setName(kuraBundle.getName());
                deviceBundle.setVersion(kuraBundle.getVersion());
                deviceBundle.setState(kuraBundle.getState());

                deviceBundlesList.add(deviceBundle);
            }

            StringWriter sw = new StringWriter();
            XmlUtil.marshal(deviceBundles, sw);
            byte[] requestBody = sw.toString().getBytes(charEncoding);

            bundleResponsePayload.setBody(requestBody);
        } catch (Exception e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_BODY,
                    e,
                    kuraBundles);
        }
    }
}
