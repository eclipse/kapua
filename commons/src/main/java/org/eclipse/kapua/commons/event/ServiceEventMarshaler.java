/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.event.ServiceEvent;

/**
 * Event bus marshaler. It allows custom marshaling/unmarshaling of the service bus event object.
 *
 * @since 1.0
 */
public interface ServiceEventMarshaler {

    public final static String CONTENT_TYPE_KEY = "ContentType";

    /**
     * Return the encoded content type
     * 
     * @return
     */
    public String getContentType();

    /**
     * Unmarshal the message received from the bus
     * 
     * @param message
     * @return
     * @throws KapuaException
     */
    public ServiceEvent unmarshal(String message) throws KapuaException;

    /**
     * Marshal the message to the service event bus
     * 
     * @param kapuaEvent
     * @throws KapuaException
     */
    public String marshal(ServiceEvent kapuaEvent) throws KapuaException;

}