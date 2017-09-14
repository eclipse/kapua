/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
    API_PATH_PARAM_SCOPEID_WILDCARD("api.path.param.scopeId.wildcard"),;

    private String key;

    private KapuaApiSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
