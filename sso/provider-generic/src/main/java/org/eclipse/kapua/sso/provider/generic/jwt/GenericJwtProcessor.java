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
package org.eclipse.kapua.sso.provider.generic.jwt;

import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.sso.exception.SsoJwtException;
import org.eclipse.kapua.sso.provider.jwt.AbstractJwtProcessor;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;

import java.net.URI;
import java.time.Duration;
import java.util.List;

/**
 * The generic JWT Processor.
 */
public class GenericJwtProcessor extends AbstractJwtProcessor {

    private static final KapuaAuthenticationSetting SETTING = KapuaAuthenticationSetting.getInstance();

    public GenericJwtProcessor() throws SsoJwtException {
        super(Duration.ofHours(1));
    }

    @Override
    protected String getOpenIdConfPath(final URI issuer) {
        return issuer.toString() + "/" +
                SsoSetting.getInstance().getString(SsoSettingKeys.SSO_OPENID_CONF_PATH);
    }

    @Override
    protected List<String> getJwtExpectedIssuers() {
        // TODO: I don't like using KapuaAuthenticationSettingKeys (this also forces to add kapua-security-shiro in the
        //  dependencies). Move these properties under SSO?
        return SETTING.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_ISSUER_ALLOWED);
    }

    @Override
    protected List<String> getJwtAudiences() {
        // TODO: I don't like using KapuaAuthenticationSettingKeys (this also forces to add kapua-security-shiro in the
        //  dependencies). Move these properties under SSO?
        return SETTING.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_AUDIENCE_ALLOWED);
    }
}
