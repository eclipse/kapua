/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.kura.model.inventory.InventoryMetrics;
import org.eclipse.kapua.service.device.call.kura.model.inventory.KuraInventoryItems;
import org.eclipse.kapua.service.device.call.kura.model.inventory.bundles.KuraInventoryBundles;
import org.eclipse.kapua.service.device.call.kura.model.inventory.packages.KuraInventoryPackages;
import org.eclipse.kapua.service.device.call.kura.model.inventory.system.KuraInventorySystemPackages;
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
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventoryItem;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.packages.DeviceInventoryPackage;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackage;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackages;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;

/**
 * {@link org.eclipse.kapua.translator.Translator} implementation from {@link KuraResponseMessage} to {@link InventoryResponseMessage}
 *
 * @since 1.5.0
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
                KuraInventoryItems inventoryItems = readJsonBodyAs(kuraResponsePayload.getBody(), KuraInventoryItems.class);

                if (!inventoryItems.getInventoryItems().isEmpty()) {
                    inventoryResponsePayload.setDeviceInventory(translate(inventoryItems));

                    return inventoryResponsePayload;
                }

                KuraInventoryBundles inventoryBundles = readJsonBodyAs(kuraResponsePayload.getBody(), KuraInventoryBundles.class);
                if (!inventoryBundles.getInventoryBundles().isEmpty()) {
                    inventoryResponsePayload.setDeviceInventoryBundles(translate(inventoryBundles));

                    return inventoryResponsePayload;
                }

                KuraInventorySystemPackages inventorySystemPackages = readJsonBodyAs(kuraResponsePayload.getBody(), KuraInventorySystemPackages.class);
                if (!inventorySystemPackages.getSystemPackages().isEmpty()) {
                    inventoryResponsePayload.setDeviceInventorySystemPackages(translate(inventorySystemPackages));

                    return inventoryResponsePayload;
                }

                KuraInventoryPackages inventoryPackages = readJsonBodyAs(kuraResponsePayload.getBody(), KuraInventoryPackages.class);
                if (!inventoryPackages.getPackages().isEmpty()) {
                    inventoryResponsePayload.setDeviceInventoryPackages(translate(inventoryPackages));

                    return inventoryResponsePayload;
                }
            }

            return inventoryResponsePayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraResponsePayload);
        }
    }

    /**
     * Translates {@link KuraInventoryItems} to {@link DeviceInventory}
     *
     * @param kuraInventoryItems The {@link KuraInventoryItems} to translate.
     * @return The translated {@link DeviceInventory}.
     * @since 1.5.0
     */
    private DeviceInventory translate(KuraInventoryItems kuraInventoryItems) {
        DeviceInventoryManagementFactory deviceInventoryFactory = LOCATOR.getFactory(DeviceInventoryManagementFactory.class);
        DeviceInventory deviceInventory = deviceInventoryFactory.newDeviceInventory();

        kuraInventoryItems.getInventoryItems().forEach(kuraInventoryItem -> {
            DeviceInventoryItem deviceInventoryItem = deviceInventoryFactory.newDeviceInventoryItem();
            deviceInventoryItem.setName(kuraInventoryItem.getName());
            deviceInventoryItem.setVersion(kuraInventoryItem.getVersion());
            deviceInventoryItem.setItemType(kuraInventoryItem.getType());

            deviceInventory.addInventoryItem(deviceInventoryItem);
        });

        return deviceInventory;
    }

    /**
     * Translates {@link KuraInventoryBundles} to {@link DeviceInventoryBundles}
     *
     * @param kuraInventoryBundles The {@link KuraInventoryBundles} to translate.
     * @return The translated {@link DeviceInventoryBundles}.
     * @since 1.5.0
     */
    private DeviceInventoryBundles translate(KuraInventoryBundles kuraInventoryBundles) {
        DeviceInventoryManagementFactory deviceInventoryFactory = LOCATOR.getFactory(DeviceInventoryManagementFactory.class);
        DeviceInventoryBundles deviceInventoryBundles = deviceInventoryFactory.newDeviceInventoryBundles();

        kuraInventoryBundles.getInventoryBundles().forEach(kuraInventoryBundle -> {
            DeviceInventoryBundle deviceInventoryBundle = deviceInventoryFactory.newDeviceInventoryBundle();
            deviceInventoryBundle.setId(String.valueOf(kuraInventoryBundle.getId()));
            deviceInventoryBundle.setName(kuraInventoryBundle.getName());
            deviceInventoryBundle.setVersion(kuraInventoryBundle.getVersion());
            deviceInventoryBundle.setStatus(kuraInventoryBundle.getState());

            deviceInventoryBundles.addInventoryBundle(deviceInventoryBundle);
        });

        return deviceInventoryBundles;
    }

    /**
     * Translates {@link KuraInventorySystemPackages} to {@link DeviceInventorySystemPackages}
     *
     * @param kuraInventorySystemPackages The {@link KuraInventorySystemPackages} to translate.
     * @return The translated {@link DeviceInventorySystemPackages}.
     * @since 1.5.0
     */
    private DeviceInventorySystemPackages translate(KuraInventorySystemPackages kuraInventorySystemPackages) {
        DeviceInventoryManagementFactory deviceInventoryFactory = LOCATOR.getFactory(DeviceInventoryManagementFactory.class);
        DeviceInventorySystemPackages deviceInventorySystemPackages = deviceInventoryFactory.newDeviceInventorySystemPackages();

        kuraInventorySystemPackages.getSystemPackages().forEach(kuraInventorySystemPackage -> {
            DeviceInventorySystemPackage deviceInventorySystemPackage = deviceInventoryFactory.newDeviceInventorySystemPackage();
            deviceInventorySystemPackage.setName(kuraInventorySystemPackage.getName());
            deviceInventorySystemPackage.setVersion(kuraInventorySystemPackage.getVersion());
            deviceInventorySystemPackage.setPackageType(kuraInventorySystemPackage.getType());

            deviceInventorySystemPackages.addSystemPackage(deviceInventorySystemPackage);
        });

        return deviceInventorySystemPackages;
    }

    /**
     * Translates {@link KuraInventoryPackages} to {@link DeviceInventoryPackages}
     *
     * @param kuraInventoryPackages The {@link KuraInventorySystemPackages} to translate.
     * @return The translated {@link DeviceInventorySystemPackages}.
     * @since 1.5.0
     */
    private DeviceInventoryPackages translate(KuraInventoryPackages kuraInventoryPackages) {
        DeviceInventoryManagementFactory deviceInventoryFactory = LOCATOR.getFactory(DeviceInventoryManagementFactory.class);
        DeviceInventoryPackages deviceInventoryPackages = deviceInventoryFactory.newDeviceInventoryPackages();

        kuraInventoryPackages.getPackages().forEach(kuraInventoryPackage -> {
            DeviceInventoryPackage deviceInventoryPackage = deviceInventoryFactory.newDeviceInventoryPackage();
            deviceInventoryPackage.setName(kuraInventoryPackage.getName());
            deviceInventoryPackage.setVersion(kuraInventoryPackage.getVersion());

            kuraInventoryPackage.getPackageBundles().forEach(kuraInventoryBundle -> {
                DeviceInventoryBundle deviceInventoryBundle = deviceInventoryFactory.newDeviceInventoryBundle();
                deviceInventoryBundle.setId(String.valueOf(kuraInventoryBundle.getId()));
                deviceInventoryBundle.setName(kuraInventoryBundle.getName());
                deviceInventoryBundle.setVersion(kuraInventoryBundle.getVersion());
                deviceInventoryBundle.setStatus(kuraInventoryBundle.getState());

                deviceInventoryPackage.addPackageBundle(deviceInventoryBundle);
            });

            deviceInventoryPackages.addPackage(deviceInventoryPackage);
        });

        return deviceInventoryPackages;
    }
}
