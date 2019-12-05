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

import java.util.Objects;
import java.util.stream.Collectors;

import org.eclipse.kapua.service.commons.app.AbstractKapuaServiceApplication;
import org.eclipse.kapua.service.commons.app.Context;
import org.eclipse.kapua.service.commons.http.HttpServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener;

@SpringBootApplication
public class JobEngineApplication extends AbstractKapuaServiceApplication<JobEngineConfiguration> {

    Logger logger = LoggerFactory.getLogger(JobEngineApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JobEngineApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        // Removing LiquibaseServiceLocatorApplicationListener avoids SpringPackageScanClassResolver,
        // who is only compatible with Liquibase >= 3.2.3
        app.setListeners(app.getListeners().stream()
                .filter((listener) -> !(listener instanceof LiquibaseServiceLocatorApplicationListener))
                .collect(Collectors.toSet()));
        app.run(args);
    }

    @Override
    protected void buildContext(Context context, JobEngineConfiguration config) throws Exception {
        Objects.requireNonNull(context, "param: context");
        Objects.requireNonNull(config, "param: config");

        // Configure Services
        context.getServiceContext("jobEngine", HttpServiceContext.class).addController(config.getController());
    }
}
