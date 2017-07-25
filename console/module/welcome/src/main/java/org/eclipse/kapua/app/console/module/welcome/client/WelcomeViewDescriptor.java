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
package org.eclipse.kapua.app.console.module.welcome.client;

import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.views.AbstractViewDescriptor;
import org.eclipse.kapua.app.console.commons.client.views.View;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.welcome.client.WelcomeView;

public class WelcomeViewDescriptor extends AbstractViewDescriptor {

    @Override
    public String getViewId() {
        return "welcome";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.INFO;
    }

    @Override
    public Integer getOrder() {
        return 100;
    }

    @Override
    public String getName() {
        return WelcomeView.getName();
    }

    @Override
    public View getViewInstance(GwtSession currentSession) {
        return new WelcomeView(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }
}
