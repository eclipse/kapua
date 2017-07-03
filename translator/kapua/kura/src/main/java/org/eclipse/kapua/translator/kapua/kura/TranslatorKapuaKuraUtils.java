/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.translator.kapua.kura;

import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.service.device.call.message.DevicePosition;
import org.eclipse.kapua.service.device.call.message.kura.KuraPosition;

/**
 * Messages translator utilities.<br>
 * It provides helpful methods for translate position and response code.
 *
 * @since 1.0
 *
 */
public final class TranslatorKapuaKuraUtils {


    private TranslatorKapuaKuraUtils() {
    }
    
    /**
     * Translate {@link DevicePosition} to {@link KapuaPosition}
     *
     * @param kapuaPosition
     * @return
     */
    public static DevicePosition translate(KapuaPosition kapuaPosition) {
        DevicePosition kuraPosition = null;

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
