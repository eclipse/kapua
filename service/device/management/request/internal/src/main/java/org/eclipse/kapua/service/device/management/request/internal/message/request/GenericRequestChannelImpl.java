/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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
