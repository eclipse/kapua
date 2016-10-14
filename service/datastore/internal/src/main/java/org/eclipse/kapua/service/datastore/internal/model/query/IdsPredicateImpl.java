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
package org.eclipse.kapua.service.datastore.internal.model.query;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.IdsPredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableField;

public class IdsPredicateImpl implements IdsPredicate
{
    private StorableField   field;
    private Set<StorableId> idSet = new HashSet<StorableId>();

    public IdsPredicateImpl()
    {
    }

    public IdsPredicateImpl(StorableField field, Collection<StorableId> ids)
    {
        this.field = field;
        this.idSet.addAll(ids);
    }

    @Override
    public StorableField getField()
    {
        return this.field;
    }

    public IdsPredicate setField(StorableField field)
    {
        this.field = field;
        return this;
    }

    @Override
    public Set<StorableId> getIdSet()
    {
        return this.idSet;
    }

    public IdsPredicate addValue(StorableId id)
    {
        this.idSet.add(id);
        return this;
    }

    public IdsPredicate addValues(Collection<StorableId> ids)
    {
        this.idSet.addAll(ids);
        return this;
    }

    public IdsPredicate clearValues()
    {
        this.idSet.clear();
        return this;
    }
}
