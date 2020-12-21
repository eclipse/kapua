/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.setting.SettingKey;

public enum JobEngineClientSettingKeys implements SettingKey {

    /**
     * The Base URL.
     */
    JOB_ENGINE_BASE_URL("job.engine.base.url"),

    /**
     * Desired Authorization mode. Allowed values: {@literal trusted}, {@literal access_token}
     */
    JOB_ENGINE_CLIENT_AUTH_MODE("job.engine.client.auth.mode");

    private final String key;

    /**
     * Set up the {@code enum} with the key value provided
     *
     * @param key
     *            The value mapped by this {@link Enum} value
     *
     * @since 1.4.0
     */
    JobEngineClientSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link JobEngineClientSettingKeys}
     *
     * @since 1.4.0
     *
     */
    public String key() {
        return key;
    }

}
