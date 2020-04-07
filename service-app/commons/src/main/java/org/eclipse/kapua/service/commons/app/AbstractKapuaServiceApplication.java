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

/**
 * This class defines the base class for a Vertx based application that uses Kapua services. It initializes
 * the context required by the application and applies the DB schema update.
 * 
 * @param <C> the configuration class associated with your own Vertx based application
 */
public abstract class AbstractKapuaServiceApplication<C extends Configuration> extends BaseApplication<C> {

    Logger logger = LoggerFactory.getLogger(AbstractKapuaServiceApplication.class);

    private JAXBContextProvider jaxbContext;
    private Module jacksonModule;

    /**
     * Sets the JAXB context of the application. JAXB is mainly used to serialize/deserialize the Kapua entities.
     * Must be called before {@link #init(InitContext,Future)} and {@link #run(Context,C,Future)} are executed.
     * 
     * @param aContext the JAXBContextProvider used by the Kapua-Service application
     */
    @Autowired
    public void setJAXBContextProvider(JAXBContextProvider aContext) {
        jaxbContext = aContext;
    }

    /**
     * @param aModule
     */
    @Qualifier("kapuaServiceApp")
    @Autowired
    public void setJacksonModule(Module aModule) {
        jacksonModule = aModule;
    }


    /**
     * Derived classes are not allowed to override this method. Their business logic must be implemented by
     * overriding {@link #initInternal(Context,C)}.
     *
     * @param context the application initialization context
     * @throws Exception
     */
    @Override
    public final void init(InitContext<C> context) throws Exception {

    }

    /**
     * Executes Kapua specific initialization logic then invokes {@link runInternal(Context,C)}. 
     * Since initialization logic may block the caller, execution runs within an executeBlocking section.
     * 
     * @param context the application initialization context
     * @param initFuture a future that should be called when run is complete
     * @throws Exception
     */
    @Override
    public final void init(InitContext<C> context, Future<Void> initFuture) throws Exception {
        Objects.requireNonNull(context, "param: context");
        Objects.requireNonNull(initFuture, "param: runFuture");

        context.getVertx().executeBlocking(runExecution -> {
            try {
                initInternal(context);
                runExecution.complete();
            } catch (Exception e) {
                runExecution.fail(e);
            }
        }, resultHandler -> {
            if (resultHandler.succeeded()) {
                initFuture.complete();
            } else {
                initFuture.fail(resultHandler.cause());
            }
        });

    }

    /**
     * Derived classes are not allowed to override this method. Their business logic must be implemented by
     * overriding {@link #runInternal(Context,C)}.
     *
     * @param context the application context
     * @param config the application configuration
     * @throws Exception
     */
    @Override
    public final void run(Context context, C config) throws Exception {

    }

    /**
     * Executes Kapua specific run logic like the DB schema update, inits the security context and the {@link XMLUtil} 
     * serialization utility then invokes {@link runInternal(Context,C)}. 
     * Since DB schema update and Shiro initialization may block the caller, execution runs within an 
     * executeBlocking section.
     * 
     * @param context the application context
     * @param config the application configuration
     * @param runFuture a future that should be called when run is complete
     * @throws Exception
     */
    @Override
    public final void run(Context context, C config, Future<Void> runFuture) throws Exception {
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
                runInternal(context, config);
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

    /**
     * Derived classes may implement this method to provide tier own business logic.
     * 
     * @param context
     * @throws Exception
     */
    protected void initInternal(InitContext<C> context) throws Exception {
    }

    /**
     * Derived classes must implement this method to provide their own business logic.
     * 
     * @param context
     * @param config
     * @throws Exception
     */
    protected abstract void runInternal(Context context, C config) throws Exception;

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
