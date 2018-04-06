/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector.hono.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * ConnectorHono setting implementation.<br>
 * This class handles settings for the {@link ConnectorHonoSettingKey}.
 */
public final class ConnectorHonoSetting extends AbstractKapuaSetting<ConnectorHonoSettingKey> {

    private static final String CONFIG_RESOURCE_NAME = "kapua-connector-hono-setting.properties";

    private static ConnectorHonoSetting instance;

    private ConnectorHonoSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

    /**
     * Return the Eclipseiot setting instance (singleton)
     */
    public static ConnectorHonoSetting getInstance() {
        synchronized (ConnectorHonoSetting.class) {
            if (instance == null) {
                instance = new ConnectorHonoSetting();
            }
            return instance;
        }
    }

    /**
     * Allow re-setting the global instance
     * <p>
     * This method clears out the internal global instance in order to let the next call
     * to {@link #getInstance()} return a fresh instance.
     * </p>
     * <p>
     * This may be helpful for unit tests which need to change system properties for testing
     * different behaviors.
     * </p>
     */
    public static void resetInstance() {
        synchronized (ConnectorHonoSetting.class) {
            instance = null;
        }
    }
}
