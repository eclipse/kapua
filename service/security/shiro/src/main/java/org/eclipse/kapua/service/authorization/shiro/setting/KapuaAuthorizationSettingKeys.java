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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authorization setting key
 */
public enum KapuaAuthorizationSettingKeys implements SettingKey {
    AUTHORIZATION_KEY("authorization.key"),
    AUTHORIZATION_EVENT_ADDRESS("authorization.eventAddress");

    private String key;

    private KapuaAuthorizationSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
