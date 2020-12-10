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
public enum KapuaApiCoreSettingKeys implements SettingKey {
    API_PATH_PARAM_SCOPEID_WILDCARD("api.path.param.scopeId.wildcard"),
    API_EXCEPTION_STACKTRACE_SHOW("api.exception.stacktrace.show");

    private final String key;

    private KapuaApiCoreSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
