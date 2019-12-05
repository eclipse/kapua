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

import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.service.commons.app.AbstractBeanProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;

@Configuration
@ComponentScan("org.eclipse.kapua.service.commons")
@ComponentScan("org.eclipse.kapua.service.job.engine.app")
public class JobEngineBeanProvider extends AbstractBeanProvider<JobEngineConfiguration> {

    @Bean
    public JAXBContextProvider jaxbContextProvider() {
        return new JobEngineJaxbContextProvider();
    }

    @Qualifier("kapuaServiceApp")
    @Bean
    public Module module() {
        return new JobEngineModule();
    }

    @Bean
    public JobEngineServiceAsync jobEngineServiceAsync() {
        return new JobEngineServiceAsync();
    }

    @Autowired
    @Bean
    public JobEngineHttpController jobEngineHttpController(JobEngineServiceAsync jobEngineServiceAsync) {
        return new JobEngineHttpController(jobEngineServiceAsync);
    }

    @Bean
    public ExitCodeExceptionMapper getExitCodeExceptionMapper() {
        return exception -> 1;
    }

    @Override
    public JobEngineConfiguration configuration() {
        return new JobEngineConfiguration();
    }
}
