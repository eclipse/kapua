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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.generator.id;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * Id generator service definition.
 * 
 * @since 1.0
 * 
 */
public interface IdGeneratorService extends KapuaService
{

    /**
     * Returns a unique identifier within the platform.<br>
     * It could be used as primary key for a record database insert.
     * 
     * @return
     * @throws KapuaException
     */
    public KapuaId generate()
        throws KapuaException;
}
