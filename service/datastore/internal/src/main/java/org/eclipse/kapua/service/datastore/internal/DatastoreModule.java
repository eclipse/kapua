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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.datastore.ChannelInfoFactory;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoFactory;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.MessageStoreFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoFactory;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;

public class DatastoreModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(ChannelInfoFactory.class).to(ChannelInfoFactoryImpl.class);
        bind(ChannelInfoRegistryService.class).to(ChannelInfoRegistryServiceImpl.class);
        bind(ClientInfoFactory.class).to(ClientInfoFactoryImpl.class);
        bind(ClientInfoRegistryService.class).to(ClientInfoRegistryServiceImpl.class);
        bind(MessageStoreFactory.class).to(MessageStoreFactoryImpl.class);
        bind(MessageStoreService.class).to(MessageStoreServiceImpl.class);
        bind(MetricInfoFactory.class).to(MetricInfoFactoryImpl.class);
        bind(MetricInfoRegistryService.class).to(MetricInfoRegistryServiceImpl.class);
    }
}
