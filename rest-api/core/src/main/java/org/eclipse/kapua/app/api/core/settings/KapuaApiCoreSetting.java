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
package org.eclipse.kapua.app.api.core.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * API setting implementation.
 *
 * @since 1.0
 */
public class KapuaApiCoreSetting extends AbstractKapuaSetting<KapuaApiCoreSettingKeys> {

    private static final String API_SETTING_RESOURCE = "kapua-api-core-settings.properties";

    private static final KapuaApiCoreSetting INSTANCE = new KapuaApiCoreSetting();

    /**
     * Construct a new api setting reading settings from {@link KapuaApiCoreSetting#API_SETTING_RESOURCE}
     */
    private KapuaApiCoreSetting() {
        super(API_SETTING_RESOURCE);
    }

    /**
     * Return the api setting instance (singleton)
     *
     * @return A singleton instance of {@link KapuaApiCoreSetting}
     */
    public static KapuaApiCoreSetting getInstance() {
        return INSTANCE;
    }
}
