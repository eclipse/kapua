/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.packages.model.internal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfo;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageBundleInfos;

/**
 * Package bundle informations list container.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "bundleInfos")
@XmlAccessorType(XmlAccessType.FIELD)
public class DevicePackageBundleInfosImpl implements DevicePackageBundleInfos {

    @XmlElement(name = "bundleInfo")
    List<DevicePackageBundleInfo> bundleInfos;

    @Override
    public List<DevicePackageBundleInfo> getBundlesInfos() {
        if (bundleInfos == null) {
            bundleInfos = new ArrayList<>();
        }
        return bundleInfos;
    }

}
