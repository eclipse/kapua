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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
public class TagRepositoryJpaImpl<T extends Tag> implements TagRepository {
    private Class<T> clazz;
    private final TagFactory tagFactory;
    private final EntityManagerSession entityManagerSession;

    @Inject
    public TagRepositoryJpaImpl(Class<T> clazz,
                                @NotNull TagFactory tagFactory,
                                @NotNull EntityManagerFactory entityManagerFactory) {
        this.clazz = clazz;
        this.tagFactory = tagFactory;
        this.entityManagerSession = new EntityManagerSession(entityManagerFactory);
    }

    @Override
    public Tag create(TagCreator tagCreator) throws KapuaException {
        final Tag tag = tagFactory.newEntity(tagCreator.getScopeId());
        tag.setName(tagCreator.getName());
        tag.setDescription(tagCreator.getDescription());

        return entityManagerSession.doTransactedAction(em -> ServiceDAO.create(em, tag));
    }

    @Override
    public Tag create(Tag tag) throws KapuaException {
        return entityManagerSession.doTransactedAction(em -> ServiceDAO.create(em, tag));
    }

    @Override
    public Tag update(Tag tag) throws KapuaException {
        return entityManagerSession.doTransactedAction(em -> ServiceDAO.update(
                em,
                clazz,
                (T) tag));
    }

    @Override
    public Tag find(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        return entityManagerSession.doAction(em -> ServiceDAO.find(em, clazz, scopeId, tagId));
    }

    @Override
    public TagListResult query(KapuaQuery tagQuery) throws KapuaException {
        return entityManagerSession.doAction(em -> ServiceDAO.query(em, Tag.class, clazz, tagFactory.newListResult(), tagQuery));
    }

    @Override
    public long count(KapuaQuery tagQuery) throws KapuaException {
        return entityManagerSession.doAction(em -> ServiceDAO.count(em, Tag.class, clazz, tagQuery));
    }

    @Override
    public Tag delete(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        return entityManagerSession.doTransactedAction(em -> ServiceDAO.delete(em, clazz, scopeId, tagId));
    }
}
