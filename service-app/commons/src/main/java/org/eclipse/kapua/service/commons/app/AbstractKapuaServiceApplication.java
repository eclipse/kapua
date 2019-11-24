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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

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
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.Module;
import com.google.common.base.MoreObjects;

import io.vertx.core.Future;
import io.vertx.core.json.Json;

public abstract class AbstractKapuaServiceApplication<C extends Configuration> extends BaseApplication<C> {

    Logger logger = LoggerFactory.getLogger(AbstractKapuaServiceApplication.class);

    private JAXBContextProvider jaxbContext;
    private Module jacksonModule;

    @Autowired
    public void setJAXBContextProvider(JAXBContextProvider aContext) {
        jaxbContext = aContext;
    }

    @Qualifier("kapuaServiceApp")
    @Autowired
    public void setJacksonModule(Module aModule) {
        jacksonModule = aModule;
    }

    @Override
    protected void runInternal(Context context, C config, Future<Void> runFuture) throws Exception {
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
                XmlUtil.setContextProvider(jaxbContext);
                registerJacksonModule(jacksonModule);

                // Configure Services
                buildContext(context, config);
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

    protected abstract void buildContext(Context context, C config) throws Exception;

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
