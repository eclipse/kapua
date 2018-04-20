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
package org.eclipse.kapua.broker;

import org.eclipse.kapua.service.KapuaDomainService;
import org.eclipse.kapua.service.KapuaService;

public interface BrokerService extends KapuaService, KapuaDomainService {

    public static final BrokerDomain BROKER_DOMAIN = new BrokerDomain();

    @Override
    public default BrokerDomain getServiceDomain() {
        return BROKER_DOMAIN;
    }
}
