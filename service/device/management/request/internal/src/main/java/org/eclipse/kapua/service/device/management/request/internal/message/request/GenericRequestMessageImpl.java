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
package org.eclipse.kapua.service.device.management.request.internal.message.request;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;

/**
 * {@link GenericRequestMessage} implementation.
 *
 * @since 1.0.0
 */
public class GenericRequestMessageImpl extends KapuaMessageImpl<GenericRequestChannel, GenericRequestPayload> implements GenericRequestMessage {

    private static final long serialVersionUID = -8491427803023664571L;

    @Override
    public Class<GenericRequestMessage> getRequestClass() {
        return GenericRequestMessage.class;
    }

    @Override
    public Class<GenericResponseMessage> getResponseClass() {
        return GenericResponseMessage.class;
    }
}
