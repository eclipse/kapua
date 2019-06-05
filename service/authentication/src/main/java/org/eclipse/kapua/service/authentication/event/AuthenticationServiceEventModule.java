/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class AuthenticationServiceEventModule extends ServiceEventModule implements KapuaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceEventModule.class);

    private static final String UNIQUE_ID = "_" + UUID.randomUUID().toString();

    @Inject
    private AuthenticationEventService authenticationEventService;

    protected ServiceEventModuleConfiguration initializeConfiguration() {
        String address = "lifecycleEvent";//TODO get from configuration
        List<ServiceEventClientConfiguration> secc = new ArrayList<>();
        secc.addAll(ServiceInspector.getEventBusClients(authenticationEventService, AuthenticationEventService.class));
        return new ServiceEventModuleConfiguration(
                address,
                AuthorizationEntityManagerFactory.getInstance(),
                updateClientConfiguration(secc, UNIQUE_ID));
    }

    protected ServiceEventClientConfiguration[] updateClientConfiguration(List<ServiceEventClientConfiguration> configs, String uniqueId) {
        ArrayList<ServiceEventClientConfiguration> configList = new ArrayList<>();
        //TODO check if passing null here is an allowed case
        if (configs==null) {
            configs = new ArrayList<>();
        }
        for(ServiceEventClientConfiguration config : configs) {
            if(config.getEventListener() == null) {
                // config for @RaiseServiceEvent
                LOGGER.debug("Adding config for @RaiseServiceEvent - address: {}, name: {}, listener: {}", config.getAddress(), config.getClientName(), config.getEventListener());
                configList.add(config);
            } else {
                // config for @ListenServiceEvent
                String uniqueName = config.getClientName() + uniqueId;
                LOGGER.debug("Adding config for @ListenServiceEvent - address: {}, name: {}, listener: {}", config.getAddress(), uniqueName, config.getEventListener());
                configList.add(new ServiceEventClientConfiguration(config.getAddress(), uniqueName, config.getEventListener()));
            }
        }
        return configList.toArray(new ServiceEventClientConfiguration[0]);
    }

}
