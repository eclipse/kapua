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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

/**
 * {@link Credential} {@link KapuaUpdatableEntityAttributes}.
 *
 * @since 1.0.0
 */
public class CredentialAttributes extends KapuaUpdatableEntityAttributes {

    /**
     * @since 1.0.0
     */
    public static final String USER_ID = "userId";
    /**
     * @since 1.0.0
     */
    public static final String CREDENTIAL_TYPE = "credentialType";
    /**
     * @since 1.0.0
     */
    public static final String CREDENTIAL_KEY = "credentialKey";
    /**
     * @since 1.0.0
     */
    public static final String EXPIRATION_DATE = "expirationDate";

}
