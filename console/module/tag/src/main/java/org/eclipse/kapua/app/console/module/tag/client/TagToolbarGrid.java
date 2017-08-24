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
package org.eclipse.kapua.app.console.module.tag.client;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;

public class TagToolbarGrid extends EntityCRUDToolbar<GwtTag> {

    public TagToolbarGrid(GwtSession currentSession) {
        super(currentSession);

    }

    @Override
    protected KapuaDialog getAddDialog() {

        return new TagAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtTag selectedTag = gridSelectionModel.getSelectedItem();
        TagEditDialog dialog = null;
        if (selectedTag != null) {
            dialog = new TagEditDialog(currentSession, selectedTag);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtTag selectedTag = gridSelectionModel.getSelectedItem();
        TagDeleteDialog dialog = null;
        if (selectedTag != null) {
            dialog = new TagDeleteDialog(selectedTag);
        }
        return dialog;
    }

}
