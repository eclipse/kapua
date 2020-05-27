/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.generic.jwt;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.kapua.sso.exception.SsoException;
import org.eclipse.kapua.sso.exception.SsoIllegalArgumentException;
import org.eclipse.kapua.sso.provider.jwt.AbstractJwtProcessor;
import org.eclipse.kapua.sso.provider.generic.setting.GenericSsoSetting;
import org.eclipse.kapua.sso.provider.generic.setting.GenericSsoSettingKeys;

import java.util.List;

/**
 * The generic JWT Processor.
 */
public class GenericJwtProcessor extends AbstractJwtProcessor {

    public GenericJwtProcessor() throws SsoException {
    }

    @Override
    protected List<String> getJwtExpectedIssuers() throws SsoIllegalArgumentException {
        List<String> jwtExpectedIssuers = GenericSsoSetting.getInstance().getList(String.class, GenericSsoSettingKeys.SSO_OPENID_JWT_ISSUER_ALLOWED);
        if (CollectionUtils.isEmpty(jwtExpectedIssuers)) {
            throw new SsoIllegalArgumentException(GenericSsoSettingKeys.SSO_OPENID_JWT_ISSUER_ALLOWED.key(), (jwtExpectedIssuers == null ? null : ""));
        }
        return jwtExpectedIssuers;
    }

    @Override
    protected List<String> getJwtAudiences() throws SsoIllegalArgumentException {
        List<String> jwtAudiences = GenericSsoSetting.getInstance().getList(String.class, GenericSsoSettingKeys.SSO_OPENID_JWT_AUDIENCE_ALLOWED);
        if (CollectionUtils.isEmpty(jwtAudiences)) {
            throw new SsoIllegalArgumentException(GenericSsoSettingKeys.SSO_OPENID_JWT_AUDIENCE_ALLOWED.key(), (jwtAudiences == null ? null : ""));
        }
        return jwtAudiences;
    }
}
