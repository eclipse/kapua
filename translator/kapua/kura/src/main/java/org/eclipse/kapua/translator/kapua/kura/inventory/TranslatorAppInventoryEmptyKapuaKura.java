/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.kapua.kura.inventory;

import org.eclipse.kapua.service.device.call.kura.model.inventory.InventoryMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestPayload;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryEmptyRequestMessage;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryRequestChannel;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryRequestPayload;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.kapua.kura.AbstractTranslatorKapuaKura;
import org.eclipse.kapua.translator.kapua.kura.TranslatorKapuaKuraUtils;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link InventoryEmptyRequestMessage} to {@link KuraRequestMessage}
 *
 * @since 1.5.0
 */
public class TranslatorAppInventoryEmptyKapuaKura extends AbstractTranslatorKapuaKura<InventoryRequestChannel, InventoryRequestPayload, InventoryEmptyRequestMessage> {

    @Override
    protected KuraRequestChannel translateChannel(InventoryRequestChannel kapuaChannel) throws InvalidChannelException {
        try {
            KuraRequestChannel kuraRequestChannel = TranslatorKapuaKuraUtils.buildBaseRequestChannel(InventoryMetrics.APP_ID, InventoryMetrics.APP_VERSION, kapuaChannel.getMethod());

            kuraRequestChannel.setResources(new String[]{kapuaChannel.getResource()});

            // Return Kura Channel
            return kuraRequestChannel;
        } catch (Exception e) {
            throw new InvalidChannelException(e, kapuaChannel);
        }
    }

    @Override
    protected KuraRequestPayload translatePayload(InventoryRequestPayload kapuaPayload) throws InvalidPayloadException {
        try {
            KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();

            if (kapuaPayload.hasBody()) {
                kuraRequestPayload.setBody(kapuaPayload.getBody());
            }

            return kuraRequestPayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kapuaPayload);
        }
    }

    @Override
    public Class<InventoryEmptyRequestMessage> getClassFrom() {
        return InventoryEmptyRequestMessage.class;
    }

    @Override
    public Class<KuraRequestMessage> getClassTo() {
        return KuraRequestMessage.class;
    }

}
