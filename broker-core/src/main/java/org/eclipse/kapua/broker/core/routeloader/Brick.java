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
import org.apache.camel.model.ProcessorDefinition;
import org.eclipse.kapua.KapuaException;

/**
 * Base Camel brick definition
 *
 */
public interface Brick {

    /**
     * Get the route id
     * 
     * @return
     */
    String getId();

    /**
     * Set the route id
     * 
     * @param id
     */
    void setId(String id);

    /**
     * Append the specific brick logic to the Camel processor definition
     * 
     * @param processorDefinition
     * @param camelContext
     * @param applicationContext
     * @throws UnsupportedOperationException
     *             if the specific ProcessorDefinition instance is not supported
     */
    void appendBrickDefinition(ProcessorDefinition<?> processorDefinition, CamelContext camelContext, Map<String, Object> applicationContext) throws UnsupportedOperationException, KapuaException;

    /**
     * Append the brick to the buffer in a human readable fashion
     * 
     * @param buffer
     * @param prefix
     */
    void toLog(StringBuffer buffer, String prefix);

}
