/*******************************************************************************
 * Copyright (c) 2017, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.commons.core.LifecycleComponent;
import org.eclipse.kapua.commons.core.ServiceRegistration;
import org.eclipse.kapua.commons.locator.ComponentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ComponentProvider
public class KapuaBrokerPlugin implements LifecycleComponent {

    private static final Logger logger = LoggerFactory.getLogger(KapuaBrokerPlugin.class);

    @Override
    public void onRegisterServices(ServiceRegistration registration) {
        return;
    }

    @Override
    public void start() {
        logger.info("Starting...");
        logger.info("Starting... DONE");
    }

    @Override
    public void stop() {
        logger.info("Stopping...");
        logger.info("Stopping... DONE");
    }

}
