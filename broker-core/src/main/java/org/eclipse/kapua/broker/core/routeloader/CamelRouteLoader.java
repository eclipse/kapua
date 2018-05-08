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
package org.eclipse.kapua.broker.core.routeloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.broker.core.BrokerJAXBContextProvider;
import org.eclipse.kapua.broker.core.router.CamelDefaultRouter;
import org.eclipse.kapua.broker.core.router.PlaceholderReplacer;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.eclipse.kapua.commons.setting.KapuaSettingException;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Custom routes loader implementation. It loads the routes configuration from the configuration file, creates and insert them into the Camel context.
 *
 */
public class CamelRouteLoader extends RouteBuilder {

    private final static Logger logger = LoggerFactory.getLogger(CamelDefaultRouter.class);
    private RouteContainer routeContainer;

    public CamelRouteLoader(CamelContext context) throws Exception {
        logger.info("Initializing Camel Routes Loader...");
        context.addRoutes(this);
        logger.info("Initializing Camel Routes Loader... DONE");
    }

    public CamelRouteLoader() throws Exception {
    }

    @Override
    public void configure() throws Exception {
        logger.info("Configuring Camel Routes Loader...");
        logger.info("Configuring Camel Routes Loader... Loading routes definition...");
        routeContainer = loadRoutes(BrokerSetting.getInstance().getString(BrokerSettingKey.CAMEL_ROUTE_LOADER_CONFIGURATION_FILE_NAME));
        logger.info("Configuring Camel Routes Loader... Loading routes definition... DONE");
        logger.info("Configuring Camel Routes Loader... Initializing routes...");
        initRoutes();
        logger.info("Configuring Camel Routes Loader... Initializing routes... DONE");
        logger.info("Configuring Camel Routes Loader... DONE");
    }

    private void initRoutes() {
        for (Route route : routeContainer.getRoute()) {
            logger.info("Configuring Camel Routes Loader... Initializing route {}...", route.getId());
            Map<String, Object> ac = loadApplicationContext();
            try {
                if (getContext().getRoute(route.getId()) == null) {
                    RouteDefinition rd = from(PlaceholderReplacer.replacePlaceholder(route.getFrom(), ac));
                    route.appendBrickDefinition(rd, getContext(), ac);// routeCollection.getRoutes().add(rd);
                    logger.info("Configuring Camel Routes Loader {}", rd);
                    logger.info("Configuring Camel Routes Loader... Initializing route {}... DONE", route.getId());
                } else {
                    logger.info("Configuring Camel Routes Loader... Initializing route {}... Route already present. nothing to do!... DONE", route.getId());
                }
            } catch (UnsupportedOperationException e) {
                logger.error("Cannot create route {} since the route is not a main route! please check the configuration...", route.getId(), e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Cannot create route {} - Error: {}", route.getId(), e.getMessage(), e);
            }
        }
        printRoutes();
    }

    private void printRoutes() {
        List<org.apache.camel.Route> routeList = getContext().getRoutes();
        if (routeList != null) {
            logger.info("Route list");
            for (org.apache.camel.Route r : routeList) {
                logger.info("          id {}", r.getId());
            }
        }
        List<RouteDefinition> routedefinitionsList = getContext().getRouteDefinitions();
        if (routedefinitionsList != null) {
            logger.info("Route definition list");
            for (RouteDefinition r : routedefinitionsList) {
                logger.info("                    id {}", r.getId());
            }
        }
    }

    /**
     * Load routes configuration from file
     * 
     * @param configurationFile
     * @return
     */
    public static RouteContainer loadRoutes(String configurationFile) {
        URL url = null;
        RouteContainer routeContainer = null;
        try {
            url = KapuaFileUtils.getAsURL(configurationFile);
        } catch (KapuaSettingException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot find configuration file!");
        }
        logger.info("Default Camel routing... Loading configuration from file {}", url.getFile());
        FileReader configurationFileReader = null;
        try {
            XmlUtil.setContextProvider(new BrokerJAXBContextProvider());
            configurationFileReader = new FileReader(url.getFile());
            routeContainer = XmlUtil.unmarshal(configurationFileReader, RouteContainer.class);
            logger.info("Default Camel route loaded! [{}]", configurationFile);
            logLoadedRoute(routeContainer);
        } catch (XMLStreamException | JAXBException | SAXException | IOException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load configuration!");
        } finally {
            if (configurationFileReader != null) {
                try {
                    configurationFileReader.close();
                } catch (IOException e) {
                    logger.warn("Cannot close configuration file '{}'!", configurationFile, e);
                }
            }
        }
        return routeContainer;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<String, Object> loadApplicationContext() {
        String propertyFile = BrokerSetting.getInstance().getString(BrokerSettingKey.CAMEL_ROUTE_LOADER_CUSTOM_CONTEXT_FILE_NAME);
        if (!StringUtils.isEmpty(propertyFile)) {
            URL url = null;
            try {
                url = KapuaFileUtils.getAsURL(propertyFile);
            } catch (KapuaSettingException e) {
                throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot find configuration file!");
            }
            Properties properties = new Properties();
            try (InputStream is = new FileInputStream(new File(url.toString()))){
                properties.load(is);
                return (Map) properties;
            } catch (IOException e) {
                logger.warn("Cannot load application properties: {}", e.getMessage(), e);
                return new HashMap<>();
            }
        }
        else {
            return new HashMap<>();
        }
    }

    private static void logLoadedRoute(RouteContainer routes) {
        StringBuffer buffer = new StringBuffer();
        for (Route route : routes.getRoute()) {
            route.toLog(buffer, "");
        }
        logger.info(buffer.toString());
    }

}
