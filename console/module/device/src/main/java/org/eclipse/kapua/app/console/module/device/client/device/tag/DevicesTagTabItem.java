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
package org.eclipse.kapua.app.console.module.device.client.device.tag;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DevicesTagTabItem extends KapuaTabItem<GwtTag> {

    private static final ConsoleTagMessages MSGS = GWT.create(ConsoleTagMessages.class);
    TagSubjectGrid tagPermissionGrid;

    public DevicesTagTabItem(GwtSession currentSession) {
        super(currentSession, MSGS.tagsTabSubjectGridTitle(), new KapuaIcon(IconSet.SUPPORT));
        tagPermissionGrid = new TagSubjectGrid(null, currentSession);
        tagPermissionGrid.setRefreshOnRender(false);
        setEnabled(false);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setBorders(false);
        add(tagPermissionGrid);
    }

    @Override
    public void setEntity(GwtTag gwtTag) {
        super.setEntity(gwtTag);
        if (gwtTag != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
        tagPermissionGrid.setEntity(gwtTag);
    }

    @Override
    protected void doRefresh() {
        if (selectedEntity != null){
            tagPermissionGrid.refresh();
        }
    }
}

