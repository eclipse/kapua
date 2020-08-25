/*******************************************************************************
 * Copyright (c) 2011, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

/**
 * Query predicate attribute name for {@link AccessInfo} entity.
 *
 * @since 1.0.0
 */
public class AccessInfoAttributes extends KapuaUpdatableEntityAttributes {

    /**
     * User id
     */
    public static final String USER_ID = "userId";
}
