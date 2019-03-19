/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

/**
 * {@link KapuaEntityFactory} definition.
 *
 * @param <E> The {@link KapuaEntity} for which this {@link KapuaEntityFactory} is for.
 * @param <C> The {@link KapuaEntityCreator} for which this {@link KapuaEntityFactory} is for.
 * @param <Q> The {@link KapuaQuery} for which this {@link KapuaEntityFactory} is for.
 * @param <L> The {@link KapuaListResult} for which this {@link KapuaEntityFactory} is for.
 * @since 1.0.0
 */
public interface KapuaEntityFactory<E extends KapuaEntity, C extends KapuaEntityCreator<E>, Q extends KapuaQuery<E>, L extends KapuaListResult<E>> extends KapuaObjectFactory {

    /**
     * Instantiates a new {@link KapuaEntity}.
     *
     * @param scopeId The scope {@link KapuaId} to be set in the {@link KapuaEntity}
     * @return The newly instantiated {@link KapuaEntity}
     * @since 1.0.0
     */
    E newEntity(KapuaId scopeId);

    /**
     * Instantiates a new {@link KapuaEntityCreator}.
     *
     * @param scopeId The scope {@link KapuaId} to be set in the {@link KapuaEntityCreator}
     * @return The newly instantiated {@link KapuaEntityCreator}
     * @since 1.0.0
     */
    C newCreator(KapuaId scopeId);

    /**
     * Instantiates a new {@link KapuaQuery}.
     *
     * @param scopeId The scope {@link KapuaId} to be set in the {@link KapuaQuery}
     * @return The newly instantiated {@link KapuaQuery}
     * @since 1.0.0
     */
    Q newQuery(KapuaId scopeId);

    /**
     * Instantiates a new {@link KapuaListResult}.
     *
     * @return The newly instantiated {@link KapuaListResult}
     * @since 1.0.0
     */
    L newListResult();

    /**
     * Deeply clones the given {@link KapuaEntity}.
     *
     * @param entity The {@link KapuaEntity} to be cloned.
     * @return A deep clone of the {@link KapuaEntity}
     * @throws KapuaEntityCloneException When error occurs while cloning the {@link KapuaEntity}
     * @since 1.1.0
     */
    E clone(E entity) throws KapuaEntityCloneException;
}
