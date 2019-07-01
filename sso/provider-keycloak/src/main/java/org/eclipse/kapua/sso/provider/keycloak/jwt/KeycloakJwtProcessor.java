/*******************************************************************************
 * Copyright (c) 2019 Eurotech and others.
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

import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.sso.provider.jwt.AbstractJwtProcessor;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.List;

public class KeycloakJwtProcessor extends AbstractJwtProcessor {

    final KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();

    // TODO: this property is not linked to Keycloak, but it is more generic. Move it on the generic provider?
    private static final String OPEN_ID_CONFIGURATION_WELL_KNOWN_PATH = ".well-known/openid-configuration";

    public KeycloakJwtProcessor() throws IOException {
        super(Duration.ofHours(1));
    }

    // TODO: also all the override below are not related to Keycloak, maybe I should move them to the generic provider
    //  The question now is: do we really need a KeyloackJwtProcessor? Can I move all to a GenericJwtProcessor?

    @Override
    protected String getOpenIdConfPath(final URI issuer) {
        return issuer.toString() + "/" + OPEN_ID_CONFIGURATION_WELL_KNOWN_PATH;
    }

    // TODO: I don't like using the KapuaAuthenticationSettingKeys for Keycloak...
    @Override
    protected List<String> getJwtExpectedIssuers() {
        return setting.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_ISSUER_ALLOWED);
    }

    @Override
    protected List<String> getJwtAudiences() {
        return setting.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_AUDIENCE_ALLOWED);
    }
}