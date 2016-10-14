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
import java.util.Stack;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;

public class KapuaBasePagingLoadConfig extends BasePagingLoadConfig implements KapuaPagingLoadConfig, Serializable {

    private static final long serialVersionUID = 8550045117054490792L;

    private int lastOffset;
    private Stack<KapuaBasePagingCursor> offsetCursors;

    public KapuaBasePagingLoadConfig() {
        super();
        offsetCursors = new Stack<KapuaBasePagingCursor>();
    }

    public KapuaBasePagingLoadConfig(int offset, int limit) {
        super(offset, limit);
    }

    public KapuaBasePagingLoadConfig(int offset, int limit, Stack<KapuaBasePagingCursor> offsetCursors) {
        super(offset, limit);
        this.offsetCursors = offsetCursors;
    }

    public int getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(int lastOffset) {
        this.lastOffset = lastOffset;
    }

    public Stack<KapuaBasePagingCursor> getOffsetCursors() {
        return offsetCursors;
    }

    public void setOffsetCursors(Stack<KapuaBasePagingCursor> offsetCursors) {
        this.offsetCursors = offsetCursors;
    }

}
