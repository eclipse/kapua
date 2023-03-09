/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.definition.quartz;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionRepository;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;

import javax.inject.Singleton;

public class SchedulerTriggerDefinitionModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(TriggerDefinitionFactory.class).to(TriggerDefinitionFactoryImpl.class);
    }

    @Provides
    @Singleton
    TriggerDefinitionService triggerDefinitionService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            TriggerDefinitionRepository triggerDefinitionRepository,
            TriggerDefinitionFactory triggerDefinitionFactory) {
        return new TriggerDefinitionServiceImpl(
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-scheduler")),
                triggerDefinitionRepository,
                triggerDefinitionFactory);
    }

    @Provides
    @Singleton
    TriggerDefinitionRepository triggerDefinitionRepository() {
        return new TriggerDefinitionImplJpaRepository();
    }
}
