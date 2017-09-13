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
package org.eclipse.kapua.app.console.module.job.client;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineServiceAsync;

public class JobGridToolbar extends EntityCRUDToolbar<GwtJob> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private static final GwtJobEngineServiceAsync JOB_ENGINE_SERVICE = GWT.create(GwtJobEngineService.class);
    private Button startJobButton;

    public JobGridToolbar(final GwtSession currentSession) {
        super(currentSession);
    }

    public Button getStartJobButton() {
        return startJobButton;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();

        add(new SeparatorMenuItem());
        startJobButton = new Button(JOB_MSGS.startJobButton(), new KapuaIcon(IconSet.PLAY), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobStartDialog dialog = new JobStartDialog(gridSelectionModel.getSelectedItem());
                dialog.show();
            }
        });
        startJobButton.disable();
        add(startJobButton);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new JobAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtJob selectedJob = gridSelectionModel.getSelectedItem();
        JobEditDialog dialog = null;
        if (selectedJob != null) {
            dialog = new JobEditDialog(currentSession, selectedJob);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtJob selectedJob = gridSelectionModel.getSelectedItem();
        JobDeleteDialog dialog = null;
        if (selectedJob != null) {
            dialog = new JobDeleteDialog(selectedJob);
        }
        return dialog;
    }
}
