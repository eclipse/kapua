/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.internal;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceLinker;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.jpa.EventStorer;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoRepository;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionRepository;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleRepository;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionRepository;
import org.eclipse.kapua.service.authorization.role.RoleRepository;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceDomain;
import org.eclipse.kapua.service.device.registry.DeviceDomains;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;
import org.eclipse.kapua.storage.TxContext;
import org.eclipse.kapua.storage.TxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceRegistryService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class DeviceRegistryServiceImpl
        extends KapuaConfigurableServiceLinker
        implements DeviceRegistryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRegistryServiceImpl.class);
    private final TxManager txManager;
    private final DeviceRepository deviceRepository;
    private final DeviceFactory entityFactory;
    private final AccessInfoFactory accessInfoFactory;
    private final AccessInfoRepository accessInfoRepository;
    private final AccessPermissionRepository accessPermissionRepository;
    private final AccessRoleRepository accessRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final EventStorer eventStorer;

    @Inject
    public DeviceRegistryServiceImpl(
            ServiceConfigurationManager serviceConfigurationManager,
            TxManager txManager,
            DeviceRepository deviceRepository,
            DeviceFactory entityFactory,
            AccessInfoFactory accessInfoFactory,
            AccessInfoRepository accessInfoRepository,
            AccessPermissionRepository accessPermissionRepository,
            AccessRoleRepository accessRoleRepository,
            RoleRepository roleRepository,
            RolePermissionRepository rolePermissionRepository, EventStorer eventStorer) {
        super(serviceConfigurationManager);
        this.txManager = txManager;
        this.deviceRepository = deviceRepository;
        this.entityFactory = entityFactory;
        this.accessInfoFactory = accessInfoFactory;
        this.accessInfoRepository = accessInfoRepository;
        this.accessPermissionRepository = accessPermissionRepository;
        this.accessRoleRepository = accessRoleRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.eventStorer = eventStorer;
    }

    @Override
    public Device create(DeviceCreator deviceCreator)
            throws KapuaException {
        DeviceValidation.validateCreatePreconditions(deviceCreator);

        //
        // Check entity limit
        serviceConfigurationManager.checkAllowedEntities(deviceCreator.getScopeId(), "Devices");

        //
        // Check duplicate clientId
        DeviceQuery query = entityFactory.newQuery(deviceCreator.getScopeId());
        query.setPredicate(query.attributePredicate(DeviceAttributes.CLIENT_ID, deviceCreator.getClientId()));

        //TODO: check whether this is anywhere efficient
        if (txManager.executeWithResult(tx -> deviceRepository.count(tx, query)) > 0) {
            throw new KapuaDuplicateNameException(deviceCreator.getClientId());
        }

        final Device device = entityFactory.newEntity(deviceCreator.getScopeId());

        device.setGroupId(deviceCreator.getGroupId());
        device.setClientId(deviceCreator.getClientId());
        device.setStatus(deviceCreator.getStatus());
        device.setDisplayName(deviceCreator.getDisplayName());
        device.setSerialNumber(deviceCreator.getSerialNumber());
        device.setModelId(deviceCreator.getModelId());
        device.setModelName(deviceCreator.getModelName());
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
        device.setExtendedProperties(deviceCreator.getExtendedProperties());

        device.setConnectionId(deviceCreator.getConnectionId());
        device.setLastEventId(deviceCreator.getLastEventId());

        //
        // Do create
        return txManager.executeWithResult(
                tx -> deviceRepository.create(tx, device),
                eventStorer::accept);
    }

    @Override
    public Device update(Device device)
            throws KapuaException {
        DeviceValidation.validateUpdatePreconditions(device);

        //
        // Do update
        return txManager.executeWithResult(tx -> {
                    final Device currentDevice = deviceRepository.find(tx, device.getScopeId(), device.getId());
                    if (currentDevice == null) {
                        throw new KapuaEntityNotFoundException(Device.TYPE, device.getId());
                    }
                    // Update
                    return deviceRepository.update(tx, device);
                },
                eventStorer::accept);
    }

    @Override
    public Device find(KapuaId scopeId, KapuaId entityId)
            throws KapuaException {
        DeviceValidation.validateFindPreconditions(scopeId, entityId);

        //
        // Do find
        return txManager.executeWithResult(tx -> deviceRepository.find(tx, scopeId, entityId));
    }

    @Override
    public Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException {
        DeviceValidation.validateFindByClientIdPreconditions(scopeId, clientId);

        //
        // Check cache and/or do find
        return txManager.executeWithResult(tx -> deviceRepository.findByClientId(tx, scopeId, clientId));
    }

    @Override
    public DeviceListResult query(KapuaQuery query)
            throws KapuaException {
        DeviceValidation.validateQueryPreconditions(query);

        // Do query
        return txManager.executeWithResult(tx -> {
            handleKapuaQueryGroupPredicate(tx, query, DeviceDomains.DEVICE_DOMAIN, DeviceAttributes.GROUP_ID);
            return deviceRepository.query(tx, query);
        });
    }

    private void handleKapuaQueryGroupPredicate(TxContext txContext, KapuaQuery query, DeviceDomain domain, String groupPredicateName) throws KapuaException {
        KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
        if (accessInfoFactory != null) {
            if (kapuaSession != null && !kapuaSession.isTrustedMode()) {
                handleKapuaQueryGroupPredicate(txContext, kapuaSession, query, domain, groupPredicateName);
            }
        } else {
            LOGGER.warn("'Access Group Permission' feature is disabled");
        }
    }

    private void handleKapuaQueryGroupPredicate(TxContext txContext, KapuaSession kapuaSession, KapuaQuery query, Domain domain, String groupPredicateName) throws KapuaException {
        try {
            KapuaId userId = kapuaSession.getUserId();

            final AccessInfo accessInfo = accessInfoRepository.findByUserId(txContext, kapuaSession.getScopeId(), userId);

            final List<Permission> groupPermissions = new ArrayList<>();
            if (accessInfo != null) {
                AccessPermissionListResult accessPermissions = accessPermissionRepository.findByAccessInfoId(txContext, accessInfo.getScopeId(), accessInfo.getId());
                for (AccessPermission ap : accessPermissions.getItems()) {
                    if (checkGroupPermission(domain, groupPermissions, ap.getPermission())) {
                        break;
                    }
                }

                AccessRoleListResult accessRoles = accessRoleRepository.findByAccessInfoId(txContext, accessInfo.getScopeId(), accessInfo.getId());

                for (AccessRole ar : accessRoles.getItems()) {
                    KapuaId roleId = ar.getRoleId();

                    Role role = roleRepository.find(txContext, ar.getScopeId(), roleId);

                    RolePermissionListResult rolePermissions = rolePermissionRepository.findByRoleId(txContext, role.getScopeId(), role.getId());

                    for (RolePermission rp : rolePermissions.getItems()) {
                        if (checkGroupPermission(domain, groupPermissions, rp.getPermission())) {
                            break;
                        }
                    }
                }
            }

            AndPredicate andPredicate = query.andPredicate();
            if (!groupPermissions.isEmpty()) {
                int i = 0;
                KapuaId[] groupsIds = new KapuaEid[groupPermissions.size()];
                for (Permission p : groupPermissions) {
                    groupsIds[i++] = p.getGroupId();
                }
                andPredicate.and(query.attributePredicate(groupPredicateName, groupsIds));
            }

            if (query.getPredicate() != null) {
                andPredicate.and(query.getPredicate());
            }

            query.setPredicate(andPredicate);
        } catch (Exception e) {
            throw KapuaException.internalError(e, "Error while grouping!");
        }
    }

    private static boolean checkGroupPermission(@NonNull Domain domain, @NonNull List<Permission> groupPermissions, @NonNull Permission permission) {
        if ((permission.getDomain() == null || domain.getName().equals(permission.getDomain())) &&
                (permission.getAction() == null || Actions.read.equals(permission.getAction()))) {
            if (permission.getGroupId() == null) {
                groupPermissions.clear();
                return true;
            } else {
                groupPermissions.add(permission);
            }
        }
        return false;
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        DeviceValidation.validateCountPreconditions(query);

        // Do count
        return txManager.executeWithResult(tx -> {
            handleKapuaQueryGroupPredicate(tx, query, DeviceDomains.DEVICE_DOMAIN, DeviceAttributes.GROUP_ID);
            return deviceRepository.count(tx, query);
        });
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceId) throws KapuaException {
        DeviceValidation.validateDeletePreconditions(scopeId, deviceId);

        // Do delete
        txManager.executeWithResult(
                tx -> deviceRepository.delete(tx, scopeId, deviceId),
                eventStorer::accept);
    }

    //@ListenServiceEvent(fromAddress="account")
    //@ListenServiceEvent(fromAddress="authorization")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOGGER.info("DeviceRegistryService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("group".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteDeviceByGroupId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        } else if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteDeviceByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    //
    // Private methods
    //

    private void deleteDeviceByGroupId(KapuaId scopeId, KapuaId groupId) throws KapuaException {
        DeviceQuery query = entityFactory.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(DeviceAttributes.GROUP_ID, groupId));

        txManager.<Void>executeWithResult(tx -> {
            DeviceListResult devicesToDelete = deviceRepository.query(tx, query);

            for (Device d : devicesToDelete.getItems()) {
                d.setGroupId(null);
                deviceRepository.update(tx, d);
            }
            return null;
        });
    }

    private void deleteDeviceByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);

        DeviceQuery query = deviceFactory.newQuery(accountId);

        txManager.<Void>executeWithResult(tx -> {
            DeviceListResult devicesToDelete = deviceRepository.query(tx, query);

            for (Device d : devicesToDelete.getItems()) {
                deviceRepository.delete(tx, d);
            }
            return null;
        });
    }
}
