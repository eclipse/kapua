/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/

package org.eclipse.kapua.app.console;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.model.config.metatype.*;
import org.eclipse.kapua.service.device.call.kura.model.bundle.KuraBundle;
import org.eclipse.kapua.service.device.call.kura.model.bundle.KuraBundles;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceComponentConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraBundleInfo;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackage;
import org.eclipse.kapua.service.device.call.kura.model.deploy.KuraDeploymentPackages;
import org.eclipse.kapua.service.device.call.kura.model.snapshot.KuraSnapshotIds;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

import javax.xml.bind.JAXBContext;

public class ConsoleJAXBContextProvider implements JAXBContextProvider {

    private JAXBContext context;

    @Override public JAXBContext getJAXBContext() throws KapuaException {
        try {
            if (context == null) {
                context = JAXBContextFactory.createContext(new Class<?>[] {
                        KuraDeviceComponentConfiguration.class,
                        KuraDeviceConfiguration.class,
                        KuraDeploymentPackage.class,
                        KuraDeploymentPackages.class,
                        KuraBundle.class,
                        KuraBundles.class,
                        KuraBundleInfo.class,
                        DevicePackages.class,
                        DeviceBundle.class,
                        DeviceBundles.class,
                        DeviceConfiguration.class,
                        DeviceComponentConfiguration.class,
                        KuraSnapshotIds.class,
                        DeviceSnapshots.class,
                        KapuaTocd.class,
                        KapuaTad.class,
                        KapuaTicon.class,
                        KapuaToption.class,
                        TscalarImpl.class,
                        MetatypeXmlRegistry.class
                }, null);
            }
            return context;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
