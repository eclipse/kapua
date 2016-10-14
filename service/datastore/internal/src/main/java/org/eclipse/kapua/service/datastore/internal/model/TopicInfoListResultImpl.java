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
import org.eclipse.kapua.service.datastore.model.TopicInfo;
import org.eclipse.kapua.service.datastore.model.TopicInfoListResult;

public class TopicInfoListResultImpl extends AbstractStorableListResult<TopicInfo> implements TopicInfoListResult
{
    private static final long serialVersionUID = -6150141413325816028L;

    public TopicInfoListResultImpl()
    {
        super();
    }

    public TopicInfoListResultImpl(Object nextKey)
    {
        super(nextKey);
    }

    public TopicInfoListResultImpl(Object nextKey, Integer totalCount)
    {
        super(nextKey, totalCount);
    }
}
