/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.service.internal;

import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaDuplicateNameInAnotherAccountError;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.QueryFactory;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.KapuaEntityService;

import java.util.Collections;
import java.util.List;

public class KapuaNamedEntityServiceUtils {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final QueryFactory QUERY_FACTORY = LOCATOR.getFactory(QueryFactory.class);

    private KapuaNamedEntityServiceUtils() {
    }

    public static <E extends KapuaNamedEntity, C extends KapuaNamedEntityCreator<E>> void checkEntityNameUniqueness(KapuaEntityService<E, C> kapuaNamedEntityService, C creator) throws KapuaException {
        checkEntityNameUniqueness(kapuaNamedEntityService, creator, Collections.emptyList());
    }

    public static <E extends KapuaNamedEntity, C extends KapuaNamedEntityCreator<E>> void checkEntityNameUniqueness(KapuaEntityService<E, C> kapuaNamedEntityService, C creator, List<QueryPredicate> additionalPredicates) throws KapuaException {
        KapuaQuery query = QUERY_FACTORY.newQuery();
        query.setScopeId(creator.getScopeId());

        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(KapuaNamedEntityAttributes.NAME, creator.getName()));

        for (QueryPredicate additionalPredicate : additionalPredicates) {
            andPredicate.and(additionalPredicate);
        }

        query.setPredicate(andPredicate);

        if (kapuaNamedEntityService.count(query) > 0) {
            throw new KapuaDuplicateNameException(creator.getName());
        }
    }

    public static <E extends KapuaNamedEntity> void checkEntityNameUniqueness(KapuaEntityService<E, ?> kapuaNamedEntityService, E entity) throws KapuaException {
        checkEntityNameUniqueness(kapuaNamedEntityService, entity, Collections.emptyList());
    }

    public static <E extends KapuaNamedEntity> void checkEntityNameUniqueness(KapuaEntityService<E, ?> kapuaNamedEntityService, E entity, List<QueryPredicate> additionalPredicates) throws KapuaException {
        KapuaQuery query = QUERY_FACTORY.newQuery();
        query.setScopeId(entity.getScopeId());

        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(KapuaNamedEntityAttributes.NAME, entity.getName()));
        andPredicate.and(query.attributePredicate(KapuaNamedEntityAttributes.ENTITY_ID, entity.getId(), AttributePredicate.Operator.NOT_EQUAL));

        for (QueryPredicate additionalPredicate : additionalPredicates) {
            andPredicate.and(additionalPredicate);
        }

        query.setPredicate(andPredicate);

        if (kapuaNamedEntityService.count(query) > 0) {
            throw new KapuaDuplicateNameException(entity.getName());
        }
    }

    public static <E extends KapuaNamedEntity, C extends KapuaNamedEntityCreator<E>> void checkEntityNameUniquenessInAllScopes(KapuaEntityService<E, C> kapuaNamedEntityService, C creator) throws KapuaException {
        KapuaQuery query = QUERY_FACTORY.newQuery();
        query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, creator.getName()));

        if (KapuaSecurityUtils.doPrivileged(() -> kapuaNamedEntityService.count(query) > 0)) {
            throw new KapuaDuplicateNameInAnotherAccountError(creator.getName());
        }
    }

    public static <E extends KapuaNamedEntity> void checkEntityNameUniquenessInAllScopes(KapuaEntityService<E, ?> kapuaNamedEntityService, E entity) throws KapuaException {
        KapuaQuery query = QUERY_FACTORY.newQuery();
        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(KapuaNamedEntityAttributes.NAME, entity.getName()),
                        query.attributePredicate(KapuaNamedEntityAttributes.ENTITY_ID, entity.getId(), AttributePredicate.Operator.NOT_EQUAL)
                )
        );

        if (KapuaSecurityUtils.doPrivileged(() -> kapuaNamedEntityService.count(query) > 0)) {
            throw new KapuaDuplicateNameInAnotherAccountError(entity.getName());
        }
    }
}
