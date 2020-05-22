/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.camel.datamessage;

import org.eclipse.kapua.camel.context.CamelContextInitializer;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelDataMessageMainTest {

    private static final Logger logger = LoggerFactory.getLogger(CamelDataMessageMainTest.class);

    private CamelDataMessageMainTest() {
    }

    public static void main(String argv[]) {
        try {
            System.setProperty("certificate.jwt.private.key", "file:///./certificates/key.pk8");
            System.setProperty("certificate.jwt.certificate", "file:///./resources/certificates/cert.pem");
            System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");

            logger.info("Starting camel context... starting");
            XmlUtil.setContextProvider(new DefaultRoutingJAXBContextProvider());
            new CamelContextInitializer("src/test/resources/configuration/camel.xml", false);
            logger.info("Starting camel context... staring DONE");
            Thread.currentThread().join();
        } catch (Exception e) {
            logger.error("Cannot start camel context {}", e.getMessage(), e);
        }
    }
}
