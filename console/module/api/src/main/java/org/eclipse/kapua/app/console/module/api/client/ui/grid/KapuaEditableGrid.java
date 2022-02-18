/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
