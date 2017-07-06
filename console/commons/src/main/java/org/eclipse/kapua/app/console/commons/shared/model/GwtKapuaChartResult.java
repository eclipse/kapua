/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.commons.shared.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class GwtKapuaChartResult implements Serializable {

    private static final long serialVersionUID = 1990747640665608353L;

    private Map<String, List<GwtDataPoint>> dataPoint;
    private Stack<KapuaBasePagingCursor> offsetCursors;
    private boolean moreData;
    private int lastOffset;

    public Stack<KapuaBasePagingCursor> getOffsetCursors() {
        return offsetCursors;
    }

    public void setOffsetCursors(Stack<KapuaBasePagingCursor> offsetCursors) {
        this.offsetCursors = offsetCursors;
    }

    public Map<String, List<GwtDataPoint>> getDataPoint() {
        return dataPoint;
    }

    public void setDataPoint(Map<String, List<GwtDataPoint>> dataPoint) {
        this.dataPoint = dataPoint;
    }


    public boolean hasMoreData() {
        return moreData;
    }

    public void setMoreData(boolean moreData) {
        this.moreData = moreData;
    }

    public int getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(int lastOffset) {
        this.lastOffset = lastOffset;
    }
}
