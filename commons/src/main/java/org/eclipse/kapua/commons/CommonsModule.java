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
package org.eclipse.kapua.commons;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.EventStorer;
import org.eclipse.kapua.commons.jpa.EventStorerImpl;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.model.query.QueryFactoryImpl;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordRepository;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreRecordImplJpaRepository;
import org.eclipse.kapua.model.query.QueryFactory;

import javax.inject.Singleton;

/**
 * {@code kapua-commons} {@link AbstractKapuaModule}.
 *
 * @since 2.0.0
 */
public class CommonsModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(QueryFactory.class).to(QueryFactoryImpl.class);
    }

    @Provides
    @Singleton
    EventStoreRecordRepository eventStoreRecordRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new EventStoreRecordImplJpaRepository(jpaRepoConfig);
    }

    @Provides
    @Singleton
    EventStorer eventPersister(EventStoreRecordRepository repository) {
        return new EventStorerImpl(repository);
    }

}