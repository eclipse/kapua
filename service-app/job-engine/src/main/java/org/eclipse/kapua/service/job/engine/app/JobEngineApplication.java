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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.commons.app.ApplicationBase;
import org.eclipse.kapua.service.commons.app.Context;
import org.eclipse.kapua.service.commons.http.HttpServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener;

import com.fasterxml.jackson.databind.Module;
import com.google.common.base.MoreObjects;

import io.vertx.core.Future;
import io.vertx.core.json.Json;

@SpringBootApplication
public class JobEngineApplication extends ApplicationBase<JobEngineApplicationConfiguration> {

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
    protected void runInternal(Context context, JobEngineApplicationConfiguration config, Future<Void> runFuture) throws Exception {
        Objects.requireNonNull(context, "param: context");
        Objects.requireNonNull(config, "param: config");
        Objects.requireNonNull(runFuture, "param: runFuture");

        context.getVertx().executeBlocking(runExecution -> {

            try {
                SystemSetting sysConfig = SystemSetting.getInstance();
                if (sysConfig.getBoolean(SystemSettingKey.DB_SCHEMA_UPDATE, false)) {
                    logger.info("Initialize Liquibase embedded client.");
                    String dbUsername = sysConfig.getString(SystemSettingKey.DB_USERNAME);
                    String dbPassword = sysConfig.getString(SystemSettingKey.DB_PASSWORD);
                    String schema = MoreObjects.firstNonNull(sysConfig.getString(SystemSettingKey.DB_SCHEMA_ENV), sysConfig.getString(SystemSettingKey.DB_SCHEMA));

                    // initialize driver
                    Class.forName(sysConfig.getString(SystemSettingKey.DB_JDBC_DRIVER));
                    logger.debug("Starting Liquibase embedded client update - URL: {}, user/pass: {}/{}", JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword);
                    new KapuaLiquibaseClient(JdbcConnectionUrlResolvers.resolveJdbcUrl(), dbUsername, dbPassword, Optional.of(schema)).update();
                }

                // Shiro, JAXB and Jackson initialization
                initShiro();
                XmlUtil.setContextProvider(new JobEngineJaxbContextProvider());
                registerJacksonModule(new JobEngineModule());

                // Configure Services
                context.getServiceContext("http", HttpServiceContext.class).addController(config.getJobEngineHttpController());
                runExecution.complete();
            } catch (Exception exc) {
                runExecution.fail(exc);
            }

        }, resultHandler -> {
            if (resultHandler.succeeded()) {
                runFuture.complete();
            } else {
                runFuture.fail(resultHandler.cause());
            }
        });
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
