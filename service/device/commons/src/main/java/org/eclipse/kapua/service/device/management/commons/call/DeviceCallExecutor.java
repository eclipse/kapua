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
package org.eclipse.kapua.service.device.management.commons.call;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.DeviceCall;
import org.eclipse.kapua.service.device.call.DeviceCallFactory;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementErrorCodes;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementException;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.translator.Translator;

@SuppressWarnings("rawtypes")
public class DeviceCallExecutor<C extends KapuaRequestChannel, P extends KapuaRequestPayload, RQ extends KapuaRequestMessage<C, P>, RS extends KapuaResponseMessage>
{
    private RQ   requestMessage;
    private Long timeout;

    public DeviceCallExecutor(RQ requestMessage)
    {
        this(requestMessage, null);
    }

    public DeviceCallExecutor(RQ requestMessage, Long timeout)
    {
        this.requestMessage = requestMessage;
        this.timeout = timeout;
    }

    @SuppressWarnings({ "unchecked" })
    public RS send()
        throws KapuaException
    {
        //
        // Get the correct device call
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceCallFactory kapuaDeviceCallFactory = locator.getFactory(DeviceCallFactory.class);
        DeviceCall<DeviceRequestMessage, DeviceResponseMessage> deviceCall = kapuaDeviceCallFactory.newDeviceCall();
        Translator tKapuaToClient = Translator.getTranslatorFor(requestMessage.getRequestClass(),
                                                                deviceCall.getBaseMessageClass());

        DeviceResponseMessage responseMessage;
        timeout = timeout == null ? DeviceManagementSetting.getInstance().getLong(DeviceManagementSettingKey.REQUEST_TIMEOUT) : timeout;

        DeviceRequestMessage deviceRequestMessage = (DeviceRequestMessage) tKapuaToClient.translate(requestMessage);
        switch (requestMessage.getChannel().getMethod()) {
            case CREATE:
            {
                responseMessage = deviceCall.create(deviceRequestMessage, timeout);
            }
                break;
            case READ:
            {
                responseMessage = deviceCall.read(deviceRequestMessage, timeout);
            }
                break;
            case OPTIONS:
            {
                responseMessage = deviceCall.options(deviceRequestMessage, timeout);
            }
                break;
            case DELETE:
            {
                responseMessage = deviceCall.delete(deviceRequestMessage, timeout);
            }
                break;
            case EXECUTE:
            {
                responseMessage = deviceCall.execute(deviceRequestMessage, timeout);
            }
                break;
            case WRITE:
            {
                responseMessage = deviceCall.write(deviceRequestMessage, timeout);
            }
                break;
            default:
                throw new DeviceManagementException(DeviceManagementErrorCodes.REQUEST_BAD_METHOD,
                                                    null,
                                                    requestMessage.getChannel().getMethod());
        }

        Translator tClientToKapua = Translator.getTranslatorFor(deviceCall.getBaseMessageClass(),
                                                                requestMessage.getResponseClass());

        return (RS) tClientToKapua.translate(responseMessage);
    }
}
