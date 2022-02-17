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
package org.eclipse.kapua.app.console.module.api.client.ui.view;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public abstract class AbstractView extends LayoutContainer implements View {

    protected GwtSession currentSession;

    public void onUserChange() {
    }

}
