/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.camel.application;

import org.eclipse.kapua.commons.core.ServiceModuleBundle;
import org.eclipse.kapua.locator.KapuaLocator;
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
    private String clientId;

    public KapuaApplication(String clientId) {
        this.clientId = clientId;
    }

    public void init() throws Exception {
        logger.info(">>> Kapua Application: calling init...");
        synchronized (KapuaApplication.class) {
            if (application == null) {
                application = KapuaLocator.getInstance().getService(ServiceModuleBundle.class);
            }
            application.startup(clientId);
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