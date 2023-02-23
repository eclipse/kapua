/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.repository.KapuaNamedEntityRepository;

public class KapuaNamedEntityRepositoryJpaImpl<E extends KapuaNamedEntity, C extends E> implements KapuaNamedEntityRepository<E> {
    private final Class<C> clazz;
    private final EntityManagerSession entityManagerSession;

    public KapuaNamedEntityRepositoryJpaImpl(Class<C> clazz, EntityManagerSession entityManagerSession) {
        this.clazz = clazz;
        this.entityManagerSession = entityManagerSession;
    }

    @Override
    public E findByName(String value) {
        try {
            return entityManagerSession.doTransactedAction(em -> ServiceDAO.findByName(em, clazz, value));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public E findByName(KapuaId scopeId, String value) {
        try {
            return entityManagerSession.doTransactedAction(em -> ServiceDAO.findByName(em, clazz, scopeId, value));
        } catch (KapuaException e) {
            throw new RuntimeException(e);
        }
    }
}
