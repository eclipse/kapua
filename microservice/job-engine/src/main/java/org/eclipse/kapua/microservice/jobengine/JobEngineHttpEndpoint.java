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
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.microservice.commons.HttpEndpoint;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobEngineHttpEndpoint implements HttpEndpoint {

    private final KapuaLocator kapuaLocator = KapuaLocator.getInstance();
    private final JobEngineService jobEngineService = kapuaLocator.getService(JobEngineService.class);

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
        jobEngineServiceAsync.startJob(ctx.get("scopeId"), ctx.get("jobId"), Future.<Void>future().setHandler(result -> {
            if (result.succeeded()) {
                ctx.response().end();
                ctx.next();
            } else {
                ctx.fail(result.cause());
            }
        }));
    }

    private void startJobWithOptions(RoutingContext ctx) {
        KapuaSecurityUtils.setSession(ctx.get("kapuaSession"));
        JobStartOptions jobStartOptions = Json.decodeValue(ctx.getBodyAsString(), JobStartOptions.class);
        try {
            jobEngineService.startJob(ctx.get("scopeId"), ctx.get("jobId"), jobStartOptions);
        } catch (KapuaException ex) {
            ctx.fail(ex);
        }
        ctx.next();
    }

    private void isRunning(RoutingContext ctx) {
        try {
            ctx.response().end("{ \"isRunning\": " + jobEngineService.isRunning(ctx.get("scopeId"), ctx.get("jobId")) + " }");
        } catch (KapuaException ex) {
            ctx.fail(ex);
        }
        ctx.next();
    }

}
