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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

/**
 * Channel information schema definition
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "id",
                        "scopeId",
                        "clientId",
                        "channel",
                        "firstMessageId",
                        "firstMessageOn",
                        "lastMessageId",
                        "lastMessageOn"})
public interface ChannelInfo extends Storable {

    /**
     * Get the record identifier
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(StorableIdAdapter.class)
    public StorableId getId();

    /**
     * Get the scope id
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getScopeId();

    /**
     * Get the client identifier
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "clientId")
    public String getClientId();

    /**
     * Get the channel
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "channel")
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
     * Get the message identifier (of the first message published on this channel)
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "firstMessageId")
    @XmlJavaTypeAdapter(StorableIdAdapter.class)
    public StorableId getFirstMessageId();

    /**
     * Set the message identifier (of the first message published on this channel)
     * 
     * @param firstMessageId
     * 
     * @since 1.0.0
     */
    public void setFirstMessageId(StorableId firstMessageId);

    /**
     * Get the message timestamp (of the first message published on this channel)
     * 
     * @return
     * 
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "firstMessageOn")
    public Date getFirstMessageOn();

    /**
     * Set the message timestamp (of the first message published on this channel)
     * 
     * @param firstMessageOn
     */
    public void setFirstMessageOn(Date firstMessageOn);

    /**
     * Get the message identifier of the last published message for this channel.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "lastMessageId")
    @XmlJavaTypeAdapter(StorableIdAdapter.class)
    
    public StorableId getLastMessageId();

    /**
     * Set the message identifier of the last published message for this channel.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     * 
     * @param lastMessageId
     * 
     * @since 1.0.0
     */
    public void setLastMessageId(StorableId lastMessageId);

    /**
     * Get the timestamp of the last published message for this channel.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "lastMessageOn")
    public Date getLastMessageOn();

    /**
     * Set the timestamp of the last published message for this channel.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     * 
     * @param lastMessageOn
     * 
     * @since 1.0.0
     */
    public void setLastMessageOn(Date lastMessageOn);

}
