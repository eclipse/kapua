/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.misc;

import org.eclipse.kapua.service.KapuaDomainService;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaNamedEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

public interface CollisionEntityService extends KapuaEntityService<CollisionEntity, CollisionEntityCreator>,
        KapuaNamedEntityService<CollisionEntity>,
        KapuaDomainService<CollisionEntityDomain>,
        KapuaConfigurableService {

    public static final CollisionEntityDomain COLLISION_ENTITY_DOMAIN = new CollisionEntityDomain();

    @Override
    public default CollisionEntityDomain getServiceDomain() {
        return COLLISION_ENTITY_DOMAIN;
    }

}
