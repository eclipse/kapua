/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

/**
 * {@link SettingKey}s for {@link JobServiceSettings}.
 *
 * @since 2.0.0
 */
public enum JobServiceSettingKeys implements SettingKey {

    /**
     * Max length of {@link JobStepProperty#getPropertyValue()}.
     *
     * @since 2.0.0
     */
    JOB_STEP_PROPERTY_VALUE_LENGTH_MAX("job.step.property.value.length.max");

    /**
     * The key value of the {@link SettingKey}.
     *
     * @since 120.0
     */
    private final String key;

    /**
     * Constructor.
     *
     * @param key The key value of the {@link SettingKey}.
     * @since 2.0.0
     */
    private JobServiceSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
