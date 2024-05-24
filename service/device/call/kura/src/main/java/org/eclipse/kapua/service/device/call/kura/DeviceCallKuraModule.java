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
package org.eclipse.kapua.service.device.call.kura;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Singleton;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.core.JaxbClassProvider;
import org.eclipse.kapua.service.device.call.DeviceCallFactory;
import org.eclipse.kapua.service.device.call.DeviceMessageFactory;
import org.eclipse.kapua.service.device.call.kura.model.inventory.KuraInventoryItem;
import org.eclipse.kapua.service.device.call.kura.model.inventory.KuraInventoryItems;
import org.eclipse.kapua.service.device.call.kura.model.inventory.bundles.KuraInventoryBundle;
import org.eclipse.kapua.service.device.call.kura.model.inventory.bundles.KuraInventoryBundles;
import org.eclipse.kapua.service.device.call.kura.model.inventory.containers.KuraInventoryContainer;
import org.eclipse.kapua.service.device.call.kura.model.inventory.containers.KuraInventoryContainers;
import org.eclipse.kapua.service.device.call.kura.model.inventory.packages.KuraInventoryPackage;
import org.eclipse.kapua.service.device.call.kura.model.inventory.system.KuraInventorySystemPackage;
import org.eclipse.kapua.service.device.call.kura.model.inventory.system.KuraInventorySystemPackages;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettings;

import com.google.inject.multibindings.Multibinder;

public class DeviceCallKuraModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(DeviceCallFactory.class).to(KuraDeviceCallFactoryImpl.class).in(Singleton.class);
        bind(DeviceMessageFactory.class).to(KuraMessageFactoryImpl.class).in(Singleton.class);
        bind(DeviceCallSettings.class).in(Singleton.class);
        final Multibinder<JaxbClassProvider> jaxbClassProviderMultibinder = Multibinder.newSetBinder(binder(), JaxbClassProvider.class);
        jaxbClassProviderMultibinder.addBinding()
                .toInstance(new JaxbClassProvider() {

                    @Override
                    public Collection<Class<?>> getClasses() {
                        return Arrays.asList(
                                KuraInventoryBundle.class,
                                KuraInventoryBundles.class,
                                KuraInventoryContainer.class,
                                KuraInventoryContainers.class,
                                KuraInventoryItem.class,
                                KuraInventoryItems.class,
                                KuraInventoryPackage.class,
                                KuraInventorySystemPackage.class,
                                KuraInventorySystemPackages.class
                        );
                    }
                });
    }
}
