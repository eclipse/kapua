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
package org.eclipse.kapua.plugin.sso.openid.provider.generic.jwt;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDIllegalArgumentException;
import org.eclipse.kapua.plugin.sso.openid.provider.jwt.AbstractJwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.provider.generic.setting.GenericOpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.generic.setting.GenericOpenIDSettingKeys;

import java.util.List;

/**
 * The generic JWT Processor.
 */
public class GenericJwtProcessor extends AbstractJwtProcessor {

    public GenericJwtProcessor() throws OpenIDException {
    }

    @Override
    protected List<String> getJwtExpectedIssuers() throws OpenIDIllegalArgumentException {
        List<String> jwtExpectedIssuers = GenericOpenIDSetting.getInstance().getList(String.class, GenericOpenIDSettingKeys.SSO_OPENID_JWT_ISSUER_ALLOWED);
        if (CollectionUtils.isEmpty(jwtExpectedIssuers)) {
            throw new OpenIDIllegalArgumentException(GenericOpenIDSettingKeys.SSO_OPENID_JWT_ISSUER_ALLOWED.key(), (jwtExpectedIssuers == null ? null : ""));
        }
        return jwtExpectedIssuers;
    }

    @Override
    protected List<String> getJwtAudiences() throws OpenIDIllegalArgumentException {
        List<String> jwtAudiences = GenericOpenIDSetting.getInstance().getList(String.class, GenericOpenIDSettingKeys.SSO_OPENID_JWT_AUDIENCE_ALLOWED);
        if (CollectionUtils.isEmpty(jwtAudiences)) {
            throw new OpenIDIllegalArgumentException(GenericOpenIDSettingKeys.SSO_OPENID_JWT_AUDIENCE_ALLOWED.key(), (jwtAudiences == null ? null : ""));
        }
        return jwtAudiences;
    }
}
