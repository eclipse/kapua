/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.security;

import javax.jms.JMSException;

import org.eclipse.kapua.client.security.bean.EntityRequest;
import org.eclipse.kapua.client.security.bean.EntityResponse;
import org.eclipse.kapua.client.security.bean.AuthRequest;
import org.eclipse.kapua.client.security.bean.AuthResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Security service
 *
 */
public interface ServiceClient {

    public enum SecurityAction {
        brokerConnect,
        brokerDisconnect,
        getEntity
    }

    public enum EntityType {
        account,
        user
    }

    public enum ResultCode {
        authorized,
        notAuthorized
    }

    /**
     * Broker connect logic
     * @param authRequest
     * @return
     * @throws InterruptedException
     * @throws JMSException
     * @throws JsonProcessingException
     */
    public AuthResponse brokerConnect(AuthRequest authRequest) throws InterruptedException, JMSException, JsonProcessingException;

    /**
     * Broker disconnect logic
     * @param authRequest
     * @return
     * @throws JMSException
     * @throws InterruptedException
     * @throws JsonProcessingException
     */
    public AuthResponse brokerDisconnect(AuthRequest authRequest) throws JMSException, InterruptedException, JsonProcessingException;

    /**
     * Return the entity id and scope id giving the entity name (supported entities are user and account. which one will be chosen depends on action field)
     * @param entityRequest
     * @return
     * @throws JMSException
     * @throws InterruptedException
     * @throws JsonProcessingException
     */
    public EntityResponse getEntity(EntityRequest entityRequest) throws JMSException, InterruptedException, JsonProcessingException;

}
