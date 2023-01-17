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
 *     Red Hat
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.common;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceDomain;
import org.eclipse.kapua.service.device.registry.DeviceExtendedProperty;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceRegistrySettingKeys;
import org.eclipse.kapua.service.device.registry.DeviceRegistrySettings;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryServiceImpl;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagService;

import java.util.List;

/**
 * Logic used to validate preconditions required to execute the {@link DeviceRegistryServiceImpl} operations.
 *
 * @since 1.0.0
 */
public final class DeviceValidation {

    private static final String DEVICE_CREATOR_CLIENT_ID = "deviceCreator.clientId";

    private static final DeviceRegistrySettings DEVICE_REGISTRY_SETTINGS = DeviceRegistrySettings.getInstance();
    private static final Integer BIRTH_FIELDS_CLOB_MAX_LENGTH = DEVICE_REGISTRY_SETTINGS.getInt(DeviceRegistrySettingKeys.DEVICE_REGISTRY_LIFECYCLE_BIRTH_FIELDS_CLOB_LENGTH_MAX);
    private static final Integer BIRTH_FIELDS_EXTENDED_PROPERTY_VALUE_MAX_LENGTH = DEVICE_REGISTRY_SETTINGS.getInt(DeviceRegistrySettingKeys.DEVICE_REGISTRY_LIFECYCLE_BIRTH_FIELDS_EXTENDED_PROPERTIES_VALUE_LENGTH_MAX);

    private static final DeviceDomain DEVICE_DOMAIN = new DeviceDomain();

    private static final KapuaLocator KAPUA_LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = KAPUA_LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = KAPUA_LOCATOR.getFactory(PermissionFactory.class);

    private static final GroupService GROUP_SERVICE = KAPUA_LOCATOR.getService(GroupService.class);

    private static final DeviceConnectionService DEVICE_CONNECTION_SERVICE = KAPUA_LOCATOR.getService(DeviceConnectionService.class);

    private static final DeviceEventService DEVICE_EVENT_SERVICE = KAPUA_LOCATOR.getService(DeviceEventService.class);

    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = KAPUA_LOCATOR.getService(DeviceRegistryService.class);
    private static final DeviceFactory DEVICE_FACTORY = KAPUA_LOCATOR.getFactory(DeviceFactory.class);

    private static final TagService TAG_SERVICE = KAPUA_LOCATOR.getService(TagService.class);

    private DeviceValidation() {
    }

