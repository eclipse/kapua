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
     * Get request.
     *
     * @since 1.0.0
     */
    GET,

    /**
     * Delete request
     *
     * @since 1.0.0
     */
    DEL,

    /**
     * Execute request
     *
     * @since 1.0.0
     */
    EXEC,

    /**
     * Post request
     *
     * @since 1.0.0
     */
    POST,

    /**
     * Put request
     *
     * @since 1.0.0
     */
    PUT,

    /**
     * Submit request.
     *
     * @since 1.5.0
     */
    SUBMIT,

    /**
     * Cancel request.
     *
     * @since 1.5.0
     */
    CANCEL
}
