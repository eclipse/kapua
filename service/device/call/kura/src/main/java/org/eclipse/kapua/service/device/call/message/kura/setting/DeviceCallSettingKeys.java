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
package org.eclipse.kapua.service.device.call.message.kura.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for device call service
 * 
 * @since 1.0
 *
 */
public enum DeviceCallSettingKeys implements SettingKey {

    /**
     * Destination reply part
     */
    DESTINATION_REPLY_PART("destination.reply.part");

    private String key;

    private DeviceCallSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
