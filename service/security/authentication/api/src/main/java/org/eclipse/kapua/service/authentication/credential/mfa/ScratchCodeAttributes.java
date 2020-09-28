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

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

/**
 * ScratchCode predicates used to build query predicates.
 */
public class ScratchCodeAttributes extends KapuaUpdatableEntityAttributes {

    public static final String MFA_CREDENTIAL_OPTION_ID = "mfaCredentialOptionId";
    public static final String CODE = "scratchCode";
}
