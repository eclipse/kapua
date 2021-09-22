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
import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.service.KapuaEntityService;

public class KapuaNamedEntityServiceUtils {

    private KapuaNamedEntityServiceUtils() {
    }

    public static <E extends KapuaNamedEntity, C extends KapuaNamedEntityCreator<E>> void checkEntityNameUniqueness(KapuaEntityService<E, C> kapuaNamedEntityService, KapuaEntityFactory<E, C, ?, ?> kapuaNamedEntityFactory, C creator) throws KapuaException {
        KapuaQuery query = kapuaNamedEntityFactory.newQuery(creator.getScopeId());
        query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, creator.getName()));

        if (kapuaNamedEntityService.count(query) > 0) {
            throw new KapuaDuplicateNameException(creator.getName());
        }
    }

    public static <E extends KapuaNamedEntity> void checkEntityNameUniqueness(KapuaEntityService<E, ?> kapuaNamedEntityService, KapuaEntityFactory<E, ?, ?, ?> kapuaNamedEntityFactory, E entity) throws KapuaException {
        KapuaQuery query = kapuaNamedEntityFactory.newQuery(entity.getScopeId());
        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(KapuaNamedEntityAttributes.NAME, entity.getName()),
                        query.attributePredicate(KapuaNamedEntityAttributes.ENTITY_ID, entity.getId(), AttributePredicate.Operator.NOT_EQUAL)
                )
        );

        if (kapuaNamedEntityService.count(query) > 0) {
            throw new KapuaDuplicateNameException(entity.getName());
        }
    }

    public static <E extends KapuaNamedEntity, C extends KapuaNamedEntityCreator<E>> void checkEntityNameUniquenessInAllScopes(KapuaEntityService<E, C> kapuaNamedEntityService, KapuaEntityFactory<E, C, ?, ?> kapuaNamedEntityFactory, C creator) throws KapuaException {
        KapuaQuery query = kapuaNamedEntityFactory.newQuery(null);
        query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, creator.getName()));

        if (KapuaSecurityUtils.doPrivileged(() -> kapuaNamedEntityService.count(query) > 0)) {
            throw new KapuaDuplicateNameInAnotherAccountError(creator.getName());
        }
    }

    public static <E extends KapuaNamedEntity> void checkEntityNameUniquenessInAllScopes(KapuaEntityService<E, ?> kapuaNamedEntityService, KapuaEntityFactory<E, ?, ?, ?> kapuaNamedEntityFactory, E entity) throws KapuaException {
        KapuaQuery query = kapuaNamedEntityFactory.newQuery(null);
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
