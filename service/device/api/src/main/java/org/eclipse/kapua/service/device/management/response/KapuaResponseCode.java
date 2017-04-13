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
package org.eclipse.kapua.service.device.management.response;

/**
 * Request/reply message response code.
 * 
 * @since 1.0
 *
 */
public enum KapuaResponseCode
{
    /**
     * Accepted
     */
    ACCEPTED, // 200
              // 204
    /**
     * Bad request
     */
    BAD_REQUEST, // 400
    /**
     * Resource not found
     */
    NOT_FOUND, // 404
    /**
     * Internal error
     */
    INTERNAL_ERROR; // 500
}
