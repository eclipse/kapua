/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.device.call.exception.DeviceCallTimeoutException;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.exception.DeviceRequestBadMethodException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementSendException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementTimeoutException;
import org.eclipse.kapua.service.device.management.exception.DeviceNotConnectedException;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.translator.Translator;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * {@link DeviceCallExecutor} definition.<br>
 * Invokes and manages the {@link DeviceCall}.
 *
 * @param <C>  The {@link KapuaRequestChannel} implementation.
 * @param <P>  The {@link KapuaRequestPayload} implementation.
 * @param <RQ> The {@link KapuaRequestMessage} implementation.
 * @param <RS> The {@link KapuaResponseMessage} implementation.
 * @since 1.0.0
 */
public class DeviceCallExecutor<C extends KapuaRequestChannel, P extends KapuaRequestPayload, RQ extends KapuaRequestMessage<C, P>, RS extends KapuaResponseMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceCallFactory DEVICE_CALL_FACTORY = LOCATOR.getFactory(DeviceCallFactory.class);

    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    private RQ requestMessage;
    private Long timeout;

    /**
     * Constructor.
     *
     * @param requestMessage The {@link KapuaRequestMessage}.
     * @since 1.0.0
     */
    public DeviceCallExecutor(@NotNull RQ requestMessage) {
        this(requestMessage, null);
    }

    /**
     * Constructor.
     * <p>
     * If the {@code timeout} parameter is not given, the default one will be used.
     *
     * @param requestMessage The {@link KapuaRequestMessage} to send.
     * @param timeout        The timeout of the request.
     * @since 1.0.0
     */
    public DeviceCallExecutor(@NotNull RQ requestMessage, @Nullable Long timeout) {
        this.requestMessage = requestMessage;
        this.timeout = timeout == null ? DeviceManagementSetting.getInstance().getLong(DeviceManagementSettingKey.REQUEST_TIMEOUT) : timeout;
    }

    /**
     * Performs the {@link DeviceCall}.
     *
     * @return The {@link KapuaResponseMessage}.
     * @throws KapuaEntityNotFoundException     If the {@link Device} is not found.
     * @throws DeviceNotConnectedException      If the {@link Device} is not {@link DeviceConnectionStatus#CONNECTED}.
     * @throws DeviceManagementTimeoutException If waiting of the {@link KapuaResponseMessage} goes on timeout.
     * @throws DeviceManagementSendException    If sending the {@link KapuaRequestMessage} goes on error.
     * @since 1.0.0
     */
    public RS send() throws KapuaEntityNotFoundException, DeviceNotConnectedException, DeviceManagementTimeoutException, DeviceManagementSendException {

        //
        // Check Device existence
        Device device = null;
        try {
            device = DEVICE_REGISTRY_SERVICE.find(requestMessage.getScopeId(), requestMessage.getDeviceId());
        } catch (KapuaException e) {
            throw new DeviceManagementSendException(e, requestMessage);
        }
        if (device == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, requestMessage.getDeviceId());
        }

        //
        // Check Device Connection
        if (device.getConnection() == null) {
            throw new DeviceNotConnectedException(device.getId());
        }

        //
        // Check Device Connection status
        if (!DeviceConnectionStatus.CONNECTED.equals(device.getConnection().getStatus())) {
            throw new DeviceNotConnectedException(device.getId(), device.getConnection().getStatus());
        }

        //
        // Translate the request from Kapua to Device
        try {
            DeviceCall<DeviceRequestMessage<?, ?>, DeviceResponseMessage<?, ?>> deviceCall = DEVICE_CALL_FACTORY.newDeviceCall();
            Translator<RQ, DeviceRequestMessage<?, ?>> tKapuaToClient = Translator.getTranslatorFor(requestMessage.getRequestClass(), deviceCall.getBaseMessageClass());
            DeviceRequestMessage<?, ?> deviceRequestMessage = tKapuaToClient.translate(requestMessage);

            //
            // Send the request
            DeviceResponseMessage<?, ?> responseMessage;
            switch (requestMessage.getChannel().getMethod()) {
                case CREATE:
                case POST:
                    responseMessage = deviceCall.create(deviceRequestMessage, timeout);
                    break;
                case READ:
                case GET:
                    responseMessage = deviceCall.read(deviceRequestMessage, timeout);
                    break;
                case OPTIONS:
                    responseMessage = deviceCall.options(deviceRequestMessage, timeout);
                    break;
                case DELETE:
                case DEL:
                    responseMessage = deviceCall.delete(deviceRequestMessage, timeout);
                    break;
                case EXECUTE:
                case EXEC:
                    responseMessage = deviceCall.execute(deviceRequestMessage, timeout);
                    break;
                case WRITE:
                case PUT:
                    responseMessage = deviceCall.write(deviceRequestMessage, timeout);
                    break;
                default:
                    throw new DeviceRequestBadMethodException();
            }

            //
            // Translate the response from Device to Kapua
            Translator<DeviceResponseMessage<?, ?>, RS> tClientToKapua = Translator.getTranslatorFor(deviceCall.getBaseMessageClass(), requestMessage.getResponseClass());
            return tClientToKapua.translate(responseMessage);
        } catch (DeviceCallTimeoutException dcte) {
            throw new DeviceManagementTimeoutException(dcte, timeout);
        } catch (Exception e) {
            throw new DeviceManagementSendException(e, requestMessage);
        }
    }
}
