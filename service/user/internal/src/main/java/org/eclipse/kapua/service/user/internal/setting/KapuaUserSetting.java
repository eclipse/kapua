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
package org.eclipse.kapua.service.user.internal.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to user settings
 *
 * 
 * @since 1.0
 *
 */
public class KapuaUserSetting extends AbstractKapuaSetting<KapuaUserSettingKeys> {

    /**
     * Resource file from which source properties.
     * 
     */
    private static final String USER_SETTING_RESOURCE = "kapua-user-setting.properties";

    /**
     * Singleton instance of this {@link class}.
     * 
     */
    private static final KapuaUserSetting instance = new KapuaUserSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link KapuaUserSettingKeys#USER_KEY} value.
     * 
     */
    private KapuaUserSetting() {
        super(USER_SETTING_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link KapuaUserSetting}.
     * 
     * @return A singleton instance of JmsClientSetting.
     */
    public static KapuaUserSetting getInstance() {
        return instance;
    }
}
