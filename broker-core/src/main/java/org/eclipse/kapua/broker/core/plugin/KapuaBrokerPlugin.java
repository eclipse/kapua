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

import org.eclipse.kapua.commons.core.KapuaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaBrokerPlugin implements KapuaPlugin {

    private static final Logger logger = LoggerFactory.getLogger(KapuaBrokerPlugin.class);

    @Override
    public void start() {
        logger.info("Starting kapua broker plugin...");
        logger.info("Starting kapua broker plugin... DONE");
    }

    @Override
    public void stop() {
        logger.info("Stopping kapua broker plugin...");
        logger.info("Stopping kapua broker plugin... DONE");
    }

}
