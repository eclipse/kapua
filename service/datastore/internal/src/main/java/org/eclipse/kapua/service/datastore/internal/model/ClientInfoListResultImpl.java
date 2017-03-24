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
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;

/**
 * Client information query result list implementation
 * 
 * @since 1.0
 *
 */
public class ClientInfoListResultImpl extends AbstractStorableListResult<ClientInfo> implements ClientInfoListResult {


    /**
     * Construct a client info result list
     */
    public ClientInfoListResultImpl() {
        super();
    }

    /**
     * Construct the client info result list from the provided list
     * 
     * @param resultList
     */
    public ClientInfoListResultImpl(ResultList<ClientInfo> resultList) {
        addItems(resultList.getResult());
        setTotalCount(resultList.getTotalCount());
    }

}
