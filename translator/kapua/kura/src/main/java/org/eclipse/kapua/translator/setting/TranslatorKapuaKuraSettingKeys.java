/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.setting;

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@link SettingKey}s for {@link TranslatorKapuaKuraSettings}
 *
 * @since 2.1.0
 */
public enum TranslatorKapuaKuraSettingKeys implements SettingKey {

    /**
     * Whether to resolve the {@link Device#getId()} from the {@link KuraChannel#getClientId()} when converting from {@link KuraDataMessage} to {@link KapuaDataMessage}.
     *
     * @since 2.1.0
     */
    TRANSLATOR_KURA_KAPUA_DATA_DEVICE_ID("translator.kura.kapua.data.deviceId.resolve");

    /**
     * The key value of the {@link SettingKey}.
     *
     * @since 2.1.0
     */
    private final String key;

    /**
     * Constructor.
     *
     * @param key The key value of the {@link SettingKey}.
     * @since 2.1.0
     */
    TranslatorKapuaKuraSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
