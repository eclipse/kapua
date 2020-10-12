/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
