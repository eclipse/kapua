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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.eclipse.kapua.service.datastore.internal.model.query.AbstractStorableQueryConverter;
import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.eclipse.kapua.service.datastore.model.query.AssetInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageFetchStyle;

public class AssetInfoQueryConverter extends AbstractStorableQueryConverter<AssetInfo, AssetInfoQuery>
{

    @Override
    protected String[] getIncludes(MessageFetchStyle fetchStyle)
    {
        return null;
    }

    @Override
    protected String[] getExcludes(MessageFetchStyle fetchStyle)
    {
        return null;
    }

    @Override
    protected String[] getFields()
    {
        return new String[] {EsSchema.ASSET_NAME,
                             EsSchema.ASSET_TIMESTAMP,
                             EsSchema.ASSET_ACCOUNT};
    }
}
