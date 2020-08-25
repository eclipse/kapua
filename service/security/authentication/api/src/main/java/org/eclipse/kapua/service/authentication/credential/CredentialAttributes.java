/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

/**
 * Credential predicates used to build query predicates.
 *
 * @since 1.0.0
 */
public class CredentialAttributes extends KapuaUpdatableEntityAttributes {

    public static final String USER_ID = "userId";
    public static final String CREDENTIAL_TYPE = "credentialType";
    public static final String CREDENTIAL_KEY = "credentialKey";
    public static final String EXPIRATION_DATE = "expirationDate";

}
