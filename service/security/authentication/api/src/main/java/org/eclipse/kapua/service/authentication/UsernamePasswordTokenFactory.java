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
package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Username password {@link UsernamePasswordTokenFactory} factory definition.
 * 
 * @since 1.0
 * 
 */
public interface UsernamePasswordTokenFactory extends KapuaObjectFactory
{

    /**
     * Creates a new {@link UsernamePasswordToken} instance based on provided username and password
     * 
     * @param username
     * @param password
     * @return
     */
    public UsernamePasswordToken newInstance(String username, char[] password);
}
