/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.job.client;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobQuery;

public class JobFilterPanel extends EntityFilterPanel<GwtJob> {

    private final static ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private final static int WIDTH = 193;
    private static final int MAX_LEN = 255;

    private final EntityGrid<GwtJob> entityGrid;
    private final GwtSession currentSession;

    private final KapuaTextField<String> jobNameField;

    public JobFilterPanel(AbstractEntityView<GwtJob> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(JOB_MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        Label jobNameLabel = new Label(JOB_MSGS.filterFieldJobNameLabel());
        jobNameLabel.setWidth(WIDTH);
        jobNameLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(jobNameLabel);

        jobNameField = new KapuaTextField<String>();
        jobNameField.setName("name");
        jobNameField.setWidth(WIDTH);
        jobNameField.setMaxLength(MAX_LEN);
        jobNameField.setStyleAttribute("margin-top", "0px");
        jobNameField.setStyleAttribute("margin-left", "5px");
        jobNameField.setStyleAttribute("margin-right", "5px");
        jobNameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(jobNameField);
    }

    @Override
    public void resetFields() {
        jobNameField.setValue(null);
        GwtJobQuery query = new GwtJobQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtJobQuery query = new GwtJobQuery();
        query.setName(jobNameField.getValue());
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }
}
