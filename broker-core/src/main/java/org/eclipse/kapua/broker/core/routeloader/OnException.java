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
package org.eclipse.kapua.broker.core.routeloader;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.model.RouteDefinition;

/**
 * On route exception definition
 *
 */
public interface OnException {

    /**
     * Append the specific component logic to the Camel processor definition
     * 
     * @param routeDefinition
     * @param camelContext
     * @param applicationContext
     */
    void appendExceptionDefinition(RouteDefinition routeDefinition, CamelContext camelContext, Map<String, Object> applicationContext);

    /**
     * Append the brick to the buffer in a human readable fashion
     * 
     * @param buffer
     * @param prefix
     */
    void toLog(StringBuffer buffer, String prefix);

}
