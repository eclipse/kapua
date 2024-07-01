/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.handler;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;

/**
 * The {@link CredentialTypeHandler} definition.
 *
 * @since 2.1.0
 */
public interface CredentialTypeHandler {

    String getName();

    void validateCreator(CredentialCreator credentialCreator) throws KapuaException;

    default boolean isServiceGenerated() {
        return false;
    }

    default String generate() throws KapuaException {
        throw new UnsupportedOperationException();
    }

    String cryptCredentialKey(String credentialPlainKey) throws KapuaException;
}
