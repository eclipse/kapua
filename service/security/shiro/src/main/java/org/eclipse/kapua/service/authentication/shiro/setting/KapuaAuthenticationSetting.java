/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

import javax.inject.Singleton;

/**
 * Authentication setting implementation.
 *
 * @since 1.0
 */
@Singleton
public class KapuaAuthenticationSetting extends AbstractKapuaSetting<KapuaAuthenticationSettingKeys> {

    private static final String AUTHENTICATION_CONFIG_RESOURCE = "kapua-authentication-setting.properties";

    /**
     * Construct a new authentication setting reading settings from {@link KapuaAuthenticationSetting#AUTHENTICATION_CONFIG_RESOURCE}
     */
    public KapuaAuthenticationSetting() {
        super(AUTHENTICATION_CONFIG_RESOURCE);
    }
}
