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
package org.eclipse.kapua.service.scheduler;

import org.eclipse.kapua.service.KapuaDomainService;
import org.eclipse.kapua.service.KapuaService;

public interface SchedulerService extends KapuaService, KapuaDomainService<SchedulerDomain> {

    public static final SchedulerDomain SCHEDULER_DOMAIN = new SchedulerDomain();

    @Override
    public default SchedulerDomain getServiceDomain() {
        return SCHEDULER_DOMAIN;
    }

}
