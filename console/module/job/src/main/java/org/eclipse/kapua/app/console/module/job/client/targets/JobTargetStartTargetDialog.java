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
package org.eclipse.kapua.app.console.module.job.client.targets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineServiceAsync;

public class JobTargetStartTargetDialog extends SimpleDialog {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private static final GwtJobEngineServiceAsync JOB_ENGINE_SERVICE = GWT.create(GwtJobEngineService.class);

    private final GwtJob gwtJob;
    private final GwtJobTarget gwtJobTarget;

    public JobTargetStartTargetDialog(GwtJob gwtJob, GwtJobTarget gwtJobTarget) {
        this.gwtJob = gwtJob;
        this.gwtJobTarget = gwtJobTarget;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void createBody() {
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public void submit() {
        JOB_ENGINE_SERVICE.start(gwtJob.getScopeId(), gwtJob.getId(), gwtJobTarget.getId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                ConsoleInfo.display(MSGS.popupError(), JOB_MSGS.jobStartTargetErrorMessage(caught.getLocalizedMessage()));
                unmask();
                hide();
            }

            @Override
            public void onSuccess(Void result) {
                ConsoleInfo.display(MSGS.popupInfo(), JOB_MSGS.jobStartTargetStartedMessage());
                unmask();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.jobStartTargetDialogHeader(gwtJob.getJobName(), gwtJobTarget.getClientId());
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return new KapuaIcon(IconSet.WARNING);
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.jobStartTargetDialogInfo();
    }
}
