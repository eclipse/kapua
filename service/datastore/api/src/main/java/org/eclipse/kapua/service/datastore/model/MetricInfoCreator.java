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

/**
 * Metric information schema creator definition
 * 
 * @since 1.0
 *
 */
public interface MetricInfoCreator extends StorableCreator<MetricInfo>
{

    /**
     * Get the account
     * 
     * @return
     */
    public String getAccount();

    /**
     * Get the client identifier
     * 
     * @return
     */
    public String getClientId();

    /**
     * Get the channel
     * 
     * @return
     */
    public String getChannel();

    /**
     * Set the channel
     * 
     * @param channel
     */
    public void setChannel(String channel);

    /**
     * Get the metric name
     * 
     * @return
     */
    public String getName();

    /**
     * Set the metric name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get the metric type
     * 
     * @return
     */
    public String getType();

    /**
     * Set the metric type
     * 
     * @param type
     */
    public void setType(String type);

    /**
     * Get the metric value
     * 
     * @param clazz metric value type
     * @return
     */
    public <T> T getValue(Class<T> clazz);

    /**
     * Set the metric value
     * 
     * @param value
     */
    public <T> void setValue(T value);

    /**
     * Get the message identifier (of the first message published that containing this metric)
     * 
     * @return
     */
    public StorableId getMessageId();

    /**
     * Set the message identifier (of the first message published that containing this metric)
     * 
     * @param messageId
     */
    public void setMessageId(StorableId messageId);

    /**
     * Get the message timestamp (of the first message published that containing this metric)
     * 
     * @return
     */
    public Date getMessageTimestamp();

    /**
     * Set the message timestamp (of the first message published that containing this metric)
     * 
     * @param messageTimestamp
     */
    public void setMessageTimestamp(Date messageTimestamp);
}
