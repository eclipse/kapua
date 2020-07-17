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
package org.eclipse.kapua.service.elasticsearch.client.exception;

import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;

/**
 * {@link ClientException} to throw when {@link ElasticsearchClient} fails to initialize
 *
 * @since 1.3.0
 */
public class ClientInitializationException extends ClientException {

    private static final long serialVersionUID = 2211521053876589804L;

    /**
     * Constructor.
     *
     * @since 1.3.0
     */
    public ClientInitializationException() {
        super(ClientErrorCodes.CLIENT_INITIALIZATION_ERROR);
    }


    /**
     * Constructor.
     *
     * @param cause The root {@link Throwable} of this {@link ClientInitializationException}.
     * @since 1.3.0
     */
    public ClientInitializationException(Throwable cause, String clientClassName) {
        super(ClientErrorCodes.CLIENT_INITIALIZATION_ERROR, cause);
    }
}
