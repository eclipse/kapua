/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.app.response.kura.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.request.GenericRequestFactory;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponsePayload;

public abstract class AbstractSimpleTranslatorResponseKuraKapua<TO_C extends KapuaResponseChannel, TO_P extends KapuaResponsePayload, TO_M extends KapuaResponseMessage<TO_C, TO_P>>
        extends AbstractTranslatorResponseKuraKapua<TO_C, TO_P, TO_M> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final GenericRequestFactory FACTORY = LOCATOR.getFactory(GenericRequestFactory.class);

    private Class<TO_M> messageClazz;

    public AbstractSimpleTranslatorResponseKuraKapua(Class<TO_M> messageClazz) {
        this.messageClazz = messageClazz;
    }

    @Override
    protected TO_M createMessage() throws KapuaException {
        try {
            if (messageClazz.equals(GenericResponseMessage.class)) {
                return (TO_M)FACTORY.newResponseMessage();
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
    protected abstract TO_C translateChannel(KuraResponseChannel kuraChannel) throws KapuaException;

    @Override
    protected abstract TO_P translatePayload(KuraResponsePayload kuraPayload) throws KapuaException;
}
