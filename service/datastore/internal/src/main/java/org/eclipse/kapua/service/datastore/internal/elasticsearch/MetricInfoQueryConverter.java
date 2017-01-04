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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;

/**
 * Metric info query converter.<br>
 * This object adds the specific metric info included and excluded fields definition to the abstract query converter.
 * 
 * @since 1.0
 *
 */
public class MetricInfoQueryConverter extends AbstractStorableQueryConverter<MetricInfo, MetricInfoQuery>
{

    @Override
    protected String[] getIncludes(StorableFetchStyle fetchStyle)
    {
        return new String[] { "" };
    }

    @Override
    protected String[] getExcludes(StorableFetchStyle fetchStyle)
    {
        return new String[] { "*" };
    }

    @Override
    protected String[] getFields()
    {
        return new String[] { MetricInfoField.ACCOUNT.field(),
                              MetricInfoField.CLIENT_ID.field(),
                              MetricInfoField.CHANNEL.field(),
                              MetricInfoField.NAME_FULL.field(),
                              MetricInfoField.TYPE_FULL.field(),
                              MetricInfoField.VALUE_FULL.field(),
                              MetricInfoField.TIMESTAMP_FULL.field(),
                              MetricInfoField.MESSAGE_ID_FULL.field() };
    }

}
