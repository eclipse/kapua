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
 * Information about device metric value. Metric is an arbitrary named value. We usually
 * keep only the most recent value of the metric.
 * 
 * @since 1.0
 */
public interface MetricInfo extends Storable
{
    /**
     * Get the record identifier
     * 
     * @return
     */
    public StorableId getId();

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
    public StorableId getFirstPublishedMessageId();

    /**
     * Set the message identifier (of the first message published that containing this metric)
     * 
     * @param firstPublishedMessageId
     */
    public void setFirstPublishedMessageId(StorableId firstPublishedMessageId);

    /**
     * Get the message timestamp (of the first message published that containing this metric)
     * 
     * @return
     */
    public Date getFirstPublishedMessageTimestamp();

    /**
     * Set the message timestamp (of the first message published that containing this metric)
     * 
     * @param firstPublishedMessageTimestamp
     */
    public void setFirstPublishedMessageTimestamp(Date firstPublishedMessageTimestamp);

    /**
     * Get the message identifier of the last published message for this metric.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     * 
     * @return
     */
    public StorableId getLastPublishedMessageId();

    /**
     * Set the message identifier of the last published message for this metric.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     * 
     * @param lastPublishedMessageId
     */
    public void setLastPublishedMessageId(StorableId lastPublishedMessageId);

    /**
     * Get the timestamp of the last published message for this metric.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     * 
     * @return
     */
    public Date getLastPublishedMessageTimestamp();

    /**
     * Set the timestamp of the last published message for this metric.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     * 
     * @param lastPublishedMessageTimestamp
     */
    public void setLastPublishedMessageTimestamp(Date lastPublishedMessageTimestamp);
}
