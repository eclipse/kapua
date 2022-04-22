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
package org.eclipse.kapua;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * KapuaEntityExistsException is thrown when an operation cannot be completed because an unique key constraint has been violated.
 *
 * @since 1.0.0
 */
public class KapuaEntityExistsException extends KapuaRuntimeException {

    private static final long serialVersionUID = -2991079800089173789L;

    private final KapuaId id;

    /**
     * Constructor for the {@link KapuaEntityExistsException} taking in the duplicated name.
     *
     * @param t  The original {@link Throwable} error that caused this exception.
     * @param id The key that conflicts on the insert operation.
     * @since 1.0.0
     */
    public KapuaEntityExistsException(Throwable t, KapuaId id) {
        super(KapuaErrorCodes.ENTITY_ALREADY_EXISTS, t);

        this.id = id;
    }

    /**
     * Gets the key that conflicts on the insert operation.
     *
     * @return The key that conflicts on the insert operation.
     * @since 1.0.0
     */
    public KapuaId getId() {
        return id;
    }

}