    /**
     * Validates the {@link DeviceCreator}.
     *
     * @param deviceCreator The {@link DeviceCreator} to validate.
     * @throws org.eclipse.kapua.KapuaIllegalArgumentException if one of the {@link DeviceCreator} fields is invalid.
     * @throws KapuaException                                  if there are other errors.
     * @since 1.0.0
     */
    public static void validateCreatePreconditions(DeviceCreator deviceCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(deviceCreator, "deviceCreator");
        ArgumentValidator.notNull(deviceCreator.getScopeId(), "deviceCreator.scopeId");

        // .clientId
        ArgumentValidator.notEmptyOrNull(deviceCreator.getClientId(), DEVICE_CREATOR_CLIENT_ID);
        ArgumentValidator.lengthRange(deviceCreator.getClientId(), 1, 255, DEVICE_CREATOR_CLIENT_ID);
        ArgumentValidator.match(deviceCreator.getClientId(), DeviceValidationRegex.CLIENT_ID, DEVICE_CREATOR_CLIENT_ID);

        // .groupId
        if (deviceCreator.getGroupId() != null) {
            ArgumentValidator.notNull(
                    KapuaSecurityUtils.doPrivileged(
                            () -> GROUP_SERVICE.find(deviceCreator.getScopeId(), deviceCreator.getGroupId())
                    ), "deviceCreator.groupId");
        }

        // .status
        ArgumentValidator.notNull(deviceCreator.getStatus(), "deviceCreator.status");

        // .connectionId
        if (deviceCreator.getConnectionId() != null) {
            ArgumentValidator.notNull(
                    KapuaSecurityUtils.doPrivileged(
                            () -> DEVICE_CONNECTION_SERVICE.find(deviceCreator.getScopeId(), deviceCreator.getConnectionId())
                    ), "deviceCreator.connectionId");
        }

        // .lastEventId
        if (deviceCreator.getLastEventId() != null) {
            ArgumentValidator.notNull(
                    KapuaSecurityUtils.doPrivileged(
                            () -> DEVICE_EVENT_SERVICE.find(deviceCreator.getScopeId(), deviceCreator.getLastEventId())
                    ), "deviceCreator.lastEventId");
        }

        // .displayName
        if (!Strings.isNullOrEmpty(deviceCreator.getDisplayName())) {
            ArgumentValidator.lengthRange(deviceCreator.getDisplayName(), 1, 255, "deviceCreator.displayName");
        }

        // .serialNumber
        if (!Strings.isNullOrEmpty(deviceCreator.getSerialNumber())) {
            ArgumentValidator.lengthRange(deviceCreator.getSerialNumber(), 1, 255, "deviceCreator.serialNumber");
        }

        // .modelId
        if (!Strings.isNullOrEmpty(deviceCreator.getModelId())) {
            ArgumentValidator.lengthRange(deviceCreator.getModelId(), 1, 255, "deviceCreator.modelId");
        }

        // .modelName
        if (!Strings.isNullOrEmpty(deviceCreator.getModelName())) {
            ArgumentValidator.lengthRange(deviceCreator.getModelName(), 1, 255, "deviceCreator.modelName");
        }

        // .imei
        if (!Strings.isNullOrEmpty(deviceCreator.getImei())) {
            ArgumentValidator.lengthRange(deviceCreator.getImei(), 1, 24, "deviceCreator.imei");
        }

        // .imsi
        if (!Strings.isNullOrEmpty(deviceCreator.getImsi())) {
            ArgumentValidator.lengthRange(deviceCreator.getImsi(), 1, 15, "deviceCreator.imsi");
        }

        // .iccid
        if (!Strings.isNullOrEmpty(deviceCreator.getIccid())) {
            ArgumentValidator.lengthRange(deviceCreator.getIccid(), 1, 22, "deviceCreator.iccd");
        }

        // .biosVersion
        if (!Strings.isNullOrEmpty(deviceCreator.getBiosVersion())) {
            ArgumentValidator.lengthRange(deviceCreator.getBiosVersion(), 1, 255, "deviceCreator.biosVersion");
        }

        // .firmwareVersion
        if (!Strings.isNullOrEmpty(deviceCreator.getFirmwareVersion())) {
            ArgumentValidator.lengthRange(deviceCreator.getFirmwareVersion(), 1, 255, "deviceCreator.firmwareVersion");
        }

        // .osVersion
        if (!Strings.isNullOrEmpty(deviceCreator.getOsVersion())) {
            ArgumentValidator.lengthRange(deviceCreator.getOsVersion(), 1, 255, "deviceCreator.osVersion");
        }

        // .jvmVersion
        if (!Strings.isNullOrEmpty(deviceCreator.getJvmVersion())) {
            ArgumentValidator.lengthRange(deviceCreator.getJvmVersion(), 1, 255, "deviceCreator.jvmVersion");
        }

        // .osgiFrameworkVersion
        if (!Strings.isNullOrEmpty(deviceCreator.getOsgiFrameworkVersion())) {
            ArgumentValidator.lengthRange(deviceCreator.getOsgiFrameworkVersion(), 1, 255, "deviceCreator.osgiFrameworkVersion");
        }

        // .applicationFrameworkVersion
        if (!Strings.isNullOrEmpty(deviceCreator.getApplicationFrameworkVersion())) {
            ArgumentValidator.lengthRange(deviceCreator.getApplicationFrameworkVersion(), 1, 255, "deviceCreator.applicationFrameworkVersion");
        }

        // .connectionInterface
        if (!Strings.isNullOrEmpty(deviceCreator.getConnectionInterface())) {
            ArgumentValidator.lengthRange(deviceCreator.getConnectionInterface(), 1, BIRTH_FIELDS_CLOB_MAX_LENGTH, "deviceCreator.connectionInterface");
        }

        // .connectionIp
        if (!Strings.isNullOrEmpty(deviceCreator.getConnectionIp())) {
            ArgumentValidator.lengthRange(deviceCreator.getConnectionIp(), 1, BIRTH_FIELDS_CLOB_MAX_LENGTH, "deviceCreator.connectionIp");
        }

        // .applicationIdentifiers
        if (!Strings.isNullOrEmpty(deviceCreator.getApplicationIdentifiers())) {
            ArgumentValidator.lengthRange(deviceCreator.getApplicationIdentifiers(), 1, 1024, "deviceCreator.applicationIdentifiers");
        }

        // .acceptEncoding
        if (!Strings.isNullOrEmpty(deviceCreator.getAcceptEncoding())) {
            ArgumentValidator.lengthRange(deviceCreator.getAcceptEncoding(), 1, 255, "deviceCreator.acceptEncoding");
        }

        // .customAttribute1
        if (!Strings.isNullOrEmpty(deviceCreator.getCustomAttribute1())) {
            ArgumentValidator.lengthRange(deviceCreator.getCustomAttribute1(), 1, 255, "deviceCreator.customAttribute1");
        }

        // .customAttribute2
        if (!Strings.isNullOrEmpty(deviceCreator.getCustomAttribute2())) {
            ArgumentValidator.lengthRange(deviceCreator.getCustomAttribute1(), 1, 255, "deviceCreator.customAttribute2");
        }

        // .customAttribute1
        if (!Strings.isNullOrEmpty(deviceCreator.getCustomAttribute3())) {
            ArgumentValidator.lengthRange(deviceCreator.getCustomAttribute3(), 1, 255, "deviceCreator.customAttribute3");
        }

        // .customAttribute1
        if (!Strings.isNullOrEmpty(deviceCreator.getCustomAttribute4())) {
            ArgumentValidator.lengthRange(deviceCreator.getCustomAttribute4(), 1, 255, "deviceCreator.customAttribute4");
        }

        // .customAttribute1
        if (!Strings.isNullOrEmpty(deviceCreator.getCustomAttribute5())) {
            ArgumentValidator.lengthRange(deviceCreator.getCustomAttribute1(), 1, 255, "deviceCreator.customAttribute5");
        }

        // .extendedProperties
        for (DeviceExtendedProperty deviceExtendedProperty : deviceCreator.getExtendedProperties()) {
            // .groupName
            if (!Strings.isNullOrEmpty(deviceExtendedProperty.getGroupName())) {
                ArgumentValidator.lengthRange(deviceExtendedProperty.getGroupName(), 1, 64, "deviceCreator.extendedProperties[].groupName");
            }

            // .name
            ArgumentValidator.notNull(deviceExtendedProperty.getName(), "deviceCreator.extendedProperties[].name");
            ArgumentValidator.lengthRange(deviceExtendedProperty.getName(), 1, 64, "deviceCreator.extendedProperties[].name");

            // .value
            if (!Strings.isNullOrEmpty(deviceExtendedProperty.getValue())) {
                ArgumentValidator.lengthRange(deviceExtendedProperty.getValue(), 1, BIRTH_FIELDS_EXTENDED_PROPERTY_VALUE_MAX_LENGTH, "deviceCreator.extendedProperties[].value");
            }
        }

        //
        // Check access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DEVICE_DOMAIN, Actions.write, deviceCreator.getScopeId(), deviceCreator.getGroupId()));
    }

