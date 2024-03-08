/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.utils.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AccountRelativeFinder;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.KapuaForwardableEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaForwardableEntityQuery;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.OrPredicate;
import org.eclipse.kapua.service.utils.KapuaEntityQueryUtil;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Implementation of converter between {@link KapuaForwardableEntityQuery} and {@link KapuaQuery}
 *
 * @since 2.0.0
 */
public class KapuaEntityQueryUtilImpl implements KapuaEntityQueryUtil {

    private final AccountRelativeFinder accountRelativeFinder;

    @Inject
    public KapuaEntityQueryUtilImpl(AccountRelativeFinder accountRelativeFinder) {
        this.accountRelativeFinder = accountRelativeFinder;
    }

    @Override
    public KapuaQuery transformInheritedQuery(@NotNull KapuaForwardableEntityQuery query) throws KapuaException {
        // Transform only if this option is enabled
        if (!query.getIncludeInherited()) {
            return query;
        }

        KapuaId scopeId = query.getScopeId();

        // Replacement predicate root
        AndPredicate newPred = query.andPredicate(query.getPredicate());

        // Create predicate to query ancestor accounts for entities that are forwardable
        query.setScopeId(KapuaId.ANY);
        OrPredicate forwardableAncestorPreds = query.orPredicate();

        for (KapuaId id : accountRelativeFinder.findParentIds(scopeId)) {
            AndPredicate scopedForwardablePred = query.andPredicate(query.attributePredicate(KapuaEntityAttributes.SCOPE_ID, id));
            scopedForwardablePred = scopedForwardablePred.and(query.attributePredicate(KapuaForwardableEntityAttributes.FORWARDABLE, true));

            forwardableAncestorPreds = forwardableAncestorPreds.or(scopedForwardablePred);
        }
        // include the original scope (which doesn't need to be forwardable)
        forwardableAncestorPreds = forwardableAncestorPreds.or(query.attributePredicate(KapuaEntityAttributes.SCOPE_ID, scopeId));

        // Use the original query predicate AND the forwardable parent scopes
        query.setPredicate(newPred.and(forwardableAncestorPreds));

        // Disable this option so that it does not get transformed again in case of subsequent calls to this function
        query.setIncludeInherited(false);

        return query;
    }
}
