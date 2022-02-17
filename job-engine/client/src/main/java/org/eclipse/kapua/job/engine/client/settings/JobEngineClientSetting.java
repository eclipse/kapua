/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.client.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;
import org.eclipse.kapua.job.engine.client.JobEngineServiceClient;

/**
 * {@link JobEngineServiceClient} {@link AbstractKapuaSetting}s
 *
 * @since 1.5.0
 */
public class JobEngineClientSetting extends AbstractKapuaSetting<JobEngineClientSettingKeys> {

    /**
     * Resource file from which source properties.
     */
    private static final String JOB_ENGINE_SETTING_RESOURCE = "job-engine-client-setting.properties";

    /**
     * Singleton instance of this {@link Class}.
     */
    private static final JobEngineClientSetting INSTANCE = new JobEngineClientSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link JobEngineClientSetting#JOB_ENGINE_SETTING_RESOURCE} value.
     */
    private JobEngineClientSetting() {
        super(JOB_ENGINE_SETTING_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link JobEngineClientSetting}.
     *
     * @return A singleton instance of JmsClientSetting.
     */
    public static JobEngineClientSetting getInstance() {
        return INSTANCE;
    }
}
