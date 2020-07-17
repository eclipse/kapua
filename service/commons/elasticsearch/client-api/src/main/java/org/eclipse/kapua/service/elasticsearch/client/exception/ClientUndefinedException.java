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
 * {@link ClientException} to throw when {@link org.eclipse.kapua.service.elasticsearch.client.DatastoreClient} is not defined.
 *
 * @since 1.0.0
 */
public class ClientUndefinedException extends ClientException {

    private static final long serialVersionUID = 2211521053876589804L;

    /**
     * Constructor.
     *
     * @since 1.3.0
     */
    public ClientUndefinedException() {
        super(ClientErrorCodes.CLIENT_UNDEFINED);
    }
}
