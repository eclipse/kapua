/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.google.gwt.user.client.Event;

/**
 * This is extension of CheckBoxSelectionModel that fixes issues with
 * multiple select not working properly on GridView that uses checkboxes.
 *
 * @param <M> data model type used in grid view
 */
public class EntityGridCheckBoxSelectionModel<M extends ModelData> extends CheckBoxSelectionModel<M> {

    @Override
    protected void handleMouseDown(GridEvent<M> e) {
      if (e.getEvent().getButton() == Event.BUTTON_LEFT) {
        M m = listStore.getAt(e.getRowIndex());
        if (m != null) {
          if (isSelected(m)) {
            deselect(m);
          } else {
            select(m, true);
          }
        }
      } else {
        super.handleMouseDown(e);
      }
    }
}
