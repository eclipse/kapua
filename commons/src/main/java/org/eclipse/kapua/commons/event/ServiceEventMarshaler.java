/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.event.ServiceEvent;

/**
 * Event bus marshaler. It allows custom marshaling/unmarshaling of the service bus event object.
 *
 * @since 1.0
 */
public interface ServiceEventMarshaler {

    String CONTENT_TYPE_KEY = "ContentType";

    /**
     * Return the encoded content type
     *
     * @return
     */
    String getContentType();

    /**
     * Unmarshal the message received from the bus
     *
     * @param message
     * @return
     * @throws KapuaException
     */
    ServiceEvent unmarshal(String message) throws KapuaException;

    /**
     * Marshal the message to the service event bus
     *
     * @param kapuaEvent
     * @throws KapuaException
     */
    String marshal(ServiceEvent kapuaEvent) throws KapuaException;

}
