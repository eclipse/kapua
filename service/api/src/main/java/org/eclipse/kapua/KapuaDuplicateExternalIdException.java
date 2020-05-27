/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
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
 * KapuaDuplicateExternalIdException is thrown when an operation cannot be completed because an unique externalId
 * constraint has been violated.
 *
 * @since 1.2
 *
 */
public class KapuaDuplicateExternalIdException extends KapuaException {

    private static final long serialVersionUID = -2761138212317761216L;

    /**
     * Constructor for the {@link KapuaDuplicateExternalIdException} taking in the duplicated externalId.
     *
     * @param duplicatedExternalId
     */
    public KapuaDuplicateExternalIdException(String duplicatedExternalId) {
        super(KapuaErrorCodes.DUPLICATE_EXTERNAL_ID, duplicatedExternalId);
    }

}
