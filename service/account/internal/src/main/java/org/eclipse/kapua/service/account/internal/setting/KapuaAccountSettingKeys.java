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
package org.eclipse.kapua.service.account.internal.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for account service
 * 
 * @since 1.0
 *
 */
public enum KapuaAccountSettingKeys implements SettingKey {
    /**
     * The key value in the configuration resources.
     */
    ACCOUNT_KEY("account.key"),
    ACCOUNT_EVENT_ADDRESS("account.eventAddress");

    private String key;

    /**
     * Set up the {@code enum} with the key value provided
     * 
     * @param key
     *            The value mapped by this {@link Enum} value
     */
    private KapuaAccountSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link KapuaAccountSettingKeys}
     * 
     */
    public String key() {
        return key;
    }
}
