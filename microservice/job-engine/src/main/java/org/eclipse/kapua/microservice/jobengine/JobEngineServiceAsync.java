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
package org.eclipse.kapua.microservice.jobengine;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobEngineServiceAsync {

    @Autowired
    private Vertx vertx;

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final JobEngineService jobEngineService = kapuaLocator.getService(JobEngineService.class);

    public void startJob(KapuaId scopeId, KapuaId jobId, Future<Void> resultFuture) {
        vertx.executeBlocking(blockingFuture -> {
            try {
                jobEngineService.startJob(scopeId, jobId);
                blockingFuture.complete();
            } catch (KapuaException ex) {
                blockingFuture.fail(ex);
            }
        }, resultFuture);
    }

    public Future<Void> startJob(KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions) {
        vertx.executeBlocking(blockingFuture -> {

        }, result -> {

        });
        return Future.succeededFuture();
    }

    public Future<Boolean> isRunning(KapuaId scopeId, KapuaId jobId) {
        return Future.succeededFuture(true);
    }
}
