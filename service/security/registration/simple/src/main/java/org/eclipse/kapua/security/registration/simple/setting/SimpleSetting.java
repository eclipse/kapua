/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
