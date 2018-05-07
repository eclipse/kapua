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

import org.eclipse.kapua.service.device.call.message.DeviceMessage;

/**
 * Device callback definition
 *
 * @param <RSM> response message type
 * @since 1.0
 */
public interface DeviceCallback<RSM extends DeviceMessage<?, ?>> {

    /**
     * Action to be invoked on response received
     *
     * @param response
     */
    void responseReceived(RSM response);

    /**
     * Action to be invoked on timed out
     */
    void timedOut();

}
