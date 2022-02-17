/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization;

import org.eclipse.kapua.service.authorization.access.AccessInfoDomain;
import org.eclipse.kapua.service.authorization.domain.DomainDomain;
import org.eclipse.kapua.service.authorization.group.GroupDomain;
import org.eclipse.kapua.service.authorization.role.RoleDomain;

public class AuthorizationDomains {

    private AuthorizationDomains() { }

    public static final AccessInfoDomain ACCESS_INFO_DOMAIN = new AccessInfoDomain();

    public static final DomainDomain DOMAIN_DOMAIN = new DomainDomain();

    public static final GroupDomain GROUP_DOMAIN = new GroupDomain();

    public static final RoleDomain ROLE_DOMAIN = new RoleDomain();
}
