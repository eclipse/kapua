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

import com.google.common.base.Strings;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItemQuery;

/**
 * {@link DeviceKeystoreItemQuery} implementation.
 *
 * @since 1.5.0
 */
public class DeviceKeystoreItemQueryImpl implements DeviceKeystoreItemQuery {

    private String keystoreId;
    private String alias;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceKeystoreItemQueryImpl() {
    }

    @Override
    public String getKeystoreId() {
        return keystoreId;
    }

    @Override
    public void setKeystoreId(String keystoreId) {
        this.keystoreId = keystoreId;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean hasFilters() {
        return !Strings.isNullOrEmpty(getKeystoreId()) ||
                !Strings.isNullOrEmpty(getAlias());
    }
}
