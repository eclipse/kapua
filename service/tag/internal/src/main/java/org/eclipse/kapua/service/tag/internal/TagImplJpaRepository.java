/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagRepository;
import org.eclipse.kapua.storage.TxContext;

import javax.inject.Singleton;

@Singleton
public class TagImplJpaRepository
        extends KapuaNamedEntityJpaRepository<Tag, TagImpl, TagListResult>
        implements TagRepository {
    public TagImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(TagImpl.class, () -> new TagListResultImpl(), jpaRepoConfig);
    }

    @Override
    public Tag delete(TxContext txContext, KapuaId scopeId, KapuaId tagId) throws KapuaException {
        final javax.persistence.EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        return doFind(em, scopeId, tagId)
                .map(t -> doDelete(em, t))
                .orElseThrow(() -> new KapuaEntityNotFoundException(Tag.TYPE, tagId));
    }

    @Override
    public Tag update(TxContext txContext, Tag tag) throws KapuaException {
        final javax.persistence.EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        // Check existence
        return doFind(em, tag.getScopeId(), tag.getId())
                .map(t -> doUpdate(em, t, tag))
                .orElseThrow(() -> new KapuaEntityNotFoundException(Tag.TYPE, tag.getId()));
    }
}
