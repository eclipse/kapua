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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;

import com.fasterxml.jackson.databind.Module;
import com.google.common.base.MoreObjects;
import io.vertx.core.json.Json;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
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
import org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener;

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
        // Removing LiquibaseServiceLocatorApplicationListener avoids SpringPackageScanClassResolver,
        // who is only compatible with Liquibase >= 3.2.3
        app.setListeners(app.getListeners().stream()
                .filter((listener) -> !(listener instanceof LiquibaseServiceLocatorApplicationListener))
                .collect(Collectors.toSet()));
        app.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Future<Void> startupFuture = Future.future();
        CountDownLatch startupLatch = new CountDownLatch(1);
        Future.succeededFuture()
                .compose(previousResult -> {
                    SystemSetting config = SystemSetting.getInstance();
                    if (config.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
                        logger.info("Initialize Liquibase embedded client.");
                        String dbUsername = config.getString(SystemSettingKey.DB_USERNAME);
                        String dbPassword = config.getString(SystemSettingKey.DB_PASSWORD);
                        String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));

                        // initialize driver
                        try {
                            Class.forName(config.getString(SystemSettingKey.DB_JDBC_DRIVER));
                        } catch (ClassNotFoundException e) {
                            logger.warn("Could not find jdbc driver: {}", config.getString(SystemSettingKey.DB_JDBC_DRIVER));
                            return Future.failedFuture(e);
                        }

                        logger.debug("Starting Liquibase embedded client update - URL: {}, user/pass: {}/{}", JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword);

                        new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
                    }
                    return Future.succeededFuture();
                })
                .compose(previousResult -> {
                    // Shiro, JAXB and Jackson initialization
                    try {
                        initShiro();
                        XmlUtil.setContextProvider(new JobEngineJaxbContextProvider());
                        registerJacksonModule(new JobEngineModule());
                    } catch (Exception ex) {
                        return Future.failedFuture(ex);
                    }
                    return Future.succeededFuture();
                })
                .compose(previousResult -> {
                    // Start service monitoring first
                    Future<String> monitorDeployFuture = Future.future();
                    vertx.deployVerticle(jobEngineServiceMonitor, monitorDeployFuture);
                    return monitorDeployFuture;
                })
                .compose(previousResult -> {
                    // Start services
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
            throw new TimeoutException();
        } else {
            if (!startupFuture.succeeded()) {
                throw new Exception(startupFuture.cause());
            }
        }
    }

    private void registerJacksonModule(Module jacksonModule) {
        Json.mapper.registerModule(jacksonModule);
        Json.prettyMapper.registerModule(jacksonModule);
    }

    private void initShiro() throws KapuaException {
        // initialize shiro context for broker plugin from shiro ini file
        final URL shiroIniUrl = KapuaFileUtils.getAsURL("shiro.ini");
        Ini shiroIni = new Ini();
        try (final InputStream input = shiroIniUrl.openStream()) {
            shiroIni.load(input);
        } catch (IOException ex) {
            logger.error("Error loading shiro.ini", ex);
            throw new KapuaException(KapuaErrorCodes.INTERNAL_ERROR);
        }

        SecurityManager securityManager = new IniSecurityManagerFactory(shiroIni).getInstance();
        SecurityUtils.setSecurityManager(securityManager);
    }

}
