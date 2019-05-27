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
package org.eclipse.kapua.service.job.engine.app;

import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.commons.http.HttpEndpoint;
import org.eclipse.kapua.service.commons.http.HttpServiceHandlers;

import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class JobEngineHttpEndpoint implements HttpEndpoint {

    private JobEngineServiceAsync jobEngineServiceAsync;

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final KapuaIdFactory kapuaIdFactory = kapuaLocator.getFactory(KapuaIdFactory.class);

    private final String basePath = "/v1/job-engine";

    public JobEngineHttpEndpoint(JobEngineServiceAsync jobEngineServiceAsync) {
        this.jobEngineServiceAsync = jobEngineServiceAsync;
    }

    @Override
    public String getBasePath() {
        return basePath;
    }

    @Override
    public void registerRoutes(Router router) {

        // Login
        router.route().blockingHandler(HttpServiceHandlers::authenticationHandler);
//        router.route().blockingHandler(HttpServiceHandlers.authenticationHandler());

        // Service Routes - Start Job
        router.post("/startJob/:scopeId/:jobId").handler(this::startJob);
        // Service Routes - Start Job
        router.post("/startJobWithOptions/:scopeId/:jobId").blockingHandler(this::startJobWithOptions);
        // Service Routes - Is running
        router.get("/isRunning/:scopeId/:jobId").blockingHandler(this::isRunning);
        // Service Routes - Stop Job
        router.post("/stopJob/:scopeId/:jobId").blockingHandler(this::stopJob);
        // Service Routes - Stop Job Execution
        router.post("/stopJobExecution/:scopeId/:jobId/:executionId").blockingHandler(this::stopJobExecution);
        // Service Routes - Resume Job Execution
        router.post("/resumeJobExecution/:scopeId/:jobId/:executionId").blockingHandler(this::resumeJobExecution);
        // Service Routes - Clean Job Data
        router.delete("/cleanJobData/:scopeId/:jobId").blockingHandler(this::cleanJobData);
    }

    private void startJob(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        jobEngineServiceAsync.startJob(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, HttpServiceHandlers.httpResponseHandler(ctx));
    }

    private void startJobWithOptions(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        JobStartOptions jobStartOptions = Json.decodeValue(ctx.getBodyAsString(), JobStartOptions.class);
        jobEngineServiceAsync.startJob(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, jobStartOptions, HttpServiceHandlers.httpResponseHandler(ctx));
    }

    private void isRunning(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        jobEngineServiceAsync.isRunning(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, HttpServiceHandlers.httpResponseHandler(ctx));
    }

    private void stopJob(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        jobEngineServiceAsync.stopJob(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, HttpServiceHandlers.httpResponseHandler(ctx));
    }

    private void stopJobExecution(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        KapuaId jobExecutionId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobExecutionId"));
        jobEngineServiceAsync.stopJobExecution(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, jobExecutionId, HttpServiceHandlers.httpResponseHandler(ctx));
    }

    private void resumeJobExecution(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        KapuaId jobExecutionId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobExecutionId"));
        jobEngineServiceAsync.resumeJobExecution(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, jobExecutionId, HttpServiceHandlers.httpResponseHandler(ctx));
    }

    private void cleanJobData(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        jobEngineServiceAsync.cleanJobData(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, HttpServiceHandlers.httpResponseHandler(ctx));
    }

}
