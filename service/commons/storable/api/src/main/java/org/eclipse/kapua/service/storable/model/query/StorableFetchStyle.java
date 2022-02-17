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
