/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Broker setting implementation.<br>
 * This class handles settings for the {@link BrokerSettingKey}.
 */
public class BrokerSetting extends AbstractKapuaSetting<BrokerSettingKey> {

    private static final String CONFIG_RESOURCE_NAME = "kapua-broker-setting.properties";

    private static final BrokerSetting instance = new BrokerSetting();

    private BrokerSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

    /**
     * Return the broker setting instance (singleton)
     */
    public static BrokerSetting getInstance() {
        return instance;
    }
}
