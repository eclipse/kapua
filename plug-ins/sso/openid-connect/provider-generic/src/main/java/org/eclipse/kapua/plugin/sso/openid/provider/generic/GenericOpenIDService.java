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
package org.eclipse.kapua.plugin.sso.openid.provider.generic;

import com.google.common.base.Strings;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDIllegalArgumentException;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDIllegalUriException;
import org.eclipse.kapua.plugin.sso.openid.provider.AbstractOpenIDService;
import org.eclipse.kapua.plugin.sso.openid.provider.OpenIDUtils;
import org.eclipse.kapua.plugin.sso.openid.provider.generic.setting.GenericOpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.generic.setting.GenericOpenIDSettingKeys;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;

import java.net.URI;
import java.util.Optional;

/**
 * The generic OpenID service class.
 */
public class GenericOpenIDService extends AbstractOpenIDService {

    private static final String AUTH_WELL_KNOWN_KEY = "authorization_endpoint";
    private static final String LOGOUT_WELL_KNOWN_KEY = "end_session_endpoint";
    private static final String USERINFO_WELL_KNOWN_KEY = "user_info_endpoint";
    private static final String TOKEN_WELL_KNOWN_KEY = "token_endpoint";

    private final GenericOpenIDSetting genericSettings;

    public GenericOpenIDService() {
        this(OpenIDSetting.getInstance(), GenericOpenIDSetting.getInstance());
    }

    public GenericOpenIDService(final OpenIDSetting ssoSettings, final GenericOpenIDSetting genericSettings) {
        super(ssoSettings);
        this.genericSettings = genericSettings;
    }

    @Override
    protected String getAuthUri() throws OpenIDException {
        try {
            final Optional<URI> uri = OpenIDUtils.getConfigUri(AUTH_WELL_KNOWN_KEY, getOpenIdConfPath());
            return uri.orElseThrow(() -> new OpenIDIllegalUriException(AUTH_WELL_KNOWN_KEY, null)).toString();
        } catch (OpenIDException se) {
            String authUri = genericSettings.getString(GenericOpenIDSettingKeys.SSO_OPENID_SERVER_ENDPOINT_AUTH);
            if (Strings.isNullOrEmpty(authUri)) {
                throw se;
            }
            return authUri;
        }
    }

    @Override
    protected String getTokenUri() throws OpenIDException {
        try {
            final Optional<URI> uri = OpenIDUtils.getConfigUri(TOKEN_WELL_KNOWN_KEY, getOpenIdConfPath());
            return uri.orElseThrow(() -> new OpenIDIllegalUriException(TOKEN_WELL_KNOWN_KEY, null)).toString();
        } catch (OpenIDException se) {
            String tokenUri = genericSettings.getString(GenericOpenIDSettingKeys.SSO_OPENID_SERVER_ENDPOINT_TOKEN);
            if (Strings.isNullOrEmpty(tokenUri)) {
                throw se;
            }
            return tokenUri;
        }
    }

    @Override
    protected String getUserInfoUri() throws OpenIDException {
        try {
            final Optional<URI> uri = OpenIDUtils.getConfigUri(USERINFO_WELL_KNOWN_KEY, getOpenIdConfPath());
            return uri.orElseThrow(() -> new OpenIDIllegalUriException(USERINFO_WELL_KNOWN_KEY, null)).toString();
        } catch (OpenIDException se) {
            String tokenUri = genericSettings.getString(GenericOpenIDSettingKeys.SSO_OPENID_SERVER_ENDPOINT_USERINFO);
            if (Strings.isNullOrEmpty(tokenUri)) {
                throw se;
            }
            return tokenUri;
        }
    }

    @Override
    protected String getLogoutUri() throws OpenIDException {
        try {
            final Optional<URI> uri = OpenIDUtils.getConfigUri(LOGOUT_WELL_KNOWN_KEY, getOpenIdConfPath());
            return uri.orElseThrow(() -> new OpenIDIllegalUriException(LOGOUT_WELL_KNOWN_KEY, null)).toString();
        } catch (OpenIDException se) {
            String logoutUri = genericSettings.getString(GenericOpenIDSettingKeys.SSO_OPENID_SERVER_ENDPOINT_LOGOUT);
            if (Strings.isNullOrEmpty(logoutUri)) {
                throw se;
            }
            return logoutUri;
        }
    }

    /**
     * Get the OpenID configuration path.
     *
     * @return a String representing the OpenID configuration URL.
     * @throws OpenIDIllegalArgumentException if it cannot retrieve the OpenID configuration path
     */
    private String getOpenIdConfPath() throws OpenIDIllegalArgumentException {
        String issuerUri = genericSettings.getString(GenericOpenIDSettingKeys.SSO_OPENID_JWT_ISSUER_ALLOWED);
        if (Strings.isNullOrEmpty(issuerUri)) {
            throw new OpenIDIllegalUriException(GenericOpenIDSettingKeys.SSO_OPENID_JWT_ISSUER_ALLOWED.key(), issuerUri);
        }
        return OpenIDUtils.getOpenIdConfPath(issuerUri);
    }
}
