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
package org.eclipse.kapua.app.console.module.api.client.ui.grid;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridView;

public class KapuaEditableGrid<M extends KapuaBaseModel> extends EditorGrid<M> {

    public KapuaEditableGrid(ListStore<M> store, ColumnModel cm) {
        super(store, cm);

        //
        // Grid properties
        setBorders(false);
        setStateful(false);
        setLoadMask(true);
        setStripeRows(true);
        setTrackMouseOver(false);
        disableTextSelection(false);

        //
        // Grid view options
        GridView gridView = getView();
        gridView.setAutoFill(true);
        gridView.setForceFit(true);
        gridView.setSortingEnabled(false);
    }
}
