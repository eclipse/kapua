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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaMaxNumberOfItemsReachedException;
import org.eclipse.kapua.commons.configuration.exception.ServiceConfigurationLimitExceededException;
import org.eclipse.kapua.commons.configuration.exception.ServiceConfigurationParentLimitExceededException;
import org.eclipse.kapua.commons.jpa.AbstractEntityCacheFactory;
import org.eclipse.kapua.commons.jpa.CacheFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import java.util.Map;

/**
 * Base {@code abstract} {@link KapuaConfigurableService} implementation for services that have a max number of entities allowed.
 * <p>
 * The usually contain properties named:
 * <ul>
 *     <li>infiniteChildEntities</li>
 *     <li>maxNumberChildEntities</li>
 * </ul>
 *
 * @param <E> The {@link KapuaEntity} type.
 * @param <C> The {@link KapuaEntityCreator} type.
 * @param <S> The {@link KapuaEntityService} type.
 * @param <L> The {@link KapuaListResult} type.
 * @param <Q> The {@link KapuaQuery} type.
 * @param <F> The {@link KapuaEntityFactory} type.
 * @since 1.0.0
 */
public abstract class AbstractKapuaConfigurableResourceLimitedService<E extends KapuaEntity, C extends KapuaEntityCreator<E>, S extends KapuaEntityService<E, C>, L extends KapuaListResult<E>, Q extends KapuaQuery, F extends KapuaEntityFactory<E, C, Q, L>>
        extends AbstractKapuaConfigurableService {

    private final Class<S> serviceClass;
    private final Class<F> factoryClass;

    /**
     * Constructor.
     *
     * @param pid                  The {@link KapuaConfigurableService} id.
     * @param domain               The {@link Domain} on which check access.
     * @param entityManagerFactory The {@link EntityManagerFactory} that handles persistence unit
     * @param serviceClass         The {@link KapuaService} type.
     * @param factoryClass         The {@link KapuaEntityFactory} type.
     * @deprecated Since 1.2.0. This constructor will be removed in a next release (may be)
     */
    @Deprecated
    protected AbstractKapuaConfigurableResourceLimitedService(
            String pid,
            Domain domain,
            EntityManagerFactory entityManagerFactory,
            Class<S> serviceClass,
            Class<F> factoryClass) {
        this(pid, domain, entityManagerFactory, null, serviceClass, factoryClass);
    }

    /**
     * Constructor.
     *
     * @param pid                  The {@link KapuaConfigurableService} id.
     * @param domain               The {@link Domain} on which check access.
     * @param entityManagerFactory The {@link EntityManagerFactory} that handles persistence unit
     * @param abstractCacheFactory The {@link CacheFactory} that handles caching of the entities
     * @param serviceClass         The {@link KapuaService} type.
     * @param factoryClass         The {@link KapuaEntityFactory} type.
     * @since 1.2.0
     */
    protected AbstractKapuaConfigurableResourceLimitedService(
            String pid,
            Domain domain,
            EntityManagerFactory entityManagerFactory,
            AbstractEntityCacheFactory abstractCacheFactory,
            Class<S> serviceClass,
            Class<F> factoryClass) {
        super(pid, domain, entityManagerFactory, abstractCacheFactory);

        this.serviceClass = serviceClass;
        this.factoryClass = factoryClass;
    }

    @Override
    protected boolean validateNewConfigValuesCoherence(KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, KapuaId parentId) throws KapuaException {
        super.validateNewConfigValuesCoherence(ocd, updatedProps, scopeId, parentId);

        // Validate against current scope
        long availableChildEntitiesWithNewConfig = allowedChildEntities(scopeId, null, updatedProps);
        if (availableChildEntitiesWithNewConfig < 0) {
            throw new ServiceConfigurationLimitExceededException(getServicePid(), scopeId, -availableChildEntitiesWithNewConfig);
        }

        // Validate against parent scope
        if (parentId != null) {
            long availableParentEntitiesWithCurrentConfig = allowedChildEntities(parentId, scopeId);
            if (availableParentEntitiesWithCurrentConfig - availableChildEntitiesWithNewConfig < 0) {
                throw new ServiceConfigurationParentLimitExceededException(getServicePid(), parentId, -(availableParentEntitiesWithCurrentConfig - availableChildEntitiesWithNewConfig));
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
    protected void checkAllowedEntities(KapuaId scopeId, String entityType) throws KapuaException {
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
    protected long allowedChildEntities(KapuaId scopeId) throws KapuaException {
        return allowedChildEntities(scopeId, null, null);
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
    protected long allowedChildEntities(KapuaId scopeId, KapuaId targetScopeId) throws KapuaException {
        return allowedChildEntities(scopeId, targetScopeId, null);
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
    protected long allowedChildEntities(KapuaId scopeId, KapuaId targetScopeId, Map<String, Object> configuration) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        S service = locator.getService(serviceClass);
        F factory = locator.getFactory(factoryClass);
        AccountFactory accountFactory = locator.getFactory(AccountFactory.class);
        AccountService accountService = locator.getService(AccountService.class);

        Map<String, Object> finalConfig = configuration == null ? getConfigValues(scopeId, false) : configuration;
        boolean allowInfiniteChildEntities = (boolean) finalConfig.get("infiniteChildEntities");
        if (!allowInfiniteChildEntities) {
            return KapuaSecurityUtils.doPrivileged(() -> {
                Q countQuery = factory.newQuery(scopeId);

                // Current used entities
                long currentUsedEntities = service.count(countQuery);

                AccountQuery childAccountsQuery = accountFactory.newQuery(scopeId);
                // Exclude the scope that is under config update
                if (targetScopeId != null) {
                    childAccountsQuery.setPredicate(childAccountsQuery.attributePredicate(KapuaEntityAttributes.ENTITY_ID, targetScopeId, Operator.NOT_EQUAL));
                }

                AccountListResult childAccounts = accountService.query(childAccountsQuery);
                // Resources assigned to children
                long childCount = 0;
                for (Account childAccount : childAccounts.getItems()) {
                    Map<String, Object> childConfigValues = getConfigValues(childAccount.getId());
                    // maxNumberChildEntities can be null if such property is disabled via the
                    // isPropertyEnabled() method in the service implementation. In such case,
                    // it makes sense to treat the service as it had 0 available entities
                    boolean childAllowInfiniteChildEntities = (boolean) childConfigValues.getOrDefault("infiniteChildEntities", false);
                    Integer childMaxNumberChildEntities = (Integer) childConfigValues.getOrDefault("maxNumberChildEntities", 0);
                    childCount += childAllowInfiniteChildEntities ? Integer.MAX_VALUE : childMaxNumberChildEntities;
                }

                // Max allowed for this account
                int maxChildAccounts = (int) finalConfig.get("maxNumberChildEntities");
                return maxChildAccounts - currentUsedEntities - childCount;
            });
        }
        return Integer.MAX_VALUE;
    }
}
