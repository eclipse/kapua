/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event.module;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.ServiceEventStoreHouseKeeperJob;
import org.eclipse.kapua.commons.event.ServiceMap;
import org.eclipse.kapua.commons.event.bus.ServiceEventBusManager;
import org.eclipse.kapua.service.event.ServiceEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base {@link ServiceModule} implementation to be used by the modules that listen for events.
 * 
 * @since 1.0
 *
 */
public abstract class ServiceEventModule implements ServiceModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEventModule.class);

    private final static int MAX_WAIT_LOOP_ON_SHUTDOWN = 30;
    private final static int SCHEDULED_EXECUTION_TIME_WINDOW = 30;
    private final static long WAIT_TIME = 1000;

    private ServiceEventModuleConfiguration serviceEventModuleConfiguration;

    private ScheduledExecutorService houseKeeperScheduler;
    private ScheduledFuture<?> houseKeeperHandler;
    private ServiceEventStoreHouseKeeperJob houseKeeperJob;

    protected abstract ServiceEventModuleConfiguration initializeConfiguration();

    private String getSubscriptionName(String address, String subscriber) {
        return String.format("%s-%s", address, subscriber);
    }

    @Override
    public void start() throws KapuaException {
        LOGGER.info("Starting service event module... {}", this.getClass().getName());
        LOGGER.info("Starting service event module... initialize configurations");
        serviceEventModuleConfiguration = initializeConfiguration();
        LOGGER.info("Starting service event module... initialize event bus");
        ServiceEventBus eventbus = ServiceEventBusManager.getInstance();
        LOGGER.info("Starting service event module... initialize event subscriptions");
        if (serviceEventModuleConfiguration.getServiceEventListenerConfigurations() != null) {
            for (ServiceEventListenerConfiguration selc : serviceEventModuleConfiguration.getServiceEventListenerConfigurations()) {
                // Listen to upstream service events
                eventbus.subscribe(selc.getAddress(), getSubscriptionName(selc.getAddress(), selc.getSubscriberName()), selc.getEventListener());
            }
        } else {
            LOGGER.info("Configuration subscriptions are missing. No subscriptions added!");
        }

        // register events to the service map
        LOGGER.info("Starting service event module... register services names");
        ServiceMap.registerServices(serviceEventModuleConfiguration.getInternalAddress(), serviceEventModuleConfiguration.getServicesNames());

        // Start the House keeper
        LOGGER.info("Starting service event module... start housekeeper");
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);
        houseKeeperJob = new ServiceEventStoreHouseKeeperJob(serviceEventModuleConfiguration.getEntityManagerFactory(), eventbus, serviceEventModuleConfiguration.getInternalAddress(),
                serviceEventModuleConfiguration.getServicesNames());
        // Start time can be made random from 0 to 30 seconds
        houseKeeperHandler = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, SCHEDULED_EXECUTION_TIME_WINDOW, SCHEDULED_EXECUTION_TIME_WINDOW, TimeUnit.SECONDS);
        LOGGER.info("Starting service event module... DONE");
    }

    @Override
    public void stop() throws KapuaException {
        LOGGER.info("Stopping service event module... {}", this.getClass().getName());
        LOGGER.info("Stopping service event module... house keeper scheduler [step 1/3]");
        if (houseKeeperJob != null) {
            houseKeeperJob.stop();
        }
        else {
            LOGGER.warn("Cannot shutdown the housekeeper scheduler [step 1/3] since it is null (initialization may not be successful)");
        }
        LOGGER.info("Stopping service event module... house keeper scheduler [step 2/3]");
        if (houseKeeperHandler != null) {
            int waitLoop = 0;
            while (houseKeeperHandler.isDone()) {
                try {
                    Thread.sleep(WAIT_TIME);
                } catch (InterruptedException e) {
                    // do nothing
                }
                if (waitLoop++ > MAX_WAIT_LOOP_ON_SHUTDOWN) {
                    LOGGER.warn("Cannot cancel the house keeper task afeter a while!");
                    break;
                }
            }
        }
        else {
            LOGGER.warn("Cannot shutdown the housekeeper scheduler [step 2/3] since it is null (initialization may not be successful)");
        }
        LOGGER.info("Stopping service event module... house keeper scheduler [step 3/3]");
        if (houseKeeperScheduler != null) {
            houseKeeperScheduler.shutdown();
        }
        else {
            LOGGER.warn("Cannot shutdown the housekeeper scheduler [step 3/3] since it is null (initialization may not be successful)");
        }
        LOGGER.info("Stopping service event module... unregister services names");
        if (serviceEventModuleConfiguration != null) {
            ServiceMap.unregisterServices(serviceEventModuleConfiguration.getServicesNames());
        } else {
            LOGGER.warn("Cannot unregister services since configuration is null (initialization may not be successful)");
        }
        LOGGER.info("Stopping service event module... DONE");
    }

}