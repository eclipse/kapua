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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceStatus;

public class DeviceDAO extends ServiceDAO {

    public static Device create(EntityManager em, DeviceCreator deviceCreator) {
        Device device = new DeviceImpl(deviceCreator.getScopeId());

        device.setClientId(deviceCreator.getClientId());
        device.setStatus(DeviceStatus.ENABLED);
        device.setDisplayName(deviceCreator.getDisplayName());
        device.setLastEventOn(null);
        device.setLastEventType(null);
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

        // issue #57
        device.setConnectionId(deviceCreator.getConnectionId());

        return ServiceDAO.create(em, device);
    }

    public static Device update(EntityManager em, Device device) {
        DeviceImpl deviceImpl = (DeviceImpl) device;
        return ServiceDAO.update(em, DeviceImpl.class, deviceImpl);
    }

    public static Device find(EntityManager em, KapuaId deviceId) {
        return em.find(DeviceImpl.class, deviceId);
    }

    public static DeviceListResult query(EntityManager em, KapuaQuery<Device> query)
            throws KapuaException {
        return ServiceDAO.query(em, Device.class, DeviceImpl.class, new DeviceListResultImpl(), query);
    }

    public static long count(EntityManager em, KapuaQuery<Device> query)
            throws KapuaException {
        return ServiceDAO.count(em, Device.class, DeviceImpl.class, query);
    }

    public static void delete(EntityManager em, KapuaId deviceId) {
        ServiceDAO.delete(em, DeviceImpl.class, deviceId);
    }
}
