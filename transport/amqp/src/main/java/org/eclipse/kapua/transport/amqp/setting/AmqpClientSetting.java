/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.amqp.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to all setting of {@link org.eclipse.kapua.transport.amqp}
 *
 * @since 1.6.0
 */
public class AmqpClientSetting extends AbstractKapuaSetting<AmqpClientSettingKeys> {

    /**
     * Resource file from which source properties.
     * 
     * @since 1.6.0
     */
    private static final String AMQP_CLIENT_CONFIG_RESOURCE = "amqp-client-setting.properties";

    /**
     * Singleton instance of this {@link Class}.
     * 
     * @since 1.6.0
     */
    private static final AmqpClientSetting INSTANCE = new AmqpClientSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link AmqpClientSetting#AMQP_CLIENT_CONFIG_RESOURCE} value.
     * 
     * @since 1.6.0
     */
    private AmqpClientSetting() {
        super(AMQP_CLIENT_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link AmqpClientSetting}.
     * 
     * @return A singleton instance of JmsClientSetting.
     * @since 1.6.0
     */
    public static AmqpClientSetting getInstance() {
        return INSTANCE;
    }
}
