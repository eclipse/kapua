/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

/**
 * Credential predicates used to build query predicates.
 *
 * @since 1.0.0
 */
public interface CredentialPredicates extends KapuaUpdatableEntityPredicates {

    String USER_ID = "userId";
    String CREDENTIAL_TYPE = "credentialType";
    String CREDENTIAL_KEY = "credentialKey";
    String EXPIRATION_DATE = "expirationDate";
    public static final String MODIFIED_ON = "modifiedOn";

}
