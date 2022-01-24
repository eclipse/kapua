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

import org.eclipse.kapua.client.security.bean.AccountRequest;
import org.eclipse.kapua.client.security.bean.AccountResponse;
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
        getAccount
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
     * Return the account infos giving the username
     * @param accountRequest
     * @return
     * @throws JMSException
     * @throws InterruptedException
     * @throws JsonProcessingException
     */
    public AccountResponse getAccount(AccountRequest accountRequest) throws JMSException, InterruptedException, JsonProcessingException;

}
