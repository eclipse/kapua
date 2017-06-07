/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;

/**
 * {@link KapuaQuery} {@link KapuaPredicate} name for {@link Tag} entity.
 * 
 * @since 1.0
 * 
 */
public class TagPredicates {

    private TagPredicates() {
    }

    /**
     * {@link Tag} name
     */
    public static final String NAME = "name";

}