/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.request;

import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponsePayload;

public interface KapuaRequestMessage<C extends KapuaRequestChannel, P extends KapuaRequestPayload> extends KapuaMessage<C, P>
{
    public <M extends KapuaRequestMessage<C, P>> Class<M> getRequestClass();

    public <RSC extends KapuaResponseChannel, RSP extends KapuaResponsePayload, M extends KapuaResponseMessage<RSC, RSP>> Class<M> getResponseClass();
}
