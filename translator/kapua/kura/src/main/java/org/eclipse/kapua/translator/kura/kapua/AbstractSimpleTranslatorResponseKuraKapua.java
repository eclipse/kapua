/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.request.GenericRequestFactory;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

/**
 * {@link org.eclipse.kapua.translator.Translator} {@code abstract} implementation for {@link KuraResponseMessage} to {@link KapuaResponseMessage}
 *
 * @since 1.0.0
 */
public abstract class AbstractSimpleTranslatorResponseKuraKapua<TO_C extends KapuaResponseChannel, TO_P extends KapuaResponsePayload, TO_M extends KapuaResponseMessage<TO_C, TO_P>>
        extends AbstractTranslatorResponseKuraKapua<TO_C, TO_P, TO_M> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private final Class<TO_M> messageClazz;

    public AbstractSimpleTranslatorResponseKuraKapua(Class<TO_M> messageClazz) {
        this.messageClazz = messageClazz;
    }

    @Override
    protected TO_M createMessage() throws KapuaException {
        GenericRequestFactory genericRequestFactory = LOCATOR.getFactory(GenericRequestFactory.class);

        try {
            if (this.messageClazz.equals(GenericResponseMessage.class)) {
                return this.messageClazz.cast(genericRequestFactory.newResponseMessage());
            } else {
                return this.messageClazz.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public Class<KuraResponseMessage> getClassFrom() {
        return KuraResponseMessage.class;
    }

    @Override
    public Class<TO_M> getClassTo() {
        return this.messageClazz;
    }

    @Override
    protected abstract TO_C translateChannel(KuraResponseChannel kuraChannel) throws InvalidChannelException;

    @Override
    protected abstract TO_P translatePayload(KuraResponsePayload kuraPayload) throws InvalidPayloadException;
}
