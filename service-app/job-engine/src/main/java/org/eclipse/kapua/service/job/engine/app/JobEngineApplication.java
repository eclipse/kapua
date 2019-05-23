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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

@SpringBootApplication
public class JobEngineApplication implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(JobEngineApplication.class);

    private long startupTimeout;

    private int jobEngineServiceInstances;

    private Vertx vertx;

    private JobEngineHttpService httpJobEngineService;

    private JobEngineHttpServiceMonitor jobEngineServiceMonitor;

    @Value("${startupTimeout}")
    public void setStartupTimeout(long startupTimeout) {
        this.startupTimeout = startupTimeout;
    }

    @Value("${job-engine.instances}")
    public void setHttpInstances(int httpInstances) {
        this.jobEngineServiceInstances = httpInstances;
    }

    @Autowired
    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }

    @Autowired
    public void setHttpAccountService(JobEngineHttpService service) {
        this.httpJobEngineService = service;
    }

    @Autowired
    public void setHttpAccountServiceMonitor(JobEngineHttpServiceMonitor monitor) {
        this.jobEngineServiceMonitor = monitor;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JobEngineApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Future<Void> startupFuture = Future.future();
        CountDownLatch startupLatch = new CountDownLatch(1);
        Future.succeededFuture()
                .compose(map -> {
                    // Start service monitoring first
                    Future<String> monitorDeployFuture = Future.future();
                    vertx.deployVerticle(jobEngineServiceMonitor, monitorDeployFuture);
                    return monitorDeployFuture;
                })
                .compose(map -> {
                    // Start services
                    @SuppressWarnings("rawtypes")
                    List<Future> deployFutures = new ArrayList<>();
                    for (int i = 0; i < jobEngineServiceInstances; i++) {
                        Future<String> accountServiceDeployFuture = Future.future();
                        deployFutures.add(accountServiceDeployFuture);
                        vertx.deployVerticle(httpJobEngineService, accountServiceDeployFuture);
                    }

                    // All services must be started before proceeding
                    return CompositeFuture.all(deployFutures);
                })
                .setHandler(startup -> {
                    if (startup.succeeded()) {
                        startupFuture.complete();
                    } else {
                        startupFuture.fail(startup.cause());
                    }
                    startupLatch.countDown();
                });

        if (!startupLatch.await(startupTimeout, TimeUnit.MILLISECONDS)) {
            throw new Exception("Prrrr!");
        } else {
            if (!startupFuture.succeeded()) {
                throw new Exception(startupFuture.cause());
            }
        }
    }
}
