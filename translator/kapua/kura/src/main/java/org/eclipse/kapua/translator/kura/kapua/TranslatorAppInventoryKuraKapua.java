/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.model.inventory.InventoryMetrics;
import org.eclipse.kapua.service.device.call.kura.model.inventory.KuraInventoryPackages;
import org.eclipse.kapua.service.device.call.kura.model.inventory.bundles.inventory.KuraInventoryBundles;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryResponseChannel;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryResponseMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryResponsePayload;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryPackage;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link KuraResponseMessage} to {@link InventoryResponseMessage}
 *
 * @since 1.0.0
 */
public class TranslatorAppInventoryKuraKapua extends AbstractSimpleTranslatorResponseKuraKapua<InventoryResponseChannel, InventoryResponsePayload, InventoryResponseMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    public TranslatorAppInventoryKuraKapua() {
        super(InventoryResponseMessage.class, InventoryResponsePayload.class);
    }

    @Override
    protected InventoryResponseChannel translateChannel(KuraResponseChannel kuraResponseChannel) throws InvalidChannelException {
        try {
            TranslatorKuraKapuaUtils.validateKuraResponseChannel(kuraResponseChannel, InventoryMetrics.APP_ID, InventoryMetrics.APP_VERSION);

            return new InventoryResponseChannel();
        } catch (Exception e) {
            throw new InvalidChannelException(e, kuraResponseChannel);
        }
    }

    @Override
    protected InventoryResponsePayload translatePayload(KuraResponsePayload kuraResponsePayload) throws InvalidPayloadException {
        try {
            InventoryResponsePayload inventoryResponsePayload = super.translatePayload(kuraResponsePayload);

            if (kuraResponsePayload.hasBody()) {
                KuraInventoryPackages inventoryPackages = readJsonBodyAs(kuraResponsePayload.getBody(), KuraInventoryPackages.class);

                if (!inventoryPackages.getInventoryPackages().isEmpty()) {
                    inventoryResponsePayload.setDeviceInventory(translate(inventoryPackages));

                    return inventoryResponsePayload;
                }

                KuraInventoryBundles inventoryBundles = readJsonBodyAs(kuraResponsePayload.getBody(), KuraInventoryBundles.class);
                if (!inventoryBundles.getInventoryBundles().isEmpty()) {
                    inventoryResponsePayload.setDeviceInventoryBundles(translate(inventoryBundles));

                    return inventoryResponsePayload;
                }
            }

            return inventoryResponsePayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraResponsePayload);
        }
    }

    /**
     * Translates {@link KuraInventoryPackages} to {@link DeviceInventory}
     *
     * @param inventoryPackages The {@link KuraInventoryPackages} to translate.
     * @return The translated {@link DeviceInventory}.
     * @since 1.0.0
     */
    private DeviceInventory translate(KuraInventoryPackages inventoryPackages) {
        DeviceInventoryManagementFactory deviceInventoryFactory = LOCATOR.getFactory(DeviceInventoryManagementFactory.class);
        DeviceInventory deviceInventory = deviceInventoryFactory.newDeviceInventory();

        inventoryPackages.getInventoryPackages().forEach(systemPackage -> {
            DeviceInventoryPackage deviceInventoryPackage = deviceInventoryFactory.newDeviceInventoryPackage();
            deviceInventoryPackage.setName(systemPackage.getName());
            deviceInventoryPackage.setVersion(systemPackage.getVersion());
            deviceInventoryPackage.setPackageType(systemPackage.getType());

            deviceInventory.addInventoryPackage(deviceInventoryPackage);
        });

        return deviceInventory;
    }

    /**
     * Translates {@link KuraInventoryBundles} to {@link DeviceInventoryBundles}
     *
     * @param inventoryBundles The {@link KuraInventoryBundles} to translate.
     * @return The translated {@link DeviceInventoryBundles}.
     * @since 1.0.0
     */
    private DeviceInventoryBundles translate(KuraInventoryBundles inventoryBundles) {
        DeviceInventoryManagementFactory deviceInventoryFactory = LOCATOR.getFactory(DeviceInventoryManagementFactory.class);
        DeviceInventoryBundles deviceInventoryBundles = deviceInventoryFactory.newDeviceInventoryBundles();

        inventoryBundles.getInventoryBundles().forEach(systemPackage -> {
            DeviceInventoryBundle deviceInventoryBundle = deviceInventoryFactory.newDeviceInventoryBundle();
            deviceInventoryBundle.setId(systemPackage.getId());
            deviceInventoryBundle.setName(systemPackage.getName());
            deviceInventoryBundle.setVersion(systemPackage.getVersion());
            deviceInventoryBundle.setStatus(systemPackage.getState());

            deviceInventoryBundles.addInventoryBundle(deviceInventoryBundle);
        });

        return deviceInventoryBundles;
    }
}
