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

import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;
import org.eclipse.kapua.service.commons.http.HttpServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

@Configuration
@ComponentScan("org.eclipse.kapua.service.commons")
@ComponentScan("org.eclipse.kapua.service.job.engine.app")
public class JobEngineApplicationConfig {

    @Autowired
    private JobEngineHttpService.Builder serviceBuilder;

    @Autowired
    private JobEngineHttpServiceMonitor.Builder monitorBuilder;

    @Bean
    @ConfigurationProperties(prefix = "vertx.metrics")
    public MetricsConfig metricsConfig() {
        return new MetricsConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "vertx")
    public VertxConfig vertxConfig() {
        return new VertxConfig();
    }

    @Qualifier("monitoring")
    @Bean
    @ConfigurationProperties(prefix = "job-engine.monitoring")
    public HttpMonitorServiceConfig httpMonitoringServiceConfig() {
        return new HttpMonitorServiceConfig();
    }

    @Qualifier("job-engine")
    @Bean
    @ConfigurationProperties(prefix = "job-engine.http")
    public HttpServiceConfig service1Config() {
        return new HttpServiceConfig();
    }

    @Bean
    public JobEngineServiceAsync jobEngineServiceAsync() {
        return new JobEngineServiceAsync();
    }

    @Bean
    @Autowired
    public JobEngineHttpEndpoint jobEngineHttpEndpoint(JobEngineServiceAsync jobEngineServiceAsync) {
        return new JobEngineHttpEndpoint(jobEngineServiceAsync);
    }

    @Bean
    public JobEngineHttpService httpAccountService() {
        return serviceBuilder.build();
    }

    @Bean
    public JobEngineHttpServiceMonitor httpAccountServiceMonitor() {
        return monitorBuilder.build();
    }

    @Bean
    public Vertx getVertx() {

        VertxConfig vertxConfig = vertxConfig();
        MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(vertxConfig.getMetrics().getRegistryName());
        SharedMetricRegistries.setDefault(vertxConfig.getMetrics().getRegistryName(), metricRegistry);

        DropwizardMetricsOptions metrOpts = new DropwizardMetricsOptions();
        metrOpts.setEnabled(vertxConfig().getMetrics().isEnable());
        metrOpts.setRegistryName(vertxConfig.getMetrics().getRegistryName());

        VertxOptions opts = new VertxOptions();
        opts.setWarningExceptionTime(vertxConfig.getWarningExceptionTime());
        opts.setBlockedThreadCheckInterval(vertxConfig.getBlockedThreadCheckInterval());
        opts.setMetricsOptions(metrOpts);
        return Vertx.vertx(opts);
    }

    @Bean
    public ExitCodeExceptionMapper getExitCodeExceptionMapper() {
        return exception -> 1;
    }

}
