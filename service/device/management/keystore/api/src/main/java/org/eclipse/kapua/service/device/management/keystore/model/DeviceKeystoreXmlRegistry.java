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
package org.eclipse.kapua.service.device.management.keystore.model;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementFactory;

/**
 * {@link DeviceKeystore} XmlFactory definition.
 *
 * @since 1.5.0
 */
public class DeviceKeystoreXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceKeystoreManagementFactory factory = locator.getFactory(DeviceKeystoreManagementFactory.class);

    /**
     * Instantiates a new {@link DeviceKeystores}.
     *
     * @return The newly instantiated {@link DeviceKeystores}
     * @since 1.5.0
     */
    DeviceKeystores newDeviceKeystores() {
        return factory.newDeviceKeystores();
    }

    /**
     * Instantiates a new {@link DeviceKeystore}.
     *
     * @return The newly instantiated {@link DeviceKeystore}
     * @since 1.5.0
     */
    DeviceKeystore newDeviceKeystore() {
        return factory.newDeviceKeystore();
    }

    /**
     * Instantiates a new {@link DeviceKeystoreItems}.
     *
     * @return The newly instantiated {@link DeviceKeystoreItems}
     * @since 1.5.0
     */
    DeviceKeystoreItems newDeviceKeystoreItems() {
        return factory.newDeviceKeystoreItems();
    }

    /**
     * Instantiates a new {@link DeviceKeystoreItem}.
     *
     * @return The newly instantiated {@link DeviceKeystoreItem}
     * @since 1.5.0
     */
    DeviceKeystoreItem newDeviceKeystoreItem() {
        return factory.newDeviceKeystoreItem();
    }

    /**
     * Instantiates a new {@link DeviceKeystoreSubjectAN}.
     *
     * @return The newly instantiated {@link DeviceKeystoreSubjectAN}
     * @since 1.5.0
     */
    DeviceKeystoreSubjectAN newDeviceKeystoreSubjectAN() {
        return factory.newDeviceKeystoreSubjectAN();
    }

    /**
     * Instantiates a new {@link DeviceKeystoreItemQuery}.
     *
     * @return The newly instantiated {@link DeviceKeystoreItemQuery}
     * @since 1.5.0
     */
    DeviceKeystoreItemQuery newDeviceKeystoreItemQuery() {
        return factory.newDeviceKeystoreItemQuery();
    }

    /**
     * Instantiates a new {@link DeviceKeystoreCertificate}.
     *
     * @return The newly instantiated {@link DeviceKeystoreCertificate}
     * @since 1.5.0
     */
    DeviceKeystoreCertificate newDeviceKeystoreCertificate() {
        return factory.newDeviceKeystoreCertificate();
    }

    /**
     * Instantiates a new {@link DeviceKeystoreKeypair}.
     *
     * @return The newly instantiated {@link DeviceKeystoreKeypair}
     * @since 1.5.0
     */
    DeviceKeystoreKeypair newDeviceKeystoreKeypair() {
        return factory.newDeviceKeystoreKeypair();
    }

    /**
     * Instantiates a new {@link DeviceKeystoreCSRInfo}.
     *
     * @return The newly instantiated {@link DeviceKeystoreCSRInfo}
     * @since 1.5.0
     */
    DeviceKeystoreCSRInfo newDeviceKeystoreCSRInfo() {
        return factory.newDeviceKeystoreCSRInfo();
    }

    /**
     * Instantiates a new {@link DeviceKeystoreCSR}.
     *
     * @return The newly instantiated {@link DeviceKeystoreCSR}
     * @since 1.5.0
     */
    DeviceKeystoreCSR newDeviceKeystoreCSR() {
        return factory.newDeviceKeystoreCSR();
    }
}
