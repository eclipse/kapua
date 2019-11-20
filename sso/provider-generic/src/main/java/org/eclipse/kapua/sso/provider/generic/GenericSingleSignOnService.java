/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.generic;

import org.eclipse.kapua.sso.exception.uri.SsoIllegalUriException;
import org.eclipse.kapua.sso.provider.AbstractSingleSignOnService;
import org.eclipse.kapua.sso.provider.generic.setting.GenericSsoSetting;
import org.eclipse.kapua.sso.provider.generic.setting.GenericSsoSettingKeys;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;

/**
 * The generic SingleSignOn service class.
 */
public class GenericSingleSignOnService extends AbstractSingleSignOnService {

    private GenericSsoSetting genericSettings;

    public GenericSingleSignOnService() {
        this(SsoSetting.getInstance(), GenericSsoSetting.getInstance());
    }

    public GenericSingleSignOnService(final SsoSetting ssoSettings, final GenericSsoSetting genericSettings) {
        super(ssoSettings);
        this.genericSettings = genericSettings;
    }

    @Override
    protected String getAuthUri() throws SsoIllegalUriException {
        String authUri = genericSettings.getString(GenericSsoSettingKeys.SSO_OPENID_SERVER_ENDPOINT_AUTH);
        if (authUri == null || authUri.isEmpty()) {
            throw new SsoIllegalUriException(GenericSsoSettingKeys.SSO_OPENID_SERVER_ENDPOINT_AUTH.key(), authUri);
        }
        return authUri;
    }

    @Override
    protected String getTokenUri() throws SsoIllegalUriException {
        String tokenUri = genericSettings.getString(GenericSsoSettingKeys.SSO_OPENID_SERVER_ENDPOINT_TOKEN);
        if (tokenUri == null || tokenUri.isEmpty()) {
            throw new SsoIllegalUriException(GenericSsoSettingKeys.SSO_OPENID_SERVER_ENDPOINT_TOKEN.key(), tokenUri);
        }
        return tokenUri;
    }

    @Override
    protected String getLogoutUri() throws SsoIllegalUriException {
        String logoutUri = genericSettings.getString(GenericSsoSettingKeys.SSO_OPENID_SERVER_ENDPOINT_LOGOUT);
        if (logoutUri == null || logoutUri.isEmpty()) {
            throw new SsoIllegalUriException(GenericSsoSettingKeys.SSO_OPENID_SERVER_ENDPOINT_LOGOUT.key(), logoutUri);
        }
        return logoutUri;
    }
}
