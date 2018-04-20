/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.mediator;

/**
 * Invalid configuration exception.<br>
 * This exception is raised if some configuration parameter is not valid
 * 
 * @since 1.0
 *
 */
public class DatastoreCommunicationException extends DatastoreException {

    private static final long serialVersionUID = 5211237236391747299L;

    private String uuid;

    /**
     * Construct the exception with the provided uuid and throwable
     * 
     * @param uuid
     *            the kapua message identifier
     * @param t
     *            cause exception
     */
    public DatastoreCommunicationException(String uuid, Throwable t) {
        super(DatastoreErrorCodes.COMMUNICATION_ERROR, t);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

}
