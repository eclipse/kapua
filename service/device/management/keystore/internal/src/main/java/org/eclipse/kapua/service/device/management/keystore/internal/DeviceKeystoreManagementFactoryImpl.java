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
package org.eclipse.kapua.service.device.management.keystore.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementFactory;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSRInfo;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCertificate;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItemQuery;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItems;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreKeypair;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreSubjectAN;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystores;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreCSRImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreCSRInfoImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreCertificateImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreItemImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreItemQueryImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreItemsImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreKeypairImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreSubjectANImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoresImpl;

/**
 * {@link DeviceKeystoreManagementFactory} implementation.
 *
 * @since 1.5.0
 */
@KapuaProvider
public class DeviceKeystoreManagementFactoryImpl implements DeviceKeystoreManagementFactory {

    @Override
    public DeviceKeystores newDeviceKeystores() {
        return new DeviceKeystoresImpl();
    }

    @Override
    public DeviceKeystore newDeviceKeystore() {
        return new DeviceKeystoreImpl();
    }

    @Override
    public DeviceKeystoreItems newDeviceKeystoreItems() {
        return new DeviceKeystoreItemsImpl();
    }

    @Override
    public DeviceKeystoreItem newDeviceKeystoreItem() {
        return new DeviceKeystoreItemImpl();
    }

    @Override
    public DeviceKeystoreSubjectAN newDeviceKeystoreSubjectAN() {
        return new DeviceKeystoreSubjectANImpl();
    }

    @Override
    public DeviceKeystoreItemQuery newDeviceKeystoreItemQuery() {
        return new DeviceKeystoreItemQueryImpl();
    }

    @Override
    public DeviceKeystoreCertificate newDeviceKeystoreCertificate() {
        return new DeviceKeystoreCertificateImpl();
    }

    @Override
    public DeviceKeystoreKeypair newDeviceKeystoreKeypair() {
        return new DeviceKeystoreKeypairImpl();
    }

    @Override
    public DeviceKeystoreCSRInfo newDeviceKeystoreCSRInfo() {
        return new DeviceKeystoreCSRInfoImpl();
    }

    @Override
    public DeviceKeystoreCSR newDeviceKeystoreCSR() {
        return new DeviceKeystoreCSRImpl();
    }
}
