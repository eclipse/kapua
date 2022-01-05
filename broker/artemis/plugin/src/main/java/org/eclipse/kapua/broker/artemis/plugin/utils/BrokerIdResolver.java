/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
 * Resolves the broker id.
 *
 * @since 1.0
 */
public interface BrokerIdResolver {

    /**
     * Resolve the broker id
     *
     * @param server
     * @return
     */
    String getBrokerId(ActiveMQServer server);

}