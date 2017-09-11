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
 *******************************************************************************/
package org.eclipse.kapua.model;

import java.util.Properties;

import org.eclipse.kapua.KapuaException;

/**
 * Kapua updateable entity creator service definition.
 *
 * @param <E>
 *            entity type
 * 
 * @since 1.0
 * 
 */
public interface KapuaUpdatableEntityCreator<E extends KapuaEntity> extends KapuaEntityCreator<E> {

    public Properties getEntityAttributes() throws KapuaException;

    public void setEntityAttributes(Properties entityAttributes) throws KapuaException;

}
