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
package org.eclipse.kapua.service.authorization.permission;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * Permission definition.
 * 
 * @since 1.0
 *
 */
public interface Permission
{

    /**
     * Set the domain
     * 
     * @param domain
     */
    public void setDomain(String domain);

    /**
     * Get the domain
     * 
     * @return
     */
    public String getDomain();

    /**
     * Set the action for this permission
     * 
     * @param action
     */
    public void setAction(Actions action);

    /**
     * Get the action for this permission
     * 
     * @return
     */
    public Actions getAction();

    /**
     * Set the target permission scope identifier
     * 
     * @param targetScopeId
     */
    public void setTargetScopeId(KapuaId targetScopeId);

    /**
     * Get the target permission scope identifier
     * 
     * @return
     */
    public KapuaId getTargetScopeId();
}
