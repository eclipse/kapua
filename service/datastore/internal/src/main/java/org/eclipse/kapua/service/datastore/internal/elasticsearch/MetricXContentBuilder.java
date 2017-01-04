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

import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * Container for the metric information content builder
 * 
 * @since 1.0
 *
 */
public class MetricXContentBuilder
{

    private String          id;
    private XContentBuilder content;

    /**
     * Get the metric identifier
     * 
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * Get the metric content builder
     * 
     * @return
     */
    public XContentBuilder getContent()
    {
        return content;
    }

    /**
     * Set the metric identifier
     * 
     * @param id
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Set the metric content builder
     * 
     * @param content
     */
    public void setContent(XContentBuilder content)
    {
        this.content = content;
    }

}
