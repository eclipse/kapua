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

import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.microservice.commons.HttpEndpoint;
import org.eclipse.kapua.microservice.commons.HttpResponseHandler;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;

import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobEngineHttpEndpoint implements HttpEndpoint {

    @Autowired
    private JobEngineServiceAsync jobEngineServiceAsync;

    private KapuaIdFactory kapuaIdFactory = KapuaLocator.getInstance().getFactory(KapuaIdFactory.class);

    @Override
    public void registerRoutes(Router router) {
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
        jobEngineServiceAsync.startJob(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, HttpResponseHandler.httpRequestHandler(ctx));
    }

    private void startJobWithOptions(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        JobStartOptions jobStartOptions = Json.decodeValue(ctx.getBodyAsString(), JobStartOptions.class);
        jobEngineServiceAsync.startJob(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, jobStartOptions, HttpResponseHandler.httpRequestHandler(ctx));
    }

    private void isRunning(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        jobEngineServiceAsync.isRunning(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, HttpResponseHandler.httpRequestHandler(ctx));
    }

    private void stopJob(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        jobEngineServiceAsync.stopJob(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, HttpResponseHandler.httpRequestHandler(ctx));
    }

    private void stopJobExecution(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        KapuaId jobExecutionId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobExecutionId"));
        jobEngineServiceAsync.stopJobExecution(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, jobExecutionId, HttpResponseHandler.httpRequestHandler(ctx));
    }

    private void resumeJobExecution(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        KapuaId jobExecutionId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobExecutionId"));
        jobEngineServiceAsync.resumeJobExecution(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, jobExecutionId, HttpResponseHandler.httpRequestHandler(ctx));
    }

    private void cleanJobData(RoutingContext ctx) {
        KapuaId scopeId = kapuaIdFactory.newKapuaId(ctx.pathParam("scopeId"));
        KapuaId jobId = kapuaIdFactory.newKapuaId(ctx.pathParam("jobId"));
        jobEngineServiceAsync.cleanJobData(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), scopeId, jobId, HttpResponseHandler.httpRequestHandler(ctx));
    }

}
