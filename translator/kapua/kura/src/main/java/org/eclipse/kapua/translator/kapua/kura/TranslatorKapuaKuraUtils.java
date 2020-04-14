/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.device.call.message.kura.KuraPosition;

/**
 * {@link org.eclipse.kapua.translator.Translator} utilities.<br>
 * It provides helpful methods for translate {@link org.eclipse.kapua.message.Position}.
 *
 * @since 1.0.0
 */
public final class TranslatorKapuaKuraUtils {

    private TranslatorKapuaKuraUtils() {
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
