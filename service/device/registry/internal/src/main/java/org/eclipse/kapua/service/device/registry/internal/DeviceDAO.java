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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoPredicates;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.group.Groupable;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DevicePredicates;
import org.eclipse.kapua.service.device.registry.DeviceQuery;

/**
 * Device DAO
 * 
 * @since 1.0
 *
 */
public class DeviceDAO extends ServiceDAO {

    private static final AccessInfoService accessInfoService = KapuaLocator.getInstance().getService(AccessInfoService.class);
    private static final AccessInfoFactory accessInfoFactory = KapuaLocator.getInstance().getFactory(AccessInfoFactory.class);

    private static final AccessPermissionService accessPermissionService = KapuaLocator.getInstance().getService(AccessPermissionService.class);
    private static final AccessRoleService accessRoleService = KapuaLocator.getInstance().getService(AccessRoleService.class);

    private static final RoleService roleService = KapuaLocator.getInstance().getService(RoleService.class);
    private static final RolePermissionService rolePermissionService = KapuaLocator.getInstance().getService(RolePermissionService.class);

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

        handleKapuaQueryGroupPredicate(query);

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
        handleKapuaQueryGroupPredicate(query);

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

    /**
     * Handles the {@link Groupable} property of the {@link KapuaEntity}.
     * 
     * @param query
     *            The {@link DeviceQuery} to manage.
     * @since 1.0.0
     */
    private static void handleKapuaQueryGroupPredicate(KapuaQuery<Device> query) {
        KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
        if (kapuaSession != null) {
            try {
                KapuaId userId = kapuaSession.getUserId();

                AccessInfoQuery accessInfoQuery = accessInfoFactory.newQuery(kapuaSession.getScopeId());
                accessInfoQuery.setPredicate(new AttributePredicate<>(AccessInfoPredicates.USER_ID, userId));
                AccessInfoListResult accessInfos = KapuaSecurityUtils.doPriviledge(() -> accessInfoService.query(accessInfoQuery));

                List<Permission> groupPermissions = new ArrayList<>();
                if (!accessInfos.isEmpty()) {

                    AccessInfo accessInfo = accessInfos.getFirstItem();
                    AccessPermissionListResult accessPermissions = KapuaSecurityUtils.doPriviledge(() -> accessPermissionService.findByAccessInfoId(accessInfo.getScopeId(), accessInfo.getId()));

                    for (AccessPermission ap : accessPermissions.getItems()) {
                        Permission p = ap.getPermission();
                        if (DeviceDomain.INSTANCE.getName().equals(p.getDomain())) {
                            if (p.getAction() == null || Actions.read.equals(p.getAction())) {
                                if (p.getGroupId() == null) {
                                    groupPermissions.clear();
                                    break;
                                } else {
                                    groupPermissions.add(p);
                                }
                            }
                        }
                    }

                    AccessRoleListResult accessRoles = KapuaSecurityUtils.doPriviledge(() -> accessRoleService.findByAccessInfoId(accessInfo.getScopeId(), accessInfo.getId()));

                    for (AccessRole ar : accessRoles.getItems()) {
                        KapuaId roleId = ar.getRoleId();

                        Role role = KapuaSecurityUtils.doPriviledge(() -> roleService.find(ar.getScopeId(), roleId));

                        RolePermissionListResult rolePermissions = KapuaSecurityUtils.doPriviledge(() -> rolePermissionService.findByRoleId(role.getScopeId(), role.getId()));

                        for (RolePermission rp : rolePermissions.getItems()) {

                            Permission p = rp.getPermission();
                            if (DeviceDomain.INSTANCE.getName().equals(p.getDomain())) {
                                if (p.getAction() == null || Actions.read.equals(p.getAction())) {
                                    if (p.getGroupId() == null) {
                                        groupPermissions.clear();
                                        break;
                                    } else {
                                        groupPermissions.add(p);
                                    }
                                }
                            }
                        }
                    }
                }

                AndPredicate andPredicate = new AndPredicate();
                if (!groupPermissions.isEmpty()) {
                    int i = 0;
                    KapuaId[] groupsIds = new KapuaEid[groupPermissions.size()];
                    for (Permission p : groupPermissions) {
                        groupsIds[i++] = p.getGroupId();
                    }
                    andPredicate.and(new AttributePredicate<>(DevicePredicates.GROUP_ID, groupsIds));
                }

                if (query.getPredicate() != null) {
                    andPredicate.and(query.getPredicate());
                }

                query.setPredicate(andPredicate);
            } catch (Exception e) {
                KapuaException.internalError(e, "Error while grouping");
            }
        }
    }

}
