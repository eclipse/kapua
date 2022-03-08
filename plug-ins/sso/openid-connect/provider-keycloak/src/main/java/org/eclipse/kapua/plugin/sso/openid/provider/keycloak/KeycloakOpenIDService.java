/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.plugin.sso.openid.provider.keycloak;

import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDIllegalArgumentException;
import org.eclipse.kapua.plugin.sso.openid.provider.AbstractOpenIDService;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;

/**
 * The Keycloak OpenID service class.
 */
public class KeycloakOpenIDService extends AbstractOpenIDService {

    private static final String KEYCLOAK_AUTH_URI_SUFFIX = "/protocol/openid-connect/auth";
    private static final String KEYCLOAK_TOKEN_URI_SUFFIX = "/protocol/openid-connect/token";
    private static final String KEYCLOAK_USERINFO_URI_SUFFIX = "/protocol/openid-connect/userinfo";
    private static final String KEYCLOAK_LOGOUT_URI_SUFFIX = "/protocol/openid-connect/logout";

    public KeycloakOpenIDService() {
        this(OpenIDSetting.getInstance());
    }

    public KeycloakOpenIDService(final OpenIDSetting ssoSettings) {
        super(ssoSettings);
    }

    @Override
    protected String getAuthUri() throws OpenIDIllegalArgumentException {
        return KeycloakOpenIDUtils.getProviderUri() + KeycloakOpenIDUtils.KEYCLOAK_URI_COMMON_PART +
                KeycloakOpenIDUtils.getRealm() + KEYCLOAK_AUTH_URI_SUFFIX;
    }

    @Override
    protected String getLogoutUri() throws OpenIDIllegalArgumentException {
        return KeycloakOpenIDUtils.getProviderUri() + KeycloakOpenIDUtils.KEYCLOAK_URI_COMMON_PART +
                KeycloakOpenIDUtils.getRealm() + KEYCLOAK_LOGOUT_URI_SUFFIX;
    }

    @Override
    protected String getTokenUri() throws OpenIDIllegalArgumentException {
        return KeycloakOpenIDUtils.getProviderUri() + KeycloakOpenIDUtils.KEYCLOAK_URI_COMMON_PART +
                KeycloakOpenIDUtils.getRealm() + KEYCLOAK_TOKEN_URI_SUFFIX;
    }

    @Override
    protected String getUserInfoUri() throws OpenIDIllegalArgumentException {
        return KeycloakOpenIDUtils.getProviderUri() + KeycloakOpenIDUtils.KEYCLOAK_URI_COMMON_PART +
                KeycloakOpenIDUtils.getRealm() + KEYCLOAK_USERINFO_URI_SUFFIX;
    }

}
