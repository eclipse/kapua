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
    protected SnapshotResponseChannel translateChannel(KuraResponseChannel kuraResponseChannel) throws InvalidChannelException {
        try {
            TranslatorKuraKapuaUtils.validateKuraResponseChannel(kuraResponseChannel, SnapshotMetrics.APP_ID, SnapshotMetrics.APP_VERSION);

            SnapshotResponseChannel snapshotResponseChannel = new SnapshotResponseChannel();
            snapshotResponseChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
            snapshotResponseChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);

            // Return Kapua Channel
            return snapshotResponseChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraResponseChannel);
        }
    }

    @Override
    protected SnapshotResponsePayload translatePayload(KuraResponsePayload kuraResponsePayload) throws InvalidPayloadException {
        try {
            SnapshotResponsePayload snapshotResponsePayload = TranslatorKuraKapuaUtils.buildBaseResponsePayload(kuraResponsePayload, new SnapshotResponsePayload());

            DeviceManagementSetting config = DeviceManagementSetting.getInstance();
            String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            KuraSnapshotIds snapshotIdResult = null;
            if (kuraResponsePayload.hasBody()) {
                String body = null;
                try {
                    body = new String(kuraResponsePayload.getBody(), charEncoding);
                } catch (Exception e) {
                    throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD, e, (Object) snapshotResponsePayload.getBody());
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
            throw new InvalidPayloadException(e, kuraResponsePayload);
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
