/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.exception;

/**
 * Datamodel mapping exception.<br>
 * This exception is raised if some error occurred when mapping a datamodel
 *
 * @since 1.0.0
 */
public class DatamodelMappingException extends ClientException {

    private static final long serialVersionUID = 5211237236391747299L;

    private final String reason;

    /**
     * Construct the exception with the provided message
     *
     * @param reason The reason of the {@link DatamodelMappingException}.
     * @since 1.3.0
     */
    public DatamodelMappingException(String reason) {
        super(ClientErrorCodes.DATAMODEL_MAPPING_EXCEPTION, reason);

        this.reason = reason;
    }

    /**
     * Constructor.
     *
     * @param cause  The root {@link Throwable} of this {@link DatamodelMappingException}.
     * @param reason The reason of the {@link DatamodelMappingException}.
     * @since 1.3.0
     */
    public DatamodelMappingException(Throwable cause, String reason) {
        super(ClientErrorCodes.DATAMODEL_MAPPING_EXCEPTION, cause, reason);

        this.reason = reason;
    }

    /**
     * Gets the reason of the {@link DatamodelMappingException}.
     *
     * @return The reason of the {@link DatamodelMappingException}.
     * @since 1.3.0
     */
    public String getReason() {
        return reason;
    }
}
