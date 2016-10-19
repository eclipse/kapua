/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;

/**
 * Entity manager callback action service definition.
 * 
 * @since 1.0
 *
 */
public interface EntityManagerActionCallback {

    /**
     * Execute the action using the provided entity manager.
     * 
     * @param entityManager
     * @throws KapuaException
     */
    void actionOn(EntityManager entityManager) throws KapuaException;

}
