/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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
 * {@link KapuaMessage}s {@link KapuaObjectFactory} definition.
 *
 * @since 1.0
 */
public interface KapuaMessageFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link KapuaMessage}
     *
     * @return the new {@link KapuaMessage}
     */
    public KapuaMessage newMessage();

    /**
     * Creates a new {@link KapuaChannel}
     *
     * @return the new {@link KapuaChannel}
     */
    public KapuaChannel newChannel();

    /**
     * Creates a new {@link KapuaPayload}
     *
     * @return the new {@link KapuaPayload}
     */
    public KapuaPayload newPayload();

    /**
     * Creates a new {@link KapuaPosition}
     *
     * @return the new {@link KapuaPosition}
     */
    public KapuaPosition newPosition();

    /**
     * Creates a new {@link KapuaDataMessage}
     *
     * @return the new {@link KapuaDataMessage}
     */
    public KapuaDataMessage newKapuaDataMessage();

    /**
     * Creates a new {@link KapuaDataChannel}
     *
     * @return the new {@link KapuaDataChannel}
     */
    KapuaDataChannel newKapuaDataChannel();

    /**
     * Creates a new {@link KapuaDataPayload}.
     *
     * @return the new {@link KapuaDataPayload}.
     */
    KapuaDataPayload newKapuaDataPayload();

    /**
     * Creates and returns a new {@link TransportMessage}
     * @return
     */
    public TransportMessage newTransportMessage();

    TransportChannel newTransportChannel();

    TransportPayload newTransportPayload();
}
