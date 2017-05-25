/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.service.device.call.DeviceMethod;

/**
 * Kura device method definition.<br>
 * This object defines the command methods supported by a Kura device.
 * 
 * @since 1.0
 *
 */
public enum KuraMethod implements DeviceMethod {
    /**
     * Get command
     */
    GET,
    /**
     * Post command
     */
    POST,
    /**
     * Put command
     */
    PUT,
    /**
     * Delete command
     */
    DEL,
    /**
     * Execute command
     */
    EXEC
}
