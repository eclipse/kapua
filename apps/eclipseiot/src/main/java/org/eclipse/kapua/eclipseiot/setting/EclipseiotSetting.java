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
package org.eclipse.kapua.eclipseiot.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Eclipseiot setting implementation.<br>
 * This class handles settings for the {@link EclipseiotSettingKey}.
 */
public final class EclipseiotSetting extends AbstractKapuaSetting<EclipseiotSettingKey> {

    private static final String CONFIG_RESOURCE_NAME = "eclipseiot-setting.properties";

    private static EclipseiotSetting instance;

    private EclipseiotSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

    /**
     * Return the Eclipseiot setting instance (singleton)
     */
    public static EclipseiotSetting getInstance() {
        synchronized (EclipseiotSetting.class) {
            if (instance == null) {
                instance = new EclipseiotSetting();
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
        synchronized (EclipseiotSetting.class) {
            instance = null;
        }
    }
}
