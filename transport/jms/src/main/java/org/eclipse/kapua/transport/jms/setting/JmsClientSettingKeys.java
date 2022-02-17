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
package org.eclipse.kapua.transport.jms.setting;

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.transport.message.jms.JmsPayload;

/**
 * Available settings key for JMS transport level
 *
 * @since 1.0.0
 */
public enum JmsClientSettingKeys implements SettingKey {

    /**
     * The max length of the {@link JmsPayload#getBody()}} used when invoking {@link JmsPayload#toString()}
     *
     * @since 1.2.0
     */
    PAYLOAD_TOSTRING_LENGTH("transport.jms.payload.body.toString.length"),

    /**
     * The character separator for topic levels.
     *
     * @since 1.0.0
     */
    TRANSPORT_TOPIC_SEPARATOR("transport.jms.topic.separator"),

    ;

    /**
     * The key value in the configuration resources.
     *
     * @since 1.0.0
     */
    private String key;

    /**
     * Set up the {@code enum} with the key value provided
     *
     * @param key The value mapped by this {@link Enum} value
     * @since 1.0.0
     */
    private JmsClientSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link JmsClientSettingKeys}
     *
     * @since 1.0.0
     */
    @Override
    public String key() {
        return key;
    }
}
