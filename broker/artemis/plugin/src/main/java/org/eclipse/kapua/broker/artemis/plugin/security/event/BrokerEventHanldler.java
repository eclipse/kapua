/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.localevent.EventHandler;

public class BrokerEventHanldler extends EventHandler<BrokerEvent> {

    private static BrokerEventHanldler instance;

    public static synchronized BrokerEventHanldler getInstance() {
        if (instance==null) {
            instance = new BrokerEventHanldler();
        }
        return instance;
    }

    private BrokerEventHanldler() {
        super(BrokerEventHanldler.class.getName(), 10, 10);
    }

}
