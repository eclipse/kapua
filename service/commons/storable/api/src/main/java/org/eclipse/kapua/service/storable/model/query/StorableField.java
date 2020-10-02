/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query;

import org.eclipse.kapua.service.storable.model.Storable;

/**
 * {@link StorableField }definition.
 * <p>
 * It identifies one of the fields of {@link Storable}
 *
 * @since 1.0.0
 */
public interface StorableField {

    /**
     * Gets the {@link Storable} field name.
     *
     * @return The {@link Storable} field name.
     * @since 1.0.0
     */
    String field();

    /**
     * Watch out using this to get the {@link StorableField} actual name.
     * Maybe {@link #field()} is what you are looking for!
     *
     * @return The {@link Enum#name()}
     * @since 1.3.0
     * @deprecated Since the beginning.
     */
    @Deprecated
    String name();
}
