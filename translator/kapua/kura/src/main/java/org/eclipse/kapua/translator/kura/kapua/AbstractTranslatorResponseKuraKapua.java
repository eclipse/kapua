/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;

/**
 * {@link org.eclipse.kapua.translator.Translator} {@code abstract} implementation from {@link KuraResponseMessage} to {@link KapuaResponseMessage}
 *
 * @since 1.0.0
 */
public abstract class AbstractTranslatorResponseKuraKapua<TO_C extends KapuaResponseChannel, TO_P extends KapuaResponsePayload, TO_M extends KapuaResponseMessage<TO_C, TO_P>> extends AbstractTranslatorKuraKapua<TO_C, TO_P, TO_M> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();

    @Override
    protected TO_M translateMessage(KuraResponseMessage kuraMessage, Account account) throws TranslateException {

        try {
            // Translate channel
            TO_C bundleResponseChannel = translateChannel(kuraMessage.getChannel());

            // Translate payload
            TO_P responsePayload = translatePayload(kuraMessage.getPayload());

            // Process messsage
            TO_M kapuaMessage = createMessage();
            kapuaMessage.setScopeId(account.getId());
            kapuaMessage.setChannel(bundleResponseChannel);
            kapuaMessage.setPayload(responsePayload);
            kapuaMessage.setCapturedOn(kuraMessage.getPayload().getTimestamp());
            kapuaMessage.setSentOn(kuraMessage.getPayload().getTimestamp());
            kapuaMessage.setReceivedOn(kuraMessage.getTimestamp());
            kapuaMessage.setResponseCode(TranslatorKuraKapuaUtils.translate(kuraMessage.getPayload().getResponseCode()));

            // Return Kapua Message
            return kapuaMessage;
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraMessage);
        }
    }

    /**
     * Gets the value from {@link SystemSetting#getMessageClassifier()}
     *
     * @return The value from {@link SystemSetting#getMessageClassifier()}
     * @since 1.2.0
     */
    protected static String getControlMessageClassifier() {
        return CONTROL_MESSAGE_CLASSIFIER;
    }

    protected abstract TO_M createMessage() throws KapuaException;

    @Override
    protected abstract TO_C translateChannel(KuraResponseChannel kuraChannel) throws InvalidChannelException;

    @Override
    protected abstract TO_P translatePayload(KuraResponsePayload kuraPayload) throws InvalidPayloadException;

}
