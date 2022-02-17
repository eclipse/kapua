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
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import com.google.common.base.Strings;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link GenericRequestMessage} to {@link KuraRequestMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppRequestKapuaKura extends AbstractTranslatorKapuaKura<GenericRequestChannel, GenericRequestPayload, GenericRequestMessage> {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();

    @Override
    protected KuraRequestChannel translateChannel(GenericRequestChannel kapuaChannel) throws InvalidChannelException {
        try {
            KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
            kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);

            StringBuilder appIdSb = new StringBuilder();
            appIdSb.append(kapuaChannel.getAppName().getValue());

            if (kapuaChannel.getVersion() != null && !Strings.isNullOrEmpty(kapuaChannel.getVersion().getValue())) {
                appIdSb.append("-").append(kapuaChannel.getVersion().getValue());
            }

            kuraRequestChannel.setAppId(appIdSb.toString());
            kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.translate(kapuaChannel.getMethod()));
            kuraRequestChannel.setResources(kapuaChannel.getResources().toArray(new String[0]));

            return kuraRequestChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kapuaChannel);
        }
    }

    @Override
    protected KuraRequestPayload translatePayload(GenericRequestPayload kapuaPayload) throws InvalidPayloadException {
        try {
            KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

            // Payload translation
            kuraRequestPayload.getMetrics().putAll(kapuaPayload.getMetrics());

            // Body translation
            kuraRequestPayload.setBody(kapuaPayload.getBody());

            // Return Kura Payload
            return kuraRequestPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kapuaPayload);
        }
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
