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
package org.eclipse.kapua.service.assetstore.config.api;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.assetstore.api.DeviceAssetStoreXmlFactory;
import org.eclipse.kapua.service.device.management.app.config.DeviceAppManagementConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "deviceAssetStoreConfiguration")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceAssetStoreXmlFactory.class, factoryMethod = "newDeviceAssetStoreConfiguration")
public interface DeviceAssetStoreConfiguration extends DeviceAppManagementConfiguration {

    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getScopeId();

    void setScopeId(KapuaId kapuaId);

    @XmlElement(name = "deviceId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getDeviceId();

    void setDeviceId(KapuaId deviceId);

    @XmlElement(name = "storeEnablementPolicy")
    AssetStoreEnablementPolicy getAssetStoreEnablementPolicy();

    void setAssetStoreEnablementPolicy(AssetStoreEnablementPolicy assetStoreEnablementPolicy);
}
