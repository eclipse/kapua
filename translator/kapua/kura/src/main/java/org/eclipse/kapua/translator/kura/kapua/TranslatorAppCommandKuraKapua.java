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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.kura.app.CommandMetrics;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.command.internal.CommandAppProperties;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandResponseChannel;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandResponseMessage;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandResponsePayload;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

/**
 * Messages translator implementation from {@link KuraResponseMessage} to {@link CommandResponseMessage}
 *
 * @since 1.0
 *
 */
public class TranslatorAppCommandKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<CommandResponseChannel, CommandResponsePayload, CommandResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();
    private static final Map<CommandMetrics, CommandAppProperties> METRICS_DICTIONARY;

    static {
        METRICS_DICTIONARY = new HashMap<>();

        METRICS_DICTIONARY.put(CommandMetrics.APP_ID, CommandAppProperties.APP_NAME);
        METRICS_DICTIONARY.put(CommandMetrics.APP_VERSION, CommandAppProperties.APP_VERSION);

        METRICS_DICTIONARY.put(CommandMetrics.APP_METRIC_STDERR, CommandAppProperties.APP_PROPERTY_STDERR);
        METRICS_DICTIONARY.put(CommandMetrics.APP_METRIC_STDOUT, CommandAppProperties.APP_PROPERTY_STDOUT);
        METRICS_DICTIONARY.put(CommandMetrics.APP_METRIC_EXIT_CODE, CommandAppProperties.APP_PROPERTY_EXIT_CODE);
        METRICS_DICTIONARY.put(CommandMetrics.APP_METRIC_TIMED_OUT, CommandAppProperties.APP_PROPERTY_TIMED_OUT);
    }

    /**
     * Constructor
     */
    public TranslatorAppCommandKuraKapua() {
        super(CommandResponseMessage.class);
    }

    protected CommandResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws KapuaException {

        if (!CONTROL_MESSAGE_CLASSIFIER.equals(kuraChannel.getMessageClassification())) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    kuraChannel.getMessageClassification());
        }

        CommandResponseChannel kapuaChannel = new CommandResponseChannel();

        String[] appIdTokens = kuraChannel.getAppId().split("-");

        if (!CommandMetrics.APP_ID.getValue().equals(appIdTokens[0])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_NAME,
                    null,
                    appIdTokens[0]);
        }

        if (!CommandMetrics.APP_VERSION.getValue().equals(appIdTokens[1])) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_APP_VERSION,
                    null,
                    appIdTokens[1]);
        }

        kapuaChannel.setAppName(CommandAppProperties.APP_NAME);
        kapuaChannel.setVersion(CommandAppProperties.APP_VERSION);

        //
        // Return Kapua Channel
        return kapuaChannel;
    }

    protected CommandResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws KapuaException {
        CommandResponsePayload commandResponsePayload = new CommandResponsePayload();

        Map<String, Object> metrics = kuraPayload.getMetrics();
        commandResponsePayload.setStderr((String) metrics.get(CommandMetrics.APP_METRIC_STDERR.getValue()));
        commandResponsePayload.setStdout((String) metrics.get(CommandMetrics.APP_METRIC_STDOUT.getValue()));
        commandResponsePayload.setExitCode((Integer) metrics.get(CommandMetrics.APP_METRIC_EXIT_CODE.getValue()));

        Boolean timedout = (Boolean) metrics.get(CommandMetrics.APP_METRIC_TIMED_OUT.getValue());
        if (timedout != null) {
            commandResponsePayload.setTimedout(timedout);
        }

        commandResponsePayload.setExceptionMessage((String) metrics.get(ResponseMetrics.RESP_METRIC_EXCEPTION_MESSAGE.getValue()));
        commandResponsePayload.setExceptionStack((String) metrics.get(ResponseMetrics.RESP_METRIC_EXCEPTION_STACK.getValue()));

        //
        // Return Kapua Payload
        return commandResponsePayload;
    }
}
