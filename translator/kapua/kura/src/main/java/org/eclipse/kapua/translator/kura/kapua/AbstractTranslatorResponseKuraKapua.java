/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.device.call.kura.app.ResponseMetrics;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponsePayload;

public abstract class AbstractTranslatorResponseKuraKapua<TO_C extends KapuaResponseChannel, TO_P extends KapuaResponsePayload, TO_M extends KapuaResponseMessage<TO_C, TO_P>>
        extends AbstractTranslatorKuraKapua<TO_C, TO_P, TO_M> {

    @Override
    protected TO_M translateMessage(KuraResponseMessage kuraMessage, Account account) throws KapuaException {
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
        kapuaMessage.setResponseCode(TranslatorKuraKapuaUtils.translate((Integer) kuraMessage.getPayload().getMetrics().get(ResponseMetrics.RESP_METRIC_EXIT_CODE.getValue())));

        //
        // Return Kapua Message
        return kapuaMessage;
    }

    protected abstract TO_M createMessage() throws KapuaException;

    protected abstract TO_C translateChannel(KuraResponseChannel kuraChannel) throws KapuaException;

    protected abstract TO_P translatePayload(KuraResponsePayload kuraPayload) throws KapuaException;

}
