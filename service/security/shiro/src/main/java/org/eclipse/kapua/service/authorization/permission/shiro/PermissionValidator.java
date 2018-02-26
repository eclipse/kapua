/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.permission.shiro;

import javax.validation.constraints.NotNull;

import com.google.common.collect.Sets;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.permission.Permission;

import java.util.Set;

public class PermissionValidator {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DomainService DOMAIN_SERVICE = LOCATOR.getService(DomainService.class);
    private static final DomainFactory DOMAIN_FACTORY = LOCATOR.getFactory(DomainFactory.class);

    private PermissionValidator() {
    }

    public static void validatePermission(@NotNull Permission permission) throws KapuaException {
        validatePermissions(Sets.newHashSet(permission));
    }

    public static void validatePermissions(@NotNull Set<Permission> permissions) throws KapuaException {

        if (!permissions.isEmpty()) {
            DomainListResult domains = DOMAIN_SERVICE.query(DOMAIN_FACTORY.newQuery(null));

            for (Permission p : permissions) {
                if (p.getDomain() != null) {
                    boolean matched = false;
                    for (Domain domain : domains.getItems()) {
                        if (domain.getName().equals(p.getDomain())) {
                            matched = true;
                            if (!domain.getGroupable() && p.getGroupId() != null) {
                                throw new KapuaIllegalArgumentException("permission.groupId", p.getGroupId().toStringId());
                            }
                            break;
                        }
                    }

                    if (!matched) {
                        throw new KapuaIllegalArgumentException("permission.domain", p.getDomain());
                    }
                }
            }
        }
    }
}
