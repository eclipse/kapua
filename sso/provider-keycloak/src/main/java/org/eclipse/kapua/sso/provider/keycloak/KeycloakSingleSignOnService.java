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
package org.eclipse.kapua.sso.provider.keycloak;

import org.eclipse.kapua.sso.provider.AbstractSingleSignOnService;
import org.eclipse.kapua.sso.provider.keycloak.setting.KeycloakSsoSetting;
import org.eclipse.kapua.sso.provider.keycloak.setting.KeycloakSsoSettingKeys;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;

public class KeycloakSingleSignOnService extends AbstractSingleSignOnService {

    private KeycloakSsoSetting keycloakSettings;

    public KeycloakSingleSignOnService() {
        this(SsoSetting.getInstance(), KeycloakSsoSetting.getInstance());
    }

    public KeycloakSingleSignOnService(final SsoSetting ssoSettings, final KeycloakSsoSetting keycloakSettings) {
        super(ssoSettings);
        this.keycloakSettings = keycloakSettings;
    }

    protected String getRealm() {
        return keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_REALM, "master");
    }

    @Override
    protected String getAuthUri() {
        return keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_URI) + "/auth/realms/" + getRealm() + "/protocol/openid-connect/auth";
    }
    
    @Override
    protected String getTokenUri() {
        return keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_URI) + "/auth/realms/" + getRealm() + "/protocol/openid-connect/token";
    }


}
