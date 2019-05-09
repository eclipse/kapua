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

import javax.annotation.PostConstruct;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.eclipse.kapua.microservice")
public class JobEngineApplication {

    private Vertx vertx = Vertx.vertx();

    @Autowired
    private JobEngineHttpServerVerticle jobEngineHttpServerVerticle;

    public static void main(String[] args) {
        SpringApplication.run(JobEngineApplication.class);
    }

    @PostConstruct
    public void init() {
        vertx.deployVerticle(jobEngineHttpServerVerticle);
    }

    @Bean
    public Vertx getVertx() { return vertx; }

}
