/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.model.container.internal;

import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainer;

/**
 * {@link DeviceInventoryContainer} implementation.
 *
 * @since 2.0.0
 */
public class DeviceInventoryContainerImpl implements DeviceInventoryContainer {

    private String name;
    private String version;
    private String containerType;

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    public DeviceInventoryContainerImpl() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getContainerType() {
        return containerType;
    }

    @Override
    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }
}
