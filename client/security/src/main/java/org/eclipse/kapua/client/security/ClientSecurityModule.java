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
package org.eclipse.kapua.client.security;

import org.eclipse.kapua.client.security.metric.AuthLoginMetricFactory;
import org.eclipse.kapua.client.security.metric.AuthMetric;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;

import javax.inject.Singleton;

public class ClientSecurityModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(MetricsClientSecurity.class).in(Singleton.class);
        bind(MessageListener.class).in(Singleton.class);
        bind(AuthMetric.class).in(Singleton.class);
        bind(AuthLoginMetricFactory.class).in(Singleton.class);
    }
}
