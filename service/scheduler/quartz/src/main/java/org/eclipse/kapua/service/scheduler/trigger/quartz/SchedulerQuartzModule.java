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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerRepository;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;

import javax.inject.Singleton;

public class SchedulerQuartzModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(TriggerFactory.class).to(TriggerFactoryImpl.class);
    }

    @Provides
    @Singleton
    TriggerService triggerService() {
        return new TriggerServiceImpl();
    }

    @Provides
    @Singleton
    TriggerRepository triggerRepository() {
        return new TriggerImplJpaRepository();
    }
}
