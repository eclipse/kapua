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

import org.eclipse.kapua.sso.provider.AbstractSingleSignOnService;
import org.eclipse.kapua.sso.provider.generic.setting.GenericSsoSetting;
import org.eclipse.kapua.sso.provider.generic.setting.GenericSsoSettingKeys;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;

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
    protected String getAuthUri() {
        return genericSettings.getString(GenericSsoSettingKeys.SSO_OPENID_SERVER_ENDPOINT_AUTH);
    }

    @Override
    protected String getTokenUri() {
        return genericSettings.getString(GenericSsoSettingKeys.SSO_OPENID_SERVER_ENDPOINT_TOKEN);
    }
}
