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
 * Client information schema definition
 * 
 * @since 1.0.0
 */
public interface ClientInfo extends Storable {

    /**
     * Get the record identifier
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public StorableId getId();

    /**
     * Get the scope id
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
     * Set the client identifier
     * 
     * @param clientId
     * 
     * @since 1.0.0
     */
    public void setClientId(String clientId);

    /**
     * Get the message identifier (of the first message published by this client)
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public StorableId getFirstMessageId();

    /**
     * Set the message identifier (of the first message published by this client)
     * 
     * @param firstMessageId
     * 
     * @since 1.0.0
     */
    public void setFirstMessageId(StorableId firstMessageId);

    /**
     * Get the message timestamp (of the first message published by this client)
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public Date getFirstMessageOn();

    /**
     * Set the message timestamp (of the first message published by this client)
     * 
     * @param firstMessageOn
     * 
     * @since 1.0.0
     */
    public void setFirstMessageOn(Date firstMessageOn);

    /**
     * Get the message identifier of the last published message for this client.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public StorableId getLastMessageId();

    /**
     * Set the message identifier of the last published message for this client.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     * 
     * @param lastMessageId
     * 
     * @since 1.0.0
     */
    public void setLastMessageId(StorableId lastMessageId);

    /**
     * Get the identifier of the last published message for this client.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public Date getLastMessageOn();

    /**
     * Set the timestamp of the last published message for this client.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     * 
     * @param lastMessageOn
     * 
     * @since 1.0.0
     */
    public void setLastMessageOn(Date lastMessageOn);
}
