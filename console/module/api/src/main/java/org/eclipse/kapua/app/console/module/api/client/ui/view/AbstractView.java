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
package org.eclipse.kapua.app.console.module.api.client.ui.view;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public abstract class AbstractView extends LayoutContainer implements View {

    protected GwtSession currentSession;

    public void onUserChange() {
    }

}
