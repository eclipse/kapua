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
 *
 *******************************************************************************/
package org.eclipse.kapua;

/**
 * KapuaUnauthenticatedException is thrown when no authenticated context is found on pre-action rights check.
 * 
 * @since 1.0
 * 
 */
public class KapuaUnauthenticatedException extends KapuaException 
{
    private static final long serialVersionUID = -8059684526029130460L;

    /**
     * Constructor
     */
    public KapuaUnauthenticatedException() {
        super(KapuaErrorCodes.UNAUTHENTICATED);
    }
}
