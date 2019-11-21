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

import java.util.Arrays;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.exception.CleanJobDataException;
import org.eclipse.kapua.job.engine.exception.JobCheckRunningException;
import org.eclipse.kapua.job.engine.exception.JobInvalidTargetException;
import org.eclipse.kapua.job.engine.exception.JobMissingStepException;
import org.eclipse.kapua.job.engine.exception.JobMissingTargetException;
import org.eclipse.kapua.job.engine.exception.JobNotRunningException;
import org.eclipse.kapua.job.engine.exception.JobResumingException;
import org.eclipse.kapua.job.engine.exception.JobStartingException;
import org.eclipse.kapua.job.engine.exception.JobStoppingException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.commons.http.HttpController;
import org.eclipse.kapua.service.commons.http.HttpServiceHandlers;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class JobEngineHttpController implements HttpController {

    private JobEngineServiceAsync jobEngineServiceAsync;

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final KapuaIdFactory kapuaIdFactory = kapuaLocator.getFactory(KapuaIdFactory.class);

    private final String basePath = "/v1/device-jobs";

    public JobEngineHttpController(JobEngineServiceAsync jobEngineServiceAsync) {
        this.jobEngineServiceAsync = jobEngineServiceAsync;
    }

    @Override
    public String getPath() {
        return basePath;
    }

    @Override
    public void registerRoutes(Router router) {

        // Login
        router.route().blockingHandler(HttpServiceHandlers::authenticationHandler);
//        router.route().blockingHandler(HttpServiceHandlers.authenticationHandler());

        // Service Routes - Start Job
        router.post("/:scopeId/:jobId/start").handler(this::startJob);
        // Service Routes - Start Job
        router.post("/:scopeId/:jobId/start-with-options").blockingHandler(this::startJobWithOptions);
        // Service Routes - Is running
        router.get("/:scopeId/:jobId/is-running").blockingHandler(this::isRunning);
        // Service Routes - Stop Job
        router.post("/:scopeId/:jobId/stop").blockingHandler(this::stopJob);
        // Service Routes - Stop Job Execution
        router.post("/:scopeId/:jobId/executions/:executionId/stop").blockingHandler(this::stopJobExecution);
        // Service Routes - Resume Job Execution
        router.post("/:scopeId/:jobId/executions/:executionId/resume").blockingHandler(this::resumeJobExecution);
        // Service Routes - Clean Job Data
        router.post("/:scopeId/:jobId/clean-data").blockingHandler(this::cleanJobData);

        // Failure handler
        router.route().failureHandler(this::failureHandler);
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

    private void failureHandler(RoutingContext ctx) {
        Throwable failure = ctx.failure();
        JsonObject error = new JsonObject();

        String errorCode;
        if (failure instanceof KapuaException) {
            errorCode = ((KapuaException)failure).getCode().name();
        } else if (failure instanceof KapuaRuntimeException) {
            errorCode = ((KapuaRuntimeException)failure).getCode().name();
        } else {
            errorCode = "UNKNOWN_ERROR";
        }

        int statusCode = -1;
        if (failure instanceof KapuaEntityNotFoundException) {
            KapuaEntityNotFoundException kapuaEntityNotFoundException = (KapuaEntityNotFoundException)failure;
            statusCode = 404;
            String entityType = kapuaEntityNotFoundException.getEntityType();
            String entity = kapuaEntityNotFoundException.getEntityName() != null ? kapuaEntityNotFoundException.getEntityName() : kapuaEntityNotFoundException.getEntityId().toCompactId();
            error.put("arguments", Arrays.asList(entityType, entity));
        } else if (failure instanceof JobStartingException) {
            JobStartingException jobStartingException = (JobStartingException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(jobStartingException.getScopeId().toCompactId(), jobStartingException.getJobId().toCompactId()));
        } else if (failure instanceof KapuaIllegalArgumentException) {
            KapuaIllegalArgumentException kapuaIllegalArgumentException = (KapuaIllegalArgumentException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(kapuaIllegalArgumentException.getArgumentName(), kapuaIllegalArgumentException.getArgumentValue()));
        } else if (failure instanceof JobInvalidTargetException) {
            JobInvalidTargetException jobInvalidTargetException = (JobInvalidTargetException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(jobInvalidTargetException.getScopeId().toCompactId(), jobInvalidTargetException.getJobId().toCompactId(), jobInvalidTargetException.getTargetSublist()));
        } else if (failure instanceof JobMissingTargetException) {
            JobMissingTargetException jobMissingTargetException = (JobMissingTargetException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(jobMissingTargetException.getScopeId().toCompactId(), jobMissingTargetException.getJobId().toCompactId()));
        } else if (failure instanceof JobMissingStepException) {
            JobMissingStepException jobMissingStepException = (JobMissingStepException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(jobMissingStepException.getScopeId().toCompactId(), jobMissingStepException.getJobId().toCompactId()));
        } else if (failure instanceof JobCheckRunningException) {
            JobCheckRunningException jobCheckRunningException = (JobCheckRunningException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(jobCheckRunningException.getScopeId().toCompactId(), jobCheckRunningException.getJobId().toCompactId()));
        } else if (failure instanceof JobNotRunningException) {
            JobNotRunningException jobNotRunningException = (JobNotRunningException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(jobNotRunningException.getScopeId().toCompactId(), jobNotRunningException.getJobId().toCompactId()));
        } else if (failure instanceof JobStoppingException) {
            JobStoppingException jobStoppingException = (JobStoppingException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(jobStoppingException.getScopeId().toCompactId(), jobStoppingException.getJobId().toCompactId(), jobStoppingException.getExecutionId()));
        } else if (failure instanceof JobResumingException) {
            JobResumingException jobResumingException = (JobResumingException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(jobResumingException.getScopeId().toCompactId(), jobResumingException.getJobId().toCompactId()));
        } else if (failure instanceof CleanJobDataException) {
            CleanJobDataException cleanJobDataException = (CleanJobDataException)failure;
            statusCode = 500;
            error.put("arguments", Arrays.asList(cleanJobDataException.getScopeId().toCompactId(), cleanJobDataException.getJobId().toCompactId()));
        }

        if (statusCode == -1) {
            // Exception not handled locally. Delegate to generic error handler
            ctx.next();
        } else {
            error.put("errorCode", errorCode);
            error.put("message", failure.getMessage());
            ctx.response().setStatusCode(statusCode).end(error.encode());
        }
    }

}
