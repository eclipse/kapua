/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Service authentication implementation.<br>
 * This class handles settings for the {@link ServiceAuthenticationSettingKey}.
 */
public final class ServiceAuthenticationSetting extends AbstractKapuaSetting<ServiceAuthenticationSettingKey> {

    private static final String CONFIG_RESOURCE_NAME = "kapua-service-authentication-setting.properties";

    private static ServiceAuthenticationSetting instance;

    private ServiceAuthenticationSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

    /**
     * Return the service authentication setting instance (singleton)
     */
    public static ServiceAuthenticationSetting getInstance() {
        synchronized (ServiceAuthenticationSetting.class) {
            if (instance == null) {
                instance = new ServiceAuthenticationSetting();
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
        synchronized (ServiceAuthenticationSetting.class) {
            instance = null;
        }
    }
}
