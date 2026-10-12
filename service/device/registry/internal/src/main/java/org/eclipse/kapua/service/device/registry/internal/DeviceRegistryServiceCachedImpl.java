/*******************************************************************************
 * Copyright (c) 2026 Eurotech and/or its affiliates and others
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

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.jpa.EventStorer;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.GroupQueryHelper;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;
import org.eclipse.kapua.storage.TxManager;

/**
 * {@link DeviceRegistryService} implementation that caches {@link #findByClientId(KapuaId, String)} results in a {@link LocalCache}.
 * <p>
 * Meant for trusted, read-only callers on hot paths (i.e. message processing), that can tolerate stale {@link Device}s:
 * <ul>
 *     <li>Cached {@link Device}s are not invalidated when updated or deleted: they are returned as they were until their entry expires.</li>
 *     <li>Cached {@link Device}s are shared among callers: they must not be modified.</li>
 *     <li>Permissions are checked only when the {@link Device} is not cached.</li>
 *     <li>Not found {@link Device}s are never cached, so that newly created ones are found on the next lookup.</li>
 * </ul>
 * {@link Device}s are cached by {@link Device#getScopeId()} and {@link Device#getClientId()}, since the latter is unique only within a scope.
 * <p>
 * All other methods behave as in {@link DeviceRegistryServiceImpl}.
 *
 * If, in the future, other methods are needed to be "cached", implement them as well here
 *
 * @since 2.1.0
 */
public class DeviceRegistryServiceCachedImpl extends DeviceRegistryServiceImpl {

    private final LocalCache<Pair<KapuaId, String>, Device> devicesByClientIdCache; //BE AWARE that devices could be staled. This is way less important for identifiers like its id, hence YMMV depending what you need from the device

    /**
     * Constructor.
     *
     * @param txManager                   The {@link TxManager}.
     * @param serviceConfigurationManager The {@link ServiceConfigurationManager}.
     * @param authorizationService        The {@link AuthorizationService}.
     * @param permissionFactory           The {@link PermissionFactory}.
     * @param deviceFactory               The {@link DeviceFactory}.
     * @param deviceValidation            The {@link DeviceValidation}.
     * @param deviceRepository            The {@link DeviceRepository}.
     * @param groupQueryHelper            The {@link GroupQueryHelper}.
     * @param eventStorer                 The {@link EventStorer}.
     * @param devicesByClientIdCache      The {@link LocalCache} of {@link Device}s by {@link Device#getScopeId()} and {@link Device#getClientId()}.
     * @since 2.1.0
     */
    public DeviceRegistryServiceCachedImpl(
            TxManager txManager,
            ServiceConfigurationManager serviceConfigurationManager,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            DeviceFactory deviceFactory,
            DeviceValidation deviceValidation,
            DeviceRepository deviceRepository,
            GroupQueryHelper groupQueryHelper,
            EventStorer eventStorer,
            LocalCache<Pair<KapuaId, String>, Device> devicesByClientIdCache) {
        super(txManager, serviceConfigurationManager, authorizationService, permissionFactory, deviceFactory, deviceValidation, deviceRepository, groupQueryHelper, eventStorer);
        this.devicesByClientIdCache = devicesByClientIdCache;
    }

    @Override
    public Device findByClientId(KapuaId scopeId, String clientId) throws KapuaException {
        // Argument validation
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notEmptyOrNull(clientId, "clientId");

        // Check cache
        Pair<KapuaId, String> cacheKey = Pair.of(scopeId, clientId);
        Device device = devicesByClientIdCache.get(cacheKey);
        if (device == null) {
            // Do find
            device = super.findByClientId(scopeId, clientId);
            if (device != null) {
                devicesByClientIdCache.put(cacheKey, device);
            }
        }

        // Return result
        return device;
    }
}
