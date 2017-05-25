/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model.query;

/**
 * Sortable field definition
 *
 */
public class SortField {

    private SortDirection sortDirection;
    private String field;

    private SortField(final SortDirection direction, final String field) {
        this.sortDirection = direction;
        this.field = field;
    }

    public String getField() {
        return this.field;
    }

    public SortDirection getSortDirection() {
        return this.sortDirection;
    }

    public static SortField of(SortDirection direction, final String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return null;
        }

        if (direction == null) {
            direction = SortDirection.ASC;
        }

        return new SortField(direction, fieldName);
    }

    public static SortField ascending(final String fieldName) {
        return of(SortDirection.ASC, fieldName);
    }

    public static SortField descending(final String fieldName) {
        return of(SortDirection.DESC, fieldName);
    }
}
