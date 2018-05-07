/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model.domain;

import java.util.Set;

/**
 * The {@link org.eclipse.kapua.service.KapuaEntityService} {@link Domain}.
 * <p>
 * A {@link org.eclipse.kapua.service.KapuaService} can be associated with a domain which it uses to validate access
 */
public interface Domain {

    /**
     * Gets the {@link Domain} name.
     *
     * @return The {@link Domain} name.
     * @since 1.0.0
     */
    String getName();

    /**
     * Gets the {@link org.eclipse.kapua.service.KapuaService} name that use this {@link Domain}.<br>
     * The value represent the {@code interface} fully qualified name of the implemented {@link org.eclipse.kapua.service.KapuaService}<br>
     *
     * @return The {@link org.eclipse.kapua.service.KapuaService} that use this {@link Domain}.<br>
     * @since 1.0.0
     */
    String getServiceName();

    /**
     * Gets the set of {@link Actions} available in this {@link Domain}.<br>
     * The implementation must return the reference of the set and not make a clone.
     *
     * @return The set of {@link Actions}.
     * @since 1.0.0
     */
    Set<Actions> getActions();

    /**
     * Gets whether or not this {@link Domain} is group-able or not.
     *
     * @return {@code true} if the KapuaEntity is group-able or not, {@code false} otherwise.
     * @since 1.0.0
     */
    boolean getGroupable();
}
