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
package org.eclipse.kapua.commons.model.query;

import org.eclipse.kapua.model.query.KapuaSortCriteria;

public class FieldSortCriteria implements KapuaSortCriteria {
    public enum SortOrder {
        ASCENDING,
        DESCENDING;
    }

    private String    attributeName;
    private SortOrder sortOrder;

    public FieldSortCriteria(String attributeName, SortOrder sortOrder) {
        this.attributeName = attributeName;
        this.sortOrder = sortOrder;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }
}
