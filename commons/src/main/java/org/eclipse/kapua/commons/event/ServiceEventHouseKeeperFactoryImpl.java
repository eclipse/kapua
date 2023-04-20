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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.commons.service.event.store.api.EventStoreService;
import org.eclipse.kapua.storage.TxManager;

import java.util.List;

public class ServiceEventHouseKeeperFactoryImpl implements ServiceEventHouseKeeperFactory {

    private final EventStoreService eventStoreService;
    private final TxManager txManager;

    public ServiceEventHouseKeeperFactoryImpl(EventStoreService eventStoreService, TxManager txManager) {
        this.eventStoreService = eventStoreService;
        this.txManager = txManager;
    }

    @Override
    public ServiceEventTransactionalHousekeeper apply(List<ServiceEntry> servicesEntryList) {
        return new ServiceEventTransactionalHousekeeper(eventStoreService, txManager, servicesEntryList);
    }
}
