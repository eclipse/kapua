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
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.app.SnapshotMetrics;
import org.eclipse.kapua.service.device.call.kura.model.snapshot.KuraSnapshotIds;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshot;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotFactory;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotResponseChannel;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotResponseMessage;
import org.eclipse.kapua.service.device.management.snapshot.message.internal.SnapshotResponsePayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

import java.io.StringWriter;
import java.util.List;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link KuraResponseMessage} to {@link SnapshotResponseMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppSnapshotKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<SnapshotResponseChannel, SnapshotResponsePayload, SnapshotResponseMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    public TranslatorAppSnapshotKuraKapua() {
        super(SnapshotResponseMessage.class);
    }

    @Override
    protected SnapshotResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws InvalidChannelException {
        try {
            if (!getControlMessageClassifier().equals(kuraChannel.getMessageClassification())) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER, null, kuraChannel.getMessageClassification());
        }

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!SnapshotMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME, null, appIdTokens[0]);
        }

        if (!SnapshotMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION, null, appIdTokens[1]);
        }

            SnapshotResponseChannel snapshotResponseChannel = new SnapshotResponseChannel();
        snapshotResponseChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        snapshotResponseChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);

        // Return Kapua Channel
        return snapshotResponseChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraChannel);
        }
    }

    @Override
    protected SnapshotResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws InvalidPayloadException {
        try {
        SnapshotResponsePayload snapshotResponsePayload = new SnapshotResponsePayload();

        snapshotResponsePayload.setExceptionMessage((String) kuraPayload.getMetrics().get(KuraResponseMetrics.EXCEPTION_MESSAGE.getName()));
        snapshotResponsePayload.setExceptionStack((String) kuraPayload.getMetrics().get(KuraResponseMetrics.EXCEPTION_STACK.getName()));

        DeviceManagementSetting config = DeviceManagementSetting.getInstance();
        String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

        KuraSnapshotIds snapshotIdResult = null;
        if (kuraPayload.hasBody()) {
            String body = null;
            try {
                body = new String(kuraPayload.getBody(), charEncoding);
            } catch (Exception e) {
                    throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, snapshotResponsePayload.getBody());
            }

            try {
                snapshotIdResult = XmlUtil.unmarshal(body, KuraSnapshotIds.class);
            } catch (Exception e) {
                    throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, body);
            }
        }
        translateBody(snapshotResponsePayload, charEncoding, snapshotIdResult);

        // Return Kapua Payload
        return snapshotResponsePayload;
        } catch (InvalidPayloadException ipe) {
            throw ipe;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraPayload);
        }
    }

    private void translateBody(SnapshotResponsePayload snapshotResponsePayload, String charEncoding, KuraSnapshotIds kuraSnapshotIdResult) throws TranslatorException {
        try {
            DeviceSnapshotFactory deviceSnapshotFactory = LOCATOR.getFactory(DeviceSnapshotFactory.class);

            if (kuraSnapshotIdResult != null) {
                DeviceSnapshots deviceSnapshots = deviceSnapshotFactory.newDeviceSnapshots();

                List<Long> snapshotIds = kuraSnapshotIdResult.getSnapshotIds();
                for (Long snapshotId : snapshotIds) {
                    DeviceSnapshot snapshot = deviceSnapshotFactory.newDeviceSnapshot();
                    snapshot.setId(Long.toString(snapshotId));
                    snapshot.setTimestamp(snapshotId);
                    deviceSnapshots.getSnapshots().add(snapshot);
                }

                StringWriter sw = new StringWriter();
                XmlUtil.marshal(deviceSnapshots, sw);
                byte[] requestBody = sw.toString().getBytes(charEncoding);

                snapshotResponsePayload.setBody(requestBody);
            }
        } catch (Exception e) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_BODY, e, kuraSnapshotIdResult); // null for now
        }
    }
}
