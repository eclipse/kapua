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
package org.eclipse.kapua.app.console.client.ui.grid;

import org.eclipse.kapua.app.console.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.store.ListStore;

public class EntityGridLoadListener<M extends GwtEntityModel> extends KapuaLoadListener {

    private EntityGrid<M> entityGrid;
    private M selectedEntity;
    private ListStore<M> entityStore;

    public EntityGridLoadListener(EntityGrid<M> entityGrid, ListStore<M> entityStore) {
        this.entityGrid = entityGrid;
        this.entityStore = entityStore;
    }

    @Override
    public void loaderBeforeLoad(LoadEvent le) {
        selectedEntity = entityGrid.getSelectionModel().getSelectedItem();
    }

    @Override
    public void loaderLoad(LoadEvent le) {
        if (selectedEntity != null) {
            for (M e : entityStore.getModels()) {
                if (selectedEntity.getId().equals(e.getId())) {
                    entityGrid.getSelectionModel().select(e, true);
                }
            }
        }
    }

}