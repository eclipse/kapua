/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.commons.jpa.AbstractEntityCacheFactory;

/**
 * Cache factory for the {@link RoleServiceImpl}
 */
public class RoleCacheFactory extends AbstractEntityCacheFactory {

    private RoleCacheFactory() {
        super("RoleId");
    }

    protected static RoleCacheFactory getInstance() {
        return new RoleCacheFactory();
    }
}
