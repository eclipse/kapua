/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.data.client;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;

/**
 * Listener for grid selection changes on Data tab.
 *
 * @param <M> the model type being selected
 */
public class GridSelectionChangedListener<M extends ModelData> implements Listener<SelectionChangedEvent<M>> {

    private GridSelectionModel<M> selectionModel;
    protected boolean selectedAgain;
    private M selectedItem;

    /**
     * The constructor
     */
    public GridSelectionChangedListener() {
        super();
    }

    /**
     * Method for setting the selectionModel
     * @param selectionModel
     */
    public void setSelectionModel(GridSelectionModel<M> selectionModel) {
        this.selectionModel = selectionModel;
    }

    /**
     * Method for handling the SelectionChangedEvent<M> on grid rows.
     * This method prevents row deselection using CTRL + Click, by selecting
     * the previously chosen value again. In all other cases the row selection
     * works as usual.
     *
     * @param se the selection event
     */
    @SuppressWarnings("unchecked")
    @Override
    public void handleEvent(SelectionChangedEvent<M> se) {
        if (selectionModel.getSelectedItem() != null && selectedAgain == false) {
            selectedItem = selectionModel.getSelectedItem();
        } else if (selectionModel.getSelectedItem() == null) {
            selectedAgain = true;
        } else if (selectedItem != selectionModel.getSelectedItem()) {
            selectedAgain = false;
            selectedItem = selectionModel.getSelectedItem();
        }
        selectionModel.select(false, selectedItem);
    }
}
