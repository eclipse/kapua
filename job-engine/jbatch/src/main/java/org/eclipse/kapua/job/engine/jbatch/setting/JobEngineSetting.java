/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.jbatch.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * {@link org.eclipse.kapua.job.engine.jbatch.JobEngineServiceJbatch} setting implementation.
 */
public class JobEngineSetting extends AbstractKapuaSetting<JobEngineSettingKeys> {

    private static final String JOB_ENGINE_SETTING_RESOURCE = "job-engine-setting.properties";

    private static final JobEngineSetting INSTANCE = new JobEngineSetting();

    /**
     * Construct a new job engine setting reading settings from {@link JobEngineSetting#JOB_ENGINE_SETTING_RESOURCE}
     */
    private JobEngineSetting() {
        super(JOB_ENGINE_SETTING_RESOURCE);
    }

    /**
     * Return the job engine setting instance (singleton)
     *
     * @return
     */
    public static JobEngineSetting getInstance() {
        return INSTANCE;
    }
}
