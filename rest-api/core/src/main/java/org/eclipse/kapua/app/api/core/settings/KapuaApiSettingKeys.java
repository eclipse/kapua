/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.settings;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authorization setting key
 *
 * @since 1.0
 */
public enum KapuaApiSettingKeys implements SettingKey {
    API_KEY("api.key"), //
    API_PATH_PARAM_SCOPEID_WILDCARD("api.path.param.scopeId.wildcard"),
    API_EXCEPTION_STACKTRACE_SHOW("api.exception.stacktrace.show"),
    API_CORS_REFRESH_INTERVAL("api.cors.refresh.interval"),
    API_CORS_ORIGINS_ALLOWED("api.cors.origins.allowed");

    private String key;

    private KapuaApiSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
