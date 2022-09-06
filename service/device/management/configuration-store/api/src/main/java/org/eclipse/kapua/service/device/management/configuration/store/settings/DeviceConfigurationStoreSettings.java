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
package org.eclipse.kapua.service.device.management.configuration.store.settings;

import org.eclipse.kapua.service.device.management.app.settings.ByDeviceAppManagementSettings;
import org.eclipse.kapua.service.device.management.configuration.store.DeviceConfigurationStoreService;
import org.eclipse.kapua.service.device.management.configuration.store.DeviceConfigurationStoreXmlFactory;
import org.eclipse.kapua.service.device.registry.Device;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link DeviceConfigurationStoreSettings} definition.
 * <p>
 * It represents settings of {@link DeviceConfigurationStoreService} by {@link Device}
 *
 * @since 2.0.0
 */
@XmlRootElement(name = "deviceConfigurationStoreSettings")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceConfigurationStoreXmlFactory.class, factoryMethod = "newDeviceConfigurationStoreSettings")
public interface DeviceConfigurationStoreSettings extends ByDeviceAppManagementSettings {

    /**
     * Gets the {@link DeviceConfigurationStoreEnablementPolicy}
     *
     * @return The {@link DeviceConfigurationStoreEnablementPolicy}
     * @since 2.0.0
     */
    @XmlElement(name = "enablementPolicy")
    DeviceConfigurationStoreEnablementPolicy getEnablementPolicy();

    /**
     * Sets the {@link DeviceConfigurationStoreEnablementPolicy}
     *
     * @param enablementPolicy The {@link DeviceConfigurationStoreEnablementPolicy}
     * @since 2.0.0
     */
    void setEnablementPolicy(DeviceConfigurationStoreEnablementPolicy enablementPolicy);
}
