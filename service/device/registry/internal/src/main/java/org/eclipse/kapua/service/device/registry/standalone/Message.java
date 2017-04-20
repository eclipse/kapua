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
package org.eclipse.kapua.service.device.registry.standalone;

import org.eclipse.kapua.model.id.KapuaId;

import java.util.List;

public class Message {

    KapuaId tenant;

    String operation;

    List<Object> arguments;

    public Message(KapuaId tenant, String operation, List<Object> arguments) {
        this.tenant = tenant;
        this.operation = operation;
        this.arguments = arguments;
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
}
