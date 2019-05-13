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
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.microservice.commons.BlockingAsyncRequestExecutor;
import org.eclipse.kapua.model.id.KapuaId;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobEngineServiceAsync {

    @Autowired
    private BlockingAsyncRequestExecutor blockingAsyncRequestExecutor;

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final JobEngineService jobEngineService = kapuaLocator.getService(JobEngineService.class);

    public void startJob(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, Handler<AsyncResult<Void>> resultHandler) {
        blockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, future -> {
            try {
                jobEngineService.startJob(scopeId, jobId);
                future.complete();
            } catch (KapuaException ex) {
                future.fail(ex);
            }
        }, resultHandler);
    }

    public void startJob(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions, Handler<AsyncResult<Void>> resultHandler) {
        blockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, future -> {
            try {
                jobEngineService.startJob(scopeId, jobId, jobStartOptions);
                future.complete();
            } catch (KapuaException ex) {
                future.fail(ex);
            }
        }, resultHandler);
    }

    public void isRunning(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, Handler<AsyncResult<Boolean>> resultHandler) {
        blockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, future -> {
            try {
                Boolean isRunning = jobEngineService.isRunning(scopeId, jobId);
                future.complete(isRunning);
            } catch (KapuaException ex) {
                future.fail(ex);
            }
        }, resultHandler);
    }
}
