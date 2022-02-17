/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.esmigrator.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class EsMigratorSetting extends AbstractKapuaSetting<EsMigratorSettingKey> {

    private static final String CONFIG_RESOURCE_NAME = "es-migrator-setting.properties";

    private static EsMigratorSetting instance;

    private EsMigratorSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

    /**
     * Return the broker setting instance (singleton)
     */
    public static EsMigratorSetting getInstance() {
        synchronized (EsMigratorSetting.class) {
            if (instance == null) {
                instance = new EsMigratorSetting();
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
        synchronized (EsMigratorSetting.class) {
            instance = null;
        }
    }

}
