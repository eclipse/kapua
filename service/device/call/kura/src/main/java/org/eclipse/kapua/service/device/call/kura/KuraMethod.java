/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.service.device.call.DeviceMethod;

/**
 * {@link DeviceMethod} {@link Kura} definition.
 *
 * @since 1.0.0
 */
public enum KuraMethod implements DeviceMethod {

    /**
     * Get command.
     *
     * @since 1.0.0
     */
    GET,

    /**
     * Delete command
     *
     * @since 1.0.0
     */
    DEL,

    /**
     * Execute command
     *
     * @since 1.0.0
     */
    EXEC,

    /**
     * Post command
     *
     * @since 1.0.0
     */
    POST,

    /**
     * Put command
     *
     * @since 1.0.0
     */
    PUT
}
