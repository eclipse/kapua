/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;

/**
 * Lookup from the configuration file
 *
 * @since 1.0
 */
public class DefaultBrokerIpResolver implements BrokerIpResolver {

    private static final String CANNOT_FIND_IP_ERROR_MSG = "Cannot find the ip. Please check the configuration!";
    private String brokerIpOrHostName;

    public DefaultBrokerIpResolver() throws KapuaException {
        brokerIpOrHostName = BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_IP);
        if (StringUtils.isEmpty(brokerIpOrHostName)) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, CANNOT_FIND_IP_ERROR_MSG);
        }
    }

    @Override
    public String getBrokerIpOrHostName() {
        return brokerIpOrHostName;
    }
}
