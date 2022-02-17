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

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * Kapua metatype objects factory service definition.<br>
 * This class provides, through locator and factory service, a factory for few objects types.
 *
 * @since 1.0
 */
public interface KapuaMetatypeFactory extends KapuaObjectFactory {

    /**
     * Returns a {@link KapuaTocd} instance
     *
     * @return
     */
    KapuaTocd newKapuaTocd();

    /**
     * Returns a {@link KapuaTad} instance
     *
     * @return
     */
    KapuaTad newKapuaTad();

    /**
     * Returns a {@link KapuaTscalar} instance
     *
     * @param type
     * @return
     */
    KapuaTscalar newKapuaTscalar(String type);

    /**
     * Returns a {@link KapuaToption} instance
     *
     * @return
     */
    KapuaToption newKapuaToption();

    /**
     * Returns a {@link KapuaTicon} instance
     *
     * @return
     */
    KapuaTicon newKapuaTicon();

    /**
     * Returns a {@link KapuaTmetadata} instance
     *
     * @return
     */
    KapuaTmetadata newKapuaTmetadata();

    /**
     * Returns a {@link KapuaTdesignate} instance
     *
     * @return
     */
    KapuaTdesignate newKapuaTdesignate();

    /**
     * Returns a {@link KapuaTobject} instance
     *
     * @return
     */
    KapuaTobject newKapuaTobject();

}
