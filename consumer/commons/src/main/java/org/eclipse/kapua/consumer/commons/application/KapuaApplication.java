/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.commons.application;

import org.eclipse.kapua.commons.core.ServiceModuleBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service Module handler<br>
 *
 * @since 1.3
 */
public class KapuaApplication {

    private static final Logger logger = LoggerFactory.getLogger(KapuaApplication.class);

    private static ServiceModuleBundle application;

    public void init() throws Exception {
        logger.info(">>> Kapua Application: calling init...");
        synchronized (KapuaApplication.class) {
            if (application == null) {
                application = new ServiceModuleBundle() {

                };
            }
            application.startup();
        }
        logger.info(">>> Kapua Application: calling init... DONE");
    }

    public void destroy() throws Exception {
        logger.info(">>> Kapua Application: calling destroy...");
        synchronized (KapuaApplication.class) {
            if (application != null) {
                application.shutdown();
                application = null;
            }
        }
        logger.info(">>> Kapua Application: calling destroy... DONE");
    }

}