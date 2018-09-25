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
package org.eclipse.kapua.processor.commons;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.kapua.commons.core.vertx.AbstractEBServer;
import org.eclipse.kapua.commons.core.vertx.EBServerConfig;

public class EBServerImpl extends AbstractEBServer {

    @Inject
    @Named("event-bus-server.default-address")
    private String defaultAddress;

    @Override
    public EBServerConfig getConfigs() {
        EBServerConfig configs = new EBServerConfig();
        configs.setAddress(defaultAddress);
        return configs;
    }
}
