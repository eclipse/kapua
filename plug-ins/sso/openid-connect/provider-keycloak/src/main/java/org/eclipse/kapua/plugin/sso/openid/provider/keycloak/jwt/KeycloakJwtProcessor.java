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
package org.eclipse.kapua.plugin.sso.openid.provider.keycloak.jwt;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDIllegalArgumentException;
import org.eclipse.kapua.plugin.sso.openid.provider.jwt.AbstractJwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.provider.keycloak.KeycloakOpenIDUtils;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSettingKeys;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * The Keycloak JWT Processor.
 */
public class KeycloakJwtProcessor extends AbstractJwtProcessor {

    private final KeycloakOpenIDUtils keycloakOpenIDUtils;

    @Inject
    public KeycloakJwtProcessor(OpenIDSetting openIDSetting, KeycloakOpenIDUtils keycloakOpenIDUtils) throws OpenIDException {
        super(openIDSetting);
        this.keycloakOpenIDUtils = keycloakOpenIDUtils;
    }

    @Override
    protected List<String> getJwtExpectedIssuers() throws OpenIDIllegalArgumentException {
        return Collections.singletonList(
                keycloakOpenIDUtils.getProviderUri() + KeycloakOpenIDUtils.KEYCLOAK_URI_COMMON_PART +
                        keycloakOpenIDUtils.getRealm());
    }

    @Override
    protected List<String> getJwtAudiences() throws OpenIDIllegalArgumentException {
        List<String> jwtAudiences = openIDSetting.getList(String.class, OpenIDSettingKeys.SSO_OPENID_CLIENT_ID);
        if (CollectionUtils.isEmpty(jwtAudiences)) {
            throw new OpenIDIllegalArgumentException(OpenIDSettingKeys.SSO_OPENID_CLIENT_ID.key(), (jwtAudiences == null ? null : ""));
        }
        return jwtAudiences;
    }

    @Override
    public String getId() {
        return "keycloak";
    }
}
