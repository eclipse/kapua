/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

/**
 * {@link KapuaEntityFactory} definition
 *
 * @param <E> The {@link KapuaEntity} for which this {@link KapuaEntityFactory} is for.
 * @param <C> The {@link KapuaEntityCreator} for which this {@link KapuaEntityFactory} is for.
 * @param <Q> The {@link KapuaQuery} for which this {@link KapuaEntityFactory} is for.
 * @param <L> The {@link KapuaListResult} for which this {@link KapuaEntityFactory} is for.
 * @since 1.0.0
 */
public interface KapuaEntityFactory<E extends KapuaEntity, C extends KapuaEntityCreator<E>, Q extends KapuaQuery<E>, L extends KapuaListResult<E>> extends KapuaObjectFactory {

    E newEntity(KapuaId scopeId);

    C newCreator(KapuaId scopeId);

    Q newQuery(KapuaId scopeId);

    L newListResult();
}
