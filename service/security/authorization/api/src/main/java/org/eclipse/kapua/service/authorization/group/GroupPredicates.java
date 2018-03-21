/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.group;

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;

/**
 * {@link KapuaQuery} {@link KapuaPredicate} name for {@link Group} entity.
 *
 * @since 1.0
 */
public interface GroupPredicates extends KapuaUpdatableEntityPredicates {

    /**
     * {@link Group} name
     */
    String NAME = "name";

}
