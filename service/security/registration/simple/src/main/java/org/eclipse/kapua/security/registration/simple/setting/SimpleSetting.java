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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.security.registration.simple.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Authorization setting implementation.
 */
public class SimpleSetting extends AbstractKapuaSetting<SimpleSettingKeys> {

    private static final String SETTING_RESOURCE = "kapua-security-registration-simple-setting.properties";

    private static final SimpleSetting INSTANCE = new SimpleSetting();

    /**
     * Construct a new setting reading settings from {@link SimpleSetting#SETTING_RESOURCE}
     */
    private SimpleSetting() {
        super(SETTING_RESOURCE);
    }

    /**
     * Return the setting instance (singleton)
     *
     * @return the settings instance
     */
    public static SimpleSetting getInstance() {
        return INSTANCE;
    }
}
