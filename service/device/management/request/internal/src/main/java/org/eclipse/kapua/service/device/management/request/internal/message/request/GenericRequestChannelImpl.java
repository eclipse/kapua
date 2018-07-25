/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.request.internal.message.request;

import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestChannelImpl;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;

public class GenericRequestChannelImpl extends KapuaRequestChannelImpl implements GenericRequestChannel {

    private String[] resources;

    @Override
    public String[] getResources() {
        return resources;
    }

    @Override
    public void setResources(String[] resources) {
        this.resources = resources;
    }
}
