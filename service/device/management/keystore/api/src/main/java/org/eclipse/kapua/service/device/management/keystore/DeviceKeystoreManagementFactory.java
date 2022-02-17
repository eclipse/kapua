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
package org.eclipse.kapua.service.device.management.keystore;

import org.eclipse.kapua.model.KapuaObjectFactory;
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

/**
 * {@link DeviceKeystore} {@link KapuaObjectFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaObjectFactory
 * @since 1.5.0
 */
public interface DeviceKeystoreManagementFactory extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link DeviceKeystores}.
     *
     * @return The newly instantiated {@link DeviceKeystores}.
     * @since 1.5.0
     */
    DeviceKeystores newDeviceKeystores();

    /**
     * Instantiates a new {@link DeviceKeystore}.
     *
     * @return The newly instantiated {@link DeviceKeystore}.
     * @since 1.5.0
     */
    DeviceKeystore newDeviceKeystore();

    /**
     * Instantiates a new {@link DeviceKeystoreItems}.
     *
     * @return The newly instantiated {@link DeviceKeystoreItems}.
     * @since 1.5.0
     */
    DeviceKeystoreItems newDeviceKeystoreItems();

    /**
     * Instantiates a new {@link DeviceKeystoreItem}.
     *
     * @return The newly instantiated {@link DeviceKeystoreItem}.
     * @since 1.5.0
     */
    DeviceKeystoreItem newDeviceKeystoreItem();

    /**
     * Instantiates a new {@link DeviceKeystoreSubjectAN}.
     *
     * @return The newly instantiated {@link DeviceKeystoreSubjectAN}.
     * @since 1.5.0
     */
    DeviceKeystoreSubjectAN newDeviceKeystoreSubjectAN();

    /**
     * Instantiates a new {@link DeviceKeystoreItemQuery}.
     *
     * @return The newly instantiated {@link DeviceKeystoreItemQuery}.
     * @since 1.5.0
     */
    DeviceKeystoreItemQuery newDeviceKeystoreItemQuery();

    /**
     * Instantiates a new {@link DeviceKeystoreCertificate}.
     *
     * @return The newly instantiated {@link DeviceKeystoreCertificate}.
     * @since 1.5.0
     */
    DeviceKeystoreCertificate newDeviceKeystoreCertificate();

    /**
     * Instantiates a new {@link DeviceKeystoreKeypair}.
     *
     * @return The newly instantiated {@link DeviceKeystoreKeypair}.
     * @since 1.5.0
     */
    DeviceKeystoreKeypair newDeviceKeystoreKeypair();

    /**
     * Instantiates a new {@link DeviceKeystoreCSRInfo}.
     *
     * @return The newly instantiated {@link DeviceKeystoreCSRInfo}.
     * @since 1.5.0
     */
    DeviceKeystoreCSRInfo newDeviceKeystoreCSRInfo();

    /**
     * Instantiates a new {@link DeviceKeystoreCSR}.
     *
     * @return The newly instantiated {@link DeviceKeystoreCSR}.
     * @since 1.5.0
     */
    DeviceKeystoreCSR newDeviceKeystoreCSR();
}
