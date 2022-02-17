/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
     *
     * @since 1.1.0
     */
    public KapuaDisconnectPayloadImpl() {
        super();
    }

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

    @Override
    public void setUptime(String uptime) {
        getMetrics().put(KapuaDisconnectPayloadAttibutes.UPTIME, uptime);
    }

    @Override
    public String getDisplayName() {
        return (String) getMetrics().get(KapuaDisconnectPayloadAttibutes.DISPLAY_NAME);
    }

    @Override
    public void setDisplayName(String displayName) {
        getMetrics().put(KapuaDisconnectPayloadAttibutes.DISPLAY_NAME, displayName);
    }
}
