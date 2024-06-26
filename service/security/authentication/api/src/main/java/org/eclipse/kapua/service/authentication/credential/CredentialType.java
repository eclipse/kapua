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

/**
 * Available credential types
 *
 * @since 1.0.0
 * @deprecated Since 2.1.0. Moved to Injectable {@link Credential} types
 */
@Deprecated
public enum CredentialType {
    /**
     * Password
     *
     * @since 1.0.0
     */
    PASSWORD,

    /**
     * API key
     *
     * @since 1.0.0
     */
    API_KEY,

    /**
     * Json Web Token
     *
     * @since 1.0.0
     */
    JWT;
}
