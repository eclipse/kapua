/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.client.user;

import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.client.user.dialog.UserAddDialog;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;

import com.google.gwt.user.client.Element;


public class UserGridToolbar extends EntityCRUDToolbar<GwtUser> {

    public UserGridToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new UserAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        // TODO Auto-generated method stub
        return super.getEditDialog();
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        // TODO Auto-generated method stub
        return super.getDeleteDialog();
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
    }

}
