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
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.eclipse.kapua.service.datastore.model.Message;
import org.eclipse.kapua.service.datastore.model.MessageCreator;
import org.eclipse.kapua.service.datastore.model.Payload;
import org.eclipse.kapua.service.datastore.model.Position;
import org.eclipse.kapua.service.datastore.model.TopicInfo;
import org.eclipse.kapua.service.datastore.model.query.AssetInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.TopicInfoQuery;

public interface DatastoreObjectFactory extends KapuaObjectFactory
{
    public Message newMessage();

    public MessageCreator newMessageCreator();

    public MessageQuery newStorableMessageQuery();

    public Payload newPayload();

    public Position newPosition();

    public AssetInfo newAssetInfo();

    public AssetInfoQuery newStorableAssetQuery();

    public TopicInfo newTopicInfo();

    public TopicInfoQuery newStorableTopicQuery();
}
