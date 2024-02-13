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
package org.eclipse.kapua.translator.kura.kapua.inventory;

import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.internal.message.InventoryNoContentResponseMessage;
import org.eclipse.kapua.translator.Translator;

import javax.inject.Inject;

/**
 * {@link Translator} implementation from {@link KuraResponseMessage} to {@link InventoryNoContentResponseMessage}
 *
 * @since 1.5.0
 */
public class TranslatorAppInventoryNoContentKuraKapua extends AbstractTranslatorAppInventoryKuraKapua<InventoryNoContentResponseMessage> {

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    @Inject
    public TranslatorAppInventoryNoContentKuraKapua(DeviceManagementSetting deviceManagementSetting, DeviceInventoryManagementFactory deviceInventoryManagementFactory) {
        super(deviceManagementSetting, deviceInventoryManagementFactory, InventoryNoContentResponseMessage.class);
    }
}
