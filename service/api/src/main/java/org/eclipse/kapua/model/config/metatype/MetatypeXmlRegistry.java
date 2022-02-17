/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
     *
     * @return
     */
    public KapuaTocd newKapuaTocd() {
        return factory.newKapuaTocd();
    }

    /**
     * Returns a {@link KapuaTad} instance
     *
     * @return
     */
    public KapuaTad newKapuaTad() {
        return factory.newKapuaTad();
    }

    /**
     * Returns a {@link KapuaTicon} instance
     *
     * @return
     */
    public KapuaTicon newKapuaTicon() {
        return factory.newKapuaTicon();
    }

    /**
     * Returns a {@link KapuaToption} instance
     *
     * @return
     */
    public KapuaToption newKapuaToption() {
        return factory.newKapuaToption();
    }

    /**
     * Returns a {@link KapuaTmetadata} instance
     *
     * @return
     */
    public KapuaTmetadata newKapuaTmetadata() {
        return factory.newKapuaTmetadata();
    }

    /**
     * Returns a {@link KapuaTdesignate} instance
     *
     * @return
     */
    public KapuaTdesignate newKapuaTdesignate() {
        return factory.newKapuaTdesignate();
    }

    /**
     * Returns a {@link KapuaTobject} instance
     *
     * @return
     */
    public KapuaTobject newKapuaTobject() {
        return factory.newKapuaTobject();
    }
}
