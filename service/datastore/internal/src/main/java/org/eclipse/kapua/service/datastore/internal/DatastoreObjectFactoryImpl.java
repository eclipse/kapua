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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.model.MessageCreatorImpl;
import org.eclipse.kapua.service.datastore.internal.model.MessageImpl;
import org.eclipse.kapua.service.datastore.internal.model.PayloadImpl;
import org.eclipse.kapua.service.datastore.internal.model.PositionImpl;
import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.eclipse.kapua.service.datastore.model.Message;
import org.eclipse.kapua.service.datastore.model.MessageCreator;
import org.eclipse.kapua.service.datastore.model.Payload;
import org.eclipse.kapua.service.datastore.model.Position;
import org.eclipse.kapua.service.datastore.model.TopicInfo;
import org.eclipse.kapua.service.datastore.model.query.AssetInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.TopicInfoQuery;

public class DatastoreObjectFactoryImpl implements DatastoreObjectFactory
{

    @Override
    public Message newMessage()
    {
        return new MessageImpl();
    }

    @Override
    public MessageQuery newStorableMessageQuery()
    {
        return null;
    }

    @Override
    public Payload newPayload()
    {
        return new PayloadImpl();
    }

    @Override
    public Position newPosition()
    {
        return new PositionImpl();
    }

    @Override
    public AssetInfo newAssetInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AssetInfoQuery newStorableAssetQuery()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TopicInfo newTopicInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TopicInfoQuery newStorableTopicQuery()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MessageCreator newMessageCreator()
    {
        return new MessageCreatorImpl();
    }
}
