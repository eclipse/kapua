/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.http;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.eclipse.kapua.service.commons.PropertyMapper;
import org.eclipse.kapua.service.commons.ServiceBuilderFactory;
import org.eclipse.kapua.service.commons.ServiceBuilderFactoryRegistry;
import org.eclipse.kapua.service.commons.ServiceConfig;

import io.vertx.core.Vertx;

public class HttpServiceBuilderFactory implements ServiceBuilderFactory<HttpServiceContext, HttpService> {

    private static final String NAME = "http";

    private HttpServiceBuilderFactory() {

    }

    public void register(@NotNull ServiceBuilderFactoryRegistry aRegistry) {
        Objects.requireNonNull(aRegistry, "param: aRegistry");
        aRegistry.registerFactory(NAME, this);
    }

    public void deregister(@NotNull ServiceBuilderFactoryRegistry aRegistry) {
        Objects.requireNonNull(aRegistry, "param: aRegistry");
        aRegistry.deregisterFactory(NAME);
    }

    public static HttpServiceBuilderFactory create(@NotNull ServiceBuilderFactoryRegistry aRegistry) {
        Objects.requireNonNull(aRegistry, "param: aRegistry");
        HttpServiceBuilderFactory factory = new HttpServiceBuilderFactory();
        aRegistry.registerFactory(NAME, factory);
        return factory;
    }

    @Override
    public HttpServiceBuilder create(Vertx aVertx, @NotNull ServiceConfig aConfig, @NotNull PropertyMapper aMapper) {
        Objects.requireNonNull(aConfig, "param: aConfig");
        Objects.requireNonNull(aMapper, "param: aMapper");
        return new HttpServiceImpl.Builder(aVertx, aMapper.convert(aConfig.getProperties(), "", HttpServiceConfig.class));
    }
}
