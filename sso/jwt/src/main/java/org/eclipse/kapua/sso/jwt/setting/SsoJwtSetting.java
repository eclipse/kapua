/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.jwt.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class SsoJwtSetting extends AbstractKapuaSetting<SsoJwtSettingKeys> {
    private static final String SSO_JWT_SETTING_RESOURCE = "sso-jwt-setting.properties";

    private static final SsoJwtSetting INSTANCE = new SsoJwtSetting();

    private SsoJwtSetting() {
        super(SSO_JWT_SETTING_RESOURCE);
    }

    SsoJwtSetting(final String resourceName) {
        super(resourceName);
    }

    public static SsoJwtSetting getInstance() {
        return INSTANCE;
    }

}
