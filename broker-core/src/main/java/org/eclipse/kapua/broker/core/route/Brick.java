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
package org.eclipse.kapua.broker.core.route;

import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;

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
     * @throws UnsupportedOperationException
     *             if the specific ProcessorDefinition instance is not supported
     */
    void appendBrickDefinition(ProcessorDefinition<?> processorDefinition, CamelContext camelContext) throws UnsupportedOperationException;

    void toLog(StringBuffer buffer, String prefix);

}
