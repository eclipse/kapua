/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.transport;

import org.eclipse.kapua.message.internal.KapuaChannelImpl;
import org.eclipse.kapua.message.transport.TransportChannel;

/**
 * Kapua data message channel object reference implementation.
 * 
 * @since 1.0
 *
 */
public class TransportChannelImpl extends KapuaChannelImpl implements TransportChannel {

    private static final long serialVersionUID = -1555858455839592300L;

    private String originalDestination;

    @Override
    public String getOriginalDestination() {
        return originalDestination;
    }

    @Override
    public void setOriginalDestination(String originalDestination) {
        this.originalDestination = originalDestination;
    }
}
