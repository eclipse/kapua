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
package org.eclipse.kapua.commons.service.event.store.internal;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreFactory;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Named;

public class CommonsServiceEventModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(EventStoreFactory.class).to(EventStoreFactoryImpl.class);
    }

    @Provides
    @Named("kapuaEventsTxManager")
    TxManager kapuaEventsTxManager(@Named("maxInsertAttempts") Integer maxInsertAttempts) {
        return new KapuaJpaTxManagerFactory(maxInsertAttempts).create("kapua-events");
    }

}
