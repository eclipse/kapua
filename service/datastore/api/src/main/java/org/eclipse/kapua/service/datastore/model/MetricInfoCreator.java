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

public interface MetricInfoCreator extends StorableCreator<MetricInfo>
{
    public void setFullTopicName(String topic);

    public StorableId getLastMessageId();

    public String getName();

    public String getType();

    public <T> T getValue();

    public <T> void setValue(T value);

    public String getFullTopicName();

    public Date getLastMessageTimestamp();
}
