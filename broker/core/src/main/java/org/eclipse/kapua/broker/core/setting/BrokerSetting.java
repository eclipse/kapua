/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Broker setting implementation.<br>
 * This class handles settings for the {@link BrokerSettingKey}.
 */
public final class BrokerSetting extends AbstractKapuaSetting<BrokerSettingKey> {

    private static final String CONFIG_RESOURCE_NAME = "kapua-broker-setting.properties";

    private static BrokerSetting instance;

    private BrokerSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

    /**
     * Return the broker setting instance (singleton)
     */
    public static BrokerSetting getInstance() {
        synchronized (BrokerSetting.class) {
            if (instance == null) {
                instance = new BrokerSetting();
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
        synchronized (BrokerSetting.class) {
            instance = null;
        }
    }
}
