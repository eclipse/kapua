/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.kapua.message.KapuaPayload;

/**
 * Kapua message payload object reference implementation.
 * 
 * @since 1.0
 *
 */
public class KapuaPayloadImpl implements KapuaPayload
{
    private Map<String, Object> properties;
    private byte[]              body;

    /**
     * Constructor
     */
    public KapuaPayloadImpl()
    {
    }

    @Override
    public Map<String, Object> getProperties()
    {        
        if (properties == null) {
            properties = new HashMap<>();
        }
        
        return properties;
    }

    @Override
    public void setProperties(Map<String, Object> properties)
    {
        this.properties = properties;
    }

    @Override
    public byte[] getBody()
    {
        return body;
    }

    @Override
    public void setBody(byte[] body)
    {
        this.body = body;
    }

    @Override
    public String toDisplayString()
    {
        StringBuilder sb = new StringBuilder();
        Iterator<String> hdrIterator = getProperties().keySet().iterator();
        while (hdrIterator.hasNext()) {
            String hdrName = hdrIterator.next();
            Object hdrValue = getProperties().get(hdrName);
            if (hdrValue != null) {

                String hdrValueString = "";
                Class<?> type = hdrValue.getClass();
                if (type == Float.class) {
                    hdrValueString = Float.toString((Float) hdrValue);
                }
                else if (type == Double.class) {
                    hdrValueString = Double.toString((Double) hdrValue);
                }
                else if (type == Integer.class) {
                    hdrValueString = Integer.toString((Integer) hdrValue);
                }
                else if (type == Long.class) {
                    hdrValueString = Long.toString((Long) hdrValue);
                }
                else if (type == Boolean.class) {
                    hdrValueString = Boolean.toString((Boolean) hdrValue);
                }
                else if (type == String.class) {
                    hdrValueString = (String) hdrValue;
                }
                else if (type == byte[].class) {
                    hdrValueString = byteArrayToHexString((byte[]) hdrValue);
                }
                sb.append(hdrName);
                sb.append("=");
                sb.append(hdrValueString);

                if (hdrIterator.hasNext()) {
                    sb.append("~~");
                }
            }
        }

        return sb.toString();
    }

    private String byteArrayToHexString(byte[] b)
    {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

}
