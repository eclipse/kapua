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

import org.eclipse.kapua.commons.jpa.AbstractNamedEntityCacheFactory;

import javax.inject.Singleton;

/**
 * Cache factory for the {@link RoleServiceImpl}
 */
@Singleton
public class RoleCacheFactory extends AbstractNamedEntityCacheFactory {

    public RoleCacheFactory() {
        super("RoleId", "RoleName");
    }

}
