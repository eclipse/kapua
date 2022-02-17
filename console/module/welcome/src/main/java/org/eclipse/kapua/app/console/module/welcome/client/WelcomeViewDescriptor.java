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
package org.eclipse.kapua.app.console.module.welcome.client;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.View;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractMainViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class WelcomeViewDescriptor extends AbstractMainViewDescriptor {

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
