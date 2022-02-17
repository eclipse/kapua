/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.mfa;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;

/**
 * {@link KapuaExistingMfaOptionException} is thrown when an operation cannot be completed because the {@link MfaOption} already exists.
 */
public class KapuaExistingMfaOptionException extends KapuaException {

    private static final long serialVersionUID = 9092695456934827181L;
    private static final String MESSAGE_FORMAT = "MfaOption already exists for this user.";

    /**
     * Constructor for the {@link KapuaExistingMfaOptionException}.
     */
    public KapuaExistingMfaOptionException() {
        super(KapuaErrorCodes.ENTITY_ALREADY_EXISTS, MESSAGE_FORMAT);
    }
}
