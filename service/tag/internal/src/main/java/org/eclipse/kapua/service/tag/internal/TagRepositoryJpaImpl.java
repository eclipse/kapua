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
import org.eclipse.kapua.service.tag.TagListResult;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
public class TagRepositoryJpaImpl<T extends Tag, L extends TagListResult> implements TagRepository {
    private Class<T> clazz;
    private Provider<L> listProvider;
    private final EntityManagerSession entityManagerSession;

    @Inject
    public TagRepositoryJpaImpl(Class<T> clazz,
                                Provider<L> listProvider,
                                @NotNull EntityManagerFactory entityManagerFactory) {
        this.clazz = clazz;
        this.listProvider = listProvider;
        this.entityManagerSession = new EntityManagerSession(entityManagerFactory);
    }

    @Override
    public Tag create(Tag tag) {
        try {
            return entityManagerSession.doTransactedAction(em -> ServiceDAO.create(em, tag));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Tag update(Tag tag) {
        try {
            return entityManagerSession.doTransactedAction(em -> ServiceDAO.update(
                    em,
                    clazz,
                    (T) tag));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Tag find(KapuaId scopeId, KapuaId tagId) {
        try {
            return entityManagerSession.doAction(em -> ServiceDAO.find(em, clazz, scopeId, tagId));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TagListResult query(KapuaQuery tagQuery) {
        try {
            return entityManagerSession.doAction(em -> ServiceDAO.query(em, Tag.class, clazz, listProvider.get(), tagQuery));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long count(KapuaQuery tagQuery) {
        try {
            return entityManagerSession.doAction(em -> ServiceDAO.count(em, Tag.class, clazz, tagQuery));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Tag delete(KapuaId scopeId, KapuaId tagId) {
        try {
            return entityManagerSession.doTransactedAction(em -> ServiceDAO.delete(em, clazz, scopeId, tagId));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }
}
