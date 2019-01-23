/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.client.model;

/**
 * Insert {@link Response} definition.
 *
 * @since 1.0.0
 */
public class InsertResponse extends Response {

    /**
     * Constructor.
     *
     * @param id             Ihe inserted record id
     * @param typeDescriptor The {@link TypeDescriptor}.
     * @since 1.0.0
     */
    public InsertResponse(String id, TypeDescriptor typeDescriptor) {
        super(id, typeDescriptor);
    }

}