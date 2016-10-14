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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.service.datastore.model.Payload;
import org.eclipse.kapua.service.datastore.model.Position;

public class PayloadImpl implements Payload
{
    private Date                collectedOn;
    private Position            position;
    private Map<String, Object> metrics;
    private byte[]              body;

    public PayloadImpl()
    {
        metrics = new HashMap<String, Object>();
        this.body = null;
    }

    @Override
    public Date getCollectedOn()
    {
        return collectedOn;
    }

    @Override
    public void setCollectedOn(Date collectedOn)
    {
        this.collectedOn = collectedOn;
    }

    @Override
    public Position getPosition()
    {
        return position;
    }

    @Override
    public void setPosition(Position position)
    {
        this.position = position;
    }

    @Override
    public Map<String, Object> getMetrics()
    {
        return metrics;
    }

    @Override
    public void setMetrics(Map<String, Object> metrics)
    {
        this.metrics = metrics;
    }

    @Override
    public byte[] getBody()
    {
        return body;
    }

    @Override
    public void setBody(byte[] body)
    {
        this.body = body;
    }
}
