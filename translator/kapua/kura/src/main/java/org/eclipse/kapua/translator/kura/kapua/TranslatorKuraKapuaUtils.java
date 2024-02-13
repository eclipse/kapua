/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.service.device.call.message.DevicePosition;
import org.eclipse.kapua.service.device.call.message.app.DeviceAppMetrics;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseCode;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.translator.exception.TranslatorException;

public interface TranslatorKuraKapuaUtils {
    void validateKuraResponseChannel(KuraResponseChannel kuraResponseChannel, DeviceAppMetrics appName, DeviceAppMetrics appVersion) throws TranslatorException;

    KapuaPosition translate(DevicePosition devicePosition);

    KapuaResponseCode translate(KuraResponseCode kuraResponseCode) throws KapuaException;
}
