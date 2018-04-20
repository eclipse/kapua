/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.model;

/**
 * Insert request
 * 
 * @since 1.0
 *
 */
public class InsertRequest extends Request {

    /**
     * Defualt constructor
     * 
     * @param id
     *            object identifier
     * @param typeDescriptor
     *            index/type descriptor
     * @param storable
     *            object to insert
     */
    public InsertRequest(String id, TypeDescriptor typeDescriptor, Object storable) {
        super(id, typeDescriptor, storable);
    }

}
