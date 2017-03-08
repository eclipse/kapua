/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model;

/**
 * Storable identifier definition.<br>
 * It defines the identifier of every object that can be stored in a datastore schema.
 * 
 * @since 1.0
 * 
 */
public interface StorableId
{
    /**
     * Return the storable identifier as a string
     * 
     * @return
     */
    public String toString();
}
