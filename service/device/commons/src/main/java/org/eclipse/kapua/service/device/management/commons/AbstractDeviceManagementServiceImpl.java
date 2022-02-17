/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.commons;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ThrowingRunnable;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.type.ObjectTypeConverter;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.DeviceManagementService;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseBadRequestException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseCodeException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseContentException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseInternalErrorException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseNotFoundException;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseUnknownCodeException;
import org.eclipse.kapua.service.device.management.message.notification.NotifyStatus;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Utility {@code abstract} {@link Class} used to provide utility methods to all implementation of {@link DeviceManagementService}s.
 *
 * @since 1.0.0
 */
public abstract class AbstractDeviceManagementServiceImpl {

    protected static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    protected static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    protected static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    private static final DeviceEventService DEVICE_EVENT_SERVICE = LOCATOR.getService(DeviceEventService.class);
    private static final DeviceEventFactory DEVICE_EVENT_FACTORY = LOCATOR.getFactory(DeviceEventFactory.class);

    private static final DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);
    private static final DeviceManagementOperationFactory DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(DeviceManagementOperationFactory.class);

    /**
     * Creates a {@link org.eclipse.kapua.service.device.registry.event.DeviceEvent} extracting data from the given {@link KapuaRequestMessage} and {@link KapuaResponseMessage}.
     * <p>
     * This operation is performed using {@link KapuaSecurityUtils#doPrivileged(ThrowingRunnable)} since {@link org.eclipse.kapua.service.device.registry.event.DeviceEventDomain} isn't a required
     * permission to use any of the Device Management Services.
     *
     * @param scopeId         The scopeId in which to create the {@link org.eclipse.kapua.service.device.registry.event.DeviceEvent}
     * @param deviceId        The {@link org.eclipse.kapua.service.device.registry.Device} id for which the {@link org.eclipse.kapua.service.device.registry.event.DeviceEvent} is created
     * @param requestMessage  The {@link KapuaRequestMessage} of the DeviceManagementService operation from which to extract data.
     * @param responseMessage The {@link KapuaResponseMessage} of the DeviceManagementService operation from which to extract data.
     * @throws KapuaException If the creation of the {@link org.eclipse.kapua.service.device.registry.event.DeviceEvent} fails for some reasons
     * @since 1.0.0
     */
    protected void createDeviceEvent(KapuaId scopeId, KapuaId deviceId, KapuaRequestMessage<?, ?> requestMessage, KapuaResponseMessage<?, ?> responseMessage) throws KapuaException {

        DeviceEventCreator deviceEventCreator =
                DEVICE_EVENT_FACTORY.newCreator(
                        scopeId,
                        deviceId,
                        responseMessage != null ? responseMessage.getReceivedOn() : requestMessage.getSentOn(),
                        requestMessage.getChannel().getAppName().getValue());

        deviceEventCreator.setPosition(responseMessage != null ? responseMessage.getPosition() : null);
        deviceEventCreator.setSentOn(responseMessage != null ? responseMessage.getSentOn() : requestMessage.getSentOn());
        deviceEventCreator.setAction(requestMessage.getChannel().getMethod());
        deviceEventCreator.setResponseCode(responseMessage != null ? responseMessage.getResponseCode() : KapuaResponseCode.SENT);
        deviceEventCreator.setEventMessage(responseMessage != null ? responseMessage.getPayload().toDisplayString() : requestMessage.getPayload().toDisplayString());

        KapuaSecurityUtils.doPrivileged(() -> DEVICE_EVENT_SERVICE.create(deviceEventCreator));
    }

    protected KapuaId createManagementOperation(KapuaId scopeId, KapuaId deviceId, KapuaId operationId, KapuaRequestMessage<?, ?> requestMessage) throws KapuaException {

        DeviceManagementOperationCreator deviceManagementOperationCreator = DEVICE_MANAGEMENT_OPERATION_FACTORY.newCreator(scopeId);
        deviceManagementOperationCreator.setDeviceId(deviceId);
        deviceManagementOperationCreator.setOperationId(operationId);
        deviceManagementOperationCreator.setStartedOn(new Date());
        deviceManagementOperationCreator.setAppId(requestMessage.getChannel().getAppName().getValue());
        deviceManagementOperationCreator.setAction(requestMessage.getChannel().getMethod());
        deviceManagementOperationCreator.setResource(!requestMessage.getChannel().getSemanticParts().isEmpty() ? requestMessage.getChannel().getSemanticParts().get(0) : null);
        deviceManagementOperationCreator.setStatus(NotifyStatus.RUNNING);
        deviceManagementOperationCreator.setInputProperties(extractInputProperties(requestMessage));

        DeviceManagementOperation deviceManagementOperation = KapuaSecurityUtils.doPrivileged(() -> DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.create(deviceManagementOperationCreator));

        return deviceManagementOperation.getId();
    }

    protected void closeManagementOperation(KapuaId scopeId, KapuaId deviceId, KapuaId operationId) throws KapuaException {
        closeManagementOperation(scopeId, deviceId, operationId, null);
    }

    protected void closeManagementOperation(KapuaId scopeId, KapuaId deviceId, KapuaId operationId, KapuaResponseMessage<?, ?> responseMessageMessage) throws KapuaException {
        DeviceManagementOperation deviceManagementOperation = DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.findByOperationId(scopeId, operationId);

        if (deviceManagementOperation == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, operationId);
        }

        if (responseMessageMessage != null) {
            deviceManagementOperation.setStatus(responseMessageMessage.getResponseCode().isAccepted() ? NotifyStatus.COMPLETED : NotifyStatus.FAILED);
            deviceManagementOperation.setEndedOn(responseMessageMessage.getReceivedOn());
        } else {
            deviceManagementOperation.setStatus(NotifyStatus.FAILED);
            deviceManagementOperation.setEndedOn(new Date());
        }

        KapuaSecurityUtils.doPrivileged(() -> DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.update(deviceManagementOperation));
    }


    /**
     * Checks the {@link KapuaResponseMessage#getResponseCode()} if it is {@link KapuaResponseCode#ACCEPTED} or throws the proper {@link DeviceManagementResponseCodeException}.
     *
     * @param responseMessage The {@link KapuaResponseMessage} to check.
     * @throws DeviceManagementResponseCodeException if {@link KapuaResponseMessage#getResponseCode()} is not {@link KapuaResponseCode#ACCEPTED}.
     * @since 1.5.0
     */
    protected void checkResponseAcceptedOrThrowError(@NotNull KapuaResponseMessage<?, ?> responseMessage) throws DeviceManagementResponseCodeException {
        try {
            checkResponseAcceptedOrThrowError(responseMessage, null);
        } catch (DeviceManagementResponseContentException e) {
            // Ignored since it cannot be thrown when resultGetter is null.
        }
    }

    /**
     * Checks the {@link KapuaResponseMessage#getResponseCode()} and returns the result if it is {@link KapuaResponseCode#ACCEPTED} or throws the proper {@link DeviceManagementResponseCodeException}.
     * <p>
     * If {@link Callable} is not provided the returned result will be {@code null}.
     *
     * @param responseMessage The {@link KapuaResponseMessage} to check.
     * @param resultGetter    The {@link Callable} that gets the result.
     * @param <R>             The type of the result.
     * @return The result from the {@link KapuaResponseMessage} if {@link KapuaResponseMessage#getResponseCode()} is {@link KapuaResponseCode#ACCEPTED}.
     * @throws DeviceManagementResponseCodeException    if {@link KapuaResponseMessage#getResponseCode()} is not {@link KapuaResponseCode#ACCEPTED}.
     * @throws DeviceManagementResponseContentException if getting the result causes any {@link Exception}.
     * @since 1.5.0
     */
    protected <R> R checkResponseAcceptedOrThrowError(@NotNull KapuaResponseMessage<?, ?> responseMessage, @Null Callable<R> resultGetter) throws DeviceManagementResponseCodeException, DeviceManagementResponseContentException {
        if (responseMessage.getResponseCode().isAccepted()) {
            if (resultGetter != null) {
                try {
                    return resultGetter.call();
                } catch (Exception e) {
                    throw new DeviceManagementResponseContentException(e, responseMessage.getPayload());
                }
            } else {
                return null;
            }
        } else {
            throw buildExceptionFromDeviceResponseNotAccepted(responseMessage);
        }
    }

    /**
     * Builds the {@link DeviceManagementResponseCodeException} from the {@link KapuaResponseMessage}.
     *
     * @param kapuaResponseMessage The {@link KapuaResponseMessage} to build from.
     * @return The proper {@link DeviceManagementResponseCodeException} for the {@link KapuaResponseMessage#getResponseCode()}
     * @since 1.5.0
     */
    protected DeviceManagementResponseCodeException buildExceptionFromDeviceResponseNotAccepted(KapuaResponseMessage<?, ?> kapuaResponseMessage) {

        KapuaResponsePayload responsePayload = kapuaResponseMessage.getPayload();

        switch (kapuaResponseMessage.getResponseCode()) {
            case BAD_REQUEST:
                return new DeviceManagementResponseBadRequestException(kapuaResponseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionMessage());
            case NOT_FOUND:
                return new DeviceManagementResponseNotFoundException(kapuaResponseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionMessage());
            case INTERNAL_ERROR:
                return new DeviceManagementResponseInternalErrorException(kapuaResponseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionMessage());
            case ACCEPTED:
            default:
                return new DeviceManagementResponseUnknownCodeException(kapuaResponseMessage.getResponseCode(), responsePayload.getExceptionMessage(), responsePayload.getExceptionMessage());
        }
    }

    private List<DeviceManagementOperationProperty> extractInputProperties(KapuaRequestMessage<?, ?> requestMessage) {

        List<DeviceManagementOperationProperty> inputProperties = new ArrayList<>();
        Map<String, Object> properties = requestMessage.getPayload().getMetrics();

        properties.forEach((k, v) -> {
            if (v != null) {
                inputProperties.add(
                        DEVICE_MANAGEMENT_OPERATION_FACTORY.newStepProperty(
                                k,
                                ObjectTypeConverter.toString(v.getClass()),
                                ObjectValueConverter.toString(v))
                );
            }
        });

        return inputProperties;
    }

}
