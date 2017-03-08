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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

/**
 * Metric value object.<br>
 * 
 * @since 1.0
 *
 */
public class EsMetric
{
    private String name;
    private String type;

    /**
     * Get the metric name
     * 
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the metric type
     * 
     * @return
     */
    public String getType()
    {
        return type;
    }

    /**
     * Set the metric name
     * 
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Set the metric type
     * 
     * @param type
     */
    public void setType(String type)
    {
        this.type = type;
    }

}
