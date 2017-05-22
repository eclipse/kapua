/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Authentication setting implementation.
 * 
 * @since 1.0
 *
 */
public class KapuaAuthenticationSetting extends AbstractKapuaSetting<KapuaAuthenticationSettingKeys> {

    private static final String AUTHENTICATION_CONFIG_RESOURCE = "kapua-authentication-setting.properties";

    private static final KapuaAuthenticationSetting INSTANCE = new KapuaAuthenticationSetting();

    /**
     * Construct a new authentication setting reading settings from {@link KapuaAuthenticationSetting#AUTHENTICATION_CONFIG_RESOURCE}
     */
    private KapuaAuthenticationSetting() {
        super(AUTHENTICATION_CONFIG_RESOURCE);
    }

    /**
     * Return the authentication setting instance (singleton)
     * 
     * @return
     */
    public static KapuaAuthenticationSetting getInstance() {
        return INSTANCE;
    }
}
