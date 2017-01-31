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

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.SubjectType;
import org.eclipse.kapua.service.authentication.credential.Credential;

/**
 * {@link DeviceConnection} entity definition.<br>
 * {@link DeviceConnection} represent a connection to the message broker.<br>
 * {@link DeviceConnection} is identified by the {@link #getClientId()} within a scope.
 * 
 * @since 1.0.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public interface DeviceConnection extends KapuaUpdatableEntity {

    public static final String TYPE = "deviceConnection";

    public default String getType() {
        return TYPE;
    }

    /**
     * Gets the {@link DeviceConnection} current {@link DeviceConnectionStatus}.
     * 
     * @return The {@link DeviceConnection} current {@link DeviceConnectionStatus}.
     * @since 1.0.0
     */
    @XmlElement(name = "status")
    public DeviceConnectionStatus getStatus();

    /**
     * Sets the {@link DeviceConnection} current {@link DeviceConnectionStatus}.
     * 
     * @param connectionStatus
     *            The {@link DeviceConnection} current {@link DeviceConnectionStatus}.
     * @since 1.0.0
     */
    public void setStatus(DeviceConnectionStatus connectionStatus);

    /**
     * Get the client id used to connect to the message broker.
     * 
     * @return The client id used to connect to the message broker.
     * @since 1.0.0
     */
    @XmlElement(name = "clientId")
    public String getClientId();

    /**
     * Set the client id used to connect to the message broker.
     * 
     * @param clientId
     *            The client id to set for this {@link DeviceConnection}.
     * @since 1.0.0
     */
    public void setClientId(String clientId);

    /**
     * Gets the {@link Credential} id that this {@link DeviceConnection} must use when connecting to the message broker.<br>
     * If set to {@code null} this connection can use any {@link Credential} that has {@link Credential#getSubject()} type set to {@link SubjectType#BROKER_CONNECTION}.
     * 
     * @return The {@link Credential} id that this {@link DeviceConnection} must use when connecting to the message broker.
     * @since 1.0.0
     */
    @XmlElement(name = "credentialId")
    public KapuaId getCredentialId();

    /**
     * Sets the {@link Credential} id that this {@link DeviceConnection} must use when connecting to the message broker.
     * 
     * @param credentialId
     *            The {@link Credential} id that this {@link DeviceConnection} must use when connecting to the message broker.
     * @since 1.0.0
     */
    public void setCredentialId(KapuaId credentialId);

    /**
     * Gets the {@link Credential} id used to establish the last successful {@link DeviceConnection}.
     * 
     * @return The {@link Credential} id used to establish the last successful {@link DeviceConnection}.
     * @since 1.0.0
     */
    @XmlElement(name = "lastCredentialId")
    public KapuaId getLastCredentialId();

    /**
     * Sets the {@link Credential} id used to establish the last successful {@link DeviceConnection}.
     * 
     * @param credentialId
     *            The {@link Credential} id used to establish the last successful {@link DeviceConnection}.
     * @since 1.0.0
     */
    public void setLastCredentialId(KapuaId lastCredentialId);

    /**
     * Gets the protocol used to connect to the message broker.
     * 
     * @return The protocol used to connect to the message broker.
     * @since 1.0.0
     */
    @XmlElement(name = "protocol")
    public String getProtocol();

    /**
     * Sets the protocol used to connect to the message broker.
     * 
     * @param protocol
     *            The protocol used to connect to the message broker.
     * @since 1.0.0
     */
    public void setProtocol(String protocol);

    /**
     * Gets the remote IP address from which the {@link DeviceConnection} is established.
     * 
     * @return The remote IP address from which the {@link DeviceConnection} is established.
     * @since 1.0.0
     */
    @XmlElement(name = "clientIp")
    public String getClientIp();

    /**
     * Sets the remote IP address for this {@link DeviceConnection}.
     * 
     * @param clientIp
     *            The remote IP address for this {@link DeviceConnection}.
     * @since 1.0.0
     */
    public void setClientIp(String clientIp);

    /**
     * Gets the server IP address to which this {@link DeviceConnection} is connected to.
     * 
     * @return The server IP address to which this {@link DeviceConnection} is connected to.
     * @since 1.0.0
     */
    @XmlElement(name = "serverIp")
    public String getServerIp();

    /**
     * Sets the server IP address for this {@link DeviceConnection}.
     * 
     * @param serverIp
     *            The server IP address for this {@link DeviceConnection}.
     * @since 1.0.0
     */
    public void setServerIp(String serverIp);
}
