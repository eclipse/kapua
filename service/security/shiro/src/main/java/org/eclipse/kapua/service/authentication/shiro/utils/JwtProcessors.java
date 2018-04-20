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
package org.eclipse.kapua.service.authentication.shiro.utils;

import java.time.Duration;
import java.util.List;

import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.sso.jwt.JwtProcessor;

public final class JwtProcessors {

    private JwtProcessors() {
    }

    public static JwtProcessor createDefault() {
        final KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
        final List<String> audiences = setting.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_AUDIENCE_ALLOWED);
        final List<String> expectedIssuers = setting.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_ISSUER_ALLOWED);

        return new JwtProcessor(audiences, expectedIssuers, Duration.ofHours(1));
    }
}
