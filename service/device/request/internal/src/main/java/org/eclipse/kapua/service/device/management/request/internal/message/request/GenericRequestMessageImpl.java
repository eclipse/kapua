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
package org.eclipse.kapua.service.device.management.request.internal.message.request;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;

public class GenericRequestMessageImpl extends KapuaMessageImpl<GenericRequestChannel, GenericRequestPayload> implements GenericRequestMessage {

    @SuppressWarnings("unchecked")
    @Override
    public Class<GenericRequestMessage> getRequestClass() {
        return GenericRequestMessage.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<GenericResponseMessage> getResponseClass() {
        return GenericResponseMessage.class;
    }
}
