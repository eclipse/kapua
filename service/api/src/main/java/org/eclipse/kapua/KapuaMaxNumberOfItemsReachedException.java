/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

public class KapuaMaxNumberOfItemsReachedException extends KapuaException {

    private static final long serialVersionUID = 8651132350411186861L;
    private String argValue;


    public KapuaMaxNumberOfItemsReachedException(String argValue) {
        super(KapuaErrorCodes.MAX_NUMBER_OF_ITEMS_REACHED, argValue);
        this.argValue = argValue;
    }

    public String getArgValue(){
        return argValue;
    }
}
