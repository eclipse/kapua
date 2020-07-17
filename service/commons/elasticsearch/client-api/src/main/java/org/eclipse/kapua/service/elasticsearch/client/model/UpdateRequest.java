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
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.model;

/**
 * Update {@link Request} definition.
 *
 * @since 1.0.0
 */
public class UpdateRequest extends Request {

    /**
     * Constructor.
     *
     * @param id             The Object identifier.
     * @param typeDescriptor The {@link TypeDescriptor}.
     * @param storable       The Object to update.
     * @since 1.0.0
     */
    public UpdateRequest(String id, TypeDescriptor typeDescriptor, Object storable) {
        super(id, typeDescriptor, storable);
    }

}
