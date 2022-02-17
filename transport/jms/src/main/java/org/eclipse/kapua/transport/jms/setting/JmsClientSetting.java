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

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to all setting of {@link org.eclipse.kapua.transport.jms}
 *
 * @since 1.0.0
 */
public class JmsClientSetting extends AbstractKapuaSetting<JmsClientSettingKeys> {

    /**
     * Resource file from which source properties.
     *
     * @since 1.0.0
     */
    private static final String JMS_CLIENT_CONFIG_RESOURCE = "jms-client-setting.properties";

    /**
     * Singleton instance of this {@link Class}.
     *
     * @since 1.0.0
     */
    private static final JmsClientSetting INSTANCE = new JmsClientSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link JmsClientSetting#JMS_CLIENT_CONFIG_RESOURCE} value.
     *
     * @since 1.0.0
     */
    private JmsClientSetting() {
        super(JMS_CLIENT_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link JmsClientSetting}.
     *
     * @return A singleton instance of JmsClientSetting.
     * @since 1.0.0
     */
    public static JmsClientSetting getInstance() {
        return INSTANCE;
    }
}
