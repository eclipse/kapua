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
 * {@link ClientException} to {@code throw} when there is a quuery mapping error.
 *
 * @since 1.0.0
 */
public class QueryMappingException extends ClientException {

    private static final long serialVersionUID = 5211237236391747299L;

    /**
     * Constructor.
     *
     * @param reason The reason of this {@link QueryMappingException}.
     * @since 1.3.0
     */
    public QueryMappingException(String reason) {
        this(null, reason);
    }

    /**
     * Constructor.
     *
     * @param cause  The root cause of the {@link QueryMappingException}.
     * @param reason The reason of this {@link QueryMappingException}.
     * @since 1.3.0
     */
    public QueryMappingException(Throwable cause, String reason) {
        super(ClientErrorCodes.QUERY_MAPPING_EXCEPTION, cause, reason);
    }
}
