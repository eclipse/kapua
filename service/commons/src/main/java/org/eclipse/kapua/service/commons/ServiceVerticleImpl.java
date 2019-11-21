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
package org.eclipse.kapua.service.commons;

import io.vertx.core.Future;

public class ServiceVerticleImpl extends AbstractServiceVerticle implements ServiceVerticle {

    private Service service;

    private ServiceVerticleImpl(Service aService) {
        service = aService;
    }

    @Override
    public void internalStart(Future<Void> startFuture) throws Exception {
        service.start(startFuture);
    }

    @Override
    public void internalStop(Future<Void> stopFuture) throws Exception {
        if (service != null) {
            service.stop(stopFuture);
            return;
        }
        stopFuture.complete();
    }

    public static ServiceVerticleImpl create(Service anEndpoint) {
        return new ServiceVerticleImpl(anEndpoint);
    }
}
