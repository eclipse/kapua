/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.kura.app.SnapshotMetrics;
import org.eclipse.kapua.service.device.call.kura.model.snapshot.KuraSnapshotIds;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.configuration.snapshot.internal.SnapshotResponseChannel;
import org.eclipse.kapua.service.device.management.configuration.snapshot.internal.SnapshotResponseMessage;
import org.eclipse.kapua.service.device.management.configuration.snapshot.internal.SnapshotResponsePayload;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshot;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotFactory;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.management.snapshot.internal.DeviceSnapshotAppProperties;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

/**
 * Messages translator implementation from {@link KuraResponseMessage} to {@link SnapshotResponseMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorAppSnapshotKuraKapua extends Translator<KuraResponseMessage, SnapshotResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);
    private static Map<SnapshotMetrics, DeviceSnapshotAppProperties> metricsDictionary;

    /**
     * Constructor
     */
    public TranslatorAppSnapshotKuraKapua() {
        metricsDictionary = new HashMap<>();

        metricsDictionary.put(SnapshotMetrics.APP_ID, DeviceSnapshotAppProperties.APP_NAME);
        metricsDictionary.put(SnapshotMetrics.APP_VERSION, DeviceSnapshotAppProperties.APP_VERSION);
    }

    @Override
    public SnapshotResponseMessage translate(KuraResponseMessage kuraMessage)
            throws KapuaException {
        //
        // Kura channel
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = KapuaSecurityUtils.doPriviledge(() -> accountService.findByName(kuraMessage.getChannel().getScope()));

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, kuraMessage.getChannel().getScope());
        }

        SnapshotResponseChannel commandResponseChannel = translate(kuraMessage.getChannel());

        //
        // Kura payload
        SnapshotResponsePayload responsePayload = translate(kuraMessage.getPayload());

        //
        // Kura Message
        SnapshotResponseMessage kapuaMessage = new SnapshotResponseMessage();
        kapuaMessage.setScopeId(account.getId());
        kapuaMessage.setChannel(commandResponseChannel);
        kapuaMessage.setPayload(responsePayload);
        kapuaMessage.setCapturedOn(kuraMessage.getPayload().getTimestamp());
        kapuaMessage.setSentOn(kuraMessage.getPayload().getTimestamp());
        kapuaMessage.setReceivedOn(kuraMessage.getTimestamp());
        kapuaMessage.setResponseCode(TranslatorKuraKapuaUtils.translate((Integer) kuraMessage.getPayload().getMetrics().get(ResponseMetrics.RESP_METRIC_EXIT_CODE.getValue())));

        //
        // Return Kapua Message
        return kapuaMessage;
    }

    private SnapshotResponseChannel translate(KuraResponseChannel kuraChannel)
            throws KapuaException {

        if (!CONTROL_MESSAGE_CLASSIFIER.equals(kuraChannel.getMessageClassification())) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    kuraChannel.getMessageClassification());
        }

        SnapshotResponseChannel snapshotResponseChannel = new SnapshotResponseChannel();

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!SnapshotMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME,
                    null,
                    appIdTokens[0]);
        }

        if (!SnapshotMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION,
                    null,
                    appIdTokens[1]);
        }

        snapshotResponseChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        snapshotResponseChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);

        //
        // Return Kapua Channel
        return snapshotResponseChannel;
    }

    private SnapshotResponsePayload translate(KuraResponsePayload kuraPayload)
            throws KapuaException {
        SnapshotResponsePayload snapshotResponsePayload = new SnapshotResponsePayload();

        snapshotResponsePayload.setExceptionMessage((String) kuraPayload.getMetrics().get(ResponseMetrics.RESP_METRIC_EXCEPTION_MESSAGE.getValue()));
        snapshotResponsePayload.setExceptionStack((String) kuraPayload.getMetrics().get(ResponseMetrics.RESP_METRIC_EXCEPTION_STACK.getValue()));

        DeviceManagementSetting config = DeviceManagementSetting.getInstance();
        String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

        KuraSnapshotIds snapshotIdResult = null;
        if (kuraPayload.getBody() != null) {
            String body = null;
            try {
                body = new String(kuraPayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD,
                        e,
                        snapshotResponsePayload.getBody());
            }

            try {
                snapshotIdResult = XmlUtil.unmarshal(body, KuraSnapshotIds.class);
            } catch (Exception e) {
                throw new TranslatorException(TranslatorErrorCodes.INVALID_PAYLOAD,
                        e,
                        body);
            }
        }
        translateBody(snapshotResponsePayload, charEncoding, snapshotIdResult);

        //
        // Return Kapua Payload
        return snapshotResponsePayload;
    }

    private void translateBody(SnapshotResponsePayload snapshotResponsePayload, String charEncoding, KuraSnapshotIds kuraSnapshotIdResult)
            throws TranslatorException {
        try {
            if (kuraSnapshotIdResult != null) {
                KapuaLocator locator = KapuaLocator.getInstance();
                DeviceSnapshotFactory deviceSnapshotFactory = locator.getFactory(DeviceSnapshotFactory.class);
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
            throw new TranslatorException(TranslatorErrorCodes.INVALID_BODY,
                    e,
                    kuraSnapshotIdResult); // null for now
        }
    }

    @Override
    public Class<KuraResponseMessage> getClassFrom() {
        return KuraResponseMessage.class;
    }

    @Override
    public Class<SnapshotResponseMessage> getClassTo() {
        return SnapshotResponseMessage.class;
    }
}
