/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.plugin.security.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

import javax.inject.Inject;

/**
 * Broker setting implementation.<br>
 * This class handles settings for the {@link BrokerSettingKey}.
 */
public final class BrokerSetting extends AbstractKapuaSetting<BrokerSettingKey> {

    private static final String CONFIG_RESOURCE_NAME = "kapua-broker-setting.properties";

    @Inject
    public BrokerSetting() {
        super(CONFIG_RESOURCE_NAME);
    }

    /**
     * Allow re-setting the global instance
     * <p>
     * This method forces the reload of the settings.
     * </p>
     * <p>
     * This may be helpful for unit tests which need to change system properties for testing
     * different behaviors.
     * </p>
     */
    public void resetInstance() {
        reset(CONFIG_RESOURCE_NAME);
    }
}
