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
package org.eclipse.kapua.service.device.management.commons.call;

import org.eclipse.kapua.KapuaEntityNotFoundException;
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
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;

/**
 * Device call executor definition.<br>
 * This object executes call, collecting the response from the device.
 *
 * @param <C>  request channel type
 * @param <P>  request payload type
 * @param <RQ> request message type
 * @param <RS> response message type
 * @since 1.0
 */
public class DeviceCallExecutor<C extends KapuaRequestChannel, P extends KapuaRequestPayload, RQ extends KapuaRequestMessage<C, P>, RS extends KapuaResponseMessage> {

    private final static KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private final static DeviceCallFactory DEVICE_CALL_FACTORY = LOCATOR.getFactory(DeviceCallFactory.class);

    private final static DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    private RQ requestMessage;
    private Long timeout;

    /**
     * Constructor
     *
     * @param requestMessage
     */
    public DeviceCallExecutor(RQ requestMessage) {
        this(requestMessage, null);
    }

    /**
     * Constructor
     *
     * @param requestMessage
     * @param timeout
     */
    public DeviceCallExecutor(RQ requestMessage, Long timeout) {
        this.requestMessage = requestMessage;
        this.timeout = timeout == null ? DeviceManagementSetting.getInstance().getLong(DeviceManagementSettingKey.REQUEST_TIMEOUT) : timeout;
    }

    /**
     * Performs the {@link DeviceCall}.
     *
     * @return The {@link KapuaResponseMessage}.
     * @throws KapuaException
     */
    public RS send() throws KapuaException {

        //
        // Check Device existence
        Device device = DEVICE_REGISTRY_SERVICE.find(requestMessage.getScopeId(), requestMessage.getDeviceId());
        if (device == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, requestMessage.getDeviceId());
        }

        //
        // Translate the request from Kapua to Device
        DeviceCall<DeviceRequestMessage, DeviceResponseMessage> deviceCall = DEVICE_CALL_FACTORY.newDeviceCall();
        Translator tKapuaToClient = Translator.getTranslatorFor(requestMessage.getRequestClass(), deviceCall.getBaseMessageClass());
        DeviceRequestMessage deviceRequestMessage = (DeviceRequestMessage) tKapuaToClient.translate(requestMessage);

        //
        // Send the request
        DeviceResponseMessage responseMessage;
        switch (requestMessage.getChannel().getMethod()) {
        case CREATE: {
            responseMessage = deviceCall.create(deviceRequestMessage, timeout);
        }
        break;
        case READ: {
            responseMessage = deviceCall.read(deviceRequestMessage, timeout);
        }
        break;
        case OPTIONS: {
            responseMessage = deviceCall.options(deviceRequestMessage, timeout);
        }
        break;
        case DELETE: {
            responseMessage = deviceCall.delete(deviceRequestMessage, timeout);
        }
        break;
        case EXECUTE: {
            responseMessage = deviceCall.execute(deviceRequestMessage, timeout);
        }
        break;
        case WRITE: {
            responseMessage = deviceCall.write(deviceRequestMessage, timeout);
        }
        break;
        default:
            throw new DeviceManagementException(DeviceManagementErrorCodes.REQUEST_BAD_METHOD, null, requestMessage.getChannel().getMethod());
        }

        //
        // Translate the response from Device to Kapua
        Translator tClientToKapua = Translator.getTranslatorFor(deviceCall.getBaseMessageClass(), requestMessage.getResponseClass());
        return (RS) tClientToKapua.translate(responseMessage);
    }
}
