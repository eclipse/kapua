/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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

/**
 * @since 1.2.0
 * @deprecated Since 2.0.0. This is not used but in tests.
 */
@Deprecated
public class KapuaDuplicateExternalIdInAnotherAccountError extends KapuaException {

    private static final long serialVersionUID = 6086560691695487741L;

    /**
     * Constructor.
     *
     * @param duplicateExternalId The duplicate User.externalId
     * @since 1.2.0
     */
    public KapuaDuplicateExternalIdInAnotherAccountError(String duplicateExternalId) {
        super(KapuaErrorCodes.EXTERNAL_ID_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, duplicateExternalId);
        // TODO Auto-generated constructor stub
    }

}
