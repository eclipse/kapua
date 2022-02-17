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
package org.eclipse.kapua.app.console.module.tag.client;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;

public class TagView extends AbstractEntityView<GwtTag> {

    private TagGrid tagGrid;

    private static final ConsoleTagMessages MSGS = GWT.create(ConsoleTagMessages.class);

    public TagView(GwtSession gwtSession) {
        super(gwtSession);
    }

    public static String getName() {
        return MSGS.tags();
    }

    @Override
    public EntityGrid<GwtTag> getEntityGrid(AbstractEntityView<GwtTag> entityView,
            GwtSession currentSession) {
        if (tagGrid == null) {
            tagGrid = new TagGrid(entityView, currentSession);
        }
        return tagGrid;
    }

    @Override
    public EntityFilterPanel<GwtTag> getEntityFilterPanel(AbstractEntityView<GwtTag> entityView,
            GwtSession currentSession) {

        return new TagFilterPanel(this, currentSession);
    }

}
