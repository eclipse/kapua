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
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.junit.Assert;
import org.junit.Test;

public class KapuaBirthMessageTest extends Assert {

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
    public void kapuaBirthPayloadInitConstructor() {
        KapuaBirthPayload kapuaBirthPayload = populateKapuaBirthPayload();

        assertEquals("12", kapuaBirthPayload.getUptime());
        assertEquals("displayName", kapuaBirthPayload.getDisplayName());
        assertEquals("modelName", kapuaBirthPayload.getModelName());
        assertEquals("modelId-1", kapuaBirthPayload.getModelId());
        assertEquals("part-1", kapuaBirthPayload.getPartNumber());
        assertEquals("SN-123", kapuaBirthPayload.getSerialNumber());
        assertEquals("firmware-1", kapuaBirthPayload.getFirmware());
        assertEquals("firmwareV-1", kapuaBirthPayload.getFirmwareVersion());
        assertEquals("bios", kapuaBirthPayload.getBios());
        assertEquals("biosV-1", kapuaBirthPayload.getBiosVersion());
        assertEquals("Linux", kapuaBirthPayload.getOs());
        assertEquals("osV-1", kapuaBirthPayload.getOsVersion());
        assertEquals("Oracle HotSpot", kapuaBirthPayload.getJvm());
        assertEquals("8", kapuaBirthPayload.getJvmVersion());
        assertEquals("desktop", kapuaBirthPayload.getJvmProfile());
        assertEquals("containerFramework", kapuaBirthPayload.getContainerFramework());
        assertEquals("containerFrameworkV-1", kapuaBirthPayload.getContainerFrameworkVersion());
        assertEquals("applicationFramework", kapuaBirthPayload.getApplicationFramework());
        assertEquals("applicationFrameworkV-1", kapuaBirthPayload.getApplicationFrameworkVersion());
        assertEquals("connectionInterface", kapuaBirthPayload.getConnectionInterface());
        assertEquals("192.168.1.1", kapuaBirthPayload.getConnectionIp());
        assertEquals("UTF-8", kapuaBirthPayload.getAcceptEncoding());
        assertEquals("applicationIdentifiers", kapuaBirthPayload.getApplicationIdentifiers());
        assertEquals("1", kapuaBirthPayload.getAvailableProcessors());
        assertEquals("4", kapuaBirthPayload.getTotalMemory());
        assertEquals("Linux x86", kapuaBirthPayload.getOsArch());
        assertEquals("49-015420-323751", kapuaBirthPayload.getModemImei());
        assertEquals("359881234567890", kapuaBirthPayload.getModemImsi());
        assertEquals("8991101200003204510", kapuaBirthPayload.getModemIccid());
    }

    @Test
    public void toDisplayString() throws Exception {
        KapuaBirthPayload kapuaBirthPayload = populateKapuaBirthPayload();

        // FIXME strings representing methods are not in sync with real method names
        String displayStr = kapuaBirthPayload.toDisplayString();
        assertEquals(PAYLOAD_DISPLAY_STR, displayStr);
    }

    @Test
    public void kapuaBirthMessageConstructor() throws Exception {
        KapuaBirthMessageImpl kapuaBirthMessage = new KapuaBirthMessageImpl();

        assertNotNull(kapuaBirthMessage);
    }

    @Test
    public void kapuaBirthMessageGetterSetters() throws Exception {
        KapuaBirthMessage kapuaBirthMessage = new KapuaBirthMessageImpl();

        kapuaBirthMessage.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaBirthMessage.getClientId());
    }

    @Test
    public void kapuaBirthChannelGetterSetters() throws Exception {
        KapuaBirthChannel kapuaBirthChannel = new KapuaBirthChannelImpl();

        kapuaBirthChannel.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaBirthChannel.getClientId());
    }

    /**
     * Prepare Kapua Birth payload data, data is not necessary semantically correct.
     *
     * @return all KapuaBirthPayload fields populated with data.
     */
    private static KapuaBirthPayload populateKapuaBirthPayload() {
        KapuaBirthPayload kapuaBirthPayload = new KapuaBirthPayloadImpl(
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

        return kapuaBirthPayload;
    }
}
