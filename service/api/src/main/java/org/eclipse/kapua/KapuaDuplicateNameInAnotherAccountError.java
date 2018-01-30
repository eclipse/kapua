/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

public class KapuaDuplicateNameInAnotherAccountError extends KapuaException {

    /**
     * 
     */
    private static final long serialVersionUID = 6086560691695487741L;

    public KapuaDuplicateNameInAnotherAccountError(String duplicateName) {
        super(KapuaErrorCodes.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, duplicateName);
        // TODO Auto-generated constructor stub
    }

}