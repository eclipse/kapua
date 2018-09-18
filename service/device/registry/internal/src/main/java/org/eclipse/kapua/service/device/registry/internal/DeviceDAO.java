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
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceDomains;
import org.eclipse.kapua.service.device.registry.DeviceListResult;

import java.util.List;

/**
 * {@link Device} DAO
 *
 * @since 1.0.0
 */
public class DeviceDAO extends ServiceDAO {

    /**
     * Creates a new Device
     *
     * @param em
     * @param deviceCreator
     * @return
     */
    public static Device create(EntityManager em, DeviceCreator deviceCreator) {
        Device device = new DeviceImpl(deviceCreator.getScopeId());

        device.setGroupId(deviceCreator.getGroupId());
        device.setClientId(deviceCreator.getClientId());
        device.setStatus(deviceCreator.getStatus());
        device.setDisplayName(deviceCreator.getDisplayName());
        device.setSerialNumber(deviceCreator.getSerialNumber());
        device.setModelId(deviceCreator.getModelId());
        device.setImei(deviceCreator.getImei());
        device.setImsi(deviceCreator.getImsi());
        device.setIccid(deviceCreator.getIccid());
        device.setBiosVersion(deviceCreator.getBiosVersion());
        device.setFirmwareVersion(deviceCreator.getFirmwareVersion());
        device.setOsVersion(deviceCreator.getOsVersion());
        device.setJvmVersion(deviceCreator.getJvmVersion());
        device.setOsgiFrameworkVersion(deviceCreator.getOsgiFrameworkVersion());
        device.setApplicationFrameworkVersion(deviceCreator.getApplicationFrameworkVersion());
        device.setConnectionInterface(deviceCreator.getConnectionInterface());
        device.setConnectionIp(deviceCreator.getConnectionIp());
        device.setApplicationIdentifiers(deviceCreator.getApplicationIdentifiers());
        device.setAcceptEncoding(deviceCreator.getAcceptEncoding());
        device.setCustomAttribute1(deviceCreator.getCustomAttribute1());
        device.setCustomAttribute2(deviceCreator.getCustomAttribute2());
        device.setCustomAttribute3(deviceCreator.getCustomAttribute3());
        device.setCustomAttribute4(deviceCreator.getCustomAttribute4());
        device.setCustomAttribute5(deviceCreator.getCustomAttribute5());

        device.setConnectionId(deviceCreator.getConnectionId());
        device.setLastEventId(deviceCreator.getLastEventId());

        return ServiceDAO.create(em, device);
    }

    /**
     * Updates the provided device
     *
     * @param em
     * @param device
     * @return
     * @throws KapuaEntityNotFoundException If {@link Device} is not found.
     */
    public static Device update(EntityManager em, Device device) throws KapuaEntityNotFoundException {
        DeviceImpl deviceImpl = (DeviceImpl) device;
        return ServiceDAO.update(em, DeviceImpl.class, deviceImpl);
    }

    /**
     * Finds the device by device identifier
     *
     * @param em
     * @param scopeId
     * @param deviceId
     * @return
     */
    public static Device find(EntityManager em, KapuaId scopeId, KapuaId deviceId) {
        return ServiceDAO.find(em, DeviceImpl.class, scopeId, deviceId);
    }

    /**
     * Returns the device list matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static DeviceListResult query(EntityManager em, KapuaQuery<Device> query)
            throws KapuaException {

        handleKapuaQueryGroupPredicate(query, DeviceDomains.DEVICE_DOMAIN, DeviceAttributes.GROUP_ID);

        // This is fix up for a the Eclipse Link limitation on OneToOne that ignores Lazy Fetch on Java SE environment.
        // Link: https://www.eclipse.org/eclipselink/documentation/2.6/concepts/mappingintro002.htm#CEGCJEHD
        // Strategy to fix this is to force the fetching and then remove what was not requested.
        List<String> fetchAttributes = query.getFetchAttributes();

        boolean deviceConnectionFetchAdded = false;
        if (fetchAttributes == null || !fetchAttributes.contains(DeviceAttributes.CONNECTION)) {
            deviceConnectionFetchAdded = true;
            query.addFetchAttributes(DeviceAttributes.CONNECTION);
        }

        boolean deviceLastEventFetchAdded = false;
        if (fetchAttributes == null || !fetchAttributes.contains(DeviceAttributes.LAST_EVENT)) {
            deviceLastEventFetchAdded = true;
            query.addFetchAttributes(DeviceAttributes.LAST_EVENT);
        }

        DeviceListResult results = ServiceDAO.query(em, Device.class, DeviceImpl.class, new DeviceListResultImpl(), query);

        if (deviceConnectionFetchAdded || deviceLastEventFetchAdded) {
            for (Device d : results.getItems()) {
                if (deviceConnectionFetchAdded) {
                    ((DeviceImpl) d).setConnection(null);
                    query.getFetchAttributes().remove(DeviceAttributes.CONNECTION);
                }

                if (deviceLastEventFetchAdded) {
                    ((DeviceImpl) d).setLastEvent(null);
                    query.getFetchAttributes().remove(DeviceAttributes.LAST_EVENT);
                }
            }
        }

        return results;
    }

    /**
     * Returns the device count matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<Device> query)
            throws KapuaException {
        handleKapuaQueryGroupPredicate(query, DeviceDomains.DEVICE_DOMAIN, DeviceAttributes.GROUP_ID);

        return ServiceDAO.count(em, Device.class, DeviceImpl.class, query);
    }

    /**
     * Deletes the device by device identifier
     *
     * @param em
     * @param scopeId
     * @param deviceId
     * @throws KapuaEntityNotFoundException If {@link Device} is not found.
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId deviceId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, DeviceImpl.class, scopeId, deviceId);
    }
}
