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
package org.eclipse.kapua.commons.configuration;

import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.authorization.domain.Domain;

public abstract class AbstractKapuaConfigurableResourceLimitedService<E extends KapuaEntity, C extends KapuaEntityCreator<E>, S extends KapuaEntityService<E, C>, L extends KapuaListResult<E>, Q extends KapuaQuery<E>, F extends KapuaEntityFactory<E, C, Q, L>>
        extends AbstractKapuaConfigurableService {

    private final Class<S> serviceClass;
    private final Class<F> factoryClass;

    protected AbstractKapuaConfigurableResourceLimitedService(
            String pid,
            Domain domain,
            EntityManagerFactory entityManagerFactory,
            Class<S> serviceClass,
            Class<F> factoryClass) {
        super(pid, domain, entityManagerFactory);
        this.serviceClass = serviceClass;
        this.factoryClass = factoryClass;
    }

    @Override
    protected boolean validateNewConfigValuesCoherence(KapuaTocd ocd, Map<String, Object> updatedProps, KapuaId scopeId, KapuaId parentId) throws KapuaException {
        boolean parentValidation = super.validateNewConfigValuesCoherence(ocd, updatedProps, scopeId, parentId);
        int availableChildEntitiesWithNewConfig = allowedChildEntities(scopeId, updatedProps);
        if (availableChildEntitiesWithNewConfig < 0) {
            throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.SELF_LIMIT_EXCEEDED_IN_CONFIG);
//            parentValidation = "you can't set limited entities if current limit is lower than actual child accounts count";
        }
        int availableParentEntitiesWithCurrentConfig = allowedChildEntities(parentId);
        if (availableParentEntitiesWithCurrentConfig - availableChildEntitiesWithNewConfig < 0) {
            throw new KapuaConfigurationException(KapuaConfigurationErrorCodes.PARENT_LIMIT_EXCEEDED_IN_CONFIG);
//            parentValidation = "parent account child entities limit is lower than the sum of his child entities and his children's assigned child entities";
        }
        return true;
    }

    protected int allowedChildEntities(KapuaId scopeId) throws KapuaException {
        return allowedChildEntities(scopeId, null);
    }

    /**
     * 
     * @param scopeId
     *            The {@link ScopeId} of the account to be tested
     * @param configuration
     *            The configuration to be tested. If null will be read
     *            from the current service configuration; otherwise the passed configuration
     *            will be used in the test
     * @return the number of child accounts spots still available
     * @throws KapuaException
     */
    protected int allowedChildEntities(KapuaId scopeId, Map<String, Object> configuration) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        S service = locator.getService(serviceClass);
        F factory = locator.getFactory(factoryClass);
        if (configuration == null) {
            configuration = getConfigValues(scopeId);
        }
        boolean allowInfiniteChildEntities = (boolean) configuration.get("infiniteChildEntities");
        if (!allowInfiniteChildEntities) {
            int maxChildAccounts = (int) configuration.get("maxNumberChildEntities");
            Q query = factory.newQuery(scopeId);
            KapuaListResult<E> currentChildEntities = service.query(query);
            long childCount = currentChildEntities.getSize();
            for (E childEntity : currentChildEntities.getItems()) {
                Map<String, Object> childConfigValues = getConfigValues(childEntity.getId());
                int maxChildChildAccounts = (int) childConfigValues.get("maxNumberChildEntities");
                childCount += maxChildChildAccounts;
            }
            return (int) (maxChildAccounts - childCount);
        }
        return Integer.MAX_VALUE;
    }

}
