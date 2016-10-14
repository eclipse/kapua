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
package org.eclipse.kapua.model.config.metatype;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Kapua metatype objects factory service definition.<br>
 * This class provides, through locator and factory service, a factory for few objects types.
 * 
 * @since 1.0
 *
 */
public interface KapuaMetatypeFactory extends KapuaObjectFactory{

    /**
     * Returns a {@link KapuaTocd} instance
     * 
     * @return
     */
    public KapuaTocd newKapuaTocd();

    /**
     * Returns a {@link KapuaTad} instance
     * 
     * @return
     */
    public KapuaTad newKapuaTad();

    /**
     * Returns a {@link KapuaTscalar} instance
     * 
     * @return
     */
    public KapuaTscalar newKapuaTscalar();

    /**
     * Returns a {@link KapuaToption} instance
     * 
     * @return
     */
    public KapuaToption newKapuaToption();

    /**
     * Returns a {@link KapuaTicon} instance
     * 
     * @return
     */
    public KapuaTicon newKapuaTicon();
}
