/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.camel.context;

import org.apache.camel.CamelContext;
import org.eclipse.kapua.camel.context.setting.CamelContextSettingKey;
import org.eclipse.kapua.camel.context.setting.CamelContextSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class CamelContextInitializer extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(CamelContextInitializer.class);

    private ApplicationContext appContext;
    private CamelContextInitializer camelContextInitializer;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        camelContextInitializer = new CamelContextInitializer();
    }

    public CamelContextInitializer(String configurationResource, boolean isLocalConfigurationFile) {
        logger.info("Starting camel context");
        if (isLocalConfigurationFile) {
            logger.info("Starting camel context... loading configuation from local resource {}...", configurationResource);
            appContext = new ClassPathXmlApplicationContext(configurationResource);
            logger.info("Starting camel context... loading configuation from local resource {}... DONE", configurationResource);
        }
        else {
            logger.info("Starting camel context... loading configuation from external resource {}...", configurationResource);
            appContext = new FileSystemXmlApplicationContext(configurationResource);
            logger.info("Starting camel context... loading configuation from external resource {}... DONE", configurationResource);
        }
        logger.info("Starting camel context... initializing");
        CamelContext camelContext = appContext.getBean(CamelContext.class);
        logger.info("Starting camel context... initializing DONE");
        try {
            logger.info("Starting camel context... starting");
            camelContext.start();
            logger.info("Starting camel context... staring DONE");
            Thread.currentThread().join();
        } catch (Exception e) {
            logger.error("Cannot start camel context {}", e.getMessage(), e);
        }
    }

    public CamelContextInitializer() {
        this(CamelContextSettings.getInstance().getString(CamelContextSettingKey.CAMEL_CONTEXT_CONFIGURATION_RESOURCE), 
            CamelContextSettings.getInstance().getBoolean(CamelContextSettingKey.CAMEL_CONTEXT_IS_LOCAL_CONFIGURATION_RESOURCE));
    }

}
