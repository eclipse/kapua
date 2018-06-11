/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources.model.data;

import org.eclipse.kapua.service.datastore.client.model.ResultList;
import org.eclipse.kapua.service.datastore.internal.model.query.AbstractStorableListResult;

public class JsonMessageListResult extends AbstractStorableListResult<JsonDatastoreMessage> {

    /**
     * Construct a message result list
     */
    public JsonMessageListResult() {
        super();
    }

    /**
     * Construct the message result list from the provided list
     *
     * @param resultList
     */
    public JsonMessageListResult(ResultList<JsonDatastoreMessage> resultList) {
        addItems(resultList.getResult());
        setTotalCount(resultList.getTotalCount());
    }

}
