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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.service.device.call.message.DevicePosition;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;

/**
 * Messages translator utilities.<br>
 * It provides helpful methods for translate position and response code.
 * 
 * @since 1.0
 *
 */
public class TranslatorKuraKapuaUtils
{

    /**
     * Translate {@link DevicePosition} to {@link KapuaPosition}
     * 
     * @param kuraPosition
     * @return
     */
    public static KapuaPosition translate(DevicePosition kuraPosition)
    {
        KapuaPosition kapuaPosition = null;

        if (kuraPosition != null) {
            KapuaLocator locator = KapuaLocator.getInstance();
            KapuaMessageFactory kapuaMessageFactory = locator.getFactory(KapuaMessageFactory.class);

            kapuaPosition = kapuaMessageFactory.newPosition();
            kapuaPosition.setAltitude(kuraPosition.getAltitude());
            kapuaPosition.setHeading(kuraPosition.getHeading());
            kapuaPosition.setLatitude(kuraPosition.getLatitude());
            kapuaPosition.setLongitude(kuraPosition.getLongitude());
            kapuaPosition.setPrecision(kuraPosition.getPrecision());
            kapuaPosition.setSatellites(kuraPosition.getSatellites());
            kapuaPosition.setSpeed(kuraPosition.getSpeed());
            kapuaPosition.setStatus(kuraPosition.getStatus());
            kapuaPosition.setTimestamp(kuraPosition.getTimestamp());
        }

        return kapuaPosition;
    }

    /**
     * Translate Kura response code to {@link KapuaResponseCode}
     * 
     * @param kuraResponseCode
     * @return
     */
    public static KapuaResponseCode translate(Integer kuraResponseCode)
    {
        KapuaResponseCode responseCode;
        if (kuraResponseCode == null) {
            responseCode = null;
        }
        else {
            switch (kuraResponseCode) {
                case 200:
                    responseCode = KapuaResponseCode.ACCEPTED;
                    break;
                case 400:
                    responseCode = KapuaResponseCode.BAD_REQUEST;
                    break;
                case 404:
                    responseCode = KapuaResponseCode.NOT_FOUND;
                    break;
                case 500:
                    responseCode = KapuaResponseCode.INTERNAL_ERROR;
                    break;
                default:
                    responseCode = null;
            }
        }
        return responseCode;

    }
}
