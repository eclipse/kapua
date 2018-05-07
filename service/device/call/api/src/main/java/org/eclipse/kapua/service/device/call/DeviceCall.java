/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;

/**
 * Device call definition.
 *
 * @param <RQ> device request message type
 * @param <RS> device response message type
 * @since 1.0
 */
public interface DeviceCall<RQ extends DeviceRequestMessage, RS extends DeviceResponseMessage> {

    /**
     * Executes a 'read command'
     *
     * @param requestMessage
     * @param timeout
     * @return
     * @throws KapuaException
     */
    RS read(RQ requestMessage, Long timeout) throws KapuaException;

    /**
     * Executes a 'create command'
     *
     * @param requestMessage
     * @param timeout
     * @return
     * @throws KapuaException
     */
    RS create(RQ requestMessage, Long timeout) throws KapuaException;

    /**
     * Executes a 'write command'
     *
     * @param requestMessage
     * @param timeout
     * @return
     * @throws KapuaException
     */
    RS write(RQ requestMessage, Long timeout) throws KapuaException;

    /**
     * Executes a 'delete command'
     *
     * @param requestMessage
     * @param timeout
     * @return
     * @throws KapuaException
     */
    RS delete(RQ requestMessage, Long timeout) throws KapuaException;

    /**
     * Executes an 'execute command'
     *
     * @param requestMessage
     * @param timeout
     * @return
     * @throws KapuaException
     */
    RS execute(RQ requestMessage, Long timeout) throws KapuaException;

    /**
     * Executes an 'options command'
     *
     * @param requestMessage
     * @param timeout
     * @return
     * @throws KapuaException
     */
    RS options(RQ requestMessage, Long timeout) throws KapuaException;

    /**
     * Get the device base message type
     *
     * @return
     */
    <M extends DeviceMessage> Class<M> getBaseMessageClass();
}
