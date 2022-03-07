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

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Service authentication settings
 */
public enum ServiceAuthenticationSettingKey implements SettingKey {
    /**
     * enable/disable the raising of connect/disconnect event
     */
    SERVICE_AUTHENTICATION_ENABLE_LIFECYCLE_EVENTS("service.authentication.enable_lifecycle_events"),
    /**
     * Lifecycle events publishing address
     */
    SERVICE_AUTHENTICATION_LIFECYCLE_EVENTS_ADDRESS("service.authentication.lifecycle_events_address");

    private String key;

    private ServiceAuthenticationSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
