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
package org.eclipse.kapua.translator.kapua.kura;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.asset.message.internal.AssetRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;

/**
 * Messages translator implementation from {@link AssetRequestMessage} to {@link KuraRequestMessage}
 * 
 * @since 1.0.0
 */
public class TranslatorGenericRequestKapuaKura extends AbstractTranslatorKapuaKura<GenericRequestChannel, GenericRequestPayload, GenericRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();

    protected KuraRequestChannel translateChannel(GenericRequestChannel kapuaChannel) throws KapuaException {
        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

        StringBuilder appIdSb = new StringBuilder();
        appIdSb.append(kapuaChannel.getAppName().getValue());
        if (kapuaChannel.getVersion() != null && StringUtils.isNotEmpty(kapuaChannel.getVersion().getValue())) {
            appIdSb.append("-")
                    .append(kapuaChannel.getVersion().getValue());
        }

        kuraRequestChannel.setAppId(appIdSb.toString());
        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.get(kapuaChannel.getMethod()));

        kuraRequestChannel.setResources(kapuaChannel.getResources());

        return kuraRequestChannel;
    }

    protected KuraRequestPayload translatePayload(GenericRequestPayload kapuaPayload) throws KapuaException {
        KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

        //
        // Payload translation
        kuraRequestPayload.getMetrics().putAll(kapuaPayload.getMetrics());

        //
        // Body translation
        kuraRequestPayload.setBody(kapuaPayload.getBody());

        //
        // Return Kura Payload
        return kuraRequestPayload;
    }

    @Override
    public Class<GenericRequestMessage> getClassFrom() {
        return GenericRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }

}
