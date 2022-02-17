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
package org.eclipse.kapua.service.device.management.commons.message.request;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;

/**
 * {@link KapuaRequestMessage} implementation.
 *
 * @param <C> The {@link KapuaRequestChannel} type.
 * @param <P> The {@link KapuaRequestPayload} type.
 * @since 1.0.0
 */
public class KapuaRequestMessageImpl<C extends KapuaRequestChannel, P extends KapuaRequestPayload> extends KapuaMessageImpl<C, P> implements KapuaRequestMessage<C, P> {

    private static final long serialVersionUID = -2127359876688789508L;

    @Override
    public Class<KapuaRequestMessage> getRequestClass() {
        return KapuaRequestMessage.class;
    }

    @Override
    public Class<KapuaResponseMessage> getResponseClass() {
        return KapuaResponseMessage.class;
    }
}
