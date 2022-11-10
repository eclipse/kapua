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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaMaxNumberOfItemsReachedException;
import org.eclipse.kapua.commons.configuration.exception.ServiceConfigurationLimitExceededException;
import org.eclipse.kapua.commons.configuration.exception.ServiceConfigurationParentLimitExceededException;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import java.util.Map;
import java.util.Optional;

public class ResourceLimitedServiceConfigurationManagerImpl
        extends ServiceConfigurationManagerImpl {

    private final AccountChildrenFinder accountChildrenFinder;
    private final UsedEntitiesCounter usedEntitiesCounter;

    public ResourceLimitedServiceConfigurationManagerImpl(String pid, Domain domain, EntityManagerSession entityManagerSession, PermissionFactory permissionFactory, AuthorizationService authorizationService, RootUserTester rootUserTester, AccountChildrenFinder accountChildrenFinder, UsedEntitiesCounter usedEntitiesCounter) {
        super(pid, domain, entityManagerSession, permissionFactory, authorizationService, rootUserTester);
        this.accountChildrenFinder = accountChildrenFinder;
        this.usedEntitiesCounter = usedEntitiesCounter;
    }

    @Override
    protected boolean validateNewConfigValuesCoherence(KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, Optional<KapuaId> parentId) throws KapuaException {
        // Validate against current scope
        long availableChildEntitiesWithNewConfig = allowedChildEntities(scopeId, Optional.empty(), Optional.ofNullable(updatedProps));
        if (availableChildEntitiesWithNewConfig < 0) {
            throw new ServiceConfigurationLimitExceededException(pid, scopeId, -availableChildEntitiesWithNewConfig);
        }

        // Validate against parent scope
        if (parentId.isPresent()) {
            long availableParentEntitiesWithCurrentConfig = allowedChildEntities(parentId.get(), Optional.of(scopeId));
            if (availableParentEntitiesWithCurrentConfig - availableChildEntitiesWithNewConfig < 0) {
                throw new ServiceConfigurationParentLimitExceededException(pid, parentId.orElse(null), -(availableParentEntitiesWithCurrentConfig - availableChildEntitiesWithNewConfig));
            }
        }
        return true;
    }

    /**
     * Checks if the given scope {@link KapuaId} can have more entities for this {@link KapuaConfigurableService}.
     *
     * @param scopeId    The scope {@link KapuaId} to check.
     * @param entityType The entity type of this {@link KapuaConfigurableService}
     * @throws KapuaException
     * @since 2.0.0
     */
    @Override
    public void checkAllowedEntities(KapuaId scopeId, String entityType) throws KapuaException {
        if (allowedChildEntities(scopeId) <= 0) {
            throw new KapuaMaxNumberOfItemsReachedException(entityType);
        }
    }

    /**
     * Gets the number of remaining allowed entity for the given scope, according to the {@link KapuaConfigurableService#getConfigValues(KapuaId)}
     *
     * @param scopeId The scope {@link KapuaId}.
     * @return The number of entities remaining for the given scope
     * @throws KapuaException
     * @since 1.0.0
     */
    private long allowedChildEntities(KapuaId scopeId) throws KapuaException {
        return allowedChildEntities(scopeId, Optional.empty(), Optional.empty());
    }

    /**
     * Gets the number of remaining allowed entity for the given scope, according to the {@link KapuaConfigurableService#getConfigValues(KapuaId)}
     * excluding a specific scope when checking resources available.
     * <p>
     * The exclusion of the scope is required when updating a limit for a target account.
     *
     * @param scopeId       The scope {@link KapuaId}.
     * @param targetScopeId The excluded scope {@link KapuaId}.
     * @return The number of entities remaining for the given scope
     * @throws KapuaException
     * @since 1.0.0
     */
    private long allowedChildEntities(KapuaId scopeId, Optional<KapuaId> targetScopeId) throws KapuaException {
        return allowedChildEntities(scopeId, targetScopeId, Optional.empty());
    }

    /**
     * Gets the number of remaining allowed entity for the given scope, according to the given {@link KapuaConfigurableService}
     * excluding a specific scope when checking resources available.
     * <p>
     * The exclusion of the scope is required when updating a limit for a target account.
     *
     * @param scopeId       The scope {@link KapuaId}.
     * @param targetScopeId The excluded scope {@link KapuaId}.
     * @param configuration The configuration to be checked. If not provided will be read from the current service configuration
     * @return The number of entities remaining for the given scope
     * @throws KapuaException
     * @since 1.0.0
     */
    private long allowedChildEntities(KapuaId scopeId, Optional<KapuaId> targetScopeId, Optional<Map<String, Object>> configuration) throws KapuaException {
        final Map<String, Object> finalConfig;
        if (configuration.isPresent()) { // Checked exceptions be damned, could have been .orElseGet(()->...)
            finalConfig = configuration.get();
        } else {
            finalConfig = getConfigValues(scopeId, false);
        }
        boolean allowInfiniteChildEntities = (boolean) finalConfig.getOrDefault("infiniteChildEntities", false);
        if (allowInfiniteChildEntities) {
            return Integer.MAX_VALUE;
        }
        return KapuaSecurityUtils.doPrivileged(() -> {
            // Current used entities
            long currentUsedEntities = usedEntitiesCounter.countEntitiesInScope(scopeId);

            AccountListResult childAccounts = accountChildrenFinder.findChildren(scopeId, targetScopeId);
            // Resources assigned to children
            long childCount = 0;
            for (Account childAccount : childAccounts.getItems()) {
                Map<String, Object> childConfigValues = getConfigValues(childAccount.getId(), true);
                // maxNumberChildEntities can be null if such property is disabled via the
                // isPropertyEnabled() method in the service implementation. In such case,
                // it makes sense to treat the service as it had 0 available entities
                boolean childAllowInfiniteChildEntities = (boolean) childConfigValues.getOrDefault("infiniteChildEntities", false);
                Integer childMaxNumberChildEntities = (Integer) childConfigValues.getOrDefault("maxNumberChildEntities", 0);
                childCount += childAllowInfiniteChildEntities ? Integer.MAX_VALUE : childMaxNumberChildEntities;
            }

            // Max allowed for this account
            int maxChildAccounts = (int) finalConfig.getOrDefault("maxNumberChildEntities", 0);
            return maxChildAccounts - currentUsedEntities - childCount;
        });
    }

}