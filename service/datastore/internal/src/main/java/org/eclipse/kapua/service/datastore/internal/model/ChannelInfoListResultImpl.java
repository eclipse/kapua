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
package org.eclipse.kapua.service.datastore.internal.model;

import org.eclipse.kapua.service.datastore.client.model.ResultList;
import org.eclipse.kapua.service.datastore.internal.model.query.AbstractStorableListResult;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;

/**
 * Channel information query result list implementation
 * 
 * @since 1.0
 *
 */
public class ChannelInfoListResultImpl extends AbstractStorableListResult<ChannelInfo> implements ChannelInfoListResult {


    /**
     * Construct a channel info result list
     */
    public ChannelInfoListResultImpl() {
        super();
    }

    /**
     * Construct the channel info result list from the provided list
     * 
     * @param resultList
     */
    public ChannelInfoListResultImpl(ResultList<ChannelInfo> resultList) {
        addItems(resultList.getResult());
        setTotalCount(resultList.getTotalCount());
    }

}
