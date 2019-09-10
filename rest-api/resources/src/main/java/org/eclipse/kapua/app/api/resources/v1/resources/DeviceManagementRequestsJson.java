/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;

import org.eclipse.kapua.app.api.resources.v1.resources.marker.JsonSerializationFixed;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.device.management.JsonGenericRequestMessage;
import org.eclipse.kapua.app.api.resources.v1.resources.model.device.management.JsonGenericResponseMessage;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.request.internal.message.request.GenericRequestMessageImpl;
import org.eclipse.kapua.service.device.management.request.internal.message.request.GenericRequestPayloadImpl;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @see JsonSerializationFixed
 */
@Api(value = "Devices", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/devices/{deviceId}/requests")
public class DeviceManagementRequestsJson extends AbstractKapuaResource implements JsonSerializationFixed {

    private static final DeviceManagementRequests DEVICE_MANAGEMENT_REQUESTS = new DeviceManagementRequests();

    /**
     * Sends a request message to a device.
     * This call is generally used to perform remote management of resources
     * attached to the device such sensors and registries.
     *
     * @param scopeId                   The {@link ScopeId} of the {@link Device}.
     * @param deviceId                  The {@link Device} ID.
     * @param timeout                   The timeout of the request execution
     * @param jsonGenericRequestMessage The input request
     * @return The response output.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(nickname = "deviceRequestSend", value = "Sends a request", notes = "Sends a request message to a device", response = JsonGenericResponseMessage.class)
    public JsonGenericResponseMessage sendRequest(
            @ApiParam(value = "The ScopeId of the device", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the request execution") @QueryParam("timeout") Long timeout,
            @ApiParam(value = "The input request", required = true) JsonGenericRequestMessage jsonGenericRequestMessage) throws Exception {

        GenericRequestMessage genericRequestMessage = new GenericRequestMessageImpl();

        genericRequestMessage.setId(jsonGenericRequestMessage.getId());
        genericRequestMessage.setScopeId(scopeId);
        genericRequestMessage.setDeviceId(jsonGenericRequestMessage.getDeviceId());
        genericRequestMessage.setClientId(jsonGenericRequestMessage.getClientId());
        genericRequestMessage.setReceivedOn(jsonGenericRequestMessage.getReceivedOn());
        genericRequestMessage.setSentOn(jsonGenericRequestMessage.getSentOn());
        genericRequestMessage.setCapturedOn(jsonGenericRequestMessage.getCapturedOn());
        genericRequestMessage.setPosition(jsonGenericRequestMessage.getPosition());
        genericRequestMessage.setChannel(jsonGenericRequestMessage.getChannel());

        GenericRequestPayload kapuaDataPayload = new GenericRequestPayloadImpl();

        if (jsonGenericRequestMessage.getPayload() != null) {
            kapuaDataPayload.setBody(jsonGenericRequestMessage.getPayload().getBody());

            jsonGenericRequestMessage.getPayload().getMetrics().forEach(
                    jsonMetric -> {
                        String name = jsonMetric.getName();
                        Object value = ObjectValueConverter.fromString(jsonMetric.getValue(), jsonMetric.getValueType());

                        kapuaDataPayload.getMetrics().put(name, value);
                    });
        }

        genericRequestMessage.setPayload(kapuaDataPayload);

        GenericResponseMessage genericResponseMessage = DEVICE_MANAGEMENT_REQUESTS.sendRequest(scopeId, deviceId, timeout, genericRequestMessage);

        return new JsonGenericResponseMessage(genericResponseMessage);
    }
}
