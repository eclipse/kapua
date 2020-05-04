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

import org.eclipse.kapua.sso.exception.SsoJwtException;
import org.eclipse.kapua.sso.provider.generic.setting.GenericSsoSetting;
import org.eclipse.kapua.sso.provider.generic.setting.GenericSsoSettingKeys;
import org.eclipse.kapua.sso.provider.jwt.AbstractJwtProcessor;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;

import java.net.URI;
import java.util.List;

/**
 * The generic JWT Processor.
 */
public class GenericJwtProcessor extends AbstractJwtProcessor {

    public GenericJwtProcessor() throws SsoJwtException {
    }

    @Override
    protected String getOpenIdConfPath(final URI issuer) {
        return issuer.toString() + "/" +
                SsoSetting.getInstance().getString(SsoSettingKeys.SSO_OPENID_CONF_PATH);
    }

    @Override
    protected List<String> getJwtExpectedIssuers() {
        return GenericSsoSetting.getInstance().getList(String.class, GenericSsoSettingKeys.SSO_OPENID_JWT_ISSUER_ALLOWED);
    }

    @Override
    protected List<String> getJwtAudiences() {
        return GenericSsoSetting.getInstance().getList(String.class, GenericSsoSettingKeys.SSO_OPENID_JWT_AUDIENCE_ALLOWED);
    }
}
