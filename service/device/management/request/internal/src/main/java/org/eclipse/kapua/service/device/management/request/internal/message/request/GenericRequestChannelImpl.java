/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import java.util.ArrayList;
import java.util.List;

/**
 * {@link GenericRequestChannel} implementation.
 *
 * @since 1.0.0
 */
public class GenericRequestChannelImpl extends KapuaRequestChannelImpl implements GenericRequestChannel {

    private static final long serialVersionUID = -5140230399807797717L;

    private List<String> resources;

    @Override
    public List<String> getResources() {
        if (resources == null) {
            resources = new ArrayList<>();
        }

        return resources;
    }

    @Override
    public void setResources(List<String> resources) {
        this.resources = resources;
    }
}
