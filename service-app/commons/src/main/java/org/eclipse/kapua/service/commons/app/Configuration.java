/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.app;

import org.eclipse.kapua.service.commons.http.HttpMonitorServiceConfig;

/**
 * This class defines the basic configuration info required by a Vertx based application. It can be extended 
 * to provide configuration info required by your own application.
 * Configuration info should be structured in such a way that they can be read from a properties file, yaml file
 * or JSON file.
 */
public interface Configuration {

    /**
     * @return a string representing the application name
     */
    public String getApplicationName();

    /**
     * @return the startup timeout in milliseconds
     */
    public long getStartupTimeout();

    public VertxConfig getVertxConfig();

    public HttpMonitorServiceConfig getHttpMonitorServiceConfig();
}