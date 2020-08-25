/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.keycloak;

import org.eclipse.kapua.sso.exception.SsoIllegalArgumentException;
import org.eclipse.kapua.sso.exception.uri.SsoIllegalUriException;
import org.eclipse.kapua.sso.provider.keycloak.setting.KeycloakSsoSetting;
import org.eclipse.kapua.sso.provider.keycloak.setting.KeycloakSsoSettingKeys;

/**
 * The Keycloak SingleSignOn service utility class.
 */
public class KeycloakSingleSignOnUtils {

    private static final KeycloakSsoSetting KEYCLOAK_SSO_SETTING = KeycloakSsoSetting.getInstance();
    public static final String KEYCLOAK_URI_COMMON_PART = "/auth/realms/";

    private KeycloakSingleSignOnUtils() {
    }

    /**
     * Get the Keycloak realm.
     *
     * @return the Keycloak realm in the form of a String.
     * @throws SsoIllegalArgumentException if the realm is not set.
     */
    public static String getRealm() throws SsoIllegalArgumentException {
        String realm = KEYCLOAK_SSO_SETTING.getString(KeycloakSsoSettingKeys.KEYCLOAK_REALM);
        if (realm == null || realm.isEmpty()) {
            throw new SsoIllegalArgumentException(KeycloakSsoSettingKeys.KEYCLOAK_REALM.key(), realm);
        }
        return realm;
    }

    /**
     * Get the Keycloak provider URI.
     *
     * @return the Keycloak provider URI in the form of a String.
     * @throws SsoIllegalUriException if the Keycloak provider URI is not set.
     */
    public static String getProviderUri() throws SsoIllegalUriException {
        String providerUri = KEYCLOAK_SSO_SETTING.getString(KeycloakSsoSettingKeys.KEYCLOAK_URI);
        if (providerUri == null || providerUri.isEmpty()) {
            throw new SsoIllegalUriException(KeycloakSsoSettingKeys.KEYCLOAK_URI.key(), providerUri);
        }
        return providerUri;
    }

}
