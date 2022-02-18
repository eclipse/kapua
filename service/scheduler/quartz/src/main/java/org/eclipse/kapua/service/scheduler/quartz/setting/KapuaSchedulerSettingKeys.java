/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.quartz.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for scheduler service
 *
 * @since 1.0.0
 */
public enum KapuaSchedulerSettingKeys implements SettingKey {
    /**
     * The key value in the configuration resources.
     */
    SCHEDULER_KEY("scheduler.key");

    private final String key;

    /**
     * Set up the {@code enum} with the key value provided
     *
     * @param key The value mapped by this {@link Enum} value
     */
    KapuaSchedulerSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link KapuaSchedulerSettingKeys}
     */
    @Override
    public String key() {
        return key;
    }
}
