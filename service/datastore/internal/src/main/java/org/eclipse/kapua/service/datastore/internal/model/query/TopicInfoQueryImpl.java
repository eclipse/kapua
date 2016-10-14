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

import org.eclipse.kapua.service.datastore.internal.AbstractStorableQuery;
import org.eclipse.kapua.service.datastore.model.TopicInfo;
import org.eclipse.kapua.service.datastore.model.query.TopicInfoQuery;

public class TopicInfoQueryImpl extends AbstractStorableQuery<TopicInfo> implements TopicInfoQuery
{
    public void copy(TopicInfoQuery query)
    {
        super.copy(query);
        // Add copy for local members
    }
}
