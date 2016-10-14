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
 *
 *******************************************************************************/
package org.eclipse.kapua.transport.jms.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for JMS transport level
 *
 * @since 1.0.0
 */
public enum JmsClientSettingKeys implements SettingKey {
    /**
     * The character separator for topic levels.
     * 
     * @since 1.0.0
     */
    TRANSPORT_TOPIC_SEPARATOR("transport.topic.separator"),
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
     * @param key
     *            The value mapped by this {@link enum} value
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
    public String key() {
        return key;
    }
}
