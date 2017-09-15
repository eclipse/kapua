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
 * KapuaIllegalStateException is thrown when the the user session is inconsistent.
 * 
 * @since 1.0
 *
 */
public class KapuaIllegalStateException extends KapuaRuntimeException {

    private static final long serialVersionUID = -912672615903975466L;

    /**
     * Constructor
     * 
     * @param message
     */
    public KapuaIllegalStateException(String message) {
        super(KapuaErrorCodes.ILLEGAL_STATE, null, message);
    }

    /**
     * Constructor
     * 
     * @param message
     * @param t
     */
    public KapuaIllegalStateException(String message, Throwable t) {
        super(KapuaErrorCodes.ILLEGAL_STATE, t, message);
    }

}
