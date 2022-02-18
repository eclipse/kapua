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
package org.eclipse.kapua.service.scheduler.trigger.fired.quartz;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerService;

public class SchedulerTriggerFiredModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(FiredTriggerFactory.class).to(FiredTriggerFactoryImpl.class);
        bind(FiredTriggerService.class).to(FiredTriggerServiceImpl.class);
    }
}
