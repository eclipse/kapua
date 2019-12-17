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

import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.service.commons.app.AbstractBeanProvider;
import org.eclipse.kapua.service.commons.http.HttpService;
import org.eclipse.kapua.service.commons.http.HttpServiceBuilder;
import org.eclipse.kapua.service.commons.http.HttpServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;

import io.vertx.core.Vertx;

@Configuration
@ComponentScan("org.eclipse.kapua.service.commons")
@ComponentScan("org.eclipse.kapua.service.job.engine.app")
public class JobEngineBeanProvider extends AbstractBeanProvider<JobEngineConfiguration> {

    private static final String JOB_ENGINE_HTTP_SERVICE_BUILDER_BEAN = "jobEngineHttpServiceBuilder";
    private static final String JOB_ENGINE_HTTP_SERVICE_NAME = "jobEngineHttpService";

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

    @Bean
    @ConfigurationProperties(prefix = "services.job-engine.http")
    public @Autowired HttpServiceConfig getHttpServiceConfig() {
        return new HttpServiceConfig();
    }

    @Autowired
    @Bean(JOB_ENGINE_HTTP_SERVICE_BUILDER_BEAN)
    public HttpServiceBuilder httpServiceBuilder(Vertx aVertx, HttpServiceConfig aConfig) {
        Objects.requireNonNull(aConfig, "param: aConfig");
        if (aConfig.getName() == null) {
            aConfig.setName(JOB_ENGINE_HTTP_SERVICE_NAME);
        }
        return HttpService.builder(aVertx, aConfig);
    }

    @Qualifier("services")
    @Bean
    public ObjectFactoryCreatingFactoryBean httpServiceBuilderFactory() {
        final ObjectFactoryCreatingFactoryBean factory = new ObjectFactoryCreatingFactoryBean();
        factory.setTargetBeanName(JOB_ENGINE_HTTP_SERVICE_BUILDER_BEAN);
        return factory;
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
