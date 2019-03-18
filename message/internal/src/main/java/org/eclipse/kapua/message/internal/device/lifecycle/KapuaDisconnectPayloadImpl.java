/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;

/**
 * {@link KapuaDisconnectPayload} implementation.
 *
 * @since 1.0.0
 */
public class KapuaDisconnectPayloadImpl extends AbstractLifecyclePayloadImpl implements KapuaDisconnectPayload {

    private String uptime;
    private String displayName;

    /**
     * Constructor.
     * <p>
     * Sets all available properties of the {@link KapuaDisconnectPayload} at once.
     *
     * @param uptime      The uptime of the {@link KapuaDisconnectPayload}
     * @param displayName The display name of the {@link KapuaDisconnectPayload}
     * @since 1.0.0
     */
    public KapuaDisconnectPayloadImpl(String uptime, String displayName) {
        this.uptime = uptime;
        this.displayName = displayName;
    }


    @Override
    public String getUptime() {
        return uptime;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
