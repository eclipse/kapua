/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.MessageStoreFactory;
import org.eclipse.kapua.service.datastore.MetricInfoFactory;
import org.eclipse.kapua.service.datastore.internal.model.DatastoreMessageImpl;
import org.eclipse.kapua.service.datastore.internal.model.MessageListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;

/**
 * {@link MetricInfoFactory} implementation.
 *
 * @since 1.3.0
 */
@KapuaProvider
public class MessageStoreFactoryImpl implements MessageStoreFactory {

    @Override
    public DatastoreMessage newStorable() {
        return new DatastoreMessageImpl();
    }

    @Override
    public MessageListResult newListResult() {
        return new MessageListResultImpl();
    }

    @Override
    public MessageQuery newQuery(KapuaId scopeId) {
        return new MessageQueryImpl(scopeId);
    }
}
