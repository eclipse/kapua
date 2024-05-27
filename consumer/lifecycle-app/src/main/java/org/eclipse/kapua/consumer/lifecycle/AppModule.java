/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.lifecycle;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.liquibase.DatabaseCheckUpdate;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.JAXBContextProviderImpl;

import com.google.inject.Provides;

public class AppModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(DatabaseCheckUpdate.class).in(Singleton.class);
        bind(MetricsLifecycle.class).in(Singleton.class);
        // Switching manually-configured JAXBContextProvider to autodiscovery one below
        // bind(JAXBContextProvider.class).to(LifecycleJAXBContextProvider.class).in(Singleton.class);
        bind(JAXBContextProvider.class).to(JAXBContextProviderImpl.class).in(Singleton.class);
    }

    @Provides
    @Named("metricModuleName")
    String metricModuleName() {
        return MetricsLifecycle.CONSUMER_LIFECYCLE;
    }

    @Provides
    @Named("eventsModuleName")
    String eventModuleName() {
        return "lifecycle";
    }
}
