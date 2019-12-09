/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.plugin.sso.openid.provider.keycloak;

import org.eclipse.kapua.plugin.sso.openid.exception.SsoIllegalArgumentException;
import org.eclipse.kapua.plugin.sso.openid.provider.AbstractSingleSignOnService;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.SsoSetting;

/**
 * The Keycloak SingleSignOn service class.
 */
public class KeycloakSingleSignOnService extends AbstractSingleSignOnService {

    private static final String KEYCLOAK_AUTH_URI_SUFFIX = "/protocol/openid-connect/auth";
    private static final String KEYCLOAK_TOKEN_URI_SUFFIX = "/protocol/openid-connect/token";
    private static final String KEYCLOAK_LOGOUT_URI_SUFFIX = "/protocol/openid-connect/logout";

    public KeycloakSingleSignOnService() {
        this(SsoSetting.getInstance());
    }

    public KeycloakSingleSignOnService(final SsoSetting ssoSettings) {
        super(ssoSettings);
    }

    @Override
    protected String getAuthUri() throws SsoIllegalArgumentException {
        return KeycloakSingleSignOnUtils.getProviderUri() + KeycloakSingleSignOnUtils.KEYCLOAK_URI_COMMON_PART +
                KeycloakSingleSignOnUtils.getRealm() + KEYCLOAK_AUTH_URI_SUFFIX;
    }

    @Override
    protected String getTokenUri() throws SsoIllegalArgumentException {
        return KeycloakSingleSignOnUtils.getProviderUri() + KeycloakSingleSignOnUtils.KEYCLOAK_URI_COMMON_PART +
                KeycloakSingleSignOnUtils.getRealm() + KEYCLOAK_TOKEN_URI_SUFFIX;
    }

    @Override
    protected String getLogoutUri() throws SsoIllegalArgumentException {
        return KeycloakSingleSignOnUtils.getProviderUri() + KeycloakSingleSignOnUtils.KEYCLOAK_URI_COMMON_PART +
                KeycloakSingleSignOnUtils.getRealm() + KEYCLOAK_LOGOUT_URI_SUFFIX;
    }

}
