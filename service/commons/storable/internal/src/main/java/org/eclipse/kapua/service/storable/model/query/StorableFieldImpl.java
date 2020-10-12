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

/**
 * {@link StorableField} implementation.
 *
 * @since 1.0.0
 */
public class StorableFieldImpl implements StorableField {

    private final String field;

    /**
     * Constructor.
     *
     * @param field The {@link StorableField} name.
     * @since 1.0.0
     */
    public StorableFieldImpl(String field) {
        this.field = field;
    }

    @Override
    public String field() {
        return field;
    }

    @Override
    public String name() {
        return field;
    }

}
