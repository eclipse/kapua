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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DevicePredicates;

/**
 * Device DAO
 * 
 * @since 1.0.0
 *
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
        device.setApplicationIdentifiers(deviceCreator.getApplicationIdentifiers());
        device.setAcceptEncoding(deviceCreator.getAcceptEncoding());
        device.setCustomAttribute1(deviceCreator.getCustomAttribute1());
        device.setCustomAttribute2(deviceCreator.getCustomAttribute2());
        device.setCustomAttribute3(deviceCreator.getCustomAttribute3());
        device.setCustomAttribute4(deviceCreator.getCustomAttribute4());
        device.setCustomAttribute5(deviceCreator.getCustomAttribute5());
        device.setCredentialsMode(deviceCreator.getCredentialsMode());
        device.setPreferredUserId(deviceCreator.getPreferredUserId());

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
     * @throws KapuaEntityNotFoundException
     *             If {@link Device} is not found.
     */
    public static Device update(EntityManager em, Device device) throws KapuaEntityNotFoundException {
        DeviceImpl deviceImpl = (DeviceImpl) device;
        return ServiceDAO.update(em, DeviceImpl.class, deviceImpl);
    }

    /**
     * Finds the device by device identifier
     * 
     * @param em
     * @param deviceId
     * @return
     */
    public static Device find(EntityManager em, KapuaId deviceId) {
        return em.find(DeviceImpl.class, deviceId);
    }

    /**
     * Returns the device list matching the provided query
     * 
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     * @throws Exception
     */
    public static DeviceListResult query(EntityManager em, KapuaQuery<Device> query)
            throws KapuaException {

        handleKapuaQueryGroupPredicate(query, DeviceDomain.INSTANCE, DevicePredicates.GROUP_ID);

        return ServiceDAO.query(em, Device.class, DeviceImpl.class, new DeviceListResultImpl(), query);
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
        handleKapuaQueryGroupPredicate(query, DeviceDomain.INSTANCE, DevicePredicates.GROUP_ID);

        return ServiceDAO.count(em, Device.class, DeviceImpl.class, query);
    }

    /**
     * Deletes the device by device identifier
     * 
     * @param em
     * @param deviceId
     * @throws KapuaEntityNotFoundException
     *             If {@link Device} is not found.
     */
    public static void delete(EntityManager em, KapuaId deviceId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, DeviceImpl.class, deviceId);
    }
}
