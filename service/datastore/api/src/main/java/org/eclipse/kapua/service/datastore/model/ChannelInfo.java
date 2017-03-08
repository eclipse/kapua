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
 * Channel information schema definition
 * 
 * @since 1.0
 *
 */
public interface ChannelInfo extends Storable
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
     * Get the message identifier (of the first message published on this channel)
     * 
     * @return
     */
    public StorableId getFirstPublishedMessageId();

    /**
     * Set the message identifier (of the first message published on this channel)
     * 
     * @param firstPublishedMessageId
     */
    public void setFirstPublishedMessageId(StorableId firstPublishedMessageId);

    /**
     * Get the message timestamp (of the first message published on this channel)
     * 
     * @return
     */
    public Date getFirstPublishedMessageTimestamp();

    /**
     * Set the message timestamp (of the first message published on this channel)
     * 
     * @param firstPublishedMessageTimestamp
     */
    public void setFirstPublishedMessageTimestamp(Date firstPublishedMessageTimestamp);

    /**
     * Get the message identifier of the last published message for this channel.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     * 
     * @return
     */
    public StorableId getLastPublishedMessageId();

    /**
     * Set the message identifier of the last published message for this channel.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     * 
     * @param lastPublishedMessageId
     */
    public void setLastPublishedMessageId(StorableId lastPublishedMessageId);

    /**
     * Get the timestamp of the last published message for this channel.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     * 
     * @return
     */
    public Date getLastPublishedMessageTimestamp();

    /**
     * Set the timestamp of the last published message for this channel.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     * 
     * @param lastPublishedMessageTimestamp
     */
    public void setLastPublishedMessageTimestamp(Date lastPublishedMessageTimestamp);

}
