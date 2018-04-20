/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    private final static String CANNOT_FIND_IP_ERROR_MSG = "Cannot find the ip. Please check the configuration!";
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
