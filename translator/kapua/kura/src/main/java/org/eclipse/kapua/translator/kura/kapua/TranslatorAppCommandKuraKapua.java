/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.service.device.call.kura.model.command.CommandMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandResponseChannel;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandResponseMessage;
import org.eclipse.kapua.service.device.management.command.message.internal.CommandResponsePayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

import java.util.Map;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link KuraResponseMessage} to {@link CommandResponseMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppCommandKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<CommandResponseChannel, CommandResponsePayload, CommandResponseMessage> {

    public TranslatorAppCommandKuraKapua() {
        super(CommandResponseMessage.class, CommandResponsePayload.class);
    }

    @Override
    protected CommandResponseChannel translateChannel(KuraResponseChannel kuraResponseChannel) throws InvalidChannelException {
        try {
            TranslatorKuraKapuaUtils.validateKuraResponseChannel(kuraResponseChannel, CommandMetrics.APP_ID, CommandMetrics.APP_VERSION);

            return new CommandResponseChannel();
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraResponseChannel);
        }
    }

    @Override
    protected CommandResponsePayload translatePayload(KuraResponsePayload kuraResponsePayload) throws InvalidPayloadException {
        CommandResponsePayload commandResponsePayload = super.translatePayload(kuraResponsePayload);

        try {
            Map<String, Object> metrics = kuraResponsePayload.getMetrics();
            commandResponsePayload.setStderr((String) metrics.get(CommandMetrics.APP_METRIC_STDERR.getName()));
            commandResponsePayload.setStdout((String) metrics.get(CommandMetrics.APP_METRIC_STDOUT.getName()));
            commandResponsePayload.setExitCode((Integer) metrics.get(CommandMetrics.APP_METRIC_EXIT_CODE.getName()));

            Boolean timedout = (Boolean) metrics.get(CommandMetrics.APP_METRIC_TIMED_OUT.getName());
            if (timedout != null) {
                commandResponsePayload.setTimedout(timedout);
            }

            // Return Kapua Payload
            return commandResponsePayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraResponsePayload);
        }
    }
}
