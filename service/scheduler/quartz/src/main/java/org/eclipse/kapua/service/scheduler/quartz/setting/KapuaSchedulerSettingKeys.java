/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.quartz.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for scheduler service
 * 
 * @since 1.0
 *
 */
public enum KapuaSchedulerSettingKeys implements SettingKey {
    /**
     * The key value in the configuration resources.
     */
    SCHEDULER_KEY("scheduler.key");

    private String key;

    /**
     * Set up the {@code enum} with the key value provided
     * 
     * @param key
     *            The value mapped by this {@link Enum} value
     */
    private KapuaSchedulerSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link KapuaSchedulerSettingKeys}
     * 
     */
    public String key() {
        return key;
    }
}
