/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
