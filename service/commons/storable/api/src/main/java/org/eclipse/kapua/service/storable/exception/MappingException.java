/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.storable.exception;

/**
 * {@link StorableException} to {@code throw} when there is an error when mapping an {@link Object}.
 *
 * @since 1.3.0
 */
public class MappingException extends StorableException {

    /**
     * Constructor.
     *
     * @param code      The {@link StorableErrorCodes}.
     * @param arguments Additional argument associated with the {@link MappingException}.
     * @since 1.3.0
     */
    public MappingException(StorableErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * Constructor.
     *
     * @param code      The {@link StorableErrorCodes}.
     * @param cause     The root {@link Throwable} of this {@link MappingException}.
     * @param arguments Additional argument associated with the {@link MappingException}.
     * @since 1.3.0
     */
    public MappingException(StorableErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

}
