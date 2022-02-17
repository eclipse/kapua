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
package org.eclipse.kapua.service.device.management.keystore.model.internal;

import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItems;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceKeystoreItems} implementation.
 *
 * @since 1.5.0
 */
public class DeviceKeystoreItemsImpl implements DeviceKeystoreItems {

    private static final long serialVersionUID = 1318691706440015345L;

    private List<DeviceKeystoreItem> keystoreItems;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceKeystoreItemsImpl() {
    }

    @Override
    public List<DeviceKeystoreItem> getKeystoreItems() {
        if (keystoreItems == null) {
            keystoreItems = new ArrayList<>();
        }

        return keystoreItems;
    }

    @Override
    public void addKeystoreItem(DeviceKeystoreItem keystoreItem) {
        getKeystoreItems().add(keystoreItem);
    }

    @Override
    public void setKeystoreItems(List<DeviceKeystoreItem> keystoreItems) {
        this.keystoreItems = keystoreItems;
    }
}
