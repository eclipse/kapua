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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.kura.app.CommandMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;
import org.eclipse.kapua.service.device.management.command.internal.CommandAppProperties;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestChannel;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestMessage;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Messages translator implementation from {@link CommandRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorAppCommandKapuaKura extends Translator<CommandRequestMessage, KuraRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);
    private static Map<CommandAppProperties, CommandMetrics> propertiesDictionary;

    /**
     * Constructor
     */
    public TranslatorAppCommandKapuaKura() {
        propertiesDictionary = new HashMap<>();

        propertiesDictionary.put(CommandAppProperties.APP_NAME, CommandMetrics.APP_ID);
        propertiesDictionary.put(CommandAppProperties.APP_VERSION, CommandMetrics.APP_VERSION);

        propertiesDictionary.put(CommandAppProperties.APP_PROPERTY_CMD, CommandMetrics.APP_METRIC_CMD);
        propertiesDictionary.put(CommandAppProperties.APP_PROPERTY_ARG, CommandMetrics.APP_METRIC_ARG);
        propertiesDictionary.put(CommandAppProperties.APP_PROPERTY_ENVP, CommandMetrics.APP_METRIC_ENVP);
        propertiesDictionary.put(CommandAppProperties.APP_PROPERTY_DIR, CommandMetrics.APP_METRIC_DIR);
        propertiesDictionary.put(CommandAppProperties.APP_PROPERTY_STDIN, CommandMetrics.APP_METRIC_STDIN);
        propertiesDictionary.put(CommandAppProperties.APP_PROPERTY_TOUT, CommandMetrics.APP_METRIC_TOUT);
        propertiesDictionary.put(CommandAppProperties.APP_PROPERTY_ASYNC, CommandMetrics.APP_METRIC_ASYNC);
        propertiesDictionary.put(CommandAppProperties.APP_PROPERTY_PASSWORD, CommandMetrics.APP_METRIC_PASSWORD);

    }

    @Override
    public KuraRequestMessage translate(CommandRequestMessage kapuaMessage) throws KapuaException {
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
        // return Kura Message
        return new KuraRequestMessage(kuraRequestChannel,
                kapuaMessage.getReceivedOn(),
                kuraPayload);
    }

    private KuraRequestChannel translate(CommandRequestChannel kapuaChannel) throws KapuaException {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        // Build appId
        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(propertiesDictionary.get(CommandAppProperties.APP_NAME).getValue())
                .append("-")
                .append(propertiesDictionary.get(CommandAppProperties.APP_VERSION).getValue());

        kuraRequestChannel.setAppId(appIdSb.toString());
        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.get(kapuaChannel.getMethod()));
        kuraRequestChannel.setResources(new String[] { "command" });

        //
        // Return Kura Channel
        return kuraRequestChannel;
    }

    private KuraRequestPayload translate(CommandRequestPayload kapuaPayload)
            throws KapuaException {
        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

        //
        // Payload translation
        Map<String, Object> metrics = kuraRequestPayload.getMetrics();
        metrics.put(propertiesDictionary.get(CommandAppProperties.APP_PROPERTY_CMD).getValue(), kapuaPayload.getCommand());
        metrics.put(propertiesDictionary.get(CommandAppProperties.APP_PROPERTY_ENVP).getValue(), kapuaPayload.getEnvironmentPairs());
        metrics.put(propertiesDictionary.get(CommandAppProperties.APP_PROPERTY_DIR).getValue(), kapuaPayload.getWorkingDir());
        metrics.put(propertiesDictionary.get(CommandAppProperties.APP_PROPERTY_STDIN).getValue(), kapuaPayload.getStdin());
        metrics.put(propertiesDictionary.get(CommandAppProperties.APP_PROPERTY_TOUT).getValue(), kapuaPayload.getTimeout());
        metrics.put(propertiesDictionary.get(CommandAppProperties.APP_PROPERTY_ASYNC).getValue(), kapuaPayload.isRunAsync());
        metrics.put(propertiesDictionary.get(CommandAppProperties.APP_PROPERTY_PASSWORD).getValue(), kapuaPayload.getPassword());

        // argument translation
        int i = 0;
        String[] arguments = kapuaPayload.getArguments();
        if (arguments != null) {
            for (String argument : arguments) {
                metrics.put(propertiesDictionary.get(CommandAppProperties.APP_PROPERTY_ARG).getValue() + i++, argument);
            }
        }

        //
        // Body translation
        kuraRequestPayload.setBody(kapuaPayload.getBody());

        //
        // Return Kura Payload
        return kuraRequestPayload;
    }

    @Override
    public Class<CommandRequestMessage> getClassFrom() {
        return CommandRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }

}
