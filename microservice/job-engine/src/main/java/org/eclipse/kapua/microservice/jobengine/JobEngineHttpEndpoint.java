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
import org.eclipse.kapua.microservice.commons.HttpEndpoint;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobEngineHttpEndpoint implements HttpEndpoint {

    @Autowired
    private JobEngineServiceAsync jobEngineServiceAsync;

    @Override
    public void registerRoutes(Router router) {
        // Service Routes - Start Job
        router.get("/startJob/:scopeId/:jobId").handler(this::startJob);
        // Service Routes - Start Job
        router.post("/startJob/:scopeId/:jobId").blockingHandler(this::startJobWithOptions);
        // Service Routes - Is running
        router.get("/isRunning/:scopeId/:jobId").blockingHandler(this::isRunning);
    }

    private void startJob(RoutingContext ctx) {
        jobEngineServiceAsync.startJob(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), ctx.get("scopeId"), ctx.get("jobId"), result -> {
            if (result.succeeded()) {
                ctx.response().end();
                ctx.next();
            } else {
                ctx.fail(500, result.cause());
            }
        });
    }

    private void startJobWithOptions(RoutingContext ctx) {
        JobStartOptions jobStartOptions = Json.decodeValue(ctx.getBodyAsString(), JobStartOptions.class);
        jobEngineServiceAsync.startJob(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), ctx.get("scopeId"), ctx.get("jobId"), jobStartOptions, result -> {
            if (result.succeeded()) {
                ctx.response().end();
                ctx.next();
            } else {
                ctx.fail(500, result.cause());
            }
        });
    }

    private void isRunning(RoutingContext ctx) {
        jobEngineServiceAsync.isRunning(ctx.vertx(), ctx.get("kapuaSession"), ctx.get("shiroSubject"), ctx.get("scopeId"), ctx.get("jobId"), result -> {
            if (result.succeeded()) {
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.put("isRunning", result.result());
                ctx.response().end(jsonResponse.encode());
                ctx.next();
            } else {
                ctx.fail(500, result.cause());
            }
        });
    }

}
