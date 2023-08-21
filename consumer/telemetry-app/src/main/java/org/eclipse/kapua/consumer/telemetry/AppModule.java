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
package org.eclipse.kapua.consumer.telemetry;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;

import javax.inject.Named;
import javax.inject.Singleton;

public class AppModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(MetricsTelemetry.class).in(Singleton.class);
    }

    @Provides
    @Named("metricModuleName")
    String metricModuleName() {
        return MetricsTelemetry.CONSUMER_TELEMETRY;
    }
}
