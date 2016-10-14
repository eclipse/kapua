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
package org.eclipse.kapua.translator.kapua.kura;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.kura.app.BundleMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.management.bundle.internal.DeviceBundleAppProperties;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestChannel;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.BundleRequestPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link BundleRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorAppBundleKapuaKura extends Translator<BundleRequestMessage, KuraRequestMessage>
{
    private static final String                            CONTROL_MESSAGE_CLASSIFIER = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);
    private static Map<DeviceBundleAppProperties, BundleMetrics> propertiesDictionary;

    /**
     * Constructor
     */
    public TranslatorAppBundleKapuaKura()
    {
        propertiesDictionary = new HashMap<>();

        propertiesDictionary.put(DeviceBundleAppProperties.APP_NAME, BundleMetrics.APP_ID);
        propertiesDictionary.put(DeviceBundleAppProperties.APP_VERSION, BundleMetrics.APP_VERSION);
    }

    @Override
    public KuraRequestMessage translate(BundleRequestMessage kapuaMessage)
        throws KapuaException
    {
        //
        // Kura channel
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        Account account = accountService.find(kapuaMessage.getScopeId());

        DeviceRegistryService deviceService = locator.getService(DeviceRegistryService.class);
        Device device = deviceService.find(kapuaMessage.getScopeId(),
                                           kapuaMessage.getDeviceId());

        KuraRequestChannel kuraRequestChannel = translate(kapuaMessage.getChannel());
        kuraRequestChannel.setScope(account.getName());
        kuraRequestChannel.setClientId(device.getClientId());

        //
        // Kura payload
        KuraRequestPayload kuraPayload = translate(kapuaMessage.getPayload());

        //
        // Return Kura Message
        return new KuraRequestMessage(kuraRequestChannel,
                                      kapuaMessage.getReceivedOn(),
                                      kuraPayload);

    }

    private KuraRequestChannel translate(BundleRequestChannel kapuaChannel)
        throws KapuaException
    {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        // Build appId
        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(propertiesDictionary.get(DeviceBundleAppProperties.APP_NAME).getValue())
               .append("-")
               .append(propertiesDictionary.get(DeviceBundleAppProperties.APP_VERSION).getValue());

        kuraRequestChannel.setAppId(appIdSb.toString());
        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.get(kapuaChannel.getMethod()));

        // Build resources
        List<String> resources = new ArrayList<>();
        switch (kapuaChannel.getMethod()) {
            case READ:
                resources.add("bundles");
                break;
            case EXECUTE:
            {
                if (kapuaChannel.isStart()) {
                    resources.add("start");
                }
                else {
                    resources.add("stop");
                }

                String bundleId = kapuaChannel.getBundleId();
                if (bundleId != null) {
                    resources.add(bundleId);
                }
            }
                break;
            case CREATE:
            case DELETE:
            case OPTIONS:
            case WRITE:
            default:
                break;

        }
        kuraRequestChannel.setResources(resources.toArray(new String[resources.size()]));

        //
        // Return Kura Channel
        return kuraRequestChannel;
    }

    private KuraRequestPayload translate(BundleRequestPayload kapuaPayload)
        throws KapuaException
    {
        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

        if (kapuaPayload.getBody() != null) {
            kuraRequestPayload.setBody(kapuaPayload.getBody());
        }

        return kuraRequestPayload;
    }

    @Override
    public Class<BundleRequestMessage> getClassFrom()
    {
        return BundleRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo()
    {
        return KuraRequestMessage.class;
    }

}
