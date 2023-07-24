/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.internal.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * {@link AbstractKapuaSetting} for {@code kapua-job-internal} module.
 *
 * @see AbstractKapuaSetting
 * @since 2.0.0
 */
public class JobServiceSettings extends AbstractKapuaSetting<JobServiceSettingKeys> {

    /**
     * Setting filename.
     *
     * @since 2.0.0
     */
    private static final String JOB_SERVICE_SETTING_RESOURCE = "job-service-settings.properties";

    /**
     * Singleton instance.
     *
     * @since 2.0.0
     */
    private static final JobServiceSettings INSTANCE = new JobServiceSettings();

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    private JobServiceSettings() {
        super(JOB_SERVICE_SETTING_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link JobServiceSettings}.
     *
     * @return A singleton instance of {@link JobServiceSettings}.
     * @since 2.0.0
     */
    public static JobServiceSettings getInstance() {
        return INSTANCE;
    }
}
