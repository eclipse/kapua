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

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Xml factory class.<br>
 * This class provides, through locator and factory service, a factory for few objects types.
 * 
 * @since 1.0
 *
 */
public class MetatypeXmlRegistry {

    /**
     * Locator instance
     */
    private KapuaLocator locator = KapuaLocator.getInstance();

    /**
     * Meta type factory instance
     */
    private KapuaMetatypeFactory factory = locator.getFactory(KapuaMetatypeFactory.class);

    /**
     * Returns a {@link KapuaTocd} instance
     * @return
     */
    public KapuaTocd newKapuaTocd()
    {
        return factory.newKapuaTocd();
    }

    /**
     * Returns a {@link KapuaTad} instance
     * 
     * @return
     */
    public KapuaTad newKapuaTad()
    {
        return factory.newKapuaTad();
    }

    /**
     * Returns a {@link KapuaTicon} instance
     * 
     * @return
     */
    public KapuaTicon newKapuaTicon()
    {
        return factory.newKapuaTicon();
    }

    /**
     * Returns a {@link KapuaTscalar} instance
     * 
     * @return
     */
    public KapuaTscalar newKapuaTscalar()
    {
        return factory.newKapuaTscalar();
    }

    /**
     * Returns a {@link KapuaToption} instance
     * 
     * @return
     */
    public KapuaToption newKapuaToption()
    {
        return factory.newKapuaToption();
    }
}
