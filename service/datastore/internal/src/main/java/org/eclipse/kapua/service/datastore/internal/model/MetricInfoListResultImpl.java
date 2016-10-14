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
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.internal.model.query.AbstractStorableListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;

public class MetricInfoListResultImpl extends AbstractStorableListResult<MetricInfo> implements MetricInfoListResult
{
    private static final long serialVersionUID = -829492000973519867L;

    public MetricInfoListResultImpl()
    {
        super();
    }

    public MetricInfoListResultImpl(Object nextKey) {
        super(nextKey);
    }

    public MetricInfoListResultImpl(Object nextKey, Integer totalCount) {
        super(nextKey, totalCount);
    }
}
