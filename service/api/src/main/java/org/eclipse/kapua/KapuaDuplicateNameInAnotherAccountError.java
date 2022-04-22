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
 * @since 1.0.0
 */
public class KapuaDuplicateNameInAnotherAccountError extends KapuaException {

    private static final long serialVersionUID = 6086560691695487741L;

    private final String duplicateName;

    /**
     * @param duplicateName The duplicate name.
     * @since 1.0.0
     */
    public KapuaDuplicateNameInAnotherAccountError(String duplicateName) {
        super(KapuaErrorCodes.ENTITY_ALREADY_EXIST_IN_ANOTHER_ACCOUNT, duplicateName);

        this.duplicateName = duplicateName;
    }

    /**
     * Gets the duplicate name.
     *
     * @return The duplicate name.
     * @since 2.0.0
     */
    public String getDuplicateName() {
        return duplicateName;
    }
}
