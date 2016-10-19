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

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.model.query.EntityFetchStyle;
import org.eclipse.kapua.model.query.KapuaFetchStyle;
import org.eclipse.kapua.model.query.KapuaSortCriteria;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
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
import org.eclipse.kapua.service.device.registry.*;
import org.eclipse.kapua.service.device.registry.connection.*;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.internal.DeviceImpl;
import org.joda.time.DateTime;

import java.util.List;

@Path("/devices")
public class Devices extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceRegistryService                registryService      = locator.getService(DeviceRegistryService.class);
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
    private final DeviceConnectionFactory              connectionFactory    = locator.getFactory(DeviceConnectionFactory.class);
    private final DeviceConnectionService              connectionService    = locator.getService(DeviceConnectionService.class);

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
            devicesResult = (DeviceListResult) registryService.query(query);
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
            device = registryService.create(deviceCreator);
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
            device = registryService.find(scopeId, id);
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
            device = registryService.update(device);
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
            registryService.delete(scopeId, deviceKapuaId);
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

    /**
     * Returns an aggregated summary of the connection status of a group
     * of devices at a point in time. It returns the number of devices
     * currently connected, the number of devices which disconnect cleanly,
     * the number of devices which disconnected abruptly, and the number
     * of devices which have been disabled.
     *
     * @return The DeviceConnectionSummary
     */
    @GET
    @Path("connectionSummary")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceConnectionSummary connectionSummary()
    {
        DeviceConnectionSummary deviceSummary = connectionFactory.newConnectionSummary();
        KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
        DeviceConnectionQuery connectedQuery = connectionFactory.newQuery(scopeId);
        KapuaAttributePredicate connectedPredicate = new AttributePredicate<>(DeviceConnectionPredicates.CONNECTION_STATUS, DeviceConnectionStatus.CONNECTED);
        connectedQuery.setPredicate(connectedPredicate);
        DeviceConnectionQuery disconnectedQuery = connectionFactory.newQuery(scopeId);
        KapuaAttributePredicate disconnectedPredicate = new AttributePredicate<>(DeviceConnectionPredicates.CONNECTION_STATUS, DeviceConnectionStatus.DISCONNECTED);
        disconnectedQuery.setPredicate(disconnectedPredicate);
        DeviceConnectionQuery missingQuery = connectionFactory.newQuery(scopeId);
        KapuaAttributePredicate missingPredicate = new AttributePredicate<>(DeviceConnectionPredicates.CONNECTION_STATUS, DeviceConnectionStatus.MISSING);
        missingQuery.setPredicate(missingPredicate);
        DeviceQuery enabledQuery = deviceFactory.newQuery(scopeId);
        KapuaAttributePredicate enabledPredicate = new AttributePredicate<>(DevicePredicates.STATUS, DeviceStatus.ENABLED);
        enabledQuery.setPredicate(enabledPredicate);
        DeviceQuery disabledQuery = deviceFactory.newQuery(scopeId);
        KapuaAttributePredicate disabledPredicate = new AttributePredicate<>(DevicePredicates.STATUS, DeviceStatus.DISABLED);
        disabledQuery.setPredicate(disabledPredicate);
        try {
            deviceSummary.setConnected(connectionService.count(connectedQuery));
            deviceSummary.setDisconnected(connectionService.count(disconnectedQuery));
            deviceSummary.setMissing(connectionService.count(missingQuery));
            deviceSummary.setEnabled(registryService.count(enabledQuery));
            deviceSummary.setDisabled(registryService.count(disabledQuery));
        }
        catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(deviceSummary);
    }

    /**
     * Searches the device registry for devices based on the specified criteria.
     * Query parameters are used to defined the filtering predicate, the sorting and the pagination.
     * The supplied query parameters are combined into a logical AND, so all specified conditions
     * need to be satisfied for the device to be selected and returned.
     * Query parameters which allow for multiple values are combined into an IN predicate effectively
     * combining them into a logical OR of the values supplied.
     * <p>
     * The "fetch" query parameter can be used to control the amount of information to be loaded and
     * returned for each device. Allowed values are "BASIC" or "FULL". With "BASIC", the core attributes
     * of the device and the version information of its profile are returned. With "FULL",
     * all the additional device extended properties are loaded and returned.
     * <p>
     * The returned devices can be sorted. Allowed values for the sortField query parameter are:
     * "clientId", "displayName", "lastEventOn". Sorting can be specified ascending or descending using the
     * "sort" query parameter with values: "asc" or "desc". If no sortField is specified,
     * the returned devices will not follow any specific order.
     * <p>
     * If the flag DevicesResult.limitExceeded is set, the maximum number of entries to be returned
     * has been reached. More data exist and can be read by moving the offset forward in a new request
     *
     * @param clientId One or more clientId for the devices to be returned.
     * @param serialNumber One or more serial number for the devices to be returned.
     * @param displayName One or more display name for the devices to be returned.
     * @param imei One or more IMEI for the devices to be returned.
     * @param imsi One or more IMSI for the devices to be returned.
     * @param iccid One or more ICCID for the devices to be returned.
     * @param modelId The id of the model of the devices to be returned.
     * @param status The device status (Enabled/Disabled) of the devices to be returned.
     * @param biosVersion The bios version of the devices to be returned.
     * @param firmwareVersion The firmware version of the devices to be returned.
     * @param osVersion The OS version of the devices to be returned.
     * @param jvmVersion The JVM version of the devices to be returned.
     * @param osgiFrameworkVersion The OSGI Framework version of the devices to be returned.
     * @param applicationFrameworkVersion The application framework version of the devices to be returned.
     * @param applicationIdentifier The application identifiers of the devices to be returned.
     * @param customAttribute1 The value of customAttribute1 for the devices to be returned.
     * @param customAttribute2 The value of customAttribute2 for the devices to be returned.
     * @param customAttribute3 The value of customAttribute3 for the devices to be returned.
     * @param customAttribute4 The value of customAttribute4 for the devices to be returned.
     * @param customAttribute5 The value of customAttribute5 for the devices to be returned.
     * @param acceptEncoding The encoding accepted by the devices to be returned.
     * @param gpsLongitude The GPS Longitude of the devices to be returned.
     * @param gpsLatitude The GPS Latitude of the devices to be returned.
     * @param credentialsMode The credentials mode of the devices to be returned.
     * @param preferredUserId The preferred user id of the devices to be returned.
     * @param limit Maximum number of entries to be returned.
     * @param offset Starting offset for the entries to be returned.
     * @param fetch Specifies the amount of information requested. Allowed values are "BASIC" or "FULL". With "BASIC",
     *            the core attributes of the device and the version information of its profile are
     *            returned. With "FULL", all the additional extended attributes are loaded and returned.
     * @param sortField Optional sorting of the returned devices. Allowed values are: "clientId", "displayName", "lastEventOn".
     * @param sortOrder Optional sorting order. Allowed values are: "asc", "desc".
     * @return The list of devices matching the criteria supplied.
     */
    @GET
    @Path("search")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceListResult searchDevices(@QueryParam("clientId") List<String> clientId,
                                          @QueryParam("serialNumber") List<String> serialNumber,
                                          @QueryParam("displayName") List<String> displayName,
                                          @QueryParam("imei") List<String> imei,
                                          @QueryParam("imsi") List<String> imsi,
                                          @QueryParam("iccid") List<String> iccid,
                                          @QueryParam("modelId") String modelId,
                                          @QueryParam("status") String status,
                                          // @QueryParam("connectionStatus") String connectionStatus,
                                          @QueryParam("biosVersion") String biosVersion,
                                          @QueryParam("firmwareVersion") String firmwareVersion,
                                          @QueryParam("osVersion") String osVersion,
                                          @QueryParam("jvmVersion") String jvmVersion,
                                          @QueryParam("osgiFrameworkVersion") String osgiFrameworkVersion,
                                          @QueryParam("applicationFrameworkVersion") String applicationFrameworkVersion,
                                          @QueryParam("applicationIdentifier") String applicationIdentifier,
                                          @QueryParam("customAttribute1") String customAttribute1,
                                          @QueryParam("customAttribute2") String customAttribute2,
                                          @QueryParam("customAttribute3") String customAttribute3,
                                          @QueryParam("customAttribute4") String customAttribute4,
                                          @QueryParam("customAttribute5") String customAttribute5,
                                          @QueryParam("acceptEncoding") String acceptEncoding,
                                          @QueryParam("gpsLongitude") String gpsLongitude,
                                          @QueryParam("gpsLatitude") String gpsLatitude,
                                          @QueryParam("credentialsMode") String credentialsMode,
                                          @QueryParam("preferredUserId") String preferredUserId,
                                          @QueryParam("sortField") String sortField,
                                          @QueryParam("limit") @DefaultValue("50") int limit,
                                          @QueryParam("offset") @DefaultValue("0") int offset,
                                          @QueryParam("fetch") @DefaultValue("BASIC") String fetch,
                                          @QueryParam("sortOrder") @DefaultValue("desc") String sortOrder)
        throws KapuaIllegalArgumentException
    {
        KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
        DeviceListResult result = deviceFactory.newDeviceListResult();
        DeviceQuery query = deviceFactory.newQuery(scopeId);
        query.setLimit(limit + 1);
        query.setOffset(offset);
        KapuaAndPredicate andPredicate = new AndPredicate();
        if (clientId != null && clientId.size() > 0) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.CLIENT_ID, clientId.toArray(new String[] {})));
        }
        if (serialNumber != null && serialNumber.size() > 0) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.SERIAL_NUMBER, serialNumber.toArray(new String[] {})));
        }
        if (displayName != null && displayName.size() > 0) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.DISPLAY_NAME, displayName.toArray(new String[] {})));
        }
        if (imei != null && imei.size() > 0) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.IMEI, imei.toArray(new String[] {})));
        }
        if (imsi != null && imsi.size() > 0) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.IMSI, imsi.toArray(new String[] {})));
        }
        if (iccid != null && iccid.size() > 0) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.ICCID, iccid.toArray(new String[] {})));
        }
        if (modelId != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.MODEL_ID, modelId));
        }
        if (status != null) {
            try {
                DeviceStatus ds = null;
                ds = DeviceStatus.valueOf(status);
                andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.STATUS, status));
            }
            catch (IllegalArgumentException iae) {
                throw new KapuaIllegalArgumentException("status", status);
            }
        }
        // if (connectionStatus != null) {
        // try {
        // DeviceConnectionStatus dcs = null;
        // dcs = DeviceConnectionStatus.valueOf(connectionStatus);
        // andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.CONNECTION_STATUS, connectionStatus));
        // } catch (IllegalArgumentException iae) {
        // throw new KapuaIllegalArgumentException("connectionStatus", connectionStatus);
        // }
        // }
        if (biosVersion != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.BIOS_VERSION, biosVersion));
        }
        if (firmwareVersion != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.FIRMWARE_VERSION, firmwareVersion));
        }
        if (osVersion != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.OS_VERSION, osVersion));
        }
        if (jvmVersion != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.JVM_VERSION, jvmVersion));
        }
        if (osgiFrameworkVersion != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.OSGI_FRAMEWORK_VERSION, osgiFrameworkVersion));
        }
        if (applicationFrameworkVersion != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.APPLICATION_FRAMEWORK_VERSION, applicationFrameworkVersion));
        }
        if (applicationIdentifier != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.APPLICATION_IDENTIFIERS, applicationFrameworkVersion, KapuaAttributePredicate.Operator.LIKE));
        }
        if (customAttribute1 != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.CUSTOM_ATTRIBUTE_1, customAttribute1));
        }
        if (customAttribute2 != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.CUSTOM_ATTRIBUTE_2, customAttribute2));
        }
        if (customAttribute3 != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.CUSTOM_ATTRIBUTE_3, customAttribute3));
        }
        if (customAttribute4 != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.CUSTOM_ATTRIBUTE_4, customAttribute4));
        }
        if (customAttribute5 != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.CUSTOM_ATTRIBUTE_5, customAttribute5));
        }
        if (acceptEncoding != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.ACCEPT_ENCODING, acceptEncoding));
        }
        if (gpsLongitude != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.GPS_LONGITUDE, gpsLongitude));
        }
        if (gpsLatitude != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.GPS_LATITUDE, gpsLatitude));
        }
        if (credentialsMode != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.CREDENTIALS_MODE, credentialsMode));
        }
        if (preferredUserId != null) {
            andPredicate = andPredicate.and(new AttributePredicate<>(DevicePredicates.PREFERRED_USER_ID, preferredUserId));
        }

        // Fetch
        if (fetch != null) {
            try {
                KapuaFetchStyle fetchStyle = EntityFetchStyle.valueOf(fetch);
                query.setFetchStyle(fetchStyle);
            }
            catch (IllegalArgumentException iae) {
                throw new KapuaIllegalArgumentException("fetch", fetch);
            }
        }

        // Sort
        SortOrder so = SortOrder.ASCENDING;
        if (sortOrder != null) {
            if ("desc".equals(sortOrder)) {
                so = SortOrder.DESCENDING;
            }
            else if ("asc".equals(sortOrder)) {
                so = SortOrder.ASCENDING;
            }
            else {
                throw new KapuaIllegalArgumentException("sortOrder", sortOrder);
            }
        }
        if (sortField != null) {
            KapuaSortCriteria sortCriteria;
            switch (sortField) {
                case DevicePredicates.STATUS:
                    sortCriteria = new FieldSortCriteria(DevicePredicates.STATUS, so);
                    break;
                case DevicePredicates.CLIENT_ID:
                    sortCriteria = new FieldSortCriteria(DevicePredicates.CLIENT_ID, so);
                    break;
                case DevicePredicates.DISPLAY_NAME:
                    sortCriteria = new FieldSortCriteria(DevicePredicates.DISPLAY_NAME, so);
                    break;
                case DevicePredicates.LAST_EVENT_ON:
                    sortCriteria = new FieldSortCriteria(DevicePredicates.LAST_EVENT_ON, so);
                    break;
                default:
                    throw new KapuaIllegalArgumentException("sortField", sortField);
            }
            query.setSortCriteria(sortCriteria);
        }
        query.setPredicate(andPredicate);
        try {
            result = (DeviceListResult) registryService.query(query);
        }
        catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(result);
    }
}
