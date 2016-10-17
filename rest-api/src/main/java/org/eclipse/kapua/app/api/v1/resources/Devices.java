/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates
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
package org.eclipse.kapua.app.api.v1.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAndPredicate;
import org.eclipse.kapua.service.device.management.command.DeviceCommandFactory;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandManagementService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotFactory;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.internal.DeviceImpl;
import org.joda.time.DateTime;

@Path("/devices")
public class Devices extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
    private final DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);
    private final DeviceCommandManagementService commandService = locator.getService(DeviceCommandManagementService.class);
    private final DeviceCommandFactory commandFactory = locator.getFactory(DeviceCommandFactory.class);
    private final DeviceConfigurationManagementService configurationService = locator.getService(DeviceConfigurationManagementService.class);
    private final DeviceSnapshotManagementService snapshotService = locator.getService(DeviceSnapshotManagementService.class);
    private final DeviceSnapshotFactory snapshotFactory = locator.getFactory(DeviceSnapshotFactory.class);
    private final DevicePackageManagementService packageService = locator.getService(DevicePackageManagementService.class);
    private final DevicePackageFactory packageFactory = locator.getFactory(DevicePackageFactory.class);
    private final DeviceEventService eventService = locator.getService(DeviceEventService.class);
    private final DeviceEventFactory eventFactory = locator.getFactory(DeviceEventFactory.class);

    /**
     * Returns the list of all the Devices visible to the currently connected user.
     *
     * @return The list of requested Device objects.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceListResult getDevices() {
        DeviceListResult devicesResult = deviceFactory.newDeviceListResult();
        try {
            DeviceQuery query = deviceFactory.newQuery(KapuaSecurityUtils.getSession().getScopeId());
            devicesResult = (DeviceListResult) deviceRegistryService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return devicesResult;
    }

    /**
     * Creates a new Device based on the information provided in DeviceCreator parameter.
     *
     * @param deviceCreator
     *            Provides the information for the new Device to be created.
     * @return The newly created Device object.
     */
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Device postDevice(DeviceCreator deviceCreator) {
        Device device = null;
        try {
            deviceCreator.setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            device = deviceRegistryService.create(deviceCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(device);
    }

    /**
     * Returns the Device specified by the "id" path parameter.
     *
     * @param deviceId
     *            The id of the Device requested.
     * @return The requested Device object.
     */
    @GET
    @Path("{deviceId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Device getDevice(@PathParam("deviceId") String deviceId) {
        Device device = null;
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId id = KapuaEid.parseShortId(deviceId);
            device = deviceRegistryService.find(scopeId, id);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(device);
    }

    /**
     * Updates a device based on the information provided in the Device parameter.
     *
     * @param device
     *            Provides the information to update the device.
     * @return The updated Device object.
     */
    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Device updateDevice(Device device) {
        try {
            ((DeviceImpl) device).setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            device = deviceRegistryService.update(device);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(device);
    }

    /**
     * Deletes a device based on the id provided in deviceId parameter.
     *
     * @param deviceId
     *            Provides the id of the device to delete.
     */
    @DELETE
    @Path("{deviceId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response deleteDevice(@PathParam("deviceId") String deviceId) {
        try {
            KapuaId deviceKapuaId = KapuaEid.parseShortId(deviceId);
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            deviceRegistryService.delete(scopeId, deviceKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    @POST
    @Path("{deviceId}/command")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public DeviceCommandOutput sendCommand(
            DeviceCommandInput commandInput,
            @PathParam("deviceId") String deviceId,
            @QueryParam("timeout") Long timeout) throws NumberFormatException, KapuaException {
        KapuaId deviceKapuaId = KapuaEid.parseShortId(deviceId);
        KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
        return commandService.exec(scopeId, deviceKapuaId, commandInput, timeout);
    }

    /**
     * Returns the configuration of a device or the configuration of the OSGi component
     * identified with specified PID (service's persistent identity).
     * In the OSGi framework, the service's persistent identity is defined as the name attribute of the
     * Component Descriptor XML file; at runtime, the same value is also available
     * in the component.name and in the service.pid attributes of the Component Configuration.
     *
     * @param deviceId
     *            The id of the device
     * @param componentId
     *            An optional id of the component to get the configuration for
     * @return The requested configurations
     */

    @GET
    @Path("{deviceId}/configurations")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceConfiguration getConfigurations(@PathParam("deviceId") String deviceId,
            @QueryParam("componentId") String componentId) {
        DeviceConfiguration deviceConfiguration = null;
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId id = KapuaEid.parseShortId(deviceId);
            deviceConfiguration = configurationService.get(scopeId, id, null, componentId, null);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(deviceConfiguration);
    }

    /**
     * Returns the list of all the Snapshots available on the device.
     *
     * @return The list of Snapshot Ids.
     */
    @GET
    @Path("{deviceId}/snapshots")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceSnapshots getSnapshots(@PathParam("deviceId") String deviceId) {
        DeviceSnapshots deviceSnapshots = snapshotFactory.newDeviceSnapshots();
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId id = KapuaEid.parseShortId(deviceId);
            deviceSnapshots = snapshotService.get(scopeId, id, null);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(deviceSnapshots);
    }

    /**
     * Creates a new Device based on the information provided in DeviceCreator parameter.
     *
     * @param deviceId
     *            The id of the device
     * @param snapshotId
     *            The id of the snapshot to be rolled bac
     * @return The newly created Device object.
     */
    @POST
    @Path("{deviceId}/rollback/{snapshotId}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response rollbackSnapshot(@PathParam("deviceId") String deviceId, @PathParam("snapshotId") String snapshotId) {
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId id = KapuaEid.parseShortId(deviceId);
            snapshotService.rollback(scopeId, id, snapshotId, null);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    /**
     * Returns the events for the device identified by the specified
     * ClientID under the account of the currently connected user.
     * <p>
     * If the flag DeviceEventsResult.limitExceeded is set, the maximum number
     * of entries to be returned has been reached, more events exist and can
     * be read by moving the offset forward in a new request
     *
     * @param deviceId
     *            The client ID of the device requested.
     * @param limit
     *            Maximum number of entries to be returned.
     * @param offset
     *            Starting offset for the entries to be returned.
     * @param startDate
     *            Start date of the date range requested. The parameter
     *            is expressed as a long counting the number of milliseconds since
     *            January 1, 1970, 00:00:00 GMT. The default value of 0 means no
     *            start date. Alternatively, the date can be expressed as a string
     *            following the ISO 8601 format.
     * @param endDate
     *            End date of the date range requested. The parameter
     *            is expressed as a long counting the number of milliseconds since
     *            January 1, 1970, 00:00:00 GMT. The default value of 0 means no
     *            start date. Alternatively, the date can be expressed as a string
     *            following the ISO 8601 format.
     * @return The list of Events
     */
    @GET
    @Path("{deviceId}/events")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceEventListResult getEvents(
            @PathParam("deviceId") String deviceId,
            @QueryParam("limit") @DefaultValue("50") int limit,
            @QueryParam("offset") int offset,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate) {
        DeviceEventListResult deviceEvents = eventFactory.newDeviceListResult();
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId id = KapuaEid.parseShortId(deviceId);
            DeviceEventQuery query = eventFactory.newQuery(scopeId);

            KapuaAndPredicate andPredicate = new AndPredicate();
            andPredicate.and(new AttributePredicate<>(DeviceEventPredicates.DEVICE_ID, id));
            // TODO Date filter not working?
            if (startDate != null) {
                DateTime parsedStartDate = DateTime.parse(startDate);
                andPredicate = andPredicate.and(new AttributePredicate<>(DeviceEventPredicates.RECEIVED_ON, parsedStartDate.toDate(), KapuaAttributePredicate.Operator.GREATER_THAN));
            }
            if (endDate != null) {
                DateTime parsedEndDate = DateTime.parse(endDate);
                andPredicate = andPredicate.and(new AttributePredicate<>(DeviceEventPredicates.RECEIVED_ON, parsedEndDate.toDate(), KapuaAttributePredicate.Operator.LESS_THAN));
            }

            query.setPredicate(andPredicate);
            query.setSortCriteria(new FieldSortCriteria(DeviceEventPredicates.RECEIVED_ON, FieldSortCriteria.SortOrder.DESCENDING));
            query.setOffset(offset);
            query.setLimit(limit);

            // query execute
            deviceEvents = (DeviceEventListResult) eventService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(deviceEvents);
    }

    /**
     * Returns the list of packages deployed on a device.
     *
     * @param deviceId
     *            The client ID of the Device requested.
     * @return The list of the installed packages.
     */
    @GET
    @Path("{deviceId}/packages")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DevicePackages getPackages(@PathParam("deviceId") String deviceId) {
        DevicePackages result = packageFactory.newDeviceDeploymentPackages();
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId id = KapuaEid.parseShortId(deviceId);
            result = packageService.getInstalled(scopeId, id, null);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(result);
    }

    /**
     * Install a new deployment package on a device.
     *
     * @param deviceId
     *            The client ID of the Device requested.
     * @param request
     *            Mandatory object with all the informations
     *            needed to download a package.
     * @return The list of the installed packages.
     */
    @POST
    @Path("{deviceId}/packages")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response installPackage(@PathParam("deviceId") String deviceId, DevicePackageDownloadRequest request) {
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId id = KapuaEid.parseShortId(deviceId);
            packageService.downloadExec(scopeId, id, request, null);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    /**
     * Uninstalls a package deployed on a device.
     *
     * @param deviceId
     *            The device ID of the Device requested.
     * @param request
     *            Mandatory object with all the informations
     *            needed to uninstall a package.
     * @return The list of the installed packages.
     */
    @DELETE
    @Path("{deviceId}/packages")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response uninstallPackage(@PathParam("deviceId") String deviceId, DevicePackageUninstallRequest request) {
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId id = KapuaEid.parseShortId(deviceId);
            packageService.uninstallExec(scopeId, id, request, null);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
