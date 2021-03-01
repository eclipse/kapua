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
import org.eclipse.kapua.service.device.call.kura.model.inventory.InventorySystemPackages;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponsePayload;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryResponseChannel;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryResponseMessage;
import org.eclipse.kapua.service.device.management.bundle.message.internal.InventoryResponsePayload;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.model.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.DeviceInventoryPackage;
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
        InventoryResponsePayload inventoryResponsePayload = super.translatePayload(kuraResponsePayload);

        try {
            if (kuraResponsePayload.hasBody()) {
                InventorySystemPackages kuraInventory = readJsonBodyAs(kuraResponsePayload.getBody(), InventorySystemPackages.class);

                inventoryResponsePayload.setDeviceInventory(translate(kuraInventory));
            }

            return inventoryResponsePayload;
        } catch (Exception e) {
            throw new InvalidPayloadException(e, kuraResponsePayload);
        }
    }

    /**
     * Translates {@link InventorySystemPackages} to {@link DeviceInventory}
     *
     * @param kuraInventory The {@link InventorySystemPackages} to translate.
     * @return The translated {@link DeviceInventory}.
     * @since 1.0.0
     */
    private DeviceInventory translate(InventorySystemPackages kuraInventory) {
        DeviceInventoryManagementFactory deviceInventoryFactory = LOCATOR.getFactory(DeviceInventoryManagementFactory.class);
        DeviceInventory deviceInventory = deviceInventoryFactory.newDeviceInventory();

        kuraInventory.getSystemPackages().forEach(systemPackage -> {
            DeviceInventoryPackage deviceInventoryPackage = deviceInventoryFactory.newDeviceInventoryPackage();
            deviceInventoryPackage.setName(systemPackage.getName());
            deviceInventoryPackage.setVersion(systemPackage.getVersion());
            deviceInventoryPackage.setPackageType(systemPackage.getType());

            deviceInventory.addInventoryPackage(deviceInventoryPackage);
        });

        return deviceInventory;
    }
}
