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
     * Singleton instance of this {@link Class}.
     *
     */
    private static final KapuaUserSetting INSTANCE = new KapuaUserSetting();

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
        return INSTANCE;
    }
}
