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
package org.eclipse.kapua.broker.artemis.plugin.utils;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSetting;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lookup from the configuration file
 *
 * @since 1.0
 */
public class DefaultBrokerHostResolver implements BrokerHostResolver {

    protected static final Logger logger = LoggerFactory.getLogger(DefaultBrokerHostResolver.class);

    private static final String CANNOT_FIND_IP_ERROR_MSG = "Cannot resolve the broker host. Please check the configuration!";
    private String brokerHost;

    public DefaultBrokerHostResolver() throws KapuaException {
        brokerHost = BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_HOST);
        logger.info("Loaded broker host: {}", brokerHost);
        if (StringUtils.isEmpty(brokerHost)) {
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, CANNOT_FIND_IP_ERROR_MSG);
        }
    }

    @Override
    public String getBrokerHost() {
        return brokerHost;
    }
}
