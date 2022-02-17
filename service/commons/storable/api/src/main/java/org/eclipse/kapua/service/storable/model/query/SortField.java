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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query;

import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.StorableListResult;

import javax.validation.constraints.NotNull;

/**
 * {@link SortField} definition.
 * <p>
 * It defines the sorting of {@link StorableListResult} when processing  {@link StorableQuery}.
 *
 * @since 1.0.0
 */
public class SortField {

    private SortDirection sortDirection;
    private String field;

    /**
     * Constructor.
     * <p>
     * Required by JAXB.
     *
     * @since 1.0.0
     */
    public SortField() {
    }

    /**
     * Constructor.
     *
     * @param direction The {@link SortDirection}.
     * @param field     The name of the field of the {@link Storable} for which to sort.
     * @since 1.0.0
     */
    private SortField(@NotNull SortDirection direction, @NotNull String field) {
        this.sortDirection = direction;
        this.field = field;
    }

    /**
     * Gets the name of the field of the {@link Storable} for which to sort.
     *
     * @return Tname of the field of the {@link Storable} for which to sort.
     * @since 1.0.0
     */
    public String getField() {
        return this.field;
    }

    /**
     * Gets the {@link SortDirection}.
     *
     * @return The {@link SortDirection}.
     * @since 1.0.0
     */
    public SortDirection getSortDirection() {
        return this.sortDirection;
    }


    /**
     * Instantiates a {@link SortField} with the given parameters.
     *
     * @param fieldName The name of the field of the {@link Storable} for which to sort.
     * @param direction The {@link SortDirection}.
     * @return The newly instantiated {@link SortField}.
     * @since 1.0.0
     */
    public static SortField of(@NotNull String fieldName, @NotNull SortDirection direction) {
        return new SortField(direction, fieldName);
    }

    /**
     * Instantiates a {@link SortField} with {@link SortDirection#ASC}.
     *
     * @param fieldName The name of the field of the {@link Storable} for which to sort.
     * @return The newly instantiated {@link SortField}.
     * @see #of(String, SortDirection)
     * @since 1.0.0
     */
    public static SortField ascending(final String fieldName) {
        return of(fieldName, SortDirection.ASC);
    }

    /**
     * Instantiates a {@link SortField} with {@link SortDirection#ASC}.
     *
     * @param fieldName The name of the field of the {@link Storable} for which to sort.
     * @return The newly instantiated {@link SortField}.
     * @see #of(String, SortDirection)
     * @since 1.0.0
     */
    public static SortField descending(final String fieldName) {
        return of(fieldName, SortDirection.DESC);
    }
}
