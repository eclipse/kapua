/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others.
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
package org.eclipse.kapua.plugin.sso.openid.provider.keycloak;

import com.google.common.base.Strings;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDIllegalArgumentException;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDIllegalUriException;
import org.eclipse.kapua.plugin.sso.openid.provider.keycloak.setting.KeycloakOpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.keycloak.setting.KeycloakOpenIDSettingKeys;

/**
 * The Keycloak OpenID service utility class.
 */
public class KeycloakOpenIDUtils {

    private static final KeycloakOpenIDSetting KEYCLOAK_OPENID_SETTING = KeycloakOpenIDSetting.getInstance();
    public static final String KEYCLOAK_URI_COMMON_PART = "/auth/realms/";

    private KeycloakOpenIDUtils() {
    }

    /**
     * Get the Keycloak realm.
     *
     * @return the Keycloak realm in the form of a String.
     * @throws OpenIDIllegalArgumentException if the realm is not set.
     */
    public static String getRealm() throws OpenIDIllegalArgumentException {
        String realm = KEYCLOAK_OPENID_SETTING.getString(KeycloakOpenIDSettingKeys.KEYCLOAK_REALM);
        if (Strings.isNullOrEmpty(realm)) {
            throw new OpenIDIllegalArgumentException(KeycloakOpenIDSettingKeys.KEYCLOAK_REALM.key(), realm);
        }
        return realm;
    }

    /**
     * Get the Keycloak provider URI.
     *
     * @return the Keycloak provider URI in the form of a String.
     * @throws OpenIDIllegalUriException if the Keycloak provider URI is not set.
     */
    public static String getProviderUri() throws OpenIDIllegalUriException {
        String providerUri = KEYCLOAK_OPENID_SETTING.getString(KeycloakOpenIDSettingKeys.KEYCLOAK_URI);
        if (Strings.isNullOrEmpty(providerUri)) {
            throw new OpenIDIllegalUriException(KeycloakOpenIDSettingKeys.KEYCLOAK_URI.key(), providerUri);
        }
        return providerUri;
    }

}
