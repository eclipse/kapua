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
package org.eclipse.kapua.service.account.internal.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to account settings
 *
 * @since 1.0
 *
 */
public class KapuaAccountSetting extends AbstractKapuaSetting<KapuaAccountSettingKeys> {

    /**
     * Resource file from which source properties.
     *
     */
    private static final String ACCOUNT_CONFIG_RESOURCE = "kapua-account-setting.properties";

    private static final KapuaAccountSetting INSTANCE = new KapuaAccountSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link KapuaAccountSettingKeys#ACCOUNT_KEY} value.
     *
     */
    private KapuaAccountSetting() {
        super(ACCOUNT_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link KapuaAccountSetting}.
     *
     * @return A singleton instance of KapuaAccountSetting.
     */
    public static KapuaAccountSetting getInstance() {
        return INSTANCE;
    }
}
