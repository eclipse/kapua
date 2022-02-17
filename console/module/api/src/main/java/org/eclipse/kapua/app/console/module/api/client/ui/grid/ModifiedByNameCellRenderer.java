/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;

public class ModifiedByNameCellRenderer<M extends GwtUpdatableEntityModel> implements GridCellRenderer<M> {

    private static final ConsoleMessages CMSGS = GWT.create(ConsoleMessages.class);

    @Override
    public Object render(M model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<M> store,
            Grid<M> grid) {
        return model.getModifiedByName() != null ? model.getModifiedByName() : CMSGS.notAvailable();
    }
}
