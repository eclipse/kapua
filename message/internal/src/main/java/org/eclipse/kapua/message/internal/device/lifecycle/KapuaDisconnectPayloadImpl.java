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
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayloadAttibutes;

/**
 * {@link KapuaDisconnectPayload} implementation.
 *
 * @since 1.0.0
 */
public class KapuaDisconnectPayloadImpl extends AbstractLifecyclePayloadImpl implements KapuaDisconnectPayload {

    /**
     * Constructor.
     * <p>
     * Sets all available properties of the {@link KapuaDisconnectPayload} at once.
     *
     * @param uptime      The {@link KapuaDisconnectPayloadAttibutes#UPTIME}of the {@link KapuaDisconnectPayload}
     * @param displayName The {@link KapuaDisconnectPayloadAttibutes#DISPLAY_NAME} of the {@link KapuaDisconnectPayload}
     * @since 1.0.0
     */
    public KapuaDisconnectPayloadImpl(String uptime, String displayName) {
        setUptime(uptime);
        setDisplayName(displayName);
    }


    @Override
    public String getUptime() {
        return (String) getMetrics().get(KapuaDisconnectPayloadAttibutes.UPTIME);
    }

    private void setUptime(String uptime) {
        getMetrics().put(KapuaDisconnectPayloadAttibutes.UPTIME, uptime);
    }

    @Override
    public String getDisplayName() {
        return (String) getMetrics().get(KapuaDisconnectPayloadAttibutes.DISPLAY_NAME);
    }

    private void setDisplayName(String displayName) {
        getMetrics().put(KapuaDisconnectPayloadAttibutes.DISPLAY_NAME, displayName);
    }
}
