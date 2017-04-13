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
 * KapuaDuplicateNameException is thrown when an operation cannot be completed because an unique name constraint has been violated.
 * 
 * @since 1.0
 * 
 */
public class KapuaDuplicateNameException extends KapuaException 
{
    private static final long serialVersionUID = -2761138212317761216L;

    /**
     * Constructor for the {@link KapuaDuplicateNameException} taking in the duplicated name.
     *
     * @param duplicatedName
     */
    public KapuaDuplicateNameException(String duplicatedName) {
        super(KapuaErrorCodes.DUPLICATE_NAME, duplicatedName);
    }
}