    /**
     * Validates the {@link Device} for {@link DeviceRegistryService#update(KapuaUpdatableEntity)} operation.
     *
     * @param device The {@link Device} to validate.
     * @throws org.eclipse.kapua.KapuaIllegalArgumentException if one of the {@link DeviceCreator} fields is invalid.
     * @throws KapuaException                                  if there are other errors.
     * @since 1.0.0
     */
    public static void validateUpdatePreconditions(Device device) throws KapuaException {
        ArgumentValidator.notNull(device, "device");
        ArgumentValidator.notNull(device.getId(), "device.id");
        ArgumentValidator.notNull(device.getScopeId(), "device.scopeId");

        // Check that current user can manage the current group of the device
        KapuaId currentGroupId = findCurrentGroupId(device.getScopeId(), device.getId());
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DEVICE_DOMAIN, Actions.write, device.getScopeId(), currentGroupId));

        // Check that current user can manage the target group of the device
        if (device.getGroupId() != null) {
            ArgumentValidator.notNull(KapuaSecurityUtils.doPrivileged(() -> GROUP_SERVICE.find(device.getScopeId(), device.getGroupId())), "device.groupId");
        }
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DEVICE_DOMAIN, Actions.write, device.getScopeId(), device.getGroupId()));

        for (KapuaId tagId : device.getTagIds()) {
            Tag tag = KapuaSecurityUtils.doPrivileged(() -> TAG_SERVICE.find(device.getScopeId(), tagId));
            if (tag == null) {
                throw new KapuaEntityNotFoundException(Tag.TYPE, tagId);
            }
        }

        return device;
    }

    /**
     * Validates the find device precondition
     *
     * @param scopeId
     * @param entityId
     * @throws KapuaException
     */
    public static void validateFindPreconditions(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(entityId, "entityId");

        KapuaId groupId = findCurrentGroupId(scopeId, entityId);
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DEVICE_DOMAIN, Actions.read, scopeId, groupId));
    }

    /**
     * Validates the device query precondition
     *
     * @param query
     * @throws KapuaException
     */
    public static void validateQueryPreconditions(KapuaQuery query) throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        List<String> fetchAttributes = query.getFetchAttributes();

        if (fetchAttributes != null) {
            for (String fetchAttribute : fetchAttributes) {
                ArgumentValidator.match(fetchAttribute, DeviceValidationRegex.QUERY_FETCH_ATTRIBUTES, "fetchAttributes");
            }
        }

        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DEVICE_DOMAIN, Actions.read, query.getScopeId(), Group.ANY));
    }

    /**
     * Validates the device count precondition
     *
     * @param query
     * @throws KapuaException
     */
    public static void validateCountPreconditions(KapuaQuery query) throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DEVICE_DOMAIN, Actions.read, query.getScopeId(), Group.ANY));
    }

    /**
     * Validates the device delete precondition
     *
     * @param scopeId
     * @param deviceId
     * @throws KapuaException
     */
    public static void validateDeletePreconditions(KapuaId scopeId, KapuaId deviceId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(deviceId, "id");

        KapuaId groupId = findCurrentGroupId(scopeId, deviceId);
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DEVICE_DOMAIN, Actions.delete, scopeId, groupId));
    }

    /**
     * Validates the device find by identifier precondition
     *
     * @param scopeId
     * @param clientId
     * @throws KapuaException
     * @since 1.0.0
     */
    public static void validateFindByClientIdPreconditions(KapuaId scopeId, String clientId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notEmptyOrNull(clientId, "clientId");

        // Check access is performed by the query method.
    }

    /**
     * Finds the current {@link Group} id assigned to the given {@link Device} id.
     *
     * @param scopeId  The scope {@link KapuaId} of the {@link Device}
     * @param entityId The {@link KapuaEntity} {@link KapuaId} of the {@link Device}.
     * @return The {@link Group} id found.
     * @throws KapuaException
     * @since 1.0.0
     */
    private static KapuaId findCurrentGroupId(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        DeviceQuery query = DEVICE_FACTORY.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(KapuaEntityAttributes.ENTITY_ID, entityId));

        DeviceListResult results = null;
        try {
            results = KapuaSecurityUtils.doPrivileged(() -> DEVICE_REGISTRY_SERVICE.query(query));
        } catch (Exception e) {
            throw KapuaException.internalError(e, "Error while searching groupId");
        }

        KapuaId groupId = null;
        if (results != null && !results.isEmpty()) {
            groupId = results.getFirstItem().getGroupId();
        }

        return groupId;
    }
}
