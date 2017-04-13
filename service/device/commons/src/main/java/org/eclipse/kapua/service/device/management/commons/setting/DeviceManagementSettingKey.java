/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for device management service
 * 
 * @since 1.0
 *
 */
public enum DeviceManagementSettingKey implements SettingKey
{

    /**
     * Character encoding
     */
    CHAR_ENCODING("character.encoding"),

    /**
     * Request timeout
     */
    REQUEST_TIMEOUT("request.timeout");

    private String key;

    /**
     * Constructor
     * 
     * @param key
     */
    private DeviceManagementSettingKey(String key)
    {
        this.key = key;
    }

    @Override
    public String key()
    {
        return key;
    }
}
