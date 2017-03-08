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
 * Client information schema creator definition
 * 
 * @since 1.0
 *
 */
public interface ClientInfoCreator extends StorableCreator<ClientInfo>
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
     * Set the client identifier
     * 
     * @param clientId
     */
    public void setClientId(String clientId);

    /**
     * Get the message identifier (of the first message published by this client)
     * 
     * @return
     */
    public StorableId getMessageId();

    /**
     * Set the message identifier (of the first message published by this client)
     * 
     * @param messageId
     */
    public void setMessageId(StorableId messageId);

    /**
     * Get the message timestamp (of the first message published by this client)
     * 
     * @return
     */
    public Date getMessageTimestamp();

    /**
     * Set the message timestamp (of the first message published by this client)
     * 
     * @param messageTimestamp
     */
    public void setMessageTimestamp(Date messageTimestamp);
}
