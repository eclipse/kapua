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
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.kura.app.AssetMetrics;
import org.eclipse.kapua.service.device.call.kura.app.BundleMetrics;
import org.eclipse.kapua.service.device.call.kura.app.CommandMetrics;
import org.eclipse.kapua.service.device.call.kura.app.ConfigurationMetrics;
import org.eclipse.kapua.service.device.call.kura.app.PackageMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyPayload;
import org.eclipse.kapua.service.device.management.asset.internal.DeviceAssetAppProperties;
import org.eclipse.kapua.service.device.management.bundle.internal.DeviceBundleAppProperties;
import org.eclipse.kapua.service.device.management.command.internal.CommandAppProperties;
import org.eclipse.kapua.service.device.management.commons.message.notification.KapuaNotifyChannelImpl;
import org.eclipse.kapua.service.device.management.commons.message.notification.KapuaNotifyMessageImpl;
import org.eclipse.kapua.service.device.management.commons.message.notification.KapuaNotifyPayloadImpl;
import org.eclipse.kapua.service.device.management.configuration.internal.DeviceConfigurationAppProperties;
import org.eclipse.kapua.service.device.management.message.KapuaAppProperties;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyChannel;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyMessage;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyPayload;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;
import org.eclipse.kapua.service.device.management.packages.message.internal.PackageAppProperties;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Messages translator implementation from {@link KuraNotifyMessage} to {@link KapuaNotifyMessage}
 *
 * @since 1.0
 */
public class TranslatorLifeNotifyKuraKapua extends Translator<KuraNotifyMessage, KapuaNotifyMessage> {

    private static final Map<String, KapuaAppProperties> APP_NAME_DICTIONARY;
    private static final Map<String, KapuaAppProperties> APP_VERSION_DICTIONARY;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaIdFactory KAPUA_ID_FACTORY = LOCATOR.getFactory(KapuaIdFactory.class);

    static {
        APP_NAME_DICTIONARY = new HashMap<>();

        APP_NAME_DICTIONARY.put(AssetMetrics.APP_ID.getValue(), DeviceAssetAppProperties.APP_NAME);
        APP_NAME_DICTIONARY.put(BundleMetrics.APP_ID.getValue(), DeviceBundleAppProperties.APP_NAME);
        APP_NAME_DICTIONARY.put(CommandMetrics.APP_ID.getValue(), CommandAppProperties.APP_NAME);
        APP_NAME_DICTIONARY.put(ConfigurationMetrics.APP_ID.getValue(), DeviceConfigurationAppProperties.APP_NAME);
        APP_NAME_DICTIONARY.put(PackageMetrics.APP_ID.getValue(), PackageAppProperties.APP_NAME);

        APP_VERSION_DICTIONARY = new HashMap<>();

        APP_VERSION_DICTIONARY.put(AssetMetrics.APP_ID.getValue(), DeviceAssetAppProperties.APP_VERSION);
        APP_VERSION_DICTIONARY.put(BundleMetrics.APP_ID.getValue(), DeviceBundleAppProperties.APP_VERSION);
        APP_VERSION_DICTIONARY.put(CommandMetrics.APP_ID.getValue(), CommandAppProperties.APP_VERSION);
        APP_VERSION_DICTIONARY.put(ConfigurationMetrics.APP_ID.getValue(), DeviceConfigurationAppProperties.APP_VERSION);
        APP_VERSION_DICTIONARY.put(PackageMetrics.APP_ID.getValue(), PackageAppProperties.APP_VERSION);
    }

    @Override
    public KapuaNotifyMessage translate(KuraNotifyMessage kuraNotifyMessage)
            throws KapuaException {
        KapuaNotifyMessage kapuaNotifyMessage = new KapuaNotifyMessageImpl();
        kapuaNotifyMessage.setChannel(translate(kuraNotifyMessage.getChannel()));
        kapuaNotifyMessage.setPayload(translate(kuraNotifyMessage.getPayload()));

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.findByName(kuraNotifyMessage.getChannel().getScope());

        if (account == null) {
            throw new KapuaEntityNotFoundException(Account.TYPE, kuraNotifyMessage.getChannel().getScope());
        }

        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device = deviceRegistryService.findByClientId(account.getId(), kuraNotifyMessage.getChannel().getClientId());

        if (device == null) {
            throw new KapuaEntityNotFoundException(Device.class.toString(), kuraNotifyMessage.getChannel().getClientId());
        }

        kapuaNotifyMessage.setDeviceId(device.getId());
        kapuaNotifyMessage.setScopeId(account.getId());
        kapuaNotifyMessage.setCapturedOn(kuraNotifyMessage.getPayload().getTimestamp());
        kapuaNotifyMessage.setSentOn(kuraNotifyMessage.getPayload().getTimestamp());
        kapuaNotifyMessage.setReceivedOn(kuraNotifyMessage.getTimestamp());
        kapuaNotifyMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraNotifyMessage.getPayload().getPosition()));

        return kapuaNotifyMessage;
    }

    private KapuaNotifyChannel translate(KuraNotifyChannel kuraNotifyChannel) {

        String kuraAppIdName = kuraNotifyChannel.getAppId().split("-")[0];

        KapuaNotifyChannel kapuaNotifyChannel = new KapuaNotifyChannelImpl();
        kapuaNotifyChannel.setAppName(APP_NAME_DICTIONARY.get(kuraAppIdName));
        kapuaNotifyChannel.setVersion(APP_VERSION_DICTIONARY.get(kuraAppIdName));

        return kapuaNotifyChannel;
    }

    private KapuaNotifyPayload translate(KuraNotifyPayload kuraNotifyPayload)
            throws KapuaException {
        KapuaNotifyPayload kapuaNotifyPayload = new KapuaNotifyPayloadImpl();

        kapuaNotifyPayload.setOperationId(KAPUA_ID_FACTORY.newKapuaId(new BigInteger(kuraNotifyPayload.getOperationId().toString())));
        kapuaNotifyPayload.setProgress(kuraNotifyPayload.getProgress());

        switch (kuraNotifyPayload.getStatus()) {
            case "IN_PROGRESS":
                kapuaNotifyPayload.setStatus(OperationStatus.RUNNING);
                break;
            case "COMPLETED":
                kapuaNotifyPayload.setStatus(OperationStatus.COMPLETED);
                break;
            case "FAILED":
                kapuaNotifyPayload.setStatus(OperationStatus.FAILED);
                break;
        }

        return kapuaNotifyPayload;
    }

    @Override
    public Class<KuraNotifyMessage> getClassFrom() {
        return KuraNotifyMessage.class;
    }

    @Override
    public Class<KapuaNotifyMessage> getClassTo() {
        return KapuaNotifyMessage.class;
    }

}
