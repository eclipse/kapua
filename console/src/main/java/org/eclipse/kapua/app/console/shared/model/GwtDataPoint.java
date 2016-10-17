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
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;

public class GwtDataPoint implements Serializable {

    private static final long serialVersionUID = -8592551619448300541L;

    private long   m_timestamp;
    private Number m_value;

    public GwtDataPoint() {
    }

    public GwtDataPoint(long timestamp, Number value) {
        m_value = value;
        m_timestamp = timestamp;
    }

    public long getTimestamp() {
        return m_timestamp;
    }

    public void setTimestamp(long timestamp) {
        m_timestamp = timestamp;
    }

    public Number getValue() {
        return m_value;
    }

    public void setValue(Number value) {
        m_value = value;
    }
}
