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
 * @since 2.0.0
 */
public class KapuaDuplicateExternalUsernameException extends KapuaException {

    private static final long serialVersionUID = -2761138212317761216L;

    /**
     * Constructor for the {@link KapuaDuplicateExternalUsernameException} taking in the duplicated external username.
     *
     * @param duplicatedExternalUsername
     * @since 2.0.0
     */
    public KapuaDuplicateExternalUsernameException(String duplicatedExternalUsername) {
        super(KapuaErrorCodes.DUPLICATE_EXTERNAL_USERNAME, duplicatedExternalUsername);
    }

}
