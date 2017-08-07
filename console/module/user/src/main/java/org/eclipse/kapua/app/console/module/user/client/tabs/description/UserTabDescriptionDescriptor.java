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
package org.eclipse.kapua.app.console.module.user.client.tabs.description;

import org.eclipse.kapua.app.console.commons.client.ui.view.descriptor.AbstractTabDescriptor;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.UserView;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;

public class UserTabDescriptionDescriptor extends AbstractTabDescriptor<GwtUser, UserTabDescription, UserView> {

    @Override
    public UserTabDescription getTabViewInstance(UserView view, GwtSession currentSession) {
        return new UserTabDescription();
    }

    @Override
    public String getViewId() {
        return "user.description";
    }

    @Override
    public Integer getOrder() {
        return 100;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }
}
