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
package org.eclipse.kapua.service.datastore.model;

import java.util.Date;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * Metric information schema creator definition
 * 
 * @since 1.0.0
 */
public interface MetricInfoCreator extends StorableCreator<MetricInfo> {

    /**
     * Get the account
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public KapuaId getScopeId();

    /**
     * Get the client identifier
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public String getClientId();

    /**
     * Get the channel
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public String getChannel();

    /**
     * Set the channel
     * 
     * @param channel
     * 
     * @since 1.0.0
     */
    public void setChannel(String channel);

    /**
     * Get the metric name
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public String getName();

    /**
     * Set the metric name
     * 
     * @param name
     * 
     * @since 1.0.0
     */
    public void setName(String name);

    /**
     * Get the metric type
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public String getType();

    /**
     * Set the metric type
     * 
     * @param type
     * 
     * @since 1.0.0
     */
    public void setType(String type);

    /**
     * Get the metric value
     * 
     * @param clazz
     *            metric value type
     * @return
     * 
     * @since 1.0.0
     */
    public Object getValue();
    
    /**
     * Get the metric value casted to the given class
     * 
     * @param clazz
     *            metric value type
     * @return
     * 
     * @since 1.0.0
     */
    public <T> T getCastedValue(Class<T> clazz);

    /**
     * Set the metric value
     * 
     * @param value
     * 
     * @since 1.0.0
     */
    public <T> void setValue(T value);

    /**
     * Get the message identifier (of the first message published that containing this metric)
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public StorableId getMessageId();

    /**
     * Set the message identifier (of the first message published that containing this metric)
     * 
     * @param messageId
     * 
     * @since 1.0.0
     */
    public void setMessageId(StorableId messageId);

    /**
     * Get the message timestamp (of the first message published that containing this metric)
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public Date getMessageTimestamp();

    /**
     * Set the message timestamp (of the first message published that containing this metric)
     * 
     * @param messageTimestamp
     * 
     * @since 1.0.0
     */
    public void setMessageTimestamp(Date messageTimestamp);
}
