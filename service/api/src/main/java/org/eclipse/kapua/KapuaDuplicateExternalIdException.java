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
 * {@link KapuaException} to {@code throw} when User.externalId exist already.
 * <p>
 * TODO: migrate this to `kapua-user-api` module.
 *
 * @since 1.2.0
 */
public class KapuaDuplicateExternalIdException extends KapuaException {

    private static final long serialVersionUID = -2761138212317761216L;

    private final String duplicateExternalId;

    /**
     * Constructor for the {@link KapuaDuplicateExternalIdException} taking in the duplicated externalId.
     *
     * @param duplicatedExternalId The duplicate User.externalId
     * @since 1.2.0
     */
    public KapuaDuplicateExternalIdException(String duplicatedExternalId) {
        super(KapuaErrorCodes.DUPLICATE_EXTERNAL_ID, duplicatedExternalId);

        this.duplicateExternalId = duplicatedExternalId;
    }

    /**
     * Gets the duplicate User.externalId.
     *
     * @return The duplicate User.externalId
     * @since 2.0.0
     */
    public String getDuplicateExternalId() {
        return duplicateExternalId;
    }
}
