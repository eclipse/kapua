/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
