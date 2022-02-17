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
package org.eclipse.kapua.commons.util;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

/**
 * System utilities.
 *
 * @since 1.0
 *
 */
public class SystemUtils {

    private SystemUtils() {
    }

    /**
     * Get the broker url. Gets the broker schema, host and port from the {@link SystemSetting}
     *
     * @return
     * @throws URISyntaxException
     */
    public static URI getNodeURI()
            throws URISyntaxException {
        SystemSetting envConfig = SystemSetting.getInstance();
        return new URI(envConfig.getString(SystemSettingKey.BROKER_SCHEME),
                null,
                envConfig.getString(SystemSettingKey.BROKER_HOST),
                envConfig.getInt(SystemSettingKey.BROKER_INTERNAL_CONNECTOR_PORT),
                null,
                null,
                null);
    }
}
