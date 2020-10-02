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
 * {@link StorableFetchStyle} definition.
 * <p>
 * According to the value it fetches less or more {@link Storable} fields
 *
 * @since 1.0.0
 */
public enum StorableFetchStyle {
    /**
     * Only indexed fields.
     *
     * @since 1.0.0
     */
    FIELDS,

    /**
     * Full document (except the {@link Storable} body).
     *
     * @since 1.0.0
     */
    SOURCE_SELECT,

    /**
     * Full document.
     *
     * @since 1.0.0
     */
    SOURCE_FULL
}
