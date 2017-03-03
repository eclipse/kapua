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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import java.util.Iterator;

import org.eclipse.kapua.service.device.call.message.DevicePayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;

/**
 * Kura device unmatched message payload implementation.
 *
 * @since 1.0
 *
 */
public class KuraMissingPayload extends KuraPayload implements DevicePayload {

    /**
     * Constructor
     */
    public KuraMissingPayload() {
        super();
    }

    /**
     * Returns a displayable representation string
     *
     * @return
     */
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        Iterator<String> hdrIterator = metrics.keySet().iterator();
        while (hdrIterator.hasNext()) {
            String hdrName = hdrIterator.next();
            Object hdrValue = metrics.get(hdrName);
            String hdrValueString = "";
            Class<?> type = hdrValue.getClass();
            if (type == Float.class) {
                hdrValueString = Float.toString((Float) hdrValue);
            } else if (type == Double.class) {
                hdrValueString = Double.toString((Double) hdrValue);
            } else if (type == Integer.class) {
                hdrValueString = Integer.toString((Integer) hdrValue);
            } else if (type == Long.class) {
                hdrValueString = Long.toString((Long) hdrValue);
            } else if (type == Boolean.class) {
                hdrValueString = Boolean.toString((Boolean) hdrValue);
            } else if (type == String.class) {
                hdrValueString = (String) hdrValue;
            } else if (type == byte[].class) {
                hdrValueString = byteArrayToHexString((byte[]) hdrValue);
            }
            sb.append(hdrName);
            sb.append("=");
            sb.append(hdrValueString);

            if (hdrIterator.hasNext()) {
                sb.append("~~");
            }
        }
        return sb.toString();
    }

    private String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (byte element : b) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

}
