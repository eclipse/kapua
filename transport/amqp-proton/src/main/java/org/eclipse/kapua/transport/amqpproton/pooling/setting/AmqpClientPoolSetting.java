/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.amqpproton.pooling.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to all setting of {@link org.eclipse.kapua.transport.amqpproton.pooling}
 *
 * @since 1.0.0
 */
public class AmqpClientPoolSetting extends AbstractKapuaSetting<AmqpClientPoolSettingKeys> {

    /**
     * Resource file from which source properties.
     * 
     * @since 1.0.0
     */
    private static final String AMQP_CLIENT_POOL_CONFIG_RESOURCE = "amqp-client-pool-setting.properties";

    /**
     * Singleton instance of this {@link Class}.
     * 
     * @since 1.0.0
     */
    private static final AmqpClientPoolSetting INSTANCE = new AmqpClientPoolSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link AmqpClientPoolSetting#AMQP_CLIENT_POOL_CONFIG_RESOURCE} value.
     * 
     * @since 1.0.0
     */
    private AmqpClientPoolSetting() {
        super(AMQP_CLIENT_POOL_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link AmqpClientPoolSetting}.
     * 
     * @return A singleton instance of AmqpClientPoolSetting.
     * @since 1.0.0
     */
    public static AmqpClientPoolSetting getInstance() {
        return INSTANCE;
    }
}
