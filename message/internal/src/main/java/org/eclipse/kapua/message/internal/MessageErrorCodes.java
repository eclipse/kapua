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
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Message error codes.
 * 
 * @since 1.0
 *
 */
public enum MessageErrorCodes implements KapuaErrorCode {
    /**
     * Invalid destination
     */
    INVALID_DESTINATION,
    /**
     * Invalid message
     */
    INVALID_MESSAGE,
    /**
     * Invalid metric type
     */
    INVALID_METRIC_TYPE,
    /**
     * Invalid metric value
     */
    INVALID_METRIC_VALUE
}
