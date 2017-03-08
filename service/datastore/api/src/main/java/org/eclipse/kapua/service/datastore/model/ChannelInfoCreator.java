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
 * Channel information schema creator definition
 * 
 * @since 1.0
 *
 */
public interface ChannelInfoCreator extends StorableCreator<ChannelInfo>
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
     * Get the message identifier (of the first message published on this channel)
     * 
     * @return
     */
    public StorableId getMessageId();

    /**
     * Set the message identifier (of the first message published on this channel)
     * 
     * @param messageId
     */
    public void setMessageId(StorableId messageId);

    /**
     * Get the message timestamp (of the first message published on this channel)
     * 
     * @return
     */
    public Date getMessageTimestamp();

    /**
     * Set the message timestamp (of the first message published on this channel)
     * 
     * @param messageTimestamp
     */
    public void setMessageTimestamp(Date messageTimestamp);
}
