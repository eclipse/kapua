/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event.dummy;

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.ServiceBundle;
import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.locator.inject.MultiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 0.3.0
 *
 */
@ComponentProvider
@MultiService(provides=ServiceBundle.class)
public class DummyEventsBundle implements ServiceBundle {

    private final static Logger logger = LoggerFactory.getLogger(DummyEventsBundle.class);

    @Inject private DummyEventBus eventBus;
    
    public DummyEventsBundle() {
    }

    @Override
    public void start() {
        logger.info("Starting...");
        eventBus.start();
        logger.info("Starting...DONE");
    }

    @Override
    public void stop() {
        logger.info("Stopping...");
        eventBus.stop();
        logger.info("Stopping...DONE");
    }
}
