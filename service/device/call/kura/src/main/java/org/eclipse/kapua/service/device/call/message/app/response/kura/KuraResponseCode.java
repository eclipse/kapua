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
package org.eclipse.kapua.service.device.call.message.app.response.kura;

import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseCode;

public enum KuraResponseCode implements DeviceResponseCode
{
    ACCEPTED(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_ERROR(500);

    private int code;

    KuraResponseCode(int code)
    {
        this.code = code;
    }

    public int getCode()
    {
        return code;
    }

    public static KuraResponseCode fromResponseCode(String responseCode)
    {
        return fromResponseCode(Integer.valueOf(responseCode));
    }

    public static KuraResponseCode fromResponseCode(int responseCode)
    {
        KuraResponseCode result = null;
        for (KuraResponseCode krc : KuraResponseCode.values()) {
            if (krc.getCode() == responseCode) {
                result = krc;
                break;
            }
        }

        return result;
    }
}
