/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.message;

import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.message.transport.TransportChannel;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportPayload;
import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Kapua message service factory definition.
 *
 * @since 1.0
 */
public interface KapuaMessageFactory extends KapuaObjectFactory {

    /**
     * Creates and returns a new {@link KapuaMessage}
     *
     * @return
     */
    public KapuaMessage<?,?> newMessage();

    /**
     * Creates and returns a new {@link KapuaDataMessage}
     * @return
     */
    public KapuaDataMessage newKapuaDataMessage();

    /**
     * Creates and returns a new {@link KapuaChannel}
     *
     * @return
     */
    public KapuaChannel newChannel();

    /**
     * Creates and returns a new {@link KapuaPayload}
     *
     * @return
     */
    public KapuaPayload newPayload();

    /**
     * Creates and returns a new {@link KapuaPosition}
     *
     * @return
     */
    public KapuaPosition newPosition();

    KapuaDataChannel newKapuaDataChannel();

    KapuaDataPayload newKapuaDataPayload();

    /**
     * Creates and returns a new {@link TransportMessage}
     * @return
     */
    public TransportMessage newTransportMessage();

    TransportChannel newTransportChannel();

    TransportPayload newTransportPayload();
}
