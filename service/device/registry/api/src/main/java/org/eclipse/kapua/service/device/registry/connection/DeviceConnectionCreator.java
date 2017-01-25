/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.authentication.credential.Credential;

/**
 * {@link DeviceConnection} creator definition.
 * 
 * @since 1.0.0
 *
 */
@XmlRootElement(name = "deviceConnection")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "clientId", "credentialId", "protocol", "clientIp", "serverIp" })
public interface DeviceConnectionCreator extends KapuaUpdatableEntityCreator<DeviceConnection> {

    /**
     * Get the client identifier
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "clientId")
    public String getClientId();

    /**
     * Set the client identifier
     * 
     * @param clientId
     * @since 1.0.0
     */
    public void setClientId(String clientId);

    /**
     * Gets the {@link Credential} id.
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "credentialId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getCredentialId();

    /**
     * Set the {@link Credential} id.
     * 
     * @param credentialId
     * @since 1.0.0
     */
    public void setCredentialId(KapuaId credentialId);

    /**
     * Get the device protocol
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "protocol")
    public String getProtocol();

    /**
     * Set the device protocol
     * 
     * @param protocol
     * @since 1.0.0
     */
    public void setProtocol(String protocol);

    /**
     * Get the client ip
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "clientIp")
    public String getClientIp();

    /**
     * Set the client ip
     * 
     * @param clientIp
     * @since 1.0.0
     */
    public void setClientIp(String clientIp);

    /**
     * Get the server ip
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "serverIp")
    public String getServerIp();

    /**
     * Set the server ip
     * 
     * @param serverIp
     * @since 1.0.0
     */
    public void setServerIp(String serverIp);
}
