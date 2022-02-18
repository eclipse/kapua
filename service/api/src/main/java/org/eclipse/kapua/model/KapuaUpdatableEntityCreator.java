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
package org.eclipse.kapua.model;

import org.eclipse.kapua.KapuaException;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Properties;

/**
 * {@link KapuaUpdatableEntityCreator} definition.
 *
 * @param <E> entity type
 * @since 1.0.0
 */
public interface KapuaUpdatableEntityCreator<E extends KapuaEntity> extends KapuaEntityCreator<E> {

    @XmlTransient
    Properties getEntityAttributes() throws KapuaException;

    void setEntityAttributes(Properties entityAttributes) throws KapuaException;
}
