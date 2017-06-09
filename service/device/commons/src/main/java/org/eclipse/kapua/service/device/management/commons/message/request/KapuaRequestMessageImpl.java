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
package org.eclipse.kapua.service.device.management.commons.message.request;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;

public class KapuaRequestMessageImpl<C extends KapuaRequestChannel, P extends KapuaRequestPayload> extends KapuaMessageImpl<C, P> implements KapuaRequestMessage<C, P> {

    @Override
    public Class<KapuaRequestMessage> getRequestClass() {
        return KapuaRequestMessage.class;
    }

    @Override
    public Class<KapuaResponseMessage> getResponseClass() {
        return KapuaResponseMessage.class;
    }
}
