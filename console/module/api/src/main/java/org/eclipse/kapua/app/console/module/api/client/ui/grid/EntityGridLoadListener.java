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

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;

import java.util.List;

public class EntityGridLoadListener<M extends GwtEntityModel> extends KapuaLoadListener {

    private EntityGrid<M> entityGrid;
    private ListStore<M> entityStore;
    private Button searchButton;
    private Button resetButton;

    private List<M> selectedEntities;
    private boolean keepSelectedOnLoad = true;

    public EntityGridLoadListener(EntityGrid<M> entityGrid, ListStore<M> entityStore) {
        this.entityGrid = entityGrid;
        this.entityStore = entityStore;
    }

    public EntityGridLoadListener(EntityGrid<M> entityGrid, ListStore<M> entityStore, Button searchButton, Button resetButton) {
        this.entityGrid = entityGrid;
        this.entityStore = entityStore;
        this.searchButton = searchButton;
        this.resetButton = resetButton;
    }

    /**
     * Keeps track of the selected item in the {@link EntityGrid} before the invocation of {@link Loader#load()}.
     *
     * @param le The {@link LoadEvent}
     */
    @Override
    public void loaderBeforeLoad(LoadEvent le) {
        selectedEntities = entityGrid.getSelectionModel().getSelectedItems();
        if (searchButton != null && resetButton != null) {
            searchButton.disable();
            resetButton.disable();
        }
    }

    /**
     * If {@link #keepSelectedOnLoad} is {@code true}, selects all entities which where selected before {@link Loader#load()}
     * invocation if they are present in the new result set.
     *
     * @param le The {@link LoadEvent}.
     */
    @Override
    public void loaderLoad(LoadEvent le) {
        if (isKeepSelectedOnLoad() && !selectedEntities.isEmpty()) {
            for (M e : entityStore.getModels()) {
                for (M se : selectedEntities) {
                    if (se.getId().equals(e.getId())) {
                        entityGrid.getSelectionModel().select(e, true);
                        break;
                    }
                }
            }
        }

        if (searchButton != null && resetButton != null) {
            searchButton.enable();
            resetButton.enable();
        }
        entityGrid.loaded();
    }

    @Override
    public void loaderLoadException(LoadEvent le) {
        entityGrid.loaded();

        if (searchButton != null && resetButton != null) {
            searchButton.enable();
            resetButton.enable();
        }
        super.loaderLoadException(le);
    }

    /**
     * Returns whether or not the currently selected entities will be kept selected after the {@link Loader#load()}
     *
     * @return {@code true} if enabled,{@code false} otherwise.
     */
    public boolean isKeepSelectedOnLoad() {
        return keepSelectedOnLoad;
    }

    /**
     * Enables or disables the feature of remembering the selected items after the {@link Loader#load()} event.
     *
     * @param keepSelectedOnLoad {@code true} to enable, {@code false} to disable.
     */
    public void setKeepSelectedOnLoad(boolean keepSelectedOnLoad) {
        this.keepSelectedOnLoad = keepSelectedOnLoad;
    }
}
