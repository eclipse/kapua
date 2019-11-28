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
package org.eclipse.kapua.service.commons.app;

import java.util.Objects;

import org.eclipse.kapua.service.commons.PropertyMapper;
import org.eclipse.kapua.service.commons.ServiceConfig;
import org.eclipse.kapua.service.commons.ServiceConfigs;
import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;
import org.eclipse.kapua.service.commons.http.HttpServiceBuilderFactory;
import org.eclipse.kapua.service.commons.http.HttpServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.codahale.metrics.SharedMetricRegistries;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;

public class BaseConfiguration implements Configuration {

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

    @Bean
    @Qualifier("monitoring")
    @ConfigurationProperties(prefix = "monitoring")
    public HttpMonitorServiceConfig httpMonitoringServiceConfig() {
        return new HttpMonitorServiceConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "service")
    public HttpServiceConfig httpServiceConfig() {
        return new HttpServiceConfig();
    }

    @Bean
    public PropertyMapper propertyMaper() {
        return new SpringPropertyMapper();
    }

    @Bean
    public ServiceBuilderManager serviceBuilderManager() {
        return new ServiceBuilderManager();
    }

    @Bean
    public ServiceConfig serviceConfig() {
        return new ServiceConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "services")
    public ServiceConfigs serviceConfigs() {
        return new ServiceConfigs();
    }

    @Autowired
    @Bean
    public Vertx vertx(VertxConfig aVertexConfig) {
        Objects.requireNonNull(aVertexConfig, "param: config");
        VertxConfig vertxConfig = aVertexConfig;

        DropwizardMetricsOptions metrOpts = new DropwizardMetricsOptions();
        metrOpts.setEnabled(aVertexConfig.getMetrics().isEnable());
        metrOpts.setRegistryName(vertxConfig.getMetrics().getRegistryName());
        if (vertxConfig.getMetrics().getRegistryName() != null) {
            SharedMetricRegistries.getOrCreate(vertxConfig.getMetrics().getRegistryName());
            SharedMetricRegistries.setDefault(vertxConfig.getMetrics().getRegistryName());
        }

        VertxOptions opts = new VertxOptions();
        opts.setWarningExceptionTime(vertxConfig.getWarningExceptionTime());
        opts.setBlockedThreadCheckInterval(vertxConfig.getBlockedThreadCheckInterval());
        opts.setMetricsOptions(metrOpts);
        Vertx vertx = Vertx.vertx(opts);
        return vertx;
    }

    private String applicationName;
    private long startupTimeout;
    private VertxConfig vertxConfig;
    private HttpMonitorServiceConfig monitorServiceConfig;
    private ServiceConfigs serviceConfigs;
    private HttpServiceBuilderFactory httpServiceBuilderFactory;

    @Value("${applicationName}")
    public void applicationName(String anApplicationName) {
        applicationName = anApplicationName;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Value("${startupTimeout}")
    public void startupTimeout(long aStartupTimeout) {
        startupTimeout = aStartupTimeout;
    }

    @Override
    public long getStartupTimeout() {
        return startupTimeout;
    }

    @Autowired
    public void setServiceConfig(ServiceConfigs someConfigs) {
        serviceConfigs = someConfigs;
    }

    @Override
    public ServiceConfigs getServiceConfigs() {
        return serviceConfigs;
    }

    @Autowired
    @Qualifier("monitoring")
    public void setHttpMonitorServiceConfig(HttpMonitorServiceConfig aConfig) {
        monitorServiceConfig = aConfig;
    }

    @Override
    public HttpMonitorServiceConfig getHttpMonitorServiceConfig() {
        return monitorServiceConfig;
    }

    @Autowired
    public void setVertxConfig(VertxConfig aConfig) {
        vertxConfig = aConfig;
    }

    @Override
    public VertxConfig getVertxConfig() {
        return vertxConfig;
    }

    @Autowired
    public void setHttpServiceBuilderFactory(ServiceBuilderManager aManager) {
        httpServiceBuilderFactory = HttpServiceBuilderFactory.create(aManager);
    }

    @Override
    public String toString() {
        return String.format("\"applicationName\":\"%s\""
                + ", \"startupTimeout\":\"%d\"",
                this.getApplicationName(),
                this.getStartupTimeout());
    }
}
