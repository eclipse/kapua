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
 * Client communication exception (timeout, no node available ...)
 *
 * @since 1.0.0
 */
public class ClientCommunicationException extends ClientException {

    private static final long serialVersionUID = 1599649533697887868L;

    /**
     * Constructor.
     *
     * @since 1.3.0
     */
    public ClientCommunicationException() {
        super(ClientErrorCodes.COMMUNICATION_ERROR);
    }
}
