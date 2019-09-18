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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Channel information schema definition
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { //
        "id", //
        "scopeId", //
        "clientId", //
        "name", //
        "firstMessageId", //
        "firstMessageOn", //
        "lastMessageId", //
        "lastMessageOn" //
})
public interface ChannelInfo extends Storable {

    /**
     * Get the record identifier
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(StorableIdAdapter.class)
    StorableId getId();

    /**
     * Set the record identifier
     *
     * @param id
     * @since 1.0.0
     */
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(StorableIdAdapter.class)
    void setId(StorableId id);

    /**
     * Get the scope id
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getScopeId();

    /**
     * Get the client identifier
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "clientId")
    String getClientId();

    /**
     * Set the client identifier
     *
     * @param clientId
     * @since 1.0.0
     */
    @XmlElement(name = "clientId")
    void setClientId(String clientId);

    /**
     * Get the channel name
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Set the channel name
     *
     * @param name
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Get the message identifier (of the first message published on this channel)
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "firstMessageId")
    @XmlJavaTypeAdapter(StorableIdAdapter.class)
    StorableId getFirstMessageId();

    /**
     * Set the message identifier (of the first message published on this channel)
     *
     * @param firstMessageId
     * @since 1.0.0
     */
    void setFirstMessageId(StorableId firstMessageId);

    /**
     * Get the message timestamp (of the first message published on this channel)
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "firstMessageOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getFirstMessageOn();

    /**
     * Set the message timestamp (of the first message published on this channel)
     *
     * @param firstMessageOn
     */
    void setFirstMessageOn(Date firstMessageOn);

    /**
     * Get the message identifier of the last published message for this channel.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "lastMessageId")
    @XmlJavaTypeAdapter(StorableIdAdapter.class)
    StorableId getLastMessageId();

    /**
     * Set the message identifier of the last published message for this channel.<br>
     * <b>Transient data field (the last publish message identifier should get from the message table by the find service)</b>
     *
     * @param lastMessageId
     * @since 1.0.0
     */
    void setLastMessageId(StorableId lastMessageId);

    /**
     * Get the timestamp of the last published message for this channel.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "lastMessageOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getLastMessageOn();

    /**
     * Set the timestamp of the last published message for this channel.<br>
     * <b>Transient data field (the last publish timestamp should get from the message table by the find service)</b>
     *
     * @param lastMessageOn
     * @since 1.0.0
     */
    void setLastMessageOn(Date lastMessageOn);

}
