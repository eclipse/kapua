/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.plugin.security;

import java.util.concurrent.TimeUnit;

import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.impl.AddressInfo;
import org.apache.activemq.artemis.core.server.plugin.ActiveMQServerPlugin;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.localevent.ExecutorWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressLogger implements ActiveMQServerPlugin {

    protected static final Logger logger = LoggerFactory.getLogger(AddressLogger.class);

    private static final long RUN_PERIOD = 30;
    private static final long RUN_DELAY = 30;
    private String pluginName;
    private long ttl;

    private ExecutorWrapper addressCleanupWrapper;

    //TODO inject!!! (check when the registered method in the startup chain. is the ServerContext already initialized?)
    protected ServerContext serverContext;

    public AddressLogger() throws KapuaException {
        super();
        pluginName = this.getClass().getName();
    }

    @Override
    public void registered(ActiveMQServer server) {
        logger.info("registering plugin {}...", pluginName);
        try {
            initializePlugin();
        } catch (Exception error) {
            logger.error("Failed to start!", error);
        }
        logger.info("registering {}... DONE", pluginName);
    }

    private void initializePlugin() {
        serverContext = ServerContext.getInstance();
        logger.info(">>> {} calling start...", pluginName);
        //assume to have just one ttl configuration for destinations?
        ttl = serverContext.getServer().getAddressSettingsRepository().getMatch("#").getAutoDeleteAddressesDelay();
        logger.info(">>> ttl (all addresses) {}", ttl);
        // Setup the periodic task
        addressCleanupWrapper = new ExecutorWrapper(pluginName, this::logAddress, RUN_DELAY, RUN_PERIOD, TimeUnit.SECONDS);
        addressCleanupWrapper.start();
        logger.info(">>> {} configuration: delay {} - running every {} secs", pluginName, RUN_DELAY, RUN_PERIOD);
        logger.info(">>> {} calling start... DONE", pluginName);
    }

    @Override
    public void unregistered(ActiveMQServer server) {
        logger.info(">>> {} calling stop...", pluginName);
        if (addressCleanupWrapper != null) {
            logger.info(">>> {} calling stop... IN PROGRESS", pluginName);
            addressCleanupWrapper.stop();
        }
        logger.info(">>> {} calling stop... DONE", pluginName);
    }

    private void logAddress() {
        logger.info(">>> {} (running every {} secs) - Start scan...", pluginName, RUN_PERIOD);
        cleanAddress(serverContext.getServer());
        logger.info(">>> {} - Done!", pluginName);
    }

    private void cleanAddress(ActiveMQServer server) {
        StringBuilder build = new StringBuilder();
        server.getPostOffice().getAddresses().forEach(address -> {
            AddressInfo addressInfo = server.getAddressInfo(address);
            String addressStr = addressInfo.getName().toString();
            logger.debug("Evaluating address: {}", addressInfo.getName());
            try {
                build.append("Processing address: ").append(addressStr).append("\n");
                if (server.getPostOffice().isAddressBound(address)) {
                    server.getPostOffice().listQueuesForAddress(address).forEach(queue -> {
                        build.append("\tqueue associated: ").append(queue.toString()).append(" ").append(queue.getAddress().toString()).append("\n");
                        queue.getConsumers().forEach(consumer ->{
                            build.append("\t\tconsumer: routing: ").
                                append(consumer.getBinding().getRoutingName()).append(" - unique name: ").
                                append(consumer.getBinding().getUniqueName()).append(" - cluster: ").
                                append(consumer.getBinding().getClusterName()).append(" - address: ").
                                append(consumer.getBinding().getAddress()).append("\n");
                        });
                    });
                }
                else {
                    build.append("\tnot bound to any queue!\n");
                }
            } catch (Exception e) {
                logger.warn("Error deleting address {}", addressStr, e);
            }
        });
        logger.info("{}", build.toString());
    }

}