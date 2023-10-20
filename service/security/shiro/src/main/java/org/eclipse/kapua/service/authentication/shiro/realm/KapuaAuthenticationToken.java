/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.Optional;

/**
 * Kapua {@link AuthenticationToken} definition.
 *
 * @since 2.0.0
 */
public interface KapuaAuthenticationToken extends AuthenticationToken {

    /**
     * Gets the {@link Optional} OpenID Connect <a href="https://auth0.com/blog/id-token-access-token-what-is-the-difference/#What-Is-an-ID-Token">idToken</a>.
     *
     * @return The {@link Optional} OpenID Connect <a href="https://auth0.com/blog/id-token-access-token-what-is-the-difference/#What-Is-an-ID-Token">idToken</a>.
     * @since 2.0.0
     */
    Optional<String> getOpenIdToken();
}
