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
 *******************************************************************************/
package org.org.eclipse.kapua.service.device.call.test;

import java.util.Date;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.DeviceCall;
import org.eclipse.kapua.service.device.call.DeviceCallFactory;
import org.eclipse.kapua.service.device.call.kura.KuraMethod;
import org.eclipse.kapua.service.device.call.kura.app.CommandMetrics;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestChannel;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.request.kura.KuraRequestPayload;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class KuraCallTest extends Assert {

    // Disabling this test until we have build fixed. Then we should register DeviceCallFactory to service locator
    // and enable the test again.
    @Ignore
    @SuppressWarnings("unchecked")
    @Test
    public void testKuraCall()
            throws Exception {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceCallFactory deviceCallFactory = locator.getFactory(DeviceCallFactory.class);
        DeviceCall<KuraRequestMessage,?> deviceCall = deviceCallFactory.newDeviceCall();

        KuraRequestChannel requestChannel = new KuraRequestChannel(SystemSetting.getInstance().getMessageClassifier(), "kapua-sys", "00:60:0C:82:52:34");
        requestChannel.setAppId("CMD-V1");
        requestChannel.setMethod(KuraMethod.EXEC);
        requestChannel.setResources(new String[] { "command" });

        KuraRequestPayload requestPayload = new KuraRequestPayload();
        // requestPayload.metrics().put(Kurame, value)
        requestPayload.getMetrics().put(CommandMetrics.APP_METRIC_CMD.getValue(), "ifconfig");

        KuraRequestMessage requestMessage = new KuraRequestMessage(requestChannel, new Date(), requestPayload);

        deviceCall.execute(requestMessage, 120000L);
    }
}
