/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.message;

/**
 * Request method definition.
 * <p>
 * This object defines the request types that can be sent to a Device.
 *
 * @since 1.0.0
 */
public enum KapuaMethod {

    /**
     * Read request.
     *
     * @since 1.0.0
     */
    READ,

    /**
     * Same as {@link #READ} but with a name that matches Kura naming.
     *
     * @since 1.2.0
     */
    GET,

    /**
     * Create request.
     *
     * @since 1.0.0
     */
    CREATE,

    /**
     * Same as {@link #CREATE} but with a name that matches Kura naming.
     *
     * @since 1.2.0
     */
    POST,

    /**
     * Write request.
     *
     * @since 1.0.0
     */
    WRITE,

    /**
     * Same as {@link #WRITE} but with a name that matches Kura naming.
     *
     * @since 1.2.0
     */
    PUT,

    /**
     * Delete request.
     *
     * @since 1.0.0
     */
    DELETE,

    /**
     * Same as {@link #DELETE} but with a name that matches Kura naming.
     *
     * @since 1.2.0
     */
    DEL,

    /**
     * Execute request.
     *
     * @since 1.0.0
     */
    EXECUTE,

    /**
     * Same as {@link #EXECUTE} but with a name that matches Kura naming.
     *
     * @since 1.2.0
     */
    EXEC,

    /**
     * Options request.
     *
     * @since 1.0.0
     */
    OPTIONS;

    /**
     * Normalizes the {@link KapuaMethod} value to match sibling {@link KapuaMethod}s
     *
     * @return The normalized {@link KapuaMethod} form {@code this} {@link KapuaMethod}
     * @since 1.2.0
     */
    public KapuaMethod normalizeAction() {

        switch (this) {
            case POST:
                return KapuaMethod.CREATE;
            case GET:
                return KapuaMethod.READ;
            case DEL:
                return KapuaMethod.DELETE;
            case EXEC:
                return KapuaMethod.EXECUTE;
            case PUT:
                return KapuaMethod.WRITE;
            default:
                return this;
        }
    }
}
