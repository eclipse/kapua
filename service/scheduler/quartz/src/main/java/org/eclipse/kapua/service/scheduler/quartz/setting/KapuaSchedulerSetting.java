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

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to scheduler settings
 *
 * @since 1.0.0
 */
public class KapuaSchedulerSetting extends AbstractKapuaSetting<KapuaSchedulerSettingKeys> {

    /**
     * Resource file from which source properties.
     */
    private static final String SCHEDULER_CONFIG_RESOURCE = "kapua-scheduler-setting.properties";

    private static final KapuaSchedulerSetting INSTANCE = new KapuaSchedulerSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link KapuaSchedulerSettingKeys#SCHEDULER_KEY} value.
     */
    private KapuaSchedulerSetting() {
        super(SCHEDULER_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link KapuaSchedulerSetting}.
     *
     * @return A singleton instance of KapuaSchedulerSetting.
     */
    public static KapuaSchedulerSetting getInstance() {
        return INSTANCE;
    }
}
