/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.keycloak.jwt;

import org.eclipse.kapua.sso.exception.SsoJwtException;
import org.eclipse.kapua.sso.provider.jwt.AbstractJwtProcessor;
import org.eclipse.kapua.sso.provider.keycloak.KeycloakSingleSignOnUtils;
import org.eclipse.kapua.sso.provider.keycloak.setting.KeycloakSsoSetting;
import org.eclipse.kapua.sso.provider.keycloak.setting.KeycloakSsoSettingKeys;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * The Keycloak JWT Processor.
 */
public class KeycloakJwtProcessor extends AbstractJwtProcessor {

    private static final SsoSetting SSO_SETTING = SsoSetting.getInstance();

    public KeycloakJwtProcessor() throws SsoJwtException {
        super(Duration.ofHours(1));
    }

    @Override
    protected String getOpenIdConfPath(final URI issuer) {
        return issuer.toString() + "/" +
                SsoSetting.getInstance().getString(SsoSettingKeys.SSO_OPENID_CONF_PATH);
    }

    @Override
    protected List<String> getJwtExpectedIssuers() {
        return Collections.singletonList(
                KeycloakSsoSetting.getInstance().getString(KeycloakSsoSettingKeys.KEYCLOAK_URI) +
                        "/auth/realms/" + KeycloakSingleSignOnUtils.getRealm());
    }

    @Override
    protected List<String> getJwtAudiences() {
        return SSO_SETTING.getList(String.class, SsoSettingKeys.SSO_OPENID_CLIENT_ID);
    }
}
