/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security.bean;

import org.eclipse.kapua.client.security.MessageListener;

public class ResponseContainer<O extends Response> {

    private String requestId;
    private O response;

    public ResponseContainer(String requestId) {
        this.requestId = requestId;
    }

    public O getResponse() {
        return response;
    }

    public void setResponse(O response) {
        this.response = response;
    }

    public String getRequestId() {
        return requestId;
    }

    public static <O extends Response> ResponseContainer<O> createAnRegisterNewMessageContainer(Request request) {
        ResponseContainer<O> messageContainer = new ResponseContainer<>(request.getRequestId());
        MessageListener.registerCallback(request.getRequestId(), messageContainer);
        return messageContainer;
    }
}
