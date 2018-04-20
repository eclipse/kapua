/*******************************************************************************
 * Copyright (c) 2011, 2017 RedHat and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     RedHat
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.standalone;

import org.eclipse.kapua.model.id.KapuaId;

import java.util.List;

/**
 * Represents request message passed to the service. Also allows to send response to the caller.
 */
public class Request {

    private final KapuaId tenant;

    private final String operation;

    private final List<Object> arguments;

    private Object response;

    public Request(KapuaId tenant, String operation, List<Object> arguments) {
        this.tenant = tenant;
        this.operation = operation;
        this.arguments = arguments;
    }

    void response(Object response) {
        this.response = response;
    }

    public KapuaId getTenant() {
        return tenant;
    }

    public String getOperation() {
        return operation;
    }

    public List<Object> getArguments() {
        return arguments;
    }

    public Object getResponse() {
        return response;
    }

}