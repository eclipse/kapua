/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.device.lifecycle;

/**
 * {@link KapuaDisconnectPayloadAttibutes} definitions.
 * <p>
 * All available fields in a {@link KapuaDisconnectPayload}
 *
 * @since 1.0.0
 */
public class KapuaDisconnectPayloadAttibutes {

    private KapuaDisconnectPayloadAttibutes() {
    }

    /**
     * @since 1.0.0
     */
    public static final String UPTIME = "uptime";

    /**
     * @since 1.0.0
     */
    public static final String DISPLAY_NAME = "displayName";
}
