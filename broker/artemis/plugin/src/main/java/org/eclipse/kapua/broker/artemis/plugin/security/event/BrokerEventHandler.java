/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.artemis.plugin.security.event;

import org.eclipse.kapua.commons.localevent.EventHandler;

public class BrokerEventHandler extends EventHandler<BrokerEvent> {

    private static BrokerEventHandler instance;

    public static synchronized BrokerEventHandler getInstance() {
        if (instance==null) {
            instance = new BrokerEventHandler();
        }
        return instance;
    }

    private BrokerEventHandler() {
        super(BrokerEventHandler.class.getName(), 10, 10);
    }

}