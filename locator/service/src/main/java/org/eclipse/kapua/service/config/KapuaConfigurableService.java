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
package org.eclipse.kapua.service.config;

import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Configurable service definition
 * 
 * @since 1.0
 * 
 */
public interface KapuaConfigurableService
{
    /**
     * Return the service configuration metadata
     * 
     * @return
     * @throws KapuaException
     */
	public KapuaTocd getConfigMetadata() throws KapuaException;
	
    /**
     * Return a map of dconfiguration values associated with the provided scope id
     * 
     * @param scopeId
     * @return
     * @throws KapuaException
     */
	public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException;
	
    /**
     * Set the configurationd values for the specified scope id
     * 
     * @param scopeId
     * @param values
     * @throws KapuaException
     */
	public void setConfigValues(KapuaId scopeId, Map<String, Object> values) throws KapuaException;

}
