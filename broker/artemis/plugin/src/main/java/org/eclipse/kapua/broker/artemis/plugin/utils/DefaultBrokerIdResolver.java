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

import org.apache.activemq.artemis.core.server.ActiveMQServer;

/**
 * Lookup from the broker filter
 *
 * @since 1.0
 */
public class DefaultBrokerIdResolver implements BrokerIdResolver {

    @Override
    public String getBrokerId(ActiveMQServer server) {
        return server.getNodeID().toString();
    }
}
