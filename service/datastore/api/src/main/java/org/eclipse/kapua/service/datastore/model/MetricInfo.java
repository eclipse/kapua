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
package org.eclipse.kapua.service.datastore.model;

import java.util.Date;

/**
 * Information about device metric value. Metric is an arbitrary named value. We usually
 * keep only the most recent value of the metric.
 */
public interface MetricInfo extends Storable
{
    public StorableId getId();
    
    public String getScope();

    public String getFullTopicName();

    public void setFullTopicName(String topic);

    public String getName();

    public void setName(String name);

    public String getType();

    public void setType(String type);

    public <T> T getValue(Class<T> clazz);

    public <T> void setValue(T value);

    public StorableId getLastMessageId();

    public void setLastMessageId(StorableId lastMessageId);

    public Date getLastMessageTimestamp();

    public void setLastMessageTimestamp(Date lastMessageTimestamp);
}
