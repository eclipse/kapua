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
package org.eclipse.kapua.consumer.telemetry;

import org.eclipse.kapua.consumer.commons.setting.ConsumerSettingKey;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * Telemetry application container main class
 *
 */
@ImportResource({"classpath:spring/applicationContext.xml"})
@PropertySource(value = "classpath:spring/application.properties")
@SpringBootApplication
public class TelemetryApplication {

    public TelemetryApplication() {
    }

    public void doNothing() {
        //spring needs a public constructor but our checkstyle doesn't allow a class with only static methods and a public constructor
    }

    public static void main(String[] args) {
        //statically set parameters
        System.setProperty(ConsumerSettingKey.JAXB_CONTEXT_CLASS_NAME.key(), TelemetryJAXBContextProvider.class.getName());
        //org.springframework.context.ApplicationContext is not needed now so don't keep the SpringApplication.run return
        SpringApplication.run(TelemetryApplication.class, args);
    }

}
