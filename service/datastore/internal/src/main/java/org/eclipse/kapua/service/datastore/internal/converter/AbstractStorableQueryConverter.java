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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.converter;

import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorableQuery;

/**
 * Abstract storable query converter implementation.<br>
 * This object defines common method to all the query converter used by Kapua.
 *
 * @param <S>
 *            persisted object type (such as messages, channels information...)
 * @param <Q>
 *            query object
 * 
 * @since 1.0
 */
public abstract class AbstractStorableQueryConverter<S extends Storable, Q extends StorableQuery<S>> {

    /**
     * Query included fields list.<br>
     * The list may use the fetchStyle parameter to differentiate the included fields list (depending on the type of the entity)
     * 
     * @param fetchStyle
     * @return
     */
    protected abstract String[] getIncludes(StorableFetchStyle fetchStyle);

    /**
     * Query excluded fields list.<br>
     * The list may use the fetchStyle parameter to differentiate the excluded fields list (depending on the type of the entity)
     * 
     * @param fetchStyle
     * @return
     */
    protected abstract String[] getExcludes(StorableFetchStyle fetchStyle);

    /**
     * Query fields list
     * 
     * @return
     */
    protected abstract String[] getFields();
}
