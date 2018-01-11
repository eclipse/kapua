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
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.request.GenericRequestFactory;
import org.eclipse.kapua.service.device.management.request.internal.GenericAppProperties;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseChannel;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponsePayload;
import org.eclipse.kapua.translator.exception.TranslatorErrorCodes;
import org.eclipse.kapua.translator.exception.TranslatorException;

public class TranslatorGenericResponseKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<GenericResponseChannel, GenericResponsePayload, GenericResponseMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();

    public TranslatorGenericResponseKuraKapua() {
        super(GenericResponseMessage.class);
    }

    @Override
    protected GenericResponseChannel translateChannel(KuraResponseChannel kuraChannel) throws KapuaException {
        if (!CONTROL_MESSAGE_CLASSIFIER.equals(kuraChannel.getMessageClassification())) {
            throw new TranslatorException(TranslatorErrorCodes.INVALID_CHANNEL_CLASSIFIER,
                    null,
                    kuraChannel.getMessageClassification());
        }

        KapuaLocator locator = KapuaLocator.getInstance();
        GenericRequestFactory genericRequestFactory = locator.getFactory(GenericRequestFactory.class);
        GenericResponseChannel genericResponseChannel = genericRequestFactory.newResponseChannel();
        String[] appIdTokens = kuraChannel.getAppId().split("-");

        genericResponseChannel.setAppName(new GenericAppProperties(appIdTokens[0]));
        if (appIdTokens.length > 1) {
            genericResponseChannel.setVersion(new GenericAppProperties(appIdTokens[1]));
        }

        return genericResponseChannel;
    }

    @Override
    protected GenericResponsePayload translatePayload(KuraResponsePayload kuraPayload) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        GenericRequestFactory genericRequestFactory = locator.getFactory(GenericRequestFactory.class);

        GenericResponsePayload genericResponsePayload = genericRequestFactory.newResponsePayload();
        genericResponsePayload.setBody(kuraPayload.getBody());
        genericResponsePayload.setMetrics(kuraPayload.getMetrics());
        genericResponsePayload.setExceptionMessage(kuraPayload.getExceptionMessage());
        genericResponsePayload.setExceptionStack(kuraPayload.getExceptionStack());
        return genericResponsePayload;
    }
}
