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
package org.eclipse.kapua.translator.kapua.kura;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.kura.app.CommandMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.command.internal.CommandAppProperties;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestChannel;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestMessage;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandRequestPayload;

/**
 * Messages translator implementation from {@link CommandRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0
 *
 */
public class TranslatorAppCommandKapuaKura extends AbstractTranslatorKapuaKura<CommandRequestChannel, CommandRequestPayload, CommandRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<CommandAppProperties, CommandMetrics> PROPERTIES_DICTIONARY = new HashMap<>();

    static {
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_NAME, CommandMetrics.APP_ID);
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_VERSION, CommandMetrics.APP_VERSION);

        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_PROPERTY_CMD, CommandMetrics.APP_METRIC_CMD);
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_PROPERTY_ARG, CommandMetrics.APP_METRIC_ARG);
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_PROPERTY_ENVP, CommandMetrics.APP_METRIC_ENVP);
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_PROPERTY_DIR, CommandMetrics.APP_METRIC_DIR);
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_PROPERTY_STDIN, CommandMetrics.APP_METRIC_STDIN);
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_PROPERTY_TOUT, CommandMetrics.APP_METRIC_TOUT);
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_PROPERTY_ASYNC, CommandMetrics.APP_METRIC_ASYNC);
        PROPERTIES_DICTIONARY.put(CommandAppProperties.APP_PROPERTY_PASSWORD, CommandMetrics.APP_METRIC_PASSWORD);
    }

    protected KuraRequestChannel translateChannel(CommandRequestChannel kapuaChannel) throws KapuaException {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        // Build appId
        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_NAME).getValue())
                .append("-")
                .append(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_VERSION).getValue());

        kuraRequestChannel.setAppId(appIdSb.toString());
        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.get(kapuaChannel.getMethod()));
        kuraRequestChannel.setResources(new String[] { "command" });

        //
        // Return Kura Channel
        return kuraRequestChannel;
    }

    protected KuraRequestPayload translatePayload(CommandRequestPayload kapuaPayload) throws KapuaException {
        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

        //
        // Payload translation
        Map<String, Object> metrics = kuraRequestPayload.getMetrics();
        metrics.put(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_PROPERTY_CMD).getValue(), kapuaPayload.getCommand());
        metrics.put(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_PROPERTY_ENVP).getValue(), kapuaPayload.getEnvironmentPairs());
        metrics.put(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_PROPERTY_DIR).getValue(), kapuaPayload.getWorkingDir());
        metrics.put(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_PROPERTY_STDIN).getValue(), kapuaPayload.getStdin());
        metrics.put(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_PROPERTY_TOUT).getValue(), kapuaPayload.getTimeout());
        metrics.put(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_PROPERTY_ASYNC).getValue(), kapuaPayload.isRunAsync());
        metrics.put(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_PROPERTY_PASSWORD).getValue(), kapuaPayload.getPassword());

        // argument translation
        int i = 0;
        String[] arguments = kapuaPayload.getArguments();
        if (arguments != null) {
            for (String argument : arguments) {
                metrics.put(PROPERTIES_DICTIONARY.get(CommandAppProperties.APP_PROPERTY_ARG).getValue() + i++, argument);
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
