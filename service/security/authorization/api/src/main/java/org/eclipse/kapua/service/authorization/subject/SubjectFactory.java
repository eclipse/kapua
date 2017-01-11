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
package org.eclipse.kapua.service.authorization.subject;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link Subject} object factory.
 * 
 * @since 1.0.0
 */
public interface SubjectFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link Subject} implementing object with the provided parameters.
     * 
     * @param type
     *            The {@link SubjectType} of the new {@link Subject}.
     * @param action
     *            The id of the new {@link Subject}.
     * @return A instance of the implementing class of {@link Subject}.
     * @since 1.0.0
     */
    public Subject newSubject(SubjectType type, KapuaId id);
}
