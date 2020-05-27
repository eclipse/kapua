/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * API setting implementation.
 *
 * @since 1.0
 */
public class KapuaApiSetting extends AbstractKapuaSetting<KapuaApiSettingKeys> {

    private static final String API_SETTING_RESOURCE = "kapua-api-settings.properties";

    private static final KapuaApiSetting INSTANCE = new KapuaApiSetting();

    /**
     * Construct a new api setting reading settings from {@link KapuaApiSetting#API_SETTING_RESOURCE}
     */
    private KapuaApiSetting() {
        super(API_SETTING_RESOURCE);
    }

    /**
     * Return the api setting instance (singleton)
     *
     * @return A singleton instance of {@link KapuaApiSetting}
     */
    public static KapuaApiSetting getInstance() {
        return INSTANCE;
    }
}
