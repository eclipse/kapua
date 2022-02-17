/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.client.model;

/**
 * Insert {@link Request} definition.
 *
 * @since 1.0.0
 */
public class InsertRequest extends Request {

    /**
     * Constructor.
     *
     * @param id             The Object identifier.
     * @param typeDescriptor The {@link TypeDescriptor}.
     * @param storable       The Object to insert.
     * @since 1.0.0
     */
    public InsertRequest(String id, TypeDescriptor typeDescriptor, Object storable) {
        super(id, typeDescriptor, storable);
    }
}
