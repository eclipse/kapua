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
import org.springframework.stereotype.Service;

/**
 * Transport-agnostic wrapper for {@link JobEngineService}. {@link JobEngineHttpEndpoint} routes all call methods
 * in this class extracting HTTP specific concepts (e.g. {@link io.vertx.ext.web.RoutingContext} so that this class
 * can be reused by clients for other transports (e.g. AMQP)
 */
@Service
public class JobEngineServiceAsync {

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final JobEngineService jobEngineService = kapuaLocator.getService(JobEngineService.class);

    public void startJob(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, Handler<AsyncResult<Void>> resultHandler) {
        BlockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> jobEngineService.startJob(scopeId, jobId), resultHandler);
    }

    public void startJob(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, JobStartOptions jobStartOptions, Handler<AsyncResult<Void>> resultHandler) {
        BlockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> jobEngineService.startJob(scopeId, jobId, jobStartOptions), resultHandler);
    }

    public void isRunning(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, Handler<AsyncResult<Boolean>> resultHandler) {
        BlockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> jobEngineService.isRunning(scopeId, jobId), resultHandler);
    }

    public void stopJob(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, Handler<AsyncResult<Boolean>> resultHandler) {
        BlockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> jobEngineService.stopJob(scopeId, jobId), resultHandler);
    }

    public void stopJobExecution(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId, Handler<AsyncResult<Boolean>> resultHandler) {
        BlockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> jobEngineService.stopJobExecution(scopeId, jobId, jobExecutionId), resultHandler);
    }

    public void resumeJobExecution(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, KapuaId jobExecutionId, Handler<AsyncResult<Boolean>> resultHandler) {
        BlockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> jobEngineService.resumeJobExecution(scopeId, jobId, jobExecutionId), resultHandler);
    }

    public void cleanJobData(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, KapuaId scopeId, KapuaId jobId, Handler<AsyncResult<Boolean>> resultHandler) {
        BlockingAsyncRequestExecutor.executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> jobEngineService.cleanJobData(scopeId, jobId), resultHandler);
    }
}
