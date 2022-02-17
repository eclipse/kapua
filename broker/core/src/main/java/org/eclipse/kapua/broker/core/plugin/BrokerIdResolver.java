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

import org.apache.activemq.broker.BrokerFilter;

/**
 * Resolves the broker id.
 *
 * @since 1.0
 */
public interface BrokerIdResolver {

    /**
     * Resolve the broker id
     *
     * @param brokerFilter
     * @return
     */
    String getBrokerId(BrokerFilter brokerFilter);

}
