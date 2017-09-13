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

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;

public class TagViewDescriptor extends AbstractEntityViewDescriptor<GwtTag> {

    @Override
    public String getViewId() {
        return "tags";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.TAGS;
    }

    @Override
    public Integer getOrder() {
        return 700;
    }

    @Override
    public String getName() {
        return TagView.getName();
    }

    @Override
    public EntityView<GwtTag> getViewInstance(GwtSession currentSession) {
        return new TagView(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasTagReadPermission();
    }
}
