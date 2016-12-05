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
package org.eclipse.kapua.app.console.client.ui.dialog.entity;

import org.eclipse.kapua.app.console.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

public abstract class EntityAddEditDialog extends SimpleDialog {

    protected GwtSession currentSession;

    public EntityAddEditDialog(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public AbstractImagePrototype getHeaderIcon() {
        return null;
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return null;
    }
}
