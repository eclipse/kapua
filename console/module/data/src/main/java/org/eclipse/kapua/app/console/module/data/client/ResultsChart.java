/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;

public class ResultsChart extends LayoutContainer {

    GwtSession currentSession;

    public ResultsChart(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        // Chart chart = new Chart("open-flash-chart.swf");
    }

}
