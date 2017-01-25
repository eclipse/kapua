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
 *
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaAppsChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.junit.Assert;
import org.junit.Test;

public class KapuaAppsMessageTest extends Assert {

    private static final String PAYLOAD_DISPLAY_STR = "[ getUptime()=12" +
            ", getDisplayName()=displayName" +
            ", getModelName()=modelName" +
            ", getModelId()=modelId-1" +
            ", getPartNumber()=part-1" +
            ", getSerialNumber()=SN-123" +
            ", getFirmwareVersion()=firmwareV-1" +
            ", getBiosVersion()=biosV-1" +
            ", getOs()=Linux" +
            ", getOsVersion()=osV-1" +
            ", getJvmName()=Oracle HotSpot" +
            ", getJvmVersion()=8" +
            ", getJvmProfile()=desktop" +
            ", getOsgiFramework()=containerFramework" +
            ", getOsgiFrameworkVersion()=containerFrameworkV-1" +
            ", getEsfKuraVersion()=applicationFrameworkV-1" +
            ", getConnectionInterface()=connectionInterface" +
            ", getConnectionIp()=192.168.1.1" +
            ", getAcceptEncoding()=UTF-8" +
            ", getApplicationIdentifiers()=applicationIdentifiers" +
            ", getAvailableProcessors()=1" +
            ", getTotalMemory()=4" +
            ", getOsArch()=Linux x86" +
            ", getModemImei()=49-015420-323751" +
            ", getModemImsi()=359881234567890" +
            ", getModemIccid()=8991101200003204510" +
            "]";

    @Test
    public void kapuaAppsPayloadInitConstructor() {
        KapuaAppsPayload kapuaAppsPayload = populateKapuaAppsPayload();

        assertEquals("12", kapuaAppsPayload.getUptime());
        assertEquals("displayName", kapuaAppsPayload.getDisplayName());
        assertEquals("modelName", kapuaAppsPayload.getModelName());
        assertEquals("modelId-1", kapuaAppsPayload.getModelId());
        assertEquals("part-1", kapuaAppsPayload.getPartNumber());
        assertEquals("SN-123", kapuaAppsPayload.getSerialNumber());
        assertEquals("firmware-1", kapuaAppsPayload.getFirmware());
        assertEquals("firmwareV-1", kapuaAppsPayload.getFirmwareVersion());
        assertEquals("bios", kapuaAppsPayload.getBios());
        assertEquals("biosV-1", kapuaAppsPayload.getBiosVersion());
        assertEquals("Linux", kapuaAppsPayload.getOs());
        assertEquals("osV-1", kapuaAppsPayload.getOsVersion());
        assertEquals("Oracle HotSpot", kapuaAppsPayload.getJvm());
        assertEquals("8", kapuaAppsPayload.getJvmVersion());
        assertEquals("desktop", kapuaAppsPayload.getJvmProfile());
        assertEquals("containerFramework", kapuaAppsPayload.getContainerFramework());
        assertEquals("containerFrameworkV-1", kapuaAppsPayload.getContainerFrameworkVersion());
        assertEquals("applicationFramework", kapuaAppsPayload.getApplicationFramework());
        assertEquals("applicationFrameworkV-1", kapuaAppsPayload.getApplicationFrameworkVersion());
        assertEquals("connectionInterface", kapuaAppsPayload.getConnectionInterface());
        assertEquals("192.168.1.1", kapuaAppsPayload.getConnectionIp());
        assertEquals("UTF-8", kapuaAppsPayload.getAcceptEncoding());
        assertEquals("applicationIdentifiers", kapuaAppsPayload.getApplicationIdentifiers());
        assertEquals("1", kapuaAppsPayload.getAvailableProcessors());
        assertEquals("4", kapuaAppsPayload.getTotalMemory());
        assertEquals("Linux x86", kapuaAppsPayload.getOsArch());
        assertEquals("49-015420-323751", kapuaAppsPayload.getModemImei());
        assertEquals("359881234567890", kapuaAppsPayload.getModemImsi());
        assertEquals("8991101200003204510", kapuaAppsPayload.getModemIccid());
    }

    @Test
    public void toDisplayString() throws Exception {
        KapuaAppsPayload kapuaAppsPayload = populateKapuaAppsPayload();

        // FIXME strings representing methods are not in sync with real method names
        String displayStr = kapuaAppsPayload.toDisplayString();
        assertEquals(PAYLOAD_DISPLAY_STR, displayStr);
    }

    @Test
    public void kapuaAppsMessageConstructor() throws Exception {
        KapuaAppsMessageImpl kapuaAppsMessage = new KapuaAppsMessageImpl();

        assertNotNull(kapuaAppsMessage);
    }

    @Test
    public void kapuaAppsChannelGetterSetters() throws Exception {
        KapuaAppsChannel kapuaAppsChannel = new KapuaAppsChannelImpl();

        kapuaAppsChannel.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaAppsChannel.getClientId());
    }

    /**
     * Prepare Kapua Apps payload data, data is not necessary semantically correct.
     *
     * @return all KapuaAppsPayload fields populated with data.
     */
    private static KapuaAppsPayload populateKapuaAppsPayload() {
        KapuaAppsPayload kapuaAppsPayload = new KapuaAppsPayloadImpl(
                "12",
                "displayName",
                "modelName",
                "modelId-1",
                "part-1",
                "SN-123",
                "firmware-1",
                "firmwareV-1",
                "bios",
                "biosV-1",
                "Linux",
                "osV-1",
                "Oracle HotSpot",
                "8",
                "desktop",
                "containerFramework",
                "containerFrameworkV-1",
                "applicationFramework",
                "applicationFrameworkV-1",
                "connectionInterface",
                "192.168.1.1",
                "UTF-8",
                "applicationIdentifiers",
                "1",
                "4",
                "Linux x86",
                "49-015420-323751",
                "359881234567890",
                "8991101200003204510"
        );

        return kapuaAppsPayload;
    }
}
