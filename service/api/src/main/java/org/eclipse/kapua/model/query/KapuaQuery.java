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
package org.eclipse.kapua.model.query;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;

/**
 * Kapua query definition.
 *
 * @param <E> query entity domain
 */
public interface KapuaQuery<E extends KapuaEntity>
{

    /**
     * Get the query offset
     * 
     * @return
     */
    public Integer getOffset();

    /**
     * Get the query limit
     * 
     * @return
     */
    public Integer getLimit();

    /**
     * set the query offset
     * 
     * @param offset
     */
    public void setOffset(Integer offset);

    /**
     * Set the query limit
     * 
     * @param limit
     */
    public void setLimit(Integer limit);

    /**
     * Set the code identifier
     * 
     * @param scopeId
     */
    public void setScopeId(KapuaId scopeId);

    /**
     * get the scope identifier
     * 
     * @return
     */
    public KapuaId getScopeId();

    /**
     * Set the query predicate
     * 
     * @param queryPredicate
     */
    public void setPredicate(KapuaPredicate queryPredicate);

    /**
     * Get the query predicate
     * 
     * @return
     */
    public KapuaPredicate getPredicate();

    /**
     * Set query sort criteria
     * 
     * @param sortCriteria
     */
    public void setSortCriteria(KapuaSortCriteria sortCriteria);

    /**
     * Get query sort criteria
     * 
     * @return
     */
    public KapuaSortCriteria getSortCriteria();

    /**
     * Get query fetch style
     * 
     * @return
     */
    public KapuaFetchStyle getFetchStyle();

    /**
     * Set query fetch style
     * 
     * @param fetchStyle
     */
    public void setFetchStyle(KapuaFetchStyle fetchStyle);
}
