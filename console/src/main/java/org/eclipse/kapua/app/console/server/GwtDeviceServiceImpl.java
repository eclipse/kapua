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
package org.eclipse.kapua.app.console.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDevice.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceCreator;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceEvent;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates.GwtDeviceConnectionStatus;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.predicate.KapuaAndPredicate;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DevicePredicates;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * The server side implementation of the Device RPC service.
 */
public class GwtDeviceServiceImpl extends KapuaConfigurableRemoteServiceServlet<DeviceRegistryService> implements GwtDeviceService {

    private static final long serialVersionUID = -1391026997499175151L;

    public GwtDeviceServiceImpl() {
        super(KapuaLocator.getInstance().getService(DeviceRegistryService.class));
    }

    public GwtDevice findDevice(String scopeIdString, String clientId)
            throws GwtKapuaException {
        GwtDevice gwtDevice = null;
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);

            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
            Device device = deviceRegistryService.findByClientId(scopeId, clientId);

            gwtDevice = KapuaGwtModelConverter.convert(device);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtDevice;
    }

    public ListLoadResult<GwtGroupedNVPair> findDeviceProfile(String scopeIdString, String clientId)
            throws GwtKapuaException {
        List<GwtGroupedNVPair> pairs = new ArrayList<GwtGroupedNVPair>();
        KapuaLocator locator = KapuaLocator.getInstance();

        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceConnectionService deviceConnectionService = locator.getService(DeviceConnectionService.class);
        try {

            KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);

            Device device = deviceRegistryService.findByClientId(scopeId, clientId);

            if (device != null) {
                pairs.add(new GwtGroupedNVPair("devInfo", "devStatus", device.getStatus().toString()));

                DeviceConnection deviceConnection = null;
                if (device.getConnectionId() != null) {
                    deviceConnection = deviceConnectionService.find(scopeId, device.getConnectionId());
                }

                if (deviceConnection != null) {
                    pairs.add(new GwtGroupedNVPair("connInfo", "connConnectionStatus", deviceConnection.getStatus().toString()));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connClientId", device.getClientId()));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connUserId", deviceConnection.getUserId().toCompactId()));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connReservedUserId", deviceConnection.getReservedUserId() != null ? deviceConnection.getReservedUserId().toCompactId() : null));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connUserCouplingMode", GwtConnectionUserCouplingMode.valueOf(deviceConnection.getUserCouplingMode().name()).getLabel()));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connClientIp", deviceConnection.getClientIp()));
					pairs.add(new GwtGroupedNVPair("netInfo", "netConnIface", device.getConnectionInterface()));
                    pairs.add(new GwtGroupedNVPair("netInfo", "netConnIp", deviceConnection.getClientIp()));
                    pairs.add(new GwtGroupedNVPair("netInfo", "netConnIfaceIp", device.getConnectionIp()));
                    pairs.add(new GwtGroupedNVPair("devInfo", "devConnectionStatus", deviceConnection.getStatus().toString()));

                } else {
                    pairs.add(new GwtGroupedNVPair("connInfo", "connConnectionStatus", DeviceConnectionStatus.DISCONNECTED.toString()));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connClientId", null));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connUserId", null));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connLastUser", null));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connUserCouplingMode", null));
                    pairs.add(new GwtGroupedNVPair("connInfo", "connClientIp", null));
                    pairs.add(new GwtGroupedNVPair("netInfo", "netConnIface", null));
                    pairs.add(new GwtGroupedNVPair("netInfo", "netConnIp", null));
                    pairs.add(new GwtGroupedNVPair("netInfo", "netConnIfaceIp", null));
                    pairs.add(new GwtGroupedNVPair("devInfo", "devConnectionStatus", DeviceConnectionStatus.DISCONNECTED.toString()));
                }

                pairs.add(new GwtGroupedNVPair("devInfo", "devClientId", device.getClientId()));
                pairs.add(new GwtGroupedNVPair("devInfo", "devDisplayName", device.getDisplayName()));
                pairs.add(new GwtGroupedNVPair("devInfo", "devGroupId", device.getGroupId() != null ? device.getGroupId().toCompactId() : null));

                if (device.getLastEventId() != null) {
                    DeviceEvent lastEvent = deviceEventService.find(scopeId, device.getLastEventId());

                    if (lastEvent != null) {
                        pairs.add(new GwtGroupedNVPair("devInfo", "devLastEventType", lastEvent.getResource()));
                        pairs.add(new GwtGroupedNVPair("devInfo", "devLastEventOn", lastEvent.getReceivedOn() != null ? lastEvent.getReceivedOn().getTime() : null));
                    } else {
                        pairs.add(new GwtGroupedNVPair("devInfo", "devLastEventType", null));
                        pairs.add(new GwtGroupedNVPair("devInfo", "devLastEventOn", null));
                    }
                } else {
                    if (deviceConnection != null) {
                        pairs.add(new GwtGroupedNVPair("devInfo", "devLastEventType", deviceConnection.getStatus().name()));
                        pairs.add(new GwtGroupedNVPair("devInfo", "devLastEventOn", deviceConnection.getModifiedOn().getTime()));
                    } else {
                        pairs.add(new GwtGroupedNVPair("devInfo", "devLastEventType", null));
                        pairs.add(new GwtGroupedNVPair("devInfo", "devLastEventOn", null));
                    }
                }

                pairs.add(new GwtGroupedNVPair("devInfo", "devApps", device.getApplicationIdentifiers()));
                pairs.add(new GwtGroupedNVPair("devInfo", "devAccEnc", device.getAcceptEncoding()));

                pairs.add(new GwtGroupedNVPair("devAttributesInfo", "devCustomAttribute1", device.getCustomAttribute1()));
                pairs.add(new GwtGroupedNVPair("devAttributesInfo", "devCustomAttribute2", device.getCustomAttribute2()));
                pairs.add(new GwtGroupedNVPair("devAttributesInfo", "devCustomAttribute3", device.getCustomAttribute3()));
                pairs.add(new GwtGroupedNVPair("devAttributesInfo", "devCustomAttribute4", device.getCustomAttribute4()));
                pairs.add(new GwtGroupedNVPair("devAttributesInfo", "devCustomAttribute5", device.getCustomAttribute5()));

                pairs.add(new GwtGroupedNVPair("devHw", "devModelName", device.getModelId()));
                pairs.add(new GwtGroupedNVPair("devHw", "devModelId", device.getModelId()));
                pairs.add(new GwtGroupedNVPair("devHw", "devSerialNumber", device.getSerialNumber()));

                pairs.add(new GwtGroupedNVPair("devSw", "devFirmwareVersion", device.getFirmwareVersion()));
                pairs.add(new GwtGroupedNVPair("devSw", "devBiosVersion", device.getBiosVersion()));
                pairs.add(new GwtGroupedNVPair("devSw", "devOsVersion", device.getOsVersion()));

                pairs.add(new GwtGroupedNVPair("devJava", "devJvmVersion", device.getJvmVersion()));

                // GPS infos retrieval
                DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);
                DeviceEventQuery eventQuery = deviceEventFactory.newQuery(device.getScopeId());
                eventQuery.setLimit(1);
                eventQuery.setSortCriteria(new FieldSortCriteria(DeviceEventPredicates.RECEIVED_ON, SortOrder.DESCENDING));

                AndPredicate andPredicate = new AndPredicate();
                andPredicate.and(new AttributePredicate<KapuaId>(DeviceEventPredicates.DEVICE_ID, device.getId()));
                andPredicate.and(new AttributePredicate<String>(DeviceEventPredicates.RESOURCE, "BIRTH"));

                eventQuery.setPredicate(andPredicate);

                KapuaListResult<DeviceEvent> events = deviceEventService.query(eventQuery);
                if (!events.isEmpty()) {
                    DeviceEvent lastEvent = events.getItem(0);
                    KapuaPosition eventPosition = lastEvent.getPosition();

                    if (eventPosition != null) {
                        pairs.add(new GwtGroupedNVPair("gpsInfo", "gpsLat", String.valueOf(eventPosition.getLatitude())));
                        pairs.add(new GwtGroupedNVPair("gpsInfo", "gpsLong", String.valueOf(eventPosition.getLongitude())));
                    }
                } else {
                    pairs.add(new GwtGroupedNVPair("gpsInfo", "gpsLat", null));
                    pairs.add(new GwtGroupedNVPair("gpsInfo", "gpsLong", null));
                }

                pairs.add(new GwtGroupedNVPair("modemInfo", "modemImei", device.getImei()));
                pairs.add(new GwtGroupedNVPair("modemInfo", "modemImsi", device.getImsi()));
                pairs.add(new GwtGroupedNVPair("modemInfo", "modemIccid", device.getIccid()));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return new BaseListLoadResult<GwtGroupedNVPair>(pairs);
    }

    public PagingLoadResult<GwtDevice> findDevices(PagingLoadConfig loadConfig,
            String scopeIdString,
            GwtDeviceQueryPredicates predicates)
            throws GwtKapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);

        List<GwtDevice> gwtDevices = new ArrayList<GwtDevice>();
        BasePagingLoadResult<GwtDevice> gwtResults;
        int totalResult = 0;
        try {
            BasePagingLoadConfig bplc = (BasePagingLoadConfig) loadConfig;
            DeviceQuery deviceQuery = deviceFactory.newQuery(KapuaEid.parseCompactId(scopeIdString));
            deviceQuery.setLimit(bplc.getLimit() + 1);
            deviceQuery.setOffset(bplc.getOffset());

            AndPredicate andPred = new AndPredicate();

            if (predicates.getClientId() != null) {
                andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.CLIENT_ID, predicates.getUnescapedClientId(), Operator.STARTS_WITH));
            }
            if (predicates.getDisplayName() != null) {
                andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.DISPLAY_NAME, predicates.getUnescapedDisplayName(), Operator.STARTS_WITH));
            }
            if (predicates.getSerialNumber() != null) {
                andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.SERIAL_NUMBER, predicates.getUnescapedSerialNumber()));
            }
            if (predicates.getDeviceStatus() != null) {
                andPred = andPred.and(new AttributePredicate<DeviceStatus>(DevicePredicates.STATUS, DeviceStatus.valueOf(predicates.getDeviceStatus())));
            }
            if (predicates.getIotFrameworkVersion() != null) {
                andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.APPLICATION_FRAMEWORK_VERSION, predicates.getIotFrameworkVersion()));
            }
            if (predicates.getApplicationIdentifiers() != null) {
                andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.APPLICATION_IDENTIFIERS, predicates.getApplicationIdentifiers(), Operator.LIKE));
            }
            if (predicates.getCustomAttribute1() != null) {
                andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.CUSTOM_ATTRIBUTE_1, predicates.getCustomAttribute1()));
            }
            if (predicates.getCustomAttribute2() != null) {
                andPred = andPred.and(new AttributePredicate<String>(DevicePredicates.CUSTOM_ATTRIBUTE_2, predicates.getCustomAttribute2()));
            }
            if (predicates.getDeviceConnectionStatus() != null) {
                andPred = andPred.and(new AttributePredicate<DeviceConnectionStatus>(DevicePredicates.CONNECTION_STATUS, DeviceConnectionStatus.valueOf(predicates.getDeviceConnectionStatus())));
            }
            if (predicates.getGroupId() != null) {
                andPred = andPred.and(new AttributePredicate<KapuaId>(DevicePredicates.GROUP_ID, KapuaEid.parseCompactId(predicates.getGroupId())));
            }

            if (predicates.getSortAttribute() != null) {
                SortOrder sortOrder = SortOrder.ASCENDING;
                if (predicates.getSortOrder().equals(SortOrder.DESCENDING.name())) {
                    sortOrder = SortOrder.DESCENDING;
                }

                if (predicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.CLIENT_ID.name())) {
                    deviceQuery.setSortCriteria(new FieldSortCriteria(DevicePredicates.CLIENT_ID, sortOrder));
                } else if (predicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.DISPLAY_NAME.name())) {
                    deviceQuery.setSortCriteria(new FieldSortCriteria(DevicePredicates.DISPLAY_NAME, sortOrder));
                } else if (predicates.getSortAttribute().equals(GwtDeviceQueryPredicates.GwtSortAttribute.LAST_EVENT_ON.name())) {
                    deviceQuery.setSortCriteria(new FieldSortCriteria(DevicePredicates.LAST_EVENT_ON, sortOrder));
                }
            } else {
                deviceQuery.setSortCriteria(new FieldSortCriteria(DevicePredicates.CLIENT_ID, SortOrder.ASCENDING));
            }

            deviceQuery.setPredicate(andPred);

            deviceQuery.addFetchAttributes(DevicePredicates.CONNECTION);
            deviceQuery.addFetchAttributes(DevicePredicates.LAST_EVENT);

            KapuaListResult<Device> devices = deviceRegistryService.query(deviceQuery);
            totalResult = (int) deviceRegistryService.count(deviceQuery);
            for (Device d : devices.getItems()) {
                GwtDevice gwtDevice = KapuaGwtModelConverter.convert(d);

                // Connection info
                gwtDevice.setGwtDeviceConnectionStatus(GwtDeviceConnectionStatus.DISCONNECTED.name());
                gwtDevice.setConnectionIp(d.getConnectionIp());
                gwtDevice.setConnectionInterface(d.getConnectionInterface());

                DeviceConnection deviceConnection = d.getConnection();
                if (deviceConnection != null) {
                    gwtDevice.setClientIp(deviceConnection.getClientIp());
                    gwtDevice.setGwtDeviceConnectionStatus(deviceConnection.getStatus().name());
                    gwtDevice.setLastEventOn(deviceConnection.getModifiedOn());
                    gwtDevice.setLastEventType(deviceConnection.getStatus().name());
                }

                if (d.getLastEvent() != null) {
                    DeviceEvent lastEvent = d.getLastEvent();

                    gwtDevice.setLastEventType(lastEvent.getResource());
                    gwtDevice.setLastEventOn(lastEvent.getReceivedOn());
                }

                gwtDevices.add(gwtDevice);
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        gwtResults = new BasePagingLoadResult<GwtDevice>(gwtDevices);
        gwtResults.setOffset(loadConfig.getOffset());
        gwtResults.setTotalLength(totalResult);

        return gwtResults;
    }

    public GwtDevice createDevice(GwtXSRFToken xsrfToken, GwtDeviceCreator gwtDeviceCreator)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);
        GwtDevice gwtDevice = null;

        try {
            KapuaId scopeId = KapuaEid.parseCompactId(gwtDeviceCreator.getScopeId());

            DeviceCreator deviceCreator = deviceFactory.newCreator(scopeId, gwtDeviceCreator.getClientId());
            deviceCreator.setDisplayName(gwtDeviceCreator.getDisplayName());
            deviceCreator.setGroupId(GwtKapuaModelConverter.convert(gwtDeviceCreator.getGroupId()));

            // FIXME One day it will be specified from the form. In the meantime, defaults to LOOSE
            // deviceCreator.setCredentialsMode(DeviceCredentialsMode.LOOSE);

            deviceCreator.setCustomAttribute1(gwtDeviceCreator.getCustomAttribute1());
            deviceCreator.setCustomAttribute2(gwtDeviceCreator.getCustomAttribute2());
            deviceCreator.setCustomAttribute3(gwtDeviceCreator.getCustomAttribute3());
            deviceCreator.setCustomAttribute4(gwtDeviceCreator.getCustomAttribute4());
            deviceCreator.setCustomAttribute5(gwtDeviceCreator.getCustomAttribute5());

            Device device = deviceRegistryService.create(deviceCreator);

            gwtDevice = KapuaGwtModelConverter.convert(device);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtDevice;
    }

    public GwtDevice updateAttributes(GwtXSRFToken xsrfToken, GwtDevice gwtDevice)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);
        Device device = null;
        GwtDevice gwtDeviceUpdated = null;

        try {
            //
            // Find original device
            KapuaId scopeId = KapuaEid.parseCompactId(gwtDevice.getScopeId());
            KapuaId deviceId = KapuaEid.parseCompactId(gwtDevice.getId());
            device = deviceRegistryService.find(scopeId, deviceId);

            //
            // Updated values
            // Gerenal info
            device.setDisplayName(gwtDevice.getUnescapedDisplayName());
            device.setStatus(DeviceStatus.valueOf(gwtDevice.getGwtDeviceStatus()));
            device.setGroupId(GwtKapuaModelConverter.convert(gwtDevice.getGroupId()));

            // Security Stuff
            // device.setCredentialsMode(DeviceCredentialsMode.valueOf(gwtDevice.getCredentialsTight()));
            // KapuaId deviceUserId = KapuaEid.parseCompactId(gwtDevice.getDeviceUserId());
            // device.setPreferredUserId(deviceUserId);

            // Custom attributes
            device.setCustomAttribute1(gwtDevice.getUnescapedCustomAttribute1());
            device.setCustomAttribute2(gwtDevice.getUnescapedCustomAttribute2());
            device.setCustomAttribute3(gwtDevice.getUnescapedCustomAttribute3());
            device.setCustomAttribute4(gwtDevice.getUnescapedCustomAttribute4());
            device.setCustomAttribute5(gwtDevice.getUnescapedCustomAttribute5());

            device.setOptlock(gwtDevice.getOptlock());

            // Do the update
            device = deviceRegistryService.update(device);

            // Convert to gwt object
            gwtDeviceUpdated = KapuaGwtModelConverter.convert(device);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtDeviceUpdated;
    }

    public void deleteDevice(GwtXSRFToken xsrfToken, String scopeIdString, String clientId)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        try {
            KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);

            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceRegistryService drs = locator.getService(DeviceRegistryService.class);
            Device d = drs.findByClientId(scopeId, clientId);
            drs.delete(d.getScopeId(), d.getId());
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    public PagingLoadResult<GwtDeviceEvent> findDeviceEvents(PagingLoadConfig loadConfig,
            GwtDevice gwtDevice,
            Date startDate,
            Date endDate)
            throws GwtKapuaException {
        ArrayList<GwtDeviceEvent> gwtDeviceEvents = new ArrayList<GwtDeviceEvent>();
        BasePagingLoadResult<GwtDeviceEvent> gwtResults = null;

        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceEventService des = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        try {

            // prepare the query
            BasePagingLoadConfig bplc = (BasePagingLoadConfig) loadConfig;
            DeviceEventQuery query = deviceEventFactory.newQuery(KapuaEid.parseCompactId(gwtDevice.getScopeId()));

            KapuaAndPredicate andPredicate = new AndPredicate();

            andPredicate.and(new AttributePredicate<KapuaId>(DeviceEventPredicates.DEVICE_ID, KapuaEid.parseCompactId(gwtDevice.getId())));
            andPredicate.and(new AttributePredicate<Date>(DeviceEventPredicates.RECEIVED_ON, startDate, Operator.GREATER_THAN));
            andPredicate.and(new AttributePredicate<Date>(DeviceEventPredicates.RECEIVED_ON, endDate, Operator.LESS_THAN));

            query.setPredicate(andPredicate);
            query.setSortCriteria(new FieldSortCriteria(DeviceEventPredicates.RECEIVED_ON, SortOrder.DESCENDING));
            query.setOffset(bplc.getOffset());
            query.setLimit(bplc.getLimit());

            // query execute
            KapuaListResult<DeviceEvent> deviceEvents = des.query(query);

            // prepare results
            for (DeviceEvent deviceEvent : deviceEvents.getItems()) {
                gwtDeviceEvents.add(KapuaGwtModelConverter.convert(deviceEvent));
            }
            gwtResults = new BasePagingLoadResult<GwtDeviceEvent>(gwtDeviceEvents);
            gwtResults.setOffset(loadConfig.getOffset());
            gwtResults.setTotalLength((int) des.count(query));

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtResults;
    }

    public String getTileEndpoint() throws GwtKapuaException {
        return ConsoleSetting.getInstance().getString(ConsoleSettingKeys.DEVICE_MAP_TILE_URI);
    }

    public boolean isMapEnabled() {
        return ConsoleSetting.getInstance().getBoolean(ConsoleSettingKeys.DEVICE_MAP_ENABLED);
    }

}
