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
package org.eclipse.kapua.app.console.module.about.client.about;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.View;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractMainViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AboutViewDescriptor extends AbstractMainViewDescriptor {

    @Override
    public String getViewId() {
        return "about";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.INFO;
    }

    @Override
    public Integer getOrder() {
        return 1200;
    }

    @Override
    public String getName() {
        return AboutView.getName();
    }

    @Override
    public View getViewInstance(GwtSession currentSession) {
        return new AboutView();
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }
}
