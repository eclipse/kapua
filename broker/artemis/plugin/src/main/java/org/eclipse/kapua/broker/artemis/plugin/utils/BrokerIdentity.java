/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSetting;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSettingKey;
import org.eclipse.kapua.commons.util.ReflectionUtil;

//TODO find a better way to share this singleton between SecurityPlugin and ServerPlugin
public class BrokerIdentity {

    private static final BrokerIdentity INSTANCE = new BrokerIdentity();

    private String brokerId;
    private String brokerHost;

    private BrokerIdentity() {
    }

    public static BrokerIdentity getInstance() {
        return INSTANCE;
    }

    //TODO find a way to inject these classes
    public synchronized void init(ActiveMQServer server) throws KapuaException {
        BrokerIdResolver brokerIdResolver =
            ReflectionUtil.newInstance(BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_ID_RESOLVER_CLASS_NAME), DefaultBrokerIdResolver.class);
        brokerId = brokerIdResolver.getBrokerId(server);
        BrokerHostResolver brokerIpResolver =
            ReflectionUtil.newInstance(BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_HOST_RESOLVER_CLASS_NAME), DefaultBrokerHostResolver.class);
        brokerHost = brokerIpResolver.getBrokerHost();
        brokerId = BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_ID);
        brokerHost = BrokerSetting.getInstance().getString(BrokerSettingKey.BROKER_HOST);
    }

    public String getBrokerId() {
        return brokerId;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

}
