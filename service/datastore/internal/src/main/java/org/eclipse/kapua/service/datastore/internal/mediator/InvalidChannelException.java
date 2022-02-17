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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.mediator;

/**
 * Invalid channel exception.<br>
 * This exception is raised if the channel doesn't match validations
 *
 * @since 1.0
 */
public class InvalidChannelException extends DatastoreException {

    private static final long serialVersionUID = 5211237236391747299L;

    /**
     * Construct the exception with the provided message
     *
     * @param message
     */
    public InvalidChannelException(String message) {
        super(DatastoreErrorCodes.INVALID_CHANNEL, message);
    }

    /**
     * Construct the exception with the provided message and throwable
     *
     * @param message
     * @param t
     */
    public InvalidChannelException(String message, Throwable t) {
        super(DatastoreErrorCodes.INVALID_CHANNEL, t, message);
    }

}
