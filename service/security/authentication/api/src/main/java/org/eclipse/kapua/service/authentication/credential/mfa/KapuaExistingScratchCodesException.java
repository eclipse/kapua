/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;

/**
 * {@link KapuaExistingScratchCodesException} is thrown when an operation cannot be completed because a {@link ScratchCodeListResult} already exists.
 */
public class KapuaExistingScratchCodesException extends KapuaException {

    private static final long serialVersionUID = -310717682142223444L;
    private static final String MESSAGE_FORMAT = "Scratch Codes already exists for this user.";

    /**
     * Constructor for the {@link KapuaExistingMfaOptionException}.
     */
    public KapuaExistingScratchCodesException() {
        super(KapuaErrorCodes.ENTITY_ALREADY_EXISTS, MESSAGE_FORMAT);
    }
}
