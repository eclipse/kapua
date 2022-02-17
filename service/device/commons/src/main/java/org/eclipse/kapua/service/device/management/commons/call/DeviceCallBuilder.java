/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.commons.call;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.call.DeviceCall;
import org.eclipse.kapua.service.device.call.DeviceCallFactory;
import org.eclipse.kapua.service.device.call.exception.DeviceCallTimeoutException;
import org.eclipse.kapua.service.device.call.message.app.request.DeviceRequestMessage;
import org.eclipse.kapua.service.device.call.message.app.response.DeviceResponseMessage;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementRequestBadMethodException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementSendException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementTimeoutException;
import org.eclipse.kapua.service.device.management.exception.DeviceNeverConnectedException;
import org.eclipse.kapua.service.device.management.exception.DeviceNotConnectedException;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestChannel;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestPayload;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.translator.Translator;

import java.util.Date;

/**
 * {@link DeviceCallBuilder} definition.
 * <p>
 * It replaces {@link DeviceCallExecutor} providing:
 * <ul>
 *     <li>Support for {@link KapuaMethod#SUBMIT}</li>
 *     <li>Support for {@link KapuaMethod#CANCEL}</li>
 *     <li>Sending {@link KapuaRequestMessage} without waiting for {@link KapuaResponseMessage}</li>
 *     <li>Better API layout</li>
 * </ul>
 * Invokes and manages the {@link DeviceCall}.
 *
 * @param <C>  The {@link KapuaRequestChannel} implementation.
 * @param <P>  The {@link KapuaRequestPayload} implementation.
 * @param <RQ> The {@link KapuaRequestMessage} implementation.
 * @param <RS> The {@link KapuaResponseMessage} implementation.
 * @since 1.4.0
 */
public class DeviceCallBuilder<C extends KapuaRequestChannel, P extends KapuaRequestPayload, RQ extends KapuaRequestMessage<C, P>, RS extends KapuaResponseMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceCallFactory DEVICE_CALL_FACTORY = LOCATOR.getFactory(DeviceCallFactory.class);

    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    private static final Long DEFAULT_TIMEOUT = DeviceManagementSetting.getInstance().getLong(DeviceManagementSettingKey.REQUEST_TIMEOUT);

    private RQ requestMessage;
    private Long timeout;

    /**
     * Constructor.
     *
     * @since 1.4.0
     */
    private DeviceCallBuilder() {
    }

    public static DeviceCallBuilder newBuilder() {
        return new DeviceCallBuilder();
    }

    public DeviceCallBuilder withRequestMessage(RQ requestMessage) {
        this.requestMessage = requestMessage;
        return this;
    }

    public DeviceCallBuilder withTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public DeviceCallBuilder withTimeoutOrDefault(Long timeout) {
        this.timeout = timeout != null ? timeout : DEFAULT_TIMEOUT;
        return this;
    }

    /**
     * Performs the {@link DeviceCall}.
     *
     * @return The {@link KapuaResponseMessage}.
     * @throws KapuaEntityNotFoundException     If the {@link Device} is not found.
     * @throws KapuaIllegalArgumentException    If {@link KapuaRequestMessage} has not been set.
     * @throws DeviceNotConnectedException      If the {@link Device} is not {@link DeviceConnectionStatus#CONNECTED}.
     * @throws DeviceManagementTimeoutException If waiting of the {@link KapuaResponseMessage} goes on timeout.
     * @throws DeviceManagementSendException    If sending the {@link KapuaRequestMessage} goes on error.
     * @since 1.0.0
     */
    public RS send() throws KapuaEntityNotFoundException, KapuaIllegalArgumentException, DeviceNotConnectedException, DeviceManagementTimeoutException, DeviceManagementSendException {

        deviceCallPreChecks();

        //
        // Translate the request from Kapua to Device
        try {
            requestMessage.setSentOn(new Date());

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
                case SUBMIT:
                    responseMessage = deviceCall.submit(deviceRequestMessage, timeout);
                    break;
                case CANCEL:
                    responseMessage = deviceCall.cancel(deviceRequestMessage, timeout);
                    break;
                default:
                    throw new DeviceManagementRequestBadMethodException(requestMessage.getChannel().getMethod());
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

    /**
     * Performs the {@link DeviceCall}.
     *
     * @throws KapuaEntityNotFoundException  If the {@link Device} is not found.
     * @throws KapuaIllegalArgumentException If {@link KapuaRequestMessage} has not been set.
     * @throws DeviceNotConnectedException   If the {@link Device} is not {@link DeviceConnectionStatus#CONNECTED}.
     * @throws DeviceManagementSendException If sending the {@link KapuaRequestMessage} goes on error.
     * @since 1.4.0
     */
    public void sendAndForget() throws KapuaEntityNotFoundException, KapuaIllegalArgumentException, DeviceNotConnectedException, DeviceManagementTimeoutException, DeviceManagementSendException {

        deviceCallPreChecks();

        //
        // Translate the request from Kapua to Device
        try {
            requestMessage.setSentOn(new Date());

            DeviceCall<DeviceRequestMessage<?, ?>, DeviceResponseMessage<?, ?>> deviceCall = DEVICE_CALL_FACTORY.newDeviceCall();
            Translator<RQ, DeviceRequestMessage<?, ?>> tKapuaToClient = Translator.getTranslatorFor(requestMessage.getRequestClass(), deviceCall.getBaseMessageClass());
            DeviceRequestMessage<?, ?> deviceRequestMessage = tKapuaToClient.translate(requestMessage);

            //
            // Send the request
            switch (requestMessage.getChannel().getMethod()) {
                case CREATE:
                case POST:
                    deviceCall.create(deviceRequestMessage, null);
                    break;
                case READ:
                case GET:
                    deviceCall.read(deviceRequestMessage, null);
                    break;
                case OPTIONS:
                    deviceCall.options(deviceRequestMessage, null);
                    break;
                case DELETE:
                case DEL:
                    deviceCall.delete(deviceRequestMessage, null);
                    break;
                case EXECUTE:
                case EXEC:
                    deviceCall.execute(deviceRequestMessage, null);
                    break;
                case WRITE:
                case PUT:
                    deviceCall.write(deviceRequestMessage, null);
                    break;
                case SUBMIT:
                    deviceCall.submit(deviceRequestMessage, null);
                    break;
                case CANCEL:
                    deviceCall.cancel(deviceRequestMessage, null);
                    break;
                default:
                    throw new DeviceManagementRequestBadMethodException(requestMessage.getChannel().getMethod());
            }
        } catch (Exception e) {
            throw new DeviceManagementSendException(e, requestMessage);
        }
    }

    private void deviceCallPreChecks() throws DeviceManagementSendException, KapuaEntityNotFoundException, DeviceNotConnectedException, KapuaIllegalNullArgumentException {
        //
        // Validate arguments
        ArgumentValidator.notNull(requestMessage, "requestMessage");

        //
        // Check Device existence
        Device device;
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
            throw new DeviceNeverConnectedException(device.getId());
        }

        //
        // Check Device Connection status
        if (!DeviceConnectionStatus.CONNECTED.equals(device.getConnection().getStatus())) {
            throw new DeviceNotConnectedException(device.getId(), device.getConnection().getStatus());
        }
    }
}
