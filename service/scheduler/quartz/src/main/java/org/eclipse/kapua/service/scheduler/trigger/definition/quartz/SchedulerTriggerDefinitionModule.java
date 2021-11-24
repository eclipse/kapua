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
package org.eclipse.kapua.service.scheduler.trigger.definition.quartz;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;

public class SchedulerTriggerDefinitionModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(TriggerDefinitionFactory.class).to(TriggerDefinitionFactoryImpl.class);
        bind(TriggerDefinitionService.class).to(TriggerDefinitionServiceImpl.class);
    }
}
