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
package org.eclipse.kapua.app.api.v1.resources;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Devices")
@Path("{scopeId}/devices/{deviceId}/bundles")
public class DeviceManagementBundles extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
    private final DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);
    private final DeviceBundleManagementService bundleService = locator.getService(DeviceBundleManagementService.class);
    
    /**
     * Returns the list of all the Bundles installed on the device.
     *
     * @param deviceId
     * The id of the device
     * @return The list of Bundles
     */
     @GET
     @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
     @ApiOperation(value = "Gets a list of bundles", notes = "Returns the list of all the Bundles installed on the device.", response = DeviceBundles.class)
     public DeviceBundles get(
             @PathParam("scopeId") ScopeId scopeId,
             @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId, //
             @QueryParam("timeout") @DefaultValue("") Long timeout) {
     DeviceBundles deviceBundles = null;
     try {
     deviceBundles = bundleService.get(scopeId, deviceId, timeout);
     } catch (Throwable t) {
     handleException(t);
     }
     return returnNotNullEntity(deviceBundles);
     }

     /**
      * Starts the bundle
      *
      * @param deviceId
      * The {@link Device} ID.
      * @param bundleId
      * the ID of the bundle to start
      * @return HTTP 200 if operation has completed successfully.
      */
      @POST
      @Path("{bundleId}/_start")
      @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
      public Response start(
              @PathParam("scopeId") ScopeId scopeId,
              @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId, //
              @ApiParam(value = "the ID of the bundle to start", required = true) @PathParam("bundleId") String bundleId,
              @QueryParam("timeout") @DefaultValue("") Long timeout) {
      try {
      bundleService.start(scopeId, deviceId, bundleId, timeout);
      } catch (Throwable t) {
      handleException(t);
      }
      return Response.ok().build();
      }
      
      /**
       * Stops the bundle
       *
       * @param deviceId
       * The {@link Device} ID.
       * @param bundleId
       * the ID of the bundle to stop
       * @return HTTP 200 if operation has completed successfully.
       */
       @POST
       @Path("{bundleId}/_stop")
       @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
       public Response stop(
               @PathParam("scopeId") ScopeId scopeId,
               @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId, //
               @ApiParam(value = "the ID of the bundle to stop", required = true) @PathParam("bundleId") String bundleId,
               @QueryParam("timeout") @DefaultValue("") Long timeout) {
       try {
       bundleService.stop(scopeId, deviceId, bundleId, timeout);
       } catch (Throwable t) {
       handleException(t);
       }
       return Response.ok().build();
       }
}
