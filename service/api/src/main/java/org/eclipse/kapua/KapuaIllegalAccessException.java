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
package org.eclipse.kapua;

/**
 * KapuaIllegalAccessException is thrown when an access check does not pass.
 * 
 * @since 1.0
 * 
 */
public class KapuaIllegalAccessException extends KapuaException {

    private static final long serialVersionUID = 7415560563036738488L;

    /**
     * Constructor
     * 
     * @param operationName
     */
    public KapuaIllegalAccessException(String operationName) {
        super(KapuaErrorCodes.ILLEGAL_ACCESS, operationName);
    }
}
