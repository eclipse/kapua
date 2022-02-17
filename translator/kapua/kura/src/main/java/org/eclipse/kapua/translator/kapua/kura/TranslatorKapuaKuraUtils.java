/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.kapua.kura;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.service.device.call.message.app.DeviceAppMetrics;
import org.eclipse.kapua.service.device.call.message.kura.KuraPosition;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestChannel;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;

/**
 * {@link org.eclipse.kapua.translator.Translator} utilities.<br>
 * It provides helpful methods for translate {@link org.eclipse.kapua.message.Position}.
 *
 * @since 1.0.0
 */
public final class TranslatorKapuaKuraUtils {

    private static final String CONTROL_MESSAGE_CLASSIFIER = SystemSetting.getInstance().getMessageClassifier();

    private TranslatorKapuaKuraUtils() {
    }

    /**
     * Builds the {@link KuraRequestChannel} with common info for a device applications.
     * <p>
     * It populates:
     * <ul>
     *     <li>{@link KuraRequestChannel#getMessageClassification()}</li>
     *     <li>{@link KuraRequestChannel#getAppId()}</li>
     *     <li>{@link KuraRequestChannel#getMethod()}</li>
     * </ul>
     *
     * @param appName    The application name.
     * @param appVersion The application version
     * @param appMethod  The {@link KapuaMethod} of the {@link KapuaRequestMessage}
     * @return The built base {@link KuraRequestChannel}
     * @since 1.2.0
     */
    public static KuraRequestChannel buildBaseRequestChannel(DeviceAppMetrics appName, DeviceAppMetrics appVersion, KapuaMethod appMethod) {

        KuraRequestChannel kuraRequestChannel = new KuraRequestChannel();
        kuraRequestChannel.setMessageClassification(CONTROL_MESSAGE_CLASSIFIER);
        kuraRequestChannel.setAppId(appName + "-" + appVersion);
        kuraRequestChannel.setMethod(MethodDictionaryKapuaKura.translate(appMethod));

        return kuraRequestChannel;
    }

    /**
     * Translates {@link KapuaPosition} to {@link KuraPosition}
     *
     * @param kapuaPosition The {@link KapuaPosition} to translate
     * @return The translated {@link KuraPosition}
     * @since 1.0.0
     */
    public static KuraPosition translate(KapuaPosition kapuaPosition) {
        KuraPosition kuraPosition = null;

        if (kapuaPosition != null) {
            kuraPosition = new KuraPosition();
            kuraPosition.setAltitude(kapuaPosition.getAltitude());
            kuraPosition.setHeading(kapuaPosition.getHeading());
            kuraPosition.setLatitude(kapuaPosition.getLatitude());
            kuraPosition.setLongitude(kapuaPosition.getLongitude());
            kuraPosition.setPrecision(kapuaPosition.getPrecision());
            kuraPosition.setSatellites(kapuaPosition.getSatellites());
            kuraPosition.setSpeed(kapuaPosition.getSpeed());
            kuraPosition.setStatus(kapuaPosition.getStatus());
            kuraPosition.setTimestamp(kapuaPosition.getTimestamp());
        }

        return kuraPosition;
    }
}
